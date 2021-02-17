package com.example.parkingappproject.ui.dashboard;
//Venkat 101287100
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parkingappproject.R;
import com.example.parkingappproject.models.Parkings;
import com.example.parkingappproject.ui.dashboard.Adapters.ParkingListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ParkingListFragment extends Fragment implements ParkingListAdapter.onListClickListener{

    private RecyclerView parkingRecyclerview;
    private ParkingListAdapter parkingListAdapter;
    private List<Parkings> parkingsArrayList=new ArrayList<>();
    private FirebaseFirestore db;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_parking_list, container, false);
        db = FirebaseFirestore.getInstance();
        readDataFirebase();
        parkingRecyclerview=root.findViewById(R.id.parkingRecyclerview);
        parkingRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));

        parkingListAdapter=new ParkingListAdapter(parkingsArrayList,getActivity(),this::onListClick);
        parkingRecyclerview.setAdapter(parkingListAdapter);

        return root;
    }

    @Override
    public void onListClick(Parkings p) {

        Intent intent=new Intent(getContext(),ParkingDetailsActivity.class);
        intent.putExtra("SelectedParking",p);
        startActivity(intent);

    }

    private void readDataFirebase(){

        db.collection("Parkings")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("Parkinglistfragment", document.getId() + " => " + document.getData());
                                Parkings p=new Parkings();
                                p.setBuildingNumber((String) document.getData().get("buildingNumber"));
                                p.setTime((String) document.getData().get("time"));
                                p.setSuiteNumber((String) document.getData().get("suiteNumber"));
                                p.setCarNumber((String) document.getData().get("buildingNumber"));
                                p.setLocation((String) document.getData().get("location"));
                                p.setLatitude((Double) document.getData().get("latitude"));
                                p.setLongitude((Double) document.getData().get("longitude"));
                                p.setDate((String) document.getData().get("date"));

                                parkingsArrayList.add(p);
                            }
                            parkingListAdapter.notifyDataSetChanged();
                        } else {
                            Log.w("Parkinglistfragment", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

}
