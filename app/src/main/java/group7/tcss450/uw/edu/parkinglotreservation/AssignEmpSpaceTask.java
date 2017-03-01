package group7.tcss450.uw.edu.parkinglotreservation;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Travis Holloway on 2/28/2017.
 * A Task to assign an employee parking space.
 */

public class AssignEmpSpaceTask extends AsyncTask<String, Void, String> {

    private AssignEmployeeSpaceFragment.AssignEmployeeSpaceListener mListener;
    private String spaceID;
    private String mSSN;
    private String mSpace;
    private String mRate;

    AssignEmpSpaceTask(AssignEmployeeSpaceFragment.AssignEmployeeSpaceListener mListener,
                       String id, String mEmpChoice, String mSpaceChoice, String mRate) {
        this.mListener = mListener;
        this.mSSN = mEmpChoice;
        this.mSpace = mSpaceChoice;
        this.mRate = mRate;
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

        if (result.equals("")) {
            result = "Employee: " + mSSN + "Successfully Assigned Space: " + mSpace +
                    "At the following monthly rate: " + mRate;
        }
        FireListener(result);
    }

    private void FireListener(String result) {
        try {
            mListener.assignEmployeeSpace(result);
        } catch (Exception e) {
            Log.e("AssignEmployeeSpaceFail", e.getMessage());
        }
    }
}
