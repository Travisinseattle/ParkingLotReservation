package group7.tcss450.uw.edu.parkinglotreservation;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AddLotFragment.AddLotListener,
        AddSpaceFragment.AddSpaceListener, AddEmpFragment.AddEmpListener,
        UpdateEmpFragment.UpdateEmpListener, MainFragment.GetAllLots, MainFragment.GetAllUsers,
        MainFragment.GetAllSpaces {

    private List<String> mSSN;
    private List<String> mLicense;

    public final static String APP_URL =
            "http://cssgate.insttech.washington.edu/~thollow/parking_lot/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            if (findViewById(R.id.fragmentContainer) != null) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragmentContainer, new MainFragment())
                        .commit();
            }
        }
    }


    @Override
    public void addLot(String result) {
        FragmentManager fragmentManager = getFragmentManager();
        ResultDialog resultDialog = new ResultDialog();
        Bundle b = new Bundle();
        b.putString("result", result);
        resultDialog.setArguments(b);
        resultDialog.show(fragmentManager, "Add Lot");
    }

    @Override
    public void addSpace(String result) {
        FragmentManager fragmentManager = getFragmentManager();
        ResultDialog resultDialog = new ResultDialog();
        Bundle b = new Bundle();
        b.putString("result", result);
        resultDialog.setArguments(b);
        resultDialog.show(fragmentManager, "Add Space");
    }

    @Override
    public void addEmp(String result) {
        FragmentManager fragmentManager = getFragmentManager();
        ResultDialog resultDialog = new ResultDialog();
        Bundle b = new Bundle();
        b.putString("result", result);
        resultDialog.setArguments(b);
        resultDialog.show(fragmentManager, "Add Emp");
    }

    @Override
    public void updateEmp(String result) {
        FragmentManager fragmentManager = getFragmentManager();
        ResultDialog resultDialog = new ResultDialog();
        Bundle b = new Bundle();
        b.putString("result", result);
        resultDialog.setArguments(b);
        resultDialog.show(fragmentManager, "Update Employee");
    }

    @Override
    public void getAllLots(List<String> array) {
        Bundle b = new Bundle();
        b.putStringArrayList("lots", (ArrayList<String>) array);
        AddSpaceFragment spaceFragment = new AddSpaceFragment();
        spaceFragment.setArguments(b);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, spaceFragment, "Get Lots")
                .addToBackStack(null).commit();
    }

    @Override
    public void getAllUsersUpdate(List<String> array, int value) {
        Bundle b = new Bundle();
        b.putStringArrayList("emps", (ArrayList<String>) array);
        switch (value) {
            case 0:
                UpdateEmpFragment  updateEmpFragment = new UpdateEmpFragment();
                updateEmpFragment.setArguments(b);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, updateEmpFragment, "Get Employees")
                        .addToBackStack(null).commit();
                break;
            case 1:
                mSSN = array;
                break;
            case 2:
                break;
            default:
                break;
        }
    }

    @Override
    public void getAllSpaces(List<String> array) {
        Bundle b = new Bundle();
        b.putStringArrayList("emps", (ArrayList<String>) mSSN);
        b.putStringArrayList("spaces", (ArrayList<String>) array);
    }


    public static class ResultDialog extends DialogFragment {


        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            String result = String.valueOf(getArguments());
            builder.setTitle("Activity Result");
            builder.setMessage(result);

            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dismiss();
                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dismiss();
                }
            });

            return builder.create();
        }
    }
}