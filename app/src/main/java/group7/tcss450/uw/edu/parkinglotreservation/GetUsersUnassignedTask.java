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

import static group7.tcss450.uw.edu.parkinglotreservation.MainActivity.APP_URL;

/**
 * Created by Travis Holloway on 3/1/2017.
 */
public class GetUsersUnassignedTask extends AsyncTask<String, Void, String> {

    private List<String> users;
    private List<String> spaces;
    private MainFragment.GetAllUnassignedUsers mListener;

    public GetUsersUnassignedTask(MainFragment.GetAllUnassignedUsers mUnassignedListeners) {

        this.mListener = mUnassignedListeners;
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
        AsyncTask<String, Void, String> task = null;
        task = new GrabUsers();
        String url = APP_URL + "getUnassignedEmps.php";
        Log.e("URL: ", url);
        task.execute(url, "Get U Emps", result);
    }

    private void FireListener(String result) {

        try {
            JSONArray jsonArray = new JSONArray(result);
            spaces = new ArrayList<String>();
            for (int i = 0; i < jsonArray.length(); i++) {
                String space = jsonArray.getJSONObject(i).getString("spaceID");
                spaces.add(space);
            }
        } catch (Exception e) {
            Log.e("GetUsers", e.getMessage());
        }

        try {
            mListener.getAllUnassignedUsersUpdate(users, spaces);
        } catch (Exception e) {
            Log.e("GetUsers Fail", e.getMessage());
        }
    }

    private class GrabUsers extends AsyncTask<String, Void, String> {

        private String value;

        @Override
        protected String doInBackground(String... strings) {
            if (strings.length != 3) {
                throw new IllegalArgumentException("2 String arguments required.");
            }
            String response = "";
            value = strings[2];
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
            try {
                JSONArray jsonArray = new JSONArray(result);
                users = new ArrayList<String>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    String user = jsonArray.getJSONObject(i).getString("ssn");
                    users.add(user);
                }
                Log.e("TESTTESTTEST", users.toString());
                FireListener(value);
            } catch (Exception e) {
                Log.e("GrabUsersUnassigned", e.getMessage());
            }
        }
    }

    }
