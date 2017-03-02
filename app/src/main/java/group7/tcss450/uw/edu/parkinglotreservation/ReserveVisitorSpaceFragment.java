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
public class ReserveVisitorSpaceFragment extends Fragment implements View.OnClickListener,
        AdapterView.OnItemSelectedListener {

    private ReserveSpaceListener mListener;
    private EditText mLicense;
    private EditText mDate;
    private Spinner mEmployee;
    private Spinner mSpace;
    private String mEmpChoice;
    private String mSpaceChoice;
    private Context mContext;

    public ReserveVisitorSpaceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_reserve_visitor_space, container, false);
        mLicense = (EditText) v.findViewById(R.id.vis_license_edit);
        mDate = (EditText) v.findViewById(R.id.visitor_date_edit);
        mEmployee = (Spinner) v.findViewById(R.id.visitor_employee_spinner);
        mEmployee.setOnItemSelectedListener(this);
        mSpace = (Spinner) v.findViewById(R.id.visitor_space_spinner);
        mSpace.setOnItemSelectedListener(this);
        final Button b = (Button) v.findViewById(R.id.reserve_space_button);
        b.setOnClickListener(this);
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        mListener = (ReserveSpaceListener) context;
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
        final ArrayList<String> emps;
        final ArrayList<String> spaces;
        try {
            emps = getArguments().getStringArrayList("emps");
            assert emps != null;
            spinnerArrayAdapter = new ArrayAdapter<>(mContext,
                    android.R.layout.simple_spinner_item, emps);
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.
                    simple_spinner_dropdown_item); // The drop down view
            mEmployee.setAdapter(spinnerArrayAdapter);
        } catch (Exception e) {
            Log.e("PopulateSpinVisFail", e.getMessage());
        }

        try {
            spaces = getArguments().getStringArrayList("spaces");
            assert spaces != null;
            spinnerArrayAdapter = new ArrayAdapter<>(mContext,
                    android.R.layout.simple_spinner_item, spaces);
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.
                    simple_spinner_dropdown_item); // The drop down view
            mSpace.setAdapter(spinnerArrayAdapter);
        } catch (Exception e) {
            Log.e("PopulateSpinSpaceFail", e.getMessage());
        }
    }

    @Override
    public void onClick(View v) {
        if (mListener != null) {
            switch (v.getId()) {
                case R.id.reserve_space_button:
                    final AsyncTask<String, Void, String> task =
                            new ReserveSpaceTask(mListener, mEmpChoice,
                            mLicense.getText().toString(),
                            mDate.getText().toString(), mSpaceChoice);
                    final String id = mEmpChoice + ":" + mSpaceChoice;
                    final String url = APP_URL + "reserveSpace.php?ssn=" + mEmpChoice
                            + "&id=" + id + "&space=" + mSpaceChoice + "&lic=" +
                            mLicense.getText().toString() + "&date=" + mDate.getText().toString();
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
        switch (parent.getId()) {
            case R.id.visitor_employee_spinner:
                mEmpChoice = (String) parent.getItemAtPosition(position);
                Log.e("Spinner Choice:", mEmpChoice);
                break;
            case R.id.visitor_space_spinner:
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

    public interface ReserveSpaceListener {
        void reserveSpace(String result);
    }
}
