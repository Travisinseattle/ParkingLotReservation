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

import java.util.List;

import group7.tcss450.uw.edu.parkinglotreservation.Tasks.GetLotsTask;
import group7.tcss450.uw.edu.parkinglotreservation.Tasks.GetUsersTask;
import group7.tcss450.uw.edu.parkinglotreservation.Tasks.GetUsersUnassignedTask;
import group7.tcss450.uw.edu.parkinglotreservation.Tasks.GetUsersVisitorsTask;
import group7.tcss450.uw.edu.parkinglotreservation.R;

import static group7.tcss450.uw.edu.parkinglotreservation.MainActivity.APP_URL;


/**
 * A fragment class used as the main menu of the app.
 */
public class MainFragment extends Fragment implements View.OnClickListener {

    /**
     * The GetAllLots listener.
     */
    private GetAllLots mGetLotListener;

    /**
     * The GetAllusers listener.
     */
    private GetAllUsers mGetUsersListener;

    /**
     * The GetAllUnassignedUsers listener.
     */
    private GetAllUnassignedUsers mGetAllUnassignedUsers;

    /**
     * The GetAllVisitorsUsers listener.
     */
    private GetAllVisitorsUsers mVisitorListener;

    /**
     * The AddLotFragment listener.
     */
    private AddLotFragment.AddLotListener mAddLotListener;

    /**
     * The UpdateEmpFragment listener.
     */
    private UpdateEmpFragment.UpdateEmpListener mUpdateEmpListener;

    /**
     * Required empty public constructor
     */
    public MainFragment() {}

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
        View theView = inflater.inflate(R.layout.fragment_main, container, false);
        Button b = (Button) theView.findViewById(R.id.add_lot);
        b.setOnClickListener(this);
        b = (Button) theView.findViewById(R.id.add_space);
        b.setOnClickListener(this);
        b = (Button) theView.findViewById(R.id.add_emp);
        b.setOnClickListener(this);
        b = (Button) theView.findViewById(R.id.update_employee);
        b.setOnClickListener(this);
        b = (Button) theView.findViewById(R.id.assign_space);
        b.setOnClickListener(this);
        b = (Button) theView.findViewById(R.id.reserve_space);
        b.setOnClickListener(this);
        return theView;
    }

    /**
     * Overridden onAttach() method.
     *
     * @param context The context of the super class.
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mGetLotListener = (GetAllLots) context;
        mAddLotListener = (AddLotFragment.AddLotListener) context;
        mGetUsersListener = (GetAllUsers) context;
        mUpdateEmpListener = (UpdateEmpFragment.UpdateEmpListener) context;
        mGetAllUnassignedUsers = (GetAllUnassignedUsers) context;
        mVisitorListener = (GetAllVisitorsUsers) context;
    }

    /**
     * Overridden onDetach() method. Sets the listener(s) NULL.
     */
    @Override
    public void onDetach() {
        super.onDetach();
    }

    /**
     * Overridden onClick method, determines the behavior of any objects that have a
     * onClickListener.
     *
     * @param v The parent view.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_lot:
                final AddLotFragment addLotFragment = new AddLotFragment();
                this.getFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, addLotFragment, "Add Lot Fragment")
                        .addToBackStack(null).commit();
                break;
            case R.id.add_space:
                 AsyncTask<String, Void, String> task =
                        new GetLotsTask(mGetLotListener, mAddLotListener);
                String url = APP_URL + "getLots.php";
                Log.e("URL: ", url);
                task.execute(url, "Get Lots");
                break;
            case R.id.add_emp:
                final AddEmpFragment addEmpFragment = new AddEmpFragment();
                this.getFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, addEmpFragment, "Add Employee Fragment")
                        .addToBackStack(null).commit();
                break;
            case R.id.update_employee:
                task = new GetUsersTask(mUpdateEmpListener, mGetUsersListener);
                url = APP_URL + "getEmps.php";
                Log.e("URL: ", url);
                task.execute(url, "Get Emps");
                break;
            case R.id.assign_space:
                task = new GetUsersUnassignedTask(mGetAllUnassignedUsers);
                url = APP_URL + "getSpace.php";
                Log.e("URL: ", url);
                task.execute(url, "Get Emps");
                break;
            case R.id.reserve_space:
                task = new GetUsersVisitorsTask(mUpdateEmpListener, mVisitorListener);
                url = APP_URL + "getVisitorSpaces.php";
                Log.e("URL: ", url);
                task.execute(url, "Get Emps");
                break;
            default:
                break;

        }
    }

    /**
     * Customer Listener interface. Used to pre-populate the spinner.
     */
    public interface GetAllLots {
        void getAllLots(List<String> array);
    }

    /**
     * Customer Listener interface. Used to pre-populate the spinner.
     */
    public interface GetAllUsers {
        void getAllUsersUpdate(List<String> array);
    }

    /**
     * Customer Listener interface. Used to pre-populate the spinner.
     */
    public interface GetAllVisitorsUsers {
        void getAllVisitors(List<String> users, List<String> spaces);
    }

    /**
     * Customer Listener interface. Used to pre-populate the spinner.
     */
    public interface GetAllUnassignedUsers {
        void getAllUnassignedUsersUpdate(List<String> users, List<String> spaces);
    }
}
