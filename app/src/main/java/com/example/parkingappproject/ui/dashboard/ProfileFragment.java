package com.example.parkingappproject.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.parkingappproject.R;
import com.example.parkingappproject.activities.SignInActivity;
import com.example.parkingappproject.activities.SignUpActivity;
import com.example.parkingappproject.models.User;
import com.example.parkingappproject.viewmodels.UserViewModel;

public class ProfileFragment extends Fragment implements View.OnClickListener{

    private final String TAG = this.getClass().getCanonicalName();
    private Button btnUpdateAccount;
    private Button btnDeleteAccount;
    private Button btnGetAccountInfo;
    private TextView tvEmail;
    private EditText edtPassword;
    private EditText edtConfirmPassword;
    private EditText edtName;
    private EditText edtContactNumber;
    private EditText edtCarPlateNumber;
    private UserViewModel userViewModel;
    private User userInfo;
    private String userID;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);



        this.userViewModel = UserViewModel.getInstance();
        
        this.tvEmail = root.findViewById(R.id.tvUEmail);
        this.edtPassword = root.findViewById(R.id.edtUPassword);
        this.edtConfirmPassword = root.findViewById(R.id.edtUConfirmPassword);
        this.edtName = root.findViewById(R.id.edtUName);
        this.edtContactNumber = root.findViewById(R.id.edtUContactNumber);
        this.edtCarPlateNumber = root.findViewById(R.id.edtUCarPlateNumber);
        this.btnUpdateAccount = root.findViewById(R.id.btnUpdateAccount);
        this.btnDeleteAccount = root.findViewById(R.id.btnDeleteAccount);
        this.btnGetAccountInfo = root.findViewById(R.id.btnGetAccountInfo);
        this.btnUpdateAccount.setOnClickListener(this);
        this.btnDeleteAccount.setOnClickListener(this);
        this.btnGetAccountInfo.setOnClickListener(this);
        this.clearFields();

        userID = this.userViewModel.getUserRepository().loggedInUserID.getValue();
        userInfo =  this.userViewModel.getUpdateUserInfo(userID);


        return root;
    }

    @Override
    public void onClick(View view) {
        if(view != null){
            switch (view.getId()){
                case R.id.btnUpdateAccount:{
                    if (this.validateData()) {

                        this.updateUserToDB();

                        Toast.makeText(getContext(),"Profile Updated Successfully",Toast.LENGTH_LONG).show();
                        Fragment fragment = new AddParkingFragment();
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.nav_host_fragment, fragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                    break;
                }
                case R.id.btnGetAccountInfo:{
                    this.getAccountInfo();
                    break;
                }
                case R.id.btnDeleteAccount:{
                    this.userViewModel.deleteUser(userID);
                    Intent i = new Intent(getContext(), SignInActivity.class);
                    startActivity(i);



                }
                default: 
                    break;
            }
        }
        
    }

    private void getAccountInfo(){
        Log.d(TAG, "onCreateView: user info in the profile fragment: " +userInfo.getEmail());

        if(userInfo != null) {
            this.tvEmail.setText(this.userViewModel.getUserRepository().newUserInfo.getEmail());
            this.edtCarPlateNumber.setText(this.userViewModel.getUserRepository().newUserInfo.getCarPlateNumber());
            this.edtConfirmPassword.setText(this.userViewModel.getUserRepository().newUserInfo.getPassword());
            this.edtPassword.setText(this.userViewModel.getUserRepository().newUserInfo.getPassword());
            this.edtName.setText(this.userViewModel.getUserRepository().newUserInfo.getName());
            this.edtContactNumber.setText(this.userViewModel.getUserRepository().newUserInfo.getContactNumber());
        }
    }

    private void updateUserToDB(){
        User newUser = new User();

        newUser.setEmail(this.tvEmail.getText().toString());
        newUser.setName(this.edtName.getText().toString());
        newUser.setPassword(this.edtPassword.getText().toString());
        newUser.setCarPlateNumber(this.edtCarPlateNumber.getText().toString());
        newUser.setContactNumber(this.edtContactNumber.getText().toString());

        this.userViewModel.updateUser(newUser);
    }
    
    private Boolean validateData(){
        
        

        if (this.edtPassword.getText().toString().isEmpty()){
            this.edtPassword.setError("Password cannot be empty");
            return false;
        }

        if (this.edtConfirmPassword.getText().toString().isEmpty()){
            this.edtConfirmPassword.setError("Please provide confirm password");
            return false;
        }

        if (!edtPassword.getText().toString().equals(this.edtConfirmPassword.getText().toString())){
            this.edtPassword.setError("Both passwords must be same");
            this.edtConfirmPassword.setError("Both passwords must be same");
            return false;
        }

        if (this.edtContactNumber.getText().toString().isEmpty()){
            this.edtContactNumber.setError("Contact Number cannot be empty");
            return false;
        }

        if (this.edtCarPlateNumber.getText().toString().isEmpty()){
            this.edtCarPlateNumber.setError("Car Plate Number cannot be empty");
            return false;
        }

        return true;
    }

    private void clearFields(){
        edtName.setText("");
        edtPassword.setText("");
        edtConfirmPassword.setText("");
        edtContactNumber.setText("");
        edtCarPlateNumber.setText("");
    }
}
