package group7.tcss450.uw.edu.parkinglotreservation.Tasks;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import group7.tcss450.uw.edu.parkinglotreservation.Fragments.AddSpaceFragment;

/**
 * Created by Travis Holloway on 2/24/2017.
 * A Task to update the database with a new Space.
 */
public class AddNewSpaceTask extends AsyncTask<String, Void, String> {


    /**
     * the AddSpaceListener passed to the class.
     */
    private AddSpaceFragment.AddSpaceListener mListener;

    /**
     * The name of the Lot to which the space belongs.
     */
    private String mLotName;

    /**
     * The Space #.
     */
    private String mSpaceNum;

    /**
     * The Constructor for the class.
     *
     * @param addSpaceListener the AddSpaceListener passed to the class.
     * @param lotValue The name of the Lot to which the space belongs.
     * @param spaceValue The Space #.
     */
    public AddNewSpaceTask(final AddSpaceFragment.AddSpaceListener addSpaceListener,
                           final String lotValue, final String spaceValue) {
        this.mListener = addSpaceListener;
        this.mSpaceNum = spaceValue;
        this.mLotName = lotValue;
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

        if (result.equals("{\"result\", \"yay\"}")) {
            result = "Space # " +mSpaceNum + " Has Been Successfully Added to Lot " + mLotName;
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
        try {
            mListener.addSpace(result);
        } catch (Exception e) {
            Log.e("FireListenerAddSpace", e.getMessage());
        }
    }
}
