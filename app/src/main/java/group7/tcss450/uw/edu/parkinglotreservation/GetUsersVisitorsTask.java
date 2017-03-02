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
 * A Task to pull Users and populate a Spinner.
 */

class GetUsersVisitorsTask extends AsyncTask<String, Void, String> {


    private List<String> users;
    private List<String> spaces;
    private MainFragment.GetAllVisitorsUsers mlistener;
    private UpdateEmpFragment.UpdateEmpListener mUpdateEmpListener;

    GetUsersVisitorsTask(final UpdateEmpFragment.UpdateEmpListener mUpdateEmpListener,
                         final MainFragment.GetAllVisitorsUsers mVisitorListener) {
        this.mUpdateEmpListener = mUpdateEmpListener;
        this.mlistener = mVisitorListener;
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
        final AsyncTask<String, Void, String> task = new GrabUsers();
        final String url = APP_URL + "getEmps.php";
        Log.e("URL: ", url);
        task.execute(url, "Get U Emps", result);

        if (result.equals("")) {
            result = "No Users Currently Exist";
            mUpdateEmpListener.updateEmp(result);
        }
        FireListener(result);
    }

    private void FireListener(String result) {
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
            mlistener.getAllVisitors(users, spaces);
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
                Log.e("TESTTESTTEST", users.toString());
                FireListener(value);
            } catch (Exception e) {
                Log.e("GrabUsersUnassigned", e.getMessage());
            }
        }
    }
}
