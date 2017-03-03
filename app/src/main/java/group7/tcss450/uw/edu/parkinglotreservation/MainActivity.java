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

import group7.tcss450.uw.edu.parkinglotreservation.Fragments.AddEmpFragment;
import group7.tcss450.uw.edu.parkinglotreservation.Fragments.AddLotFragment;
import group7.tcss450.uw.edu.parkinglotreservation.Fragments.AddSpaceFragment;
import group7.tcss450.uw.edu.parkinglotreservation.Fragments.AssignEmployeeSpaceFragment;
import group7.tcss450.uw.edu.parkinglotreservation.Fragments.MainFragment;
import group7.tcss450.uw.edu.parkinglotreservation.Fragments.ReserveVisitorSpaceFragment;
import group7.tcss450.uw.edu.parkinglotreservation.Fragments.UpdateEmpFragment;

/**
 * The main activity of the application, launches all fragments and contains methods for all
 * listeners.
 */
public class MainActivity extends AppCompatActivity implements AddLotFragment.AddLotListener,
        AddSpaceFragment.AddSpaceListener, AddEmpFragment.AddEmpListener,
        UpdateEmpFragment.UpdateEmpListener, MainFragment.GetAllLots, MainFragment.GetAllUsers,
        AssignEmployeeSpaceFragment.AssignEmployeeSpaceListener,
        MainFragment.GetAllUnassignedUsers, MainFragment.GetAllVisitorsUsers,
        ReserveVisitorSpaceFragment.ReserveSpaceListener {

    /**
     * The constant representing the URL of the CSS server.
     */
    public final static String APP_URL =
            "http://cssgate.insttech.washington.edu/~thollow/parking_lot/";

    /**
     * The onCreate() method.
     *
     * @param savedInstanceState The Bundle containing the savedinstance info.
     */
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

    /**
     * AddLot method the implements the interface to launch a ResultDialog.  Used to pass
     * the result of the JSON request to the user if the result is an error, or to confirm
     * that the intended action was successful if the JSON request as successful.
     *
     * @param result The result of the JSON request as a string.
     */
    @Override
    public void addLot(String result) {
        final FragmentManager fragmentManager = getFragmentManager();
        ResultDialog resultDialog = new ResultDialog();
        final Bundle b = new Bundle();
        b.putString("result", result);
        resultDialog.setArguments(b);
        resultDialog.show(fragmentManager, "Add Lot");
    }

    /**
     * AddLot method the implements the interface to launch a ResultDialog.  Used to pass
     * the result of the JSON request to the user if the result is an error, or to confirm
     * that the intended action was successful if the JSON request as successful.
     *
     * @param result The result of the JSON request as a string.
     */
    @Override
    public void addSpace(String result) {
        final FragmentManager fragmentManager = getFragmentManager();
        ResultDialog resultDialog = new ResultDialog();
        final Bundle b = new Bundle();
        b.putString("result", result);
        resultDialog.setArguments(b);
        resultDialog.show(fragmentManager, "Add Space");
    }

    /**
     * AddLot method the implements the interface to launch a ResultDialog.  Used to pass
     * the result of the JSON request to the user if the result is an error, or to confirm
     * that the intended action was successful if the JSON request as successful.
     *
     * @param result The result of the JSON request as a string.
     */
    @Override
    public void addEmp(String result) {
        final FragmentManager fragmentManager = getFragmentManager();
        ResultDialog resultDialog = new ResultDialog();
        final Bundle b = new Bundle();
        b.putString("result", result);
        resultDialog.setArguments(b);
        resultDialog.show(fragmentManager, "Add Emp");
    }

    /**
     * AddLot method the implements the interface to launch a ResultDialog.  Used to pass
     * the result of the JSON request to the user if the result is an error, or to confirm
     * that the intended action was successful if the JSON request as successful.
     *
     * @param result The result of the JSON request as a string.
     */
    @Override
    public void updateEmp(String result) {
        final FragmentManager fragmentManager = getFragmentManager();
        ResultDialog resultDialog = new ResultDialog();
        final Bundle b = new Bundle();
        b.putString("result", result);
        resultDialog.setArguments(b);
        resultDialog.show(fragmentManager, "Update Employee");
    }

    /**
     * AddLot method the implements the interface to launch a ResultDialog.  Used to pass
     * the result of the JSON request to the user if the result is an error, or to confirm
     * that the intended action was successful if the JSON request as successful.
     *
     * @param result The result of the JSON request as a string.
     */
    @Override
    public void assignEmployeeSpace(String result) {
        final FragmentManager fragmentManager = getFragmentManager();
        ResultDialog resultDialog = new ResultDialog();
        final Bundle b = new Bundle();
        b.putString("result", result);
        resultDialog.setArguments(b);
        resultDialog.show(fragmentManager, "Assign Space Dialog");
    }

    /**
     * AddLot method the implements the interface to launch a ResultDialog.  Used to pass
     * the result of the JSON request to the user if the result is an error, or to confirm
     * that the intended action was successful if the JSON request as successful.
     *
     * @param result The result of the JSON request as a string.
     */
    @Override
    public void reserveSpace(String result) {
        final FragmentManager fragmentManager = getFragmentManager();
        ResultDialog resultDialog = new ResultDialog();
        final Bundle b = new Bundle();
        b.putString("result", result);
        resultDialog.setArguments(b);
        resultDialog.show(fragmentManager, "Assign Space Dialog");
    }

    /**
     * Method that implements the associated interface to populate a spinner.
     *
     * @param array The array of Strings used to populate the spinner.
     */
    @Override
    public void getAllLots(List<String> array) {
        final Bundle b = new Bundle();
        b.putStringArrayList("lots", (ArrayList<String>) array);
        final AddSpaceFragment spaceFragment = new AddSpaceFragment();
        spaceFragment.setArguments(b);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, spaceFragment, "Get Lots")
                .addToBackStack(null).commit();
    }

    /**
     * Method that implements the associated interface to populate a spinner.
     *
     * @param array The array of Strings used to populate the spinner.
     */
    @Override
    public void getAllUsersUpdate(List<String> array) {
        final Bundle b = new Bundle();
        b.putStringArrayList("emps", (ArrayList<String>) array);
        final UpdateEmpFragment  updateEmpFragment = new UpdateEmpFragment();
        updateEmpFragment.setArguments(b);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, updateEmpFragment, "Get Employees")
                .addToBackStack(null).commit();
    }

    /**
     * Method that implements the associated interface to populate a spinner.
     *
     * @param users The array of Strings used to populate the employee spinner.
     * @param spaces The array of Strings used to populate the parking space spinner.
     */
    @Override
    public void getAllUnassignedUsersUpdate(List<String> users, List<String> spaces) {
        final Bundle b = new Bundle();
        b.putStringArrayList("emps", (ArrayList<String>) users);
        b.putStringArrayList("spaces", (ArrayList<String>) spaces);
        final AssignEmployeeSpaceFragment frag = new AssignEmployeeSpaceFragment();
        frag.setArguments(b);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, frag, "Get Employees")
                .addToBackStack(null).commit();
    }

    /**
     * Method that implements the associated interface to populate a spinner.
     *
     * @param users The array of Strings used to populate the employee spinner.
     * @param spaces The array of Strings used to populate the parking space spinner.
     */
    @Override
    public void getAllVisitors(List<String> users, List<String> spaces) {
        final Bundle b = new Bundle();
        b.putStringArrayList("emps", (ArrayList<String>) users);
        b.putStringArrayList("spaces", (ArrayList<String>) spaces);
        final ReserveVisitorSpaceFragment frag = new ReserveVisitorSpaceFragment();
        frag.setArguments(b);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, frag, "Get Employees")
                .addToBackStack(null).commit();
    }

    /**
     * A class used to create a Dialog Fragment that either passes the error message of a
     * unsuccessful JSON request, or alerts the user that the request was successful.
     */
    public static class ResultDialog extends DialogFragment {

        /**
         * Overridden onCreateDialog method.
         *
         * @param savedInstanceState The bundle representing the saved instance info.
         * @return a Dialog containing the desired message.
         */
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            final String result = getArguments().getString("result");
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
