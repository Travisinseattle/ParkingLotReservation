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

import java.util.ArrayList;

import static group7.tcss450.uw.edu.parkinglotreservation.MainActivity.APP_URL;


/**
 * A simple {@link Fragment} subclass.
 */
public class AssignEmployeeSpaceFragment extends Fragment implements View.OnClickListener,
        AdapterView.OnItemSelectedListener {

    private AssignEmployeeSpaceFragment.AssignEmployeeSpaceListener mListener;
    private Spinner mEmpSpinner;
    private Spinner mSpaceSpinner;
    private Context mContext;
    private String mEmpChoice;
    private String mSpaceChoice;
    private EditText mRate;


    public AssignEmployeeSpaceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_assign_employee_space, container, false);
        Button mButton = (Button) v.findViewById(R.id.assign_space_button);
        mButton.setOnClickListener(this);
        mEmpSpinner = (Spinner) v.findViewById(R.id.assign_user_spinner);
        mEmpSpinner.setOnItemSelectedListener(this);
        mSpaceSpinner = (Spinner) v.findViewById(R.id.assign_space_spinner);
        mSpaceSpinner.setOnItemSelectedListener(this);
        mRate = (EditText) v.findViewById(R.id.rate_edit);
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        mListener = (AssignEmployeeSpaceFragment.AssignEmployeeSpaceListener) context;
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
        ArrayList<String> spaces;
        try {
            emps = getArguments().getStringArrayList("emps");
            spaces = getArguments().getStringArrayList("spaces");
            assert emps != null;
            spinnerArrayAdapter = new ArrayAdapter<String>(mContext,
                    android.R.layout.simple_spinner_item, emps);
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.
                    simple_spinner_dropdown_item); // The drop down view
            mEmpSpinner.setAdapter(spinnerArrayAdapter);

            assert spaces != null;
            spinnerArrayAdapter = new ArrayAdapter<String>(mContext,
                    android.R.layout.simple_spinner_item, spaces);
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.
                    simple_spinner_dropdown_item); // The drop down view
            mSpaceSpinner.setAdapter(spinnerArrayAdapter);
        } catch (Exception e) {
            Log.e("PopulateSpinAssignFail", e.getMessage());
        }
    }

    @Override
    public void onClick(View v) {
        if (mListener != null) {
            switch (v.getId()) {
                case R.id.assign_space_button:
                    AsyncTask<String, Void, String> task = null;
                    String id = mEmpChoice + ":" + mSpaceChoice;
                    task = new AssignEmpSpaceTask(mListener, id, mEmpChoice, mSpaceChoice,
                            mRate.getText().toString());
                    String url = APP_URL + "assignSpace.php?ssn='" + mEmpChoice
                            + "'&id='" + id + "'&space='" + mSpaceChoice + "'&rate='" +
                            mRate.getText().toString() + "'";
                    Log.e("URL: ", url);
                    task.execute(url, "Update Employee");
                    getActivity().onBackPressed();
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (view.getId()) {
            case R.id.assign_user_spinner:
                mEmpChoice = (String) parent.getItemAtPosition(position);
                Log.e("Spinner Choice:", mEmpChoice);
                break;
            case R.id.assign_space_spinner:
                mSpaceChoice = (String) parent.getItemAtPosition(position);
                Log.e("Spinner Choice:", mSpaceChoice);
                break;
            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public interface AssignEmployeeSpaceListener {
        void assignEmployeeSpace(String result);
    }
}
