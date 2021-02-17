package com.example.parkingappproject.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.parkingappproject.R;
import com.example.parkingappproject.models.User;
import com.example.parkingappproject.viewmodels.UserViewModel;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = this.getClass().getCanonicalName();
    private Button btnCreateAccount;
    private EditText edtEmail;
    private EditText edtPassword;
    private EditText edtConfirmPassword;
    private EditText edtName;
    private EditText edtContactNumber;
    private EditText edtCarPlateNumber;
    private UserViewModel userViewModel;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        this.userViewModel = UserViewModel.getInstance();

        this.edtEmail = findViewById(R.id.edtEmail);
        this.edtPassword = findViewById(R.id.edtPassword);
        this.edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        this.edtName = findViewById(R.id.edtName);
        this.edtContactNumber = findViewById(R.id.edtContactNumber);
        this.edtCarPlateNumber = findViewById(R.id.edtCarPlateNumber);
        this.btnCreateAccount = findViewById(R.id.btnCreateAccount);
        this.btnCreateAccount.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        if( view != null){
            switch (view.getId()){
                case R.id.btnCreateAccount: {
                    if (this.validateData()){
                        //save data to database
                        this.saveUserToDB();

                        //go to main activity
                        this.goToMain();
                    }
                }
                default:
                    break;
            }
        }

    }

    private void saveUserToDB(){
        User newUser = new User();

        newUser.setEmail(this.edtEmail.getText().toString());
        newUser.setName(this.edtName.getText().toString());
        newUser.setPassword(this.edtPassword.getText().toString());
        newUser.setCarPlateNumber(this.edtCarPlateNumber.getText().toString());
        newUser.setContactNumber(this.edtContactNumber.getText().toString());

        this.userViewModel.addUser(newUser);
    }

    private void goToMain(){
        this.finish();
        Intent mainIntent = new Intent(this, SignInActivity.class);
        startActivity(mainIntent);
    }



    private Boolean validateData(){
        if (this.edtEmail.getText().toString().isEmpty()){
            this.edtEmail.setError("Please enter email");
            return false;
        }

       
        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(this.edtEmail.getText().toString()).matches()) {
            this.edtEmail.setError("Please enter a valid email");
            return false;
        }
        

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

}