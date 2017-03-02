package group7.tcss450.uw.edu.parkinglotreservation;


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

import static group7.tcss450.uw.edu.parkinglotreservation.MainActivity.APP_URL;


/**
 * A simple {@link Fragment} subclass.
 */
public class UpdateEmpFragment extends Fragment implements View.OnClickListener,
        AdapterView.OnItemSelectedListener {

    private UpdateEmpFragment.UpdateEmpListener mListener;
    private Context mContext;
    private EditText mAddress;
    private EditText mLicense;
    private Spinner mSpinner;
    private String mSpinnerChoice = null;


    public UpdateEmpFragment() {
        // Required empty public constructor
    }


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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        mListener = (UpdateEmpFragment.UpdateEmpListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        mSpinnerChoice = (String) parent.getItemAtPosition(position);
        final AsyncTask<String, Void, String> task = new PopulateFields();
        final String url = APP_URL + "getEmpInfo.php?ssn=" + mSpinnerChoice;
        Log.e("URL: ", url);
        task.execute(url, "Populate Fields");
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public interface UpdateEmpListener {
        void updateEmp(String result);
    }

    private class PopulateFields extends AsyncTask<String, Void, String> {

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
