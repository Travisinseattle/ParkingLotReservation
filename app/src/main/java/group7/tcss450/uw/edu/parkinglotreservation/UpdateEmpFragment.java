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

import org.json.JSONObject;

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
    private Button mAdd;
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
        mAdd = (Button) v.findViewById(R.id.update_emp_button);
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
        ArrayAdapter<String> spinnerArrayAdapter;
        ArrayList<String> emps;
        try {
            emps = getArguments().getStringArrayList("emps");
            assert emps != null;
            spinnerArrayAdapter = new ArrayAdapter<String>(mContext,
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
                        Toast toast = Toast.makeText(mContext, "You Must Choose An Employee",
                                Toast.LENGTH_LONG);
                        toast.show();
                    } else {
                        AsyncTask<String, Void, String> task = null;
                        task = new UpdateUserTask(mContext, mListener, mSpinnerChoice,
                                mAddress.getText().toString(), mLicense.getText().toString());
                        String url = APP_URL + "updateEMP.php?ssn='" + mSpinnerChoice
                                + "'&add='" + mAddress.getText() + "'&lic='" +
                                mLicense.getText() + "'";
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
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public interface UpdateEmpListener {
        void updateEmp(String result);
    }
}
