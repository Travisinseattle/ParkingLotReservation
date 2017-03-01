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
 */

public class AddNewEmpTask extends AsyncTask<String, Void, String> {

    private AddEmpFragment.AddEmpListener mListener;
    private Context mContext;

    AddNewEmpTask(final Context context, final AddEmpFragment.AddEmpListener addEmpListener) {
        this.mContext = context;
        this.mListener = addEmpListener;
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
            result = "New Employee Added Successfully";
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
