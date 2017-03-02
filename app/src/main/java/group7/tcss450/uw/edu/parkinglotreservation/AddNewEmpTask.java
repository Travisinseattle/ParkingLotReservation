package group7.tcss450.uw.edu.parkinglotreservation;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Travis Holloway on 2/26/2017.
 * A task to add a new Employee
 */

public class AddNewEmpTask extends AsyncTask<String, Void, String> {

    private AddEmpFragment.AddEmpListener mListener;
    private String fName;
    private String lName;
    private String address;
    private String ssn;
    private String license;

    AddNewEmpTask(final AddEmpFragment.AddEmpListener addEmpListener,
                  String first, String last, String add, String ssn, String lic) {
        this.mListener = addEmpListener;
        this.fName = first;
        this.lName = last;
        this.address = add;
        this.ssn = ssn;
        this.license = lic;
    }

    @Override
    protected String doInBackground(String... strings) {
        if (strings.length != 2) {
            throw new IllegalArgumentException("8 String arguments required.");
        }
        String response = "";
        HttpURLConnection urlConnection = null;
        String url = strings[0];
        try {
            URL urlObject = new URL(url);
            urlConnection = (HttpURLConnection) urlObject.openConnection();
            InputStream content = urlConnection.getInputStream();
            BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
            String s = "";
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

    @Override
    protected void onPostExecute(String result) {

        if (result.equals("{\"result\", \"yay\"}")) {
            result = "New Employee Added Successfully:\n" + fName + "\n" + lName + "\n" +
                    address + "\n" + ssn + "\n" + license;
        }

        FireListener(result);
    }

    private void FireListener(String result) {
        try {
            mListener.addEmp(result);
        } catch (Exception e) {
            Log.e("FireListenerAddEmp", e.getMessage());
        }
    }
}
