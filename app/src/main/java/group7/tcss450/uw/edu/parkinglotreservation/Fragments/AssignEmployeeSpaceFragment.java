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

import java.util.ArrayList;

import group7.tcss450.uw.edu.parkinglotreservation.Tasks.AssignEmpSpaceTask;
import group7.tcss450.uw.edu.parkinglotreservation.R;

import static group7.tcss450.uw.edu.parkinglotreservation.MainActivity.APP_URL;


/**
 * A fragment class used to assign employee parking spaces.
 */
public class AssignEmployeeSpaceFragment extends Fragment implements View.OnClickListener,
        AdapterView.OnItemSelectedListener {

    /**
     * The listener defined in the class.
     */
    private AssignEmployeeSpaceFragment.AssignEmployeeSpaceListener mListener;

    /**
     * The spinner representing employees.
     */
    private Spinner mEmpSpinner;

    /**
     * The spinner representing spaces.
     */
    private Spinner mSpaceSpinner;

    /**
     * The context of the parent activity.
     */
    private Context mContext;

    /**
     * The String representing the employee chosen by the employee spinner.
     */
    private String mEmpChoice;

    /**
     * The String representing space chosen by the space spinner.
     */
    private String mSpaceChoice;

    /**
     *
     */
    private EditText mRate;

    /**
     * Required empty public constructor
     */
    public AssignEmployeeSpaceFragment() {}

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
        final View v = inflater.inflate(R.layout.fragment_assign_employee_space, container, false);
        final Button mButton = (Button) v.findViewById(R.id.assign_space_button);
        mButton.setOnClickListener(this);
        mEmpSpinner = (Spinner) v.findViewById(R.id.assign_user_spinner);
        mEmpSpinner.setOnItemSelectedListener(this);
        mSpaceSpinner = (Spinner) v.findViewById(R.id.assign_space_spinner);
        mSpaceSpinner.setOnItemSelectedListener(this);
        mRate = (EditText) v.findViewById(R.id.rate_edit);
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
        mListener = (AssignEmployeeSpaceFragment.AssignEmployeeSpaceListener) context;
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
        ArrayAdapter<String> spinnerArrayAdapter;
        final ArrayList<String> emps;
        final ArrayList<String> spaces;
        try {
            emps = getArguments().getStringArrayList("emps");
            assert emps != null;
            spinnerArrayAdapter = new ArrayAdapter<>(mContext,
                    android.R.layout.simple_spinner_item, emps);
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.
                    simple_spinner_dropdown_item); // The drop down view
            mEmpSpinner.setAdapter(spinnerArrayAdapter);
        } catch (Exception e) {
            Log.e("PopulateSpinUsersFail", e.getMessage());
        }

        try {
            spaces = getArguments().getStringArrayList("spaces");
            assert spaces != null;
            spinnerArrayAdapter = new ArrayAdapter<>(mContext,
                    android.R.layout.simple_spinner_item, spaces);
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.
                    simple_spinner_dropdown_item); // The drop down view
            mSpaceSpinner.setAdapter(spinnerArrayAdapter);
        } catch (Exception e) {
            Log.e("PopulateSpinSpaceFail", e.getMessage());
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
                case R.id.assign_space_button:
                    final AsyncTask<String, Void, String> task =
                            new AssignEmpSpaceTask(mListener, mEmpChoice, mSpaceChoice,
                            mRate.getText().toString());
                    final String id = mEmpChoice + ":" + mSpaceChoice;
                    final String url = APP_URL + "assignSpace.php?ssn=" + mEmpChoice
                            + "&id=" + id + "&space=" + mSpaceChoice + "&rate=" +
                            mRate.getText().toString();
                    Log.e("URL: ", url);
                    task.execute(url, "Update Employee");
                    getActivity().onBackPressed();
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
        switch (parent.getId()) {
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
    public interface AssignEmployeeSpaceListener {
        void assignEmployeeSpace(String result);
    }
}
