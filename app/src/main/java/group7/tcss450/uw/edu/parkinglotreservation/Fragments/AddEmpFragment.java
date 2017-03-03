package group7.tcss450.uw.edu.parkinglotreservation.Fragments;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import group7.tcss450.uw.edu.parkinglotreservation.Tasks.AddNewEmpTask;
import group7.tcss450.uw.edu.parkinglotreservation.R;

import static group7.tcss450.uw.edu.parkinglotreservation.MainActivity.APP_URL;


/**
 * A fragment class used to add Employees to the data base.
 */
public class AddEmpFragment extends Fragment implements View.OnClickListener {

    /**
     * The listener defined in the class.
     */
    private AddEmpFragment.AddEmpListener mListener;

    /**
     * The first name EditText.
     */
    private EditText mFirstName;

    /**
     * The last name EditText.
     */
    private EditText mLastName;

    /**
     * The address EditText.
     */
    private EditText mAddress;

    /**
     * The SSN EditText.
     */
    private EditText mSSN;

    /**
     * The license EditText.
     */
    private EditText mLicenseNum;

    /**
     *  The add Button.
     */
    Button mAdd;

    /**
     * Required empty public constructor
     */
    public AddEmpFragment() {}

    /**
     * onCreate Method.
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
        View v = inflater.inflate(R.layout.fragment_add_emp, container, false);
        mFirstName = (EditText) v.findViewById(R.id.emp_first_name);
        mLastName = (EditText) v.findViewById(R.id.emp_last_name);
        mAddress = (EditText) v.findViewById(R.id.address);
        mSSN = (EditText) v.findViewById(R.id.ssn_edit);
        mLicenseNum = (EditText) v.findViewById(R.id.license);
        mAdd = (Button) v.findViewById(R.id.add_emp_button);
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
        mListener = (AddEmpFragment.AddEmpListener) context;
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
     * Overridden onClick method, determines the behavior of any objects that have a
     * onClickListener.
     *
     * @param v The parent view.
     */
    @Override
    public void onClick(View v) {
        if (mListener != null) {
            switch (v.getId()) {
                case R.id.add_emp_button:
                    if (mFirstName.getText().toString().trim().length() <= 0) {
                        mFirstName.setError(getString(R.string.user_error));
                    }  else if (mLastName.getText().toString().trim().length() <= 0) {
                        mLastName.setError(getString(R.string.user_error));
                    }  else if (mAdd.getText().toString().trim().length() <= 0) {
                        mAdd.setError(getString(R.string.user_error));
                    } else if (mSSN.getText().toString().trim().length() <= 0) {
                        mSSN.setError(getString(R.string.user_error));
                    } else if (mLicenseNum.getText().toString().trim().length() <= 0) {
                        mLicenseNum.setError(getString(R.string.user_error));
                    } else {
                        AsyncTask<String, Void, String> task =
                                new AddNewEmpTask(mListener, mFirstName.getText().toString(),
                                mLastName.getText().toString(),
                                mAddress.getText().toString(),
                                mSSN.getText().toString(),
                                mLicenseNum.getText().toString());
                        String url = APP_URL + "addEmp.php?fname=" +
                                mFirstName.getText().toString() + "&lname=" +
                                mLastName.getText().toString() + "&add=" +
                                mAddress.getText().toString() + "&ssn=" +
                                mSSN.getText().toString() + "&lic=" +
                                mLicenseNum.getText().toString();
                        Log.e("URL: ", url);
                        task.execute(url, "Add New Employee");
                        getActivity().onBackPressed();
                    }
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Customer Listener interface. Used to display the JSON return.
     */
    public interface AddEmpListener {
        void addEmp(String result);
    }
}
