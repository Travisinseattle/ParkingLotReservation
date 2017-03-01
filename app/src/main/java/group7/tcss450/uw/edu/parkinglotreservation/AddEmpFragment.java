package group7.tcss450.uw.edu.parkinglotreservation;


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

import static group7.tcss450.uw.edu.parkinglotreservation.MainActivity.APP_URL;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddEmpFragment extends Fragment implements View.OnClickListener {

    private AddEmpFragment.AddEmpListener mListener;
    private Context mContext;
    EditText mFirstName;
    EditText mLastName;
    EditText mAddress;
    EditText mSSN;
    EditText mLicenseNum;
    Button mAdd;


    public AddEmpFragment() {
        // Required empty public constructor
    }


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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        mListener = (AddEmpFragment.AddEmpListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

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
                        AsyncTask<String, Void, String> task = null;
                        task = new AddNewEmpTask(mListener);
                        String url = APP_URL + "addEmp.php?fname='" + mFirstName.getText() +
                                "'&lname='" + mLastName.getText() + "'&add='" +
                                mAddress.getText() + "'&ssn='" + mSSN.getText() + "'&lic='" +
                                mLicenseNum.getText() + "'";
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

    public interface AddEmpListener {
        void addEmp(String result);
    }
}
