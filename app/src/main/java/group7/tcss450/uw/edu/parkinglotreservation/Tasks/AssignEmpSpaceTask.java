package group7.tcss450.uw.edu.parkinglotreservation.Tasks;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import group7.tcss450.uw.edu.parkinglotreservation.Fragments.AssignEmployeeSpaceFragment;

/**
 * Created by Travis Holloway on 2/28/2017.
 * A Task to assign an employee parking space.
 */
public class AssignEmpSpaceTask extends AsyncTask<String, Void, String> {


    /**
     * The listener passed to the class.
     */
    private AssignEmployeeSpaceFragment.AssignEmployeeSpaceListener mListener;

    /**
     * The SSN of the employee that was chosen by the spinner.
     */
    private String mSSN;

    /**
     * The space that was chosen by the spinner.
     */
    private String mSpace;

    /**
     * The monthly rate of the assigned space.
     */
    private String mRate;

    /**
     * The constructor for the class.
     *
     * @param mListener The listener passed to the class.
     * @param mEmpChoice The SSN of the employee that was chosen by the spinner.
     * @param mSpaceChoice The space that was chosen by the spinner.
     * @param mRate The monthly rate of the assigned space.
     */
    public AssignEmpSpaceTask(final AssignEmployeeSpaceFragment.AssignEmployeeSpaceListener mListener,
                       final String mEmpChoice, final String mSpaceChoice, final String mRate) {
        this.mListener = mListener;
        this.mSSN = mEmpChoice;
        this.mSpace = mSpaceChoice;
        this.mRate = mRate;
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
            result = "Employee: " + mSSN + " Successfully Assigned Space: " + mSpace +
                    " At the following monthly rate: $" + mRate;
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
            mListener.assignEmployeeSpace(result);
        } catch (Exception e) {
            Log.e("AssignEmployeeSpaceFail", e.getMessage());
        }
    }
}
