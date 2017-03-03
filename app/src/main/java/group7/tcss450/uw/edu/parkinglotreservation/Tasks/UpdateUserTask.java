package group7.tcss450.uw.edu.parkinglotreservation.Tasks;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import group7.tcss450.uw.edu.parkinglotreservation.Fragments.UpdateEmpFragment;

/**
 * Created by Travis Holloway on 2/28/2017.
 * A class to update users in the DB.
 */
public class UpdateUserTask extends AsyncTask<String, Void, String> {

    /**
     * The listener passed to the class.
     */
    private UpdateEmpFragment.UpdateEmpListener mlistener;

    /**
     * The SSN of the Employee
     */
    private String mSSN;

    /**
     * The address of the employee.
     */
    private String mAddress;

    /**
     * The License # of the employee.
     */
    private String mLicense;


    /**
     * The constructor for the class.
     *
     * @param mListener The listener passed to the class.
     * @param mSpinnerChoice The String representing the spinner choice.
     * @param address The address of the Employee.
     * @param license The license # of the Employee.
     */
    public UpdateUserTask(final UpdateEmpFragment.UpdateEmpListener mListener,
                          final String mSpinnerChoice, final String address, String license) {
        this.mlistener = mListener;
        this.mSSN = mSpinnerChoice;
        this.mAddress = address;
        this.mLicense = license;
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
            result = "Successful update of Employee: " + mSSN + ";\nNew Address: " + mAddress +
                    ", New License: " + mLicense + ".";
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
            mlistener.updateEmp(result);
        } catch (Exception e) {
            Log.e("FireListenerAddUSER", e.getMessage());
        }
    }
}
