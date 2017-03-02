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
 * Created by Travis Holloway on 2/24/2017.
 */
public class AddNewSpaceTask extends AsyncTask<String, Void, String> {

    private AddSpaceFragment.AddSpaceListener mListener;
    private String mLotName;
    private String mSpaceNum;

    AddNewSpaceTask(final Context context, final AddSpaceFragment.AddSpaceListener addSpaceListener,
                    final String lotValue, final String spaceValue) {
        this.mListener = addSpaceListener;
        this.mLotName = lotValue;
        this.mSpaceNum = spaceValue;
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
            result = "Space # " +mSpaceNum + " Has Been Successfully Added to Lot " + mLotName;
        }

        FireListener(result);
    }

    private void FireListener(String result) {
        try {
            mListener.addSpace(result);
        } catch (Exception e) {
            Log.e("FireListenerAddSpace", e.getMessage());
        }
    }
}
