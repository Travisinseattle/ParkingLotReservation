package group7.tcss450.uw.edu.parkinglotreservation.Tasks;

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

import group7.tcss450.uw.edu.parkinglotreservation.Fragments.MainFragment;

import static group7.tcss450.uw.edu.parkinglotreservation.MainActivity.APP_URL;

/**
 * Created by Travis Holloway on 3/1/2017.
 * A Task that grabs all Employees without an assigned parking space.
 */
public class GetUsersUnassignedTask extends AsyncTask<String, Void, String> {

    /**
     * An array of employees to be used in populating the employee spinner.
     */
    private List<String> users;

    /**
     * An array of parking spaces to be used in populating the parking spaces spinner.
     */
    private List<String> spaces;

    /**
     * The GetAllUnassignedUsers listener passed to the class.
     */
    private MainFragment.GetAllUnassignedUsers mListener;

    /**
     * The constructor of the class.
     *
     * @param mUnassignedListeners The GetAllUnassignedUsers listener passed to the class.
     */
    public GetUsersUnassignedTask(final MainFragment.GetAllUnassignedUsers mUnassignedListeners) {

        this.mListener = mUnassignedListeners;
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
        final AsyncTask<String, Void, String> task = new GrabUsers();
        final String url = APP_URL + "getUnassignedEmps.php";
        Log.e("URL: ", url);
        task.execute(url, "Get U Emps", result);
    }

    /**
     * A method to utilize the customer listener interface and pass the result back
     * to MainActivity.
     *
     * @param result The final result of the parsing of the json request.
     */
    private void FireListener(final String result) {

        try {
            final JSONArray jsonArray = new JSONArray(result);
            spaces = new ArrayList<>();
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

    /**
     * Inner class Async Task, used to pull the Employees from the database.
     */
    private class GrabUsers extends AsyncTask<String, Void, String> {

        private String value;

        /**
         * Overridden doInBackground() method.  Used to parse the JSON return from the
         * php url that is provided to the method.
         *
         * @param strings array of strings provided by the execute() method.
         * @return the result of the parsing as a string.
         */
        @Override
        protected String doInBackground(String... strings) {
            if (strings.length != 3) {
                throw new IllegalArgumentException("2 String arguments required.");
            }
            String response = "";
            value = strings[2];
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
            try {
                final JSONArray jsonArray = new JSONArray(result);
                users = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    String user = jsonArray.getJSONObject(i).getString("ssn");
                    users.add(user);
                }
                FireListener(value);
            } catch (Exception e) {
                Log.e("GrabUsersUnassigned", e.getMessage());
            }
        }
    }
}
