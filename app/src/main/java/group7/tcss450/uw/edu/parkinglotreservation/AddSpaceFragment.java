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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import static group7.tcss450.uw.edu.parkinglotreservation.MainActivity.APP_URL;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddSpaceFragment extends Fragment implements View.OnClickListener,
        AdapterView.OnItemSelectedListener, android.widget.CompoundButton.OnCheckedChangeListener {


    private AddSpaceFragment.AddSpaceListener mListener;
    private Context mContext;
    private Spinner mSpinner;
    private EditText mSpaceNum;
    private String mSpinnerChoice = null;
    private boolean mCovChecked = false;
    private boolean mVisChecked = false;

    public AddSpaceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_add_space, container, false);
        //mEditLotName = (EditText) v.findViewById(R.id.lot_name_for_space);
        mSpinner = (Spinner) v.findViewById(R.id.lots_spinner);
        mSpinner.setOnItemSelectedListener(this);
        mSpaceNum = (EditText) v.findViewById(R.id.space_number);
        final CheckBox covered = (CheckBox) v.findViewById(R.id.covered_checkbox);
        covered.setOnCheckedChangeListener(this);
        final CheckBox visitor = (CheckBox) v.findViewById(R.id.visitor_checkbox);
        visitor.setOnCheckedChangeListener(this);
        Button b = (Button) v.findViewById(R.id.add_space_button);
        b.setOnClickListener(this);
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        mListener = (AddSpaceFragment.AddSpaceListener) context;
    }

    @Override
    public void onStart() {
        super.onStart();
        ArrayAdapter<String> spinnerArrayAdapter;
        ArrayList<String> lots;
        try {
            lots = getArguments().getStringArrayList("lots");
            assert lots != null;
            spinnerArrayAdapter = new ArrayAdapter<String>(mContext,
                    android.R.layout.simple_spinner_item, lots);
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.
                    simple_spinner_dropdown_item); // The drop down view
            mSpinner.setAdapter(spinnerArrayAdapter);
        } catch (Exception e) {
            Log.e("AddSpace Array Fail", e.getMessage());
        }
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
                case R.id.add_space_button:
                    if (mSpaceNum.getText().toString().trim().length() <= 0) {
                        mSpaceNum.setError(getString(R.string.user_error));
                    } else if (mSpinnerChoice == null) {
                        Toast toast = Toast.makeText(mContext, "You Must Choose A Lot",
                                Toast.LENGTH_LONG);
                        toast.show();
                    } else {
                        AsyncTask<String, Void, String> task = null;
                        final String spaceID = mSpinnerChoice + ":" + mSpaceNum.getText();
                        task = new AddNewSpaceTask(mContext, mListener, mSpinnerChoice,
                                mSpaceNum.getText().toString());
                        String url = APP_URL +
                                getString(R.string.add_space_php_text) + mSpinnerChoice
                                + "'&cov=" + mCovChecked + "&vis=" + mVisChecked +
                                "&space_num='"  + mSpaceNum.getText() + "'&spaceID='" +
                                spaceID + "'";
                        Log.e("URL: ", url);
                        task.execute(url, "Add New Space");
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

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.covered_checkbox:
                mCovChecked = isChecked;
                Log.e("CLICKED COV", String.valueOf(isChecked));
                break;
            case R.id.visitor_checkbox:
                mVisChecked = isChecked;
                Log.e("CLICKED VIS", String.valueOf(isChecked));
                break;
            default:
                break;
        }
        mCovChecked = isChecked;
    }

    public interface AddSpaceListener {
        void addSpace(String result);
    }

}
