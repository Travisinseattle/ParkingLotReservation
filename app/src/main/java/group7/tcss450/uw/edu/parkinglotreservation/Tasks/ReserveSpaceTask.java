package group7.tcss450.uw.edu.parkinglotreservation.Tasks;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import group7.tcss450.uw.edu.parkinglotreservation.Fragments.ReserveVisitorSpaceFragment;

/**
 * Created by Travis Holloway on 3/1/2017.
 * A task to reserve a visitor Parking Space.
 */
public class ReserveSpaceTask extends AsyncTask<String, Void, String> {

    /**
     * The Listener passed to the class.
     */
    private ReserveVisitorSpaceFragment.ReserveSpaceListener mListener;

    /**
     * The SSN of the Employee chosen by the spinner.
     */
    private String ssn;

    /**
     * The parking space that is reserved.
     */
    private String space;

    /**
     * The License # of the Employee reserving the space.
     */
    private String license;

    /**
     * The date that the reservation is for.
     */
    private String date;

    /**
     * The constructor for the class.
     *
     * @param mListener The Listener passed to the class.
     * @param mEmpChoice The SSN of the Employee chosen by the spinner.
     * @param license The License # of the Employee reserving the space.
     * @param date The date that the reservation is for.
     * @param space The parking space that is reserved.
     */
    public ReserveSpaceTask(final ReserveVisitorSpaceFragment.ReserveSpaceListener mListener,
                            final String mEmpChoice, final String license, final String date,
                            final String space) {
        this.mListener = mListener;
        this.ssn = mEmpChoice;
        this.license = license;
        this.date = date;
        this.space = space;
    }

    /**
     * Overridden doInBackground() method.  Used to parse the JSON return from the
     * php url that is provided to the method.
     *
     * @param strings array of strings provided by the execute() method.
     * @return the result of the parsing as a string.
     */
    @Override
    protected String doInBackground(final String... strings) {
        if (strings.length != 2) {
            throw new IllegalArgumentException("8 String arguments required.");
        }
        String response = "";
        HttpURLConnection urlConnection = null;
        final String url = strings[0];
        try {
            final URL urlObject = new URL(url);
            urlConnection = (HttpURLConnection) urlObject.openConnection();
            final InputStream content = urlConnection.getInputStream();
            final BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
            String s;
            while ((s = buffer.readLine()) != null) {
                response += s;
            }
        } catch (Exception e) {
            response = "Unable to connect, Reason: "
                    + e.getMessage();
        } finally {
            if (urlConnection != null)
                urlConnection.disconnect();
        }
        return response;
    }

    /**
     * Overridden onPostExecute() method.  Used for any additional processing required before
     * returning from the Task.
     *
     * @param result The result of the parsing done in doInBackground() method.
     */
    @Override
    protected void onPostExecute(String result) {

        if (result.equals("{\"result\", \"yay\"}")) {
            result = "Employee: " + ssn + " Successfully Reserved Space: " + space +
                    " For Visitor With License #: " + license +
                    " On the Following Date: " + date;
        }
        FireListener(result);
    }

    /**
     * A method to utilize the customer listener interface and pass the result back
     * to MainActivity.
     *
     * @param result The final result of the parsing of the json request.
     */
    private void FireListener(final String result) {
        try {
            mListener.reserveSpace(result);
        } catch (Exception e) {
            Log.e("AssignEmployeeSpaceFail", e.getMessage());
        }
    }
}
