package com.example.parkingappproject.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parkingappproject.DashboardMainActivity;
import com.example.parkingappproject.R;
import com.example.parkingappproject.viewmodels.UserViewModel;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener{

    private final String TAG = this.getClass().getCanonicalName();
    private TextView tvCreateAccount;
    private EditText edtEmail;
    private EditText edtPassword;
    private Button btnSignIn;
    private ProgressBar progressBar;
    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        this.userViewModel = UserViewModel.getInstance();

        this.tvCreateAccount = findViewById(R.id.tvCreateAccount);
        this.tvCreateAccount.setOnClickListener(this);

        this.edtEmail = findViewById(R.id.edtEmail);
        this.edtEmail.setText("surriyaraj@gmail.com");
        this.edtPassword = findViewById(R.id.edtPassword);
        this.edtPassword.setText("surriya123");

        this.btnSignIn = findViewById(R.id.btnSignIn);
        this.btnSignIn.setOnClickListener(this);

        this.progressBar = findViewById(R.id.progressBar);

        this.userViewModel.getUserRepository().signInStatus.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String status) {
                if(status.equals("SUCCESS")){
                    progressBar.setVisibility(View.INVISIBLE);
                    goToParking();
                }else if (status.equals("LOADING")){
                    progressBar.setVisibility(View.VISIBLE);
                }else if(status.equals("FAILURE")){
                    progressBar.setVisibility(View.GONE);
                    Log.d(TAG, "onChanged: Error Login");
                    Toast.makeText(getApplicationContext(),"Login Failed", Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    private void goToParking(){
        this.finish();
        Intent mainIntent = new Intent(this, DashboardMainActivity.class);
        startActivity(mainIntent);
    }
    

    @Override
    public void onClick(View view) {
        if(view != null){
            switch (view.getId()){
                case R.id.tvCreateAccount:{
                    Intent signUpIntent = new Intent(this, SignUpActivity.class);
                    startActivity(signUpIntent);
                    break;
                }
                case R.id.btnSignIn:{
                    if(this.validateData()){

                        //verify the user
                        this.validateLogin();
                            //go to main activity
                            //this.goToAddParking();

                    }
                    break;
                }
                default: break;
            }
        }
    }

    private void goToAddParking(){
        this.finish();
        Intent mainIntent = new Intent(this, DashboardMainActivity.class);
        startActivity(mainIntent);
    }

    private Boolean validateData() {
        if (this.edtEmail.getText().toString().isEmpty()) {
            this.edtEmail.setError("Please enter email");
            return false;
        }

        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(this.edtEmail.getText().toString()).matches()) {
            this.edtEmail.setError("Please enter a valid email");
        }

        if (this.edtPassword.getText().toString().isEmpty()){
            this.edtPassword.setError("Password cannot be empty");
        }
        return true;
    }

    private void validateLogin(){
        String email = this.edtEmail.getText().toString();
        String password = this.edtPassword.getText().toString();

        this.userViewModel.validateUser(email, password);

    }
}