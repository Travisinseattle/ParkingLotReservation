package group7.tcss450.uw.edu.parkinglotreservation;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Travis Holloway on 3/1/2017.
 * A task to reserve a visitor Parking Space.
 */

class ReserveSpaceTask extends AsyncTask<String, Void, String> {

    private ReserveVisitorSpaceFragment.ReserveSpaceListener mListener;
    private String ssn;
    private String space;
    private String license;
    private String date;

    ReserveSpaceTask(final ReserveVisitorSpaceFragment.ReserveSpaceListener mListener,
                     final String mEmpChoice, final String license, final String date,
                     final String space) {
        this.mListener = mListener;
        this.ssn = mEmpChoice;
        this.license = license;
        this.date = date;
        this.space = space;
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
            result = "Employee: " + ssn + " Successfully Reserved Space: " + space +
                    " For Visitor With License #: " + license +
                    " On the Following Date: " + date;
        }
        FireListener(result);
    }

    private void FireListener(String result) {
        try {
            mListener.reserveSpace(result);
        } catch (Exception e) {
            Log.e("AssignEmployeeSpaceFail", e.getMessage());
        }
    }
}
