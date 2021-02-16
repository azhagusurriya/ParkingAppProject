package com.example.parkingappproject.ui.dashboard;

import android.content.Context;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
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
import com.example.parkingappproject.models.Parkings;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.type.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddParkingFragment extends Fragment implements AdapterView.OnItemSelectedListener {


    private EditText et_buildingcode, et_car_plate_num, et_suite_num, et_parking_location;
    private Button bt_book;
    private Spinner sp_time;

    private String buildingCode, carNumber, suiteNumber, parkingLocation;
    private String time;
    private String currentDateTime;
    private double latitude;
    private double longitude;

    private ArrayList<String> spinnerArrayList=new ArrayList<>();
    private ArrayAdapter spinnerAdapter;

    private  FirebaseFirestore db;
    private ArrayList<Parkings> parkingsArrayList= new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_add_parking, container, false);

        db = FirebaseFirestore.getInstance();
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
            buildingCode=et_buildingcode.getText().toString();
            if(!mSuite.matches()){
                et_suite_num.setError("Input should have atleast 2 alphanumerical, max 5 char");
                return;
            }
            else {
                et_suite_num.setError(null);
                suiteNumber=et_suite_num.getText().toString();
                if(!mCar.matches()){
                    et_car_plate_num.setError("Input should have atleast 2 alphanumerical, max 8 char");
                    return;
                }
                else {
                    et_car_plate_num.setError(null);
                    carNumber=et_car_plate_num.getText().toString();
                    if(et_parking_location.getText().toString().isEmpty()){
                        et_parking_location.setError("Location should not be empty");
                    }
                    else {
                        et_parking_location.setError(null);
                        parkingLocation=et_parking_location.getText().toString();
                        if(time==null){
                            Toast.makeText(getContext(), "Please select the time slot", Toast.LENGTH_SHORT).show();
                        }
                        else {

                            getCurrentDateTime();
                            try {
                                getLocationFromAddress(parkingLocation);
                            }catch (Exception e){
                                e.printStackTrace();
                                Toast.makeText(getContext(), ""+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                return;
                            }

                            Parkings p= new Parkings();
                            p.setBuildingNumber(buildingCode);
                            p.setSuiteNumber(suiteNumber);
                            p.setCarNumber(carNumber);
                            p.setTime(time);
                            p.setLocation(parkingLocation);
                            p.setLatitude(latitude);
                            p.setLongitude(longitude);
                            p.setDate(currentDateTime);

                            storeFirebase(p);


                        }
                    }
                }
            }

        }

    }
    public String getLocationFromAddress(String strAddress) throws IOException {

        Geocoder coder = new Geocoder(getContext());
        List<Address> address;
        GeoPoint p1 = null;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
             latitude = location.getLatitude();
             longitude = location.getLongitude();

            return latitude+","+longitude;
        } catch (Exception e){
            e.printStackTrace();
        }
    return null;
    }
    private void getCurrentDateTime() {
        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
         currentDateTime = df.format(Calendar.getInstance().getTime());
        Toast.makeText(getContext(), ""+currentDateTime, Toast.LENGTH_SHORT).show();
    }

    private void storeFirebase(Parkings parkings){
        // Add a new document with a generated ID
        db.collection("Parkings")
                .add(parkings)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("AddparkingFragment", "DocumentSnapshot added with ID: " + documentReference.getId());
                        Toast.makeText(getContext(), "Parking added.....", Toast.LENGTH_SHORT).show();
                        clearFields();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("AddparkingFragment", "Error adding document", e);
                        Toast.makeText(getContext(), ""+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
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

    private void clearFields(){
        et_buildingcode.setText("");
        et_suite_num.setText("");
        et_car_plate_num.setText("");
        et_parking_location.setText("");
        sp_time.setSelection(0);
    }
}