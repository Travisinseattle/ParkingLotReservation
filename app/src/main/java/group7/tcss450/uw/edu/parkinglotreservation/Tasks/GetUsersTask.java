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
import group7.tcss450.uw.edu.parkinglotreservation.Fragments.UpdateEmpFragment;

/**
 * Created by Travis Holloway on 2/26/2017.
 * A class to Retrieve all Users
 */
public class GetUsersTask extends AsyncTask<String, Void, String> {

    /**
     * The updateEmpListener passed to the class.
     */
    private UpdateEmpFragment.UpdateEmpListener mUpdateEmpListener;

    /**
     * The getAllUsersListener passed to the class.
     */
    private MainFragment.GetAllUsers mGetUsersListener;

    /**
     * The constructor of the Class.
     *
     * @param updateEmpListener The updateEmpListener passed to the class.
     * @param getAllUsers The getAllUsersListener passed to the class.
     */
    public GetUsersTask(final UpdateEmpFragment.UpdateEmpListener updateEmpListener,
                        final MainFragment.GetAllUsers getAllUsers) {
        this.mUpdateEmpListener = updateEmpListener;
        this.mGetUsersListener = getAllUsers;
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

        if (result.equals("")) {
            result = "No Users Currently Exist";
            mUpdateEmpListener.updateEmp(result);
        }
        FireListener(result);
    }

    /**
     * A method to utilize the customer listener interface and pass the result back
     * to MainActivity.
     *
     * @param result The final result of the parsing of the json request.
     */
    private void FireListener(final String result) {
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
