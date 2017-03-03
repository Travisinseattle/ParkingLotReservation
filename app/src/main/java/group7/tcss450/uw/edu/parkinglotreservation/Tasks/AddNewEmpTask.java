package group7.tcss450.uw.edu.parkinglotreservation.Tasks;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import group7.tcss450.uw.edu.parkinglotreservation.Fragments.AddEmpFragment;

/**
 * Created by Travis Holloway on 2/26/2017.
 * A task to add a new Employee
 */
public class AddNewEmpTask extends AsyncTask<String, Void, String> {

    /**
     * The listener passed to the class.
     */
    private AddEmpFragment.AddEmpListener mListener;

    /**
     * The first name of the employee.
     */
    private String fName;

    /**
     * The last name of the employee.
     */
    private String lName;

    /**
     * The address of the employee.
     */
    private String address;

    /**
     * The SSN of the employee.
     */
    private String ssn;

    /**
     * The license # of the employee.
     */
    private String license;

    /**
     * The Constructor of the Class.
     *
     * @param addEmpListener The listener passed to the class.
     * @param first The first name of the employee.
     * @param last The last name of the employee.
     * @param add The address of the employee.
     * @param ssn The SSN of the employee.
     * @param lic The license # of the employee.
     */
    public AddNewEmpTask(final AddEmpFragment.AddEmpListener addEmpListener,
                         final String first, final String last, final String add, final String ssn,
                         final String lic) {
        this.mListener = addEmpListener;
        this.fName = first;
        this.lName = last;
        this.address = add;
        this.ssn = ssn;
        this.license = lic;
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
            result = "New Employee Added Successfully:\n" + fName + "\n" + lName + "\n" +
                    address + "\n" + ssn + "\n" + license;
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
            mListener.addEmp(result);
        } catch (Exception e) {
            Log.e("FireListenerAddEmp", e.getMessage());
        }
    }
}
