package group7.tcss450.uw.edu.parkinglotreservation;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Travis Holloway on 2/28/2017.
 * A class to update users in the DB.
 */
class UpdateUserTask extends AsyncTask<String, Void, String> {

    private UpdateEmpFragment.UpdateEmpListener mlistener;
    private String mSSN;
    private String mAddress;
    private String mLicense;


    UpdateUserTask(final UpdateEmpFragment.UpdateEmpListener mListener,
                   final String mSpinnerChoice, final String address, String license) {
        this.mlistener = mListener;
        this.mSSN = mSpinnerChoice;
        this.mAddress = address;
        this.mLicense = license;
    }

    @Override
    protected String doInBackground(String... strings) {
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

    @Override
    protected void onPostExecute(String result) {

        if (result.equals("{\"result\", \"yay\"}")) {
            result = "Successful update of Employee: " + mSSN + ";\nNew Address: " + mAddress +
                    ", New License: " + mLicense + ".";
        }
        FireListener(result);
    }

    private void FireListener(String result) {
        try {
            mlistener.updateEmp(result);
        } catch (Exception e) {
            Log.e("FireListenerAddUSER", e.getMessage());
        }
    }
}
