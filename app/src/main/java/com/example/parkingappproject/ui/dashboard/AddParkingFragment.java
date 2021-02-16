package com.example.parkingappproject.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.example.parkingappproject.R;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddParkingFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private DashboardViewModel dashboardViewModel;
    private EditText et_buildingcode, et_car_plate_num, et_suite_num, et_parking_location;
    private Button bt_book;
    private Spinner sp_time;

    private String buildingCode, carNumber, suiteNumber, parkingLocation;
    private String time;

    private ArrayList<String> spinnerArrayList=new ArrayList<>();
    private ArrayAdapter spinnerAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_add_parking, container, false);

        et_buildingcode=root.findViewById(R.id.et_building_code);
        et_car_plate_num=root.findViewById(R.id.et_car_plate_num);
        et_suite_num=root.findViewById(R.id.et_suite_num);
        et_parking_location=root.findViewById(R.id.et_parking_location);
        bt_book=root.findViewById(R.id.bt_book);
        sp_time=root.findViewById(R.id.sp_time);

        spinnerArrayList.add("Select hours...");
        spinnerArrayList.add("1 hour or less");
        spinnerArrayList.add("4 hours");
        spinnerArrayList.add("12 hours");
        spinnerArrayList.add("24 hours");
        spinnerAdapter=new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item,spinnerArrayList);
        // Drop down layout style - list view with radio button
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        sp_time.setAdapter(spinnerAdapter);

        sp_time.setOnItemSelectedListener(this);
       bt_book.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               validation();
           }
       });
        return root;
    }

    private void validation() {

        Pattern pBuildingCode = Pattern.compile("^(?=.{0,}$*[A-Za-z])(?=.{0,}$*\\d)[A-Za-z\\d]{5,5}$");
        Pattern pCarPlate = Pattern.compile("^(?=.{0,}$*[A-Za-z])(?=.{0,}$*\\d)[A-Za-z\\d]{2,8}$");
        Pattern pSuiteNumber = Pattern.compile("^(?=.{0,}$*[A-Za-z])(?=.{0,}$*\\d)[A-Za-z\\d]{2,5}$");

        Matcher mBuuilding = pBuildingCode.matcher(et_buildingcode.getText().toString());
        Matcher mCar = pCarPlate.matcher(et_car_plate_num.getText().toString());
        Matcher mSuite = pSuiteNumber.matcher(et_suite_num.getText().toString());

        if(!mBuuilding.matches()){
            et_buildingcode.setError("Input should have exactly 5 alphanumerical characters");
            return;
        }
        else {
            et_buildingcode.setError(null);
            if(!mSuite.matches()){
                et_suite_num.setError("Input should have atleast 2 alphanumerical, max 5 char");
                return;
            }
            else {
                if(!mCar.matches()){
                    et_car_plate_num.setError("Input should have atleast 2 alphanumerical, max 8 char");
                    return;
                }
                else {

                    if(et_parking_location.getText().toString().isEmpty()){
                        et_parking_location.setError("Location should not be empty");
                    }
                    else {
                        if(time==null){
                            Toast.makeText(getContext(), "Please select the time slot", Toast.LENGTH_SHORT).show();
                        }
                        else {


                            Toast.makeText(getContext(), "Parking added.....", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

        }
//        if(et_buildingcode.getText().toString().isEmpty()||
//                et_parking_location.getText().toString().isEmpty()||
//                et_suite_num.getText().toString().isEmpty()||
//                et_car_plate_num.getText().toString().isEmpty()||time==null){
//
//            Toast.makeText(getContext(), "Please fill all fields...", Toast.LENGTH_SHORT).show();
//        }
//        else {
//
//            buildingCode=et_buildingcode.getText().toString();
//            suiteNumber=et_suite_num.getText().toString();
//            carNumber=et_car_plate_num.getText().toString();
//            parkingLocation=et_parking_location.getText().toString();
//
//
//
//        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(position!=0) {
            time = parent.getItemAtPosition(position).toString();
        }
        else {
            time=null;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
          time=null;
    }
}