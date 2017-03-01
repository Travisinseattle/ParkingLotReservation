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
 * Created by Travis Holloway on 2/28/2017.
 * A Task to retrieve all Available Parking Spaces
 */

public class GetAvailableParkingSpacesTask extends AsyncTask<String, Void, String> {

    private UpdateEmpFragment.UpdateEmpListener mUpdateEmpListener;
    private MainFragment.GetAllSpaces mGetAllSpaces;

    public GetAvailableParkingSpacesTask (UpdateEmpFragment.UpdateEmpListener uListener,
                                          MainFragment.GetAllSpaces sListener) {
        this.mUpdateEmpListener = uListener;
        this.mGetAllSpaces = sListener;
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
            result = "No Users Currently Exist";
            mUpdateEmpListener.updateEmp(result);
        }
        FireListener(result);
    }

    private void FireListener(String result) {
        List<String> spaces = new ArrayList<String>();
        try {
            JSONArray jsonArray = new JSONArray(result);
            spaces = new ArrayList<String>();
            for (int i = 0; i < jsonArray.length(); i++) {
                String space = jsonArray.getJSONObject(i).getString("spaceID");
                spaces.add(space);
            }
        } catch (Exception e) {
            Log.e("GetAvailableSpacesFail", e.getMessage());
        }

        try {
            mGetAllSpaces.getAllSpaces(spaces);
        } catch (Exception e) {
            Log.e("GetAllSpaces Fail", e.getMessage());
        }
    }
}
