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
public class AddLotFragment extends Fragment implements View.OnClickListener {

    private AddLotListener addLotListener;
    EditText mNameEdit;
    EditText mLocEdit;
    EditText mCapEdit;
    EditText mFloorEdit;


    public AddLotFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_add_lot, container, false);
        mNameEdit = (EditText) v.findViewById(R.id.lot_name_text);
        mLocEdit = (EditText) v.findViewById(R.id.lot_loc_text);
        mCapEdit = (EditText) v.findViewById(R.id.lot_cap_text);
        mFloorEdit = (EditText) v.findViewById(R.id.lot_numfloors_text);
        final Button b = (Button) v.findViewById(R.id.add_lot_button);
        b.setOnClickListener(this);
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        addLotListener = (AddLotListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        addLotListener = null;
    }

    @Override
    public void onClick(View v) {
        if (addLotListener != null) {
            switch (v.getId()) {
                case R.id.add_lot_button:
                    if (mNameEdit.getText().toString().trim().length() <= 0) {
                        mNameEdit.setError(getString(R.string.user_error));
                    } else if (mLocEdit.getText().toString().trim().length() <= 0) {
                        mLocEdit.setError(getString(R.string.user_error));
                    } else if (mCapEdit.getText().toString().trim().length() <= 0) {
                        mCapEdit.setError(getString(R.string.user_error));
                    } else if (mFloorEdit.getText().toString().trim().length() <= 0) {
                        mFloorEdit.setError(getString(R.string.user_error));
                    } else {
                        final AsyncTask<String, Void, String> task =
                                new AddNewLotTask(addLotListener, mNameEdit.getText().toString());
                        final String url = APP_URL + getString(R.string.add_lot_php) +
                                mNameEdit.getText().toString() + "&loc=" +
                                mLocEdit.getText().toString() +
                                "&cap=" + mCapEdit.getText() + "&nfloor=" +
                                mFloorEdit.getText().toString();
                        Log.e("URL: ", url);
                        task.execute(url, "Add New Lot");
                        getActivity().onBackPressed();
                    }
                    break;
                default:
                    break;
            }
        }
    }

    public interface AddLotListener {
        void addLot(String result);
    }
}
