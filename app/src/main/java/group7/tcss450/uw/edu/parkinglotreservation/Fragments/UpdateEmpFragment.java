package group7.tcss450.uw.edu.parkinglotreservation.Fragments;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import group7.tcss450.uw.edu.parkinglotreservation.R;
import group7.tcss450.uw.edu.parkinglotreservation.Tasks.UpdateUserTask;

import static group7.tcss450.uw.edu.parkinglotreservation.MainActivity.APP_URL;


/**
 * A fragment class used to update an employee in the data base.
 */
public class UpdateEmpFragment extends Fragment implements View.OnClickListener,
        AdapterView.OnItemSelectedListener {


    /**
     * The listener defined in the class.
     */
    private UpdateEmpFragment.UpdateEmpListener mListener;

    /**
     * The context of the parent activity.
     */
    private Context mContext;

    /**
     * The address EditText.
     */
    private EditText mAddress;

    /**
     * The license EditText.
     */
    private EditText mLicense;

    /**
     * The spinner of employee SSN's.
     */
    private Spinner mSpinner;

    /**
     * String representing the choice of the spinner.
     */
    private String mSpinnerChoice = null;

    /**
     * Required empty public constructor
     */
    public UpdateEmpFragment() {}

    /**
     * onCreateView Method.
     *
     * @param inflater the LayoutInflator.
     * @param container The ViewGroup container.
     * @param savedInstanceState The Bundle for savedInstanceState.
     *
     * @return the View that is created through the inflator.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_update_emp, container, false);
        mAddress = (EditText) v.findViewById(R.id.update_address_edit);
        mLicense = (EditText) v.findViewById(R.id.update_license_edit);
        mSpinner = (Spinner) v.findViewById(R.id.user_spinner);
        mSpinner.setOnItemSelectedListener(this);
        final Button mAdd = (Button) v.findViewById(R.id.update_emp_button);
        mAdd.setOnClickListener(this);
        return v;
    }

    /**
     * Overridden onAttach() method.
     *
     * @param context The context of the super class.
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        mListener = (UpdateEmpFragment.UpdateEmpListener) context;
    }

    /**
     * Overridden onDetach() method. Sets the listener(s) NULL.
     */
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * Overridden onStart() method. Ensures that the fragment loads before any logic takes place.
     */
    @Override
    public void onStart() {
        super.onStart();
        final ArrayAdapter<String> spinnerArrayAdapter;
        final ArrayList<String> emps;
        try {
            emps = getArguments().getStringArrayList("emps");
            assert emps != null;
            spinnerArrayAdapter = new ArrayAdapter<>(mContext,
                    android.R.layout.simple_spinner_item, emps);
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.
                    simple_spinner_dropdown_item); // The drop down view
            mSpinner.setAdapter(spinnerArrayAdapter);
        } catch (Exception e) {
            Log.e("AddSpace Array Fail", e.getMessage());
        }
    }

    /**
     * Overridden onClick method, determines the behavior of any objects that have a
     * onClickListener.
     *
     * @param v The parent view.
     */
    @Override
    public void onClick(View v) {
        if (mListener != null) {
            switch (v.getId()) {
                case R.id.update_emp_button:
                    if (mAddress.getText().toString().trim().length() <= 0) {
                        mAddress.setError(getString(R.string.user_error));
                    } else if (mLicense.getText().toString().trim().length() <= 0) {
                        mLicense.setError(getString(R.string.user_error));
                    } else if (mSpinnerChoice == null) {
                        final Toast toast = Toast.makeText(mContext, "You Must Choose An Employee",
                                Toast.LENGTH_LONG);
                        toast.show();
                    } else {
                        final AsyncTask<String, Void, String> task =
                                new UpdateUserTask(mListener, mSpinnerChoice,
                                mAddress.getText().toString(), mLicense.getText().toString());
                        final String url = APP_URL + "updateEMP.php?ssn=" + mSpinnerChoice
                                + "&add=" + mAddress.getText() + "&lic=" +
                                mLicense.getText();
                        Log.e("URL: ", url);
                        task.execute(url, "Update Employee");
                        getActivity().onBackPressed();
                    }
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Overridden onItemSelected() method.  Listens for interaction with the Spinner(s) and
     * determines their behavior.
     *
     * @param parent The parent spinner.
     * @param view The parent view.
     * @param position The current position of the spinner
     * @param id The id of the spinner.
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        mSpinnerChoice = (String) parent.getItemAtPosition(position);
        final AsyncTask<String, Void, String> task = new PopulateFields();
        final String url = APP_URL + "getEmpInfo.php?ssn=" + mSpinnerChoice;
        Log.e("URL: ", url);
        task.execute(url, "Populate Fields");
    }

    /**
     * Overridden method onNothingSelected().  Required to implement OnItemSelectedListener,
     * not used in this class.
     *
     * @param parent The parent spinner.
     */
    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

    /**
     * Customer Listener interface. Used to display the JSON return.
     */
    public interface UpdateEmpListener {
        void updateEmp(String result);
    }

    /**
     * Inner class Async Task, used to pre-populate the EditViews of the fragment with
     * their current value.
     */
    private class PopulateFields extends AsyncTask<String, Void, String> {

        /**
         * Overridden doInBackground() method.  Used to parse the JSON return from the
         * php url that is provided to the method.
         *
         * @param strings array of strings provided by the execute() method.
         * @return the result of the parsing as a string.
         */
        @Override
        protected String doInBackground(String... strings) {
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
            try {
                final JSONArray jsonArray = new JSONArray(result);
                mAddress.setText(jsonArray.getJSONObject(0).getString("emp_address"));
                mAddress.setHint(jsonArray.getJSONObject(0).getString("emp_address"));
                mLicense.setText(jsonArray.getJSONObject(0).getString("vehicleLNum"));
                mLicense.setHint(jsonArray.getJSONObject(0).getString("vehicleLNum"));
            } catch (Exception e) {
                Log.e("GrabUsersUnassigned", e.getMessage());
            }
        }
    }
}
