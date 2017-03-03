package group7.tcss450.uw.edu.parkinglotreservation.Tasks;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import group7.tcss450.uw.edu.parkinglotreservation.Fragments.AddLotFragment;

/**
 * Created by Travis Holloway on 2/24/2017.
 * A Task to add a new Lot to the Database.
 */
public class AddNewLotTask extends AsyncTask<String, Void, String> {

    /**
     * The addLotListener passed to the class.
     */
    private AddLotFragment.AddLotListener mListener;

    /**
     * The name of the lot.
     */
    private String lotName;

    /**
     * The Constructor of the class.
     *
     * @param addLotListener The addLotListener passed to the class.
     * @param lot The name of the lot.
     */
    public AddNewLotTask(final AddLotFragment.AddLotListener addLotListener, final String lot) {
        this.mListener = addLotListener;
        this.lotName = lot;
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

    /**
     * Overridden onPostExecute() method.  Used for any additional processing required before
     * returning from the Task.
     *
     * @param result The result of the parsing done in doInBackground() method.
     */
    @Override
    protected void onPostExecute(String result) {

            if (result.equals("")) {
                result = "New Lot '" + lotName + "' Was Added Successfully";
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
            mListener.addLot(result);
        } catch (Exception e) {
            Log.e("FireListenerAddLot", e.getMessage());
        }
    }
}
