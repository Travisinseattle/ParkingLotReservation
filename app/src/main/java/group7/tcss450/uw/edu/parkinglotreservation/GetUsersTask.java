package group7.tcss450.uw.edu.parkinglotreservation;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Travis Holloway on 2/26/2017.
 * A class to Retrieve all Users
 */

class GetUsersTask extends AsyncTask<String, Void, String> {

    private UpdateEmpFragment.UpdateEmpListener mUpdateEmpListener;
    private MainFragment.GetAllUsers mGetUsersListener;

    GetUsersTask(final UpdateEmpFragment.UpdateEmpListener updateEmpListener,
                 final MainFragment.GetAllUsers getAllUsers) {
        this.mUpdateEmpListener = updateEmpListener;
        this.mGetUsersListener = getAllUsers;
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

        if (result.equals("")) {
            result = "No Users Currently Exist";
            mUpdateEmpListener.updateEmp(result);
        }
        FireListener(result);
    }

    private void FireListener(String result) {
        final List<String> users = new ArrayList<>();
        try {
            final JSONArray jsonArray = new JSONArray(result);
            for (int i = 0; i < jsonArray.length(); i++) {
                final String lot = jsonArray.getJSONObject(i).getString("ssn");
                users.add(lot);
            }
        } catch (Exception e) {
            Log.e("GetUsersFirelistener", e.getMessage());
        }

        try {
            mGetUsersListener.getAllUsersUpdate(users);
        } catch (Exception e) {
            Log.e("GetUsers Fail", e.getMessage());
        }
    }
}
