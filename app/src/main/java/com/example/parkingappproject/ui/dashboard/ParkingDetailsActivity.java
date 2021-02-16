package com.example.parkingappproject.ui.dashboard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parkingappproject.R;
import com.example.parkingappproject.models.Parkings;

public class ParkingDetailsActivity extends AppCompatActivity {

    private TextView tv_address,tv_duration,tv_suiteno,tv_carplate, tv_datetime;
    Button bt_direction;
    Parkings parking;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_details);

        Intent intent=getIntent();
         parking=(Parkings)intent.getSerializableExtra("SelectedParking");
        Toast.makeText(this, ""+parking.getLocation(), Toast.LENGTH_SHORT).show();

        tv_address=findViewById(R.id.tv_address);
        tv_duration=findViewById(R.id.tv_duration);
        tv_suiteno=findViewById(R.id.tv_suiteno);
        tv_carplate=findViewById(R.id.tv_carplate);
        tv_datetime=findViewById(R.id.tv_datetime);
        bt_direction=findViewById(R.id.bt_direction);

        tv_address.setText(parking.getLocation());
        tv_duration.setText(parking.getTime());
        tv_carplate.setText(parking.getCarNumber());
        tv_datetime.setText(parking.getDate());
        tv_suiteno.setText(parking.getSuiteNumber());

        bt_direction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
Intent intent1=new Intent(getApplicationContext(),MapActivity.class);
intent.putExtra("SelectedParking",parking);
startActivity(intent1);
            }
        });

    }


}