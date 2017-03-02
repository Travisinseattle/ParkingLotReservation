package group7.tcss450.uw.edu.parkinglotreservation;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Travis Holloway on 2/24/2017.
 * A Task to add a new Lot to the Database.
 */

class AddNewLotTask extends AsyncTask<String, Void, String> {

    private AddLotFragment.AddLotListener mListener;
    private String value;

    AddNewLotTask(final AddLotFragment.AddLotListener addLotListener, final String value) {
        this.mListener = addLotListener;
        this.value = value;
    }

    @Override
    protected String doInBackground(String... strings) {
        if (strings.length != 2) {
            throw new IllegalArgumentException("2 String arguments required.");
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

            if (result.equals("")) {
                result = "New Lot '" + value + "' Was Added Successfully";
            }

            FireListener(result);
    }

    private void FireListener(String result) {
        try {
            mListener.addLot(result);
        } catch (Exception e) {
            Log.e("FireListenerAddLot", e.getMessage());
        }
    }
}
