package com.example.parkingappproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.parkingappproject.activities.SignInActivity;
import com.example.parkingappproject.ui.dashboard.AddParkingFragment;
import com.example.parkingappproject.ui.dashboard.ParkingListFragment;
import com.example.parkingappproject.ui.dashboard.ProfileFragment;
import com.example.parkingappproject.viewmodels.UserViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class DashboardMainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private UserViewModel userViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_main);
        this.userViewModel = UserViewModel.getInstance();

        bottomNavigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                switch (item.getItemId()) {
                    case R.id.navigation_profile:
//                        toolbar.setTitle("Profile");
                        loadFragment(new ProfileFragment());
                        return true;
                    case R.id.navigation_add_parking:
//                        toolbar.setTitle("Add Parking");
                        loadFragment(new AddParkingFragment());
                        return true;
                    case R.id.navigation_viewparking_list:
//                        toolbar.setTitle("View Parking List");
                        loadFragment(new ParkingListFragment());
                        return true;

                }
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadFragment(new ProfileFragment());
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { switch(item.getItemId()) {
        case R.id.add:
            //add the function to perform here
            this.userViewModel.getUserRepository().signInStatus.postValue("FAILURE");
            finish();
            Intent i=new Intent(getApplicationContext(),SignInActivity.class);
            startActivity(i);
            return(true);
    }
        return(super.onOptionsItemSelected(item));
    }

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.nav_host_fragment, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}