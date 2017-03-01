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

import java.util.List;

import static group7.tcss450.uw.edu.parkinglotreservation.MainActivity.APP_URL;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment implements View.OnClickListener {

    private MainFragment.GetAllLots mGetLotListener;
    private MainFragment.GetAllUsers mGetUsersListener;
    private GetAllSpaces mGetAllLicensesListener;
    private AddLotFragment.AddLotListener mAddLotListener;
    private UpdateEmpFragment.UpdateEmpListener mUpdateEmpListener;
    private Context mContext;


    public MainFragment() {
        // Required empty public constructor
    }


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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        mGetLotListener = (MainFragment.GetAllLots) context;
        mAddLotListener = (AddLotFragment.AddLotListener) context;
        mGetUsersListener = (MainFragment.GetAllUsers) context;
        mUpdateEmpListener = (UpdateEmpFragment.UpdateEmpListener) context;
        mGetAllLicensesListener = (GetAllSpaces) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_lot:
                AddLotFragment addLotFragment = new AddLotFragment();
                this.getFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, addLotFragment, "Add Lot Fragment")
                        .addToBackStack(null).commit();
                break;
            case R.id.add_space:
                AsyncTask<String, Void, String> task = null;
                task = new GetLotsTask(mContext, mGetLotListener, mAddLotListener);
                String url = APP_URL + "getLots.php";
                Log.e("URL: ", url);
                task.execute(url, "Get Lots");
                break;
            case R.id.add_emp:
                AddEmpFragment addEmpFragment = new AddEmpFragment();
                this.getFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, addEmpFragment, "Add Employee Fragment")
                        .addToBackStack(null).commit();
                break;
            case R.id.update_employee:
                task = new GetUsersTask(mUpdateEmpListener, mGetUsersListener, 0);
                url = APP_URL + "getEmps.php";
                Log.e("URL: ", url);
                task.execute(url, "Get Emps");
            case R.id.assign_space:
                task = new GetUsersTask(mUpdateEmpListener, mGetUsersListener, 1);
                url = APP_URL + "getEmps.php";
                Log.e("URL: ", url);
                task.execute(url, "Get Emps");
                task = new GetAvailableParkingSpacesTask(mUpdateEmpListener, mGetAllLicensesListener);
                        url = APP_URL + "getSpace.php";
                Log.e("URL: ", url);
                task.execute(url, "Get Licenses");
                break;
            case R.id.reserve_space:
//                ReserveVisitorFragment reserveVisitorFragment = new ReserveVisitorFragment();
//                this.getFragmentManager().beginTransaction()
//                        .replace(R.id.fragmentContainer, reserveVisitorFragment, "Reserve Visitor Space Fragment")
//                        .addToBackStack(null).commit();
                break;
            default:
                break;

        }
    }

    public interface GetAllLots {
        void getAllLots(List<String> array);
    }

    public interface GetAllUsers {
        void getAllUsersUpdate(List<String> array, int value);
    }

    public interface GetAllSpaces {
        void getAllSpaces(List<String> array);
    }

}
