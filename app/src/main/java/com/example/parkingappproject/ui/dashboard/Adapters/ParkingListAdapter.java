package com.example.parkingappproject.ui.dashboard.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parkingappproject.R;
import com.example.parkingappproject.models.Parkings;

import java.util.ArrayList;
import java.util.List;

public class ParkingListAdapter extends RecyclerView.Adapter<ParkingListAdapter.MyViewHolder> {

    private List<Parkings> parkingsArrayList;
    private Context context;
    private onListClickListener onListClickListener;

    public ParkingListAdapter(List<Parkings> parkingsArrayList, Context context, ParkingListAdapter.onListClickListener onListClickListener) {
        this.parkingsArrayList = parkingsArrayList;
        this.context = context;
        this.onListClickListener = onListClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {

        View view = LayoutInflater.from(context).inflate(R.layout.fragment_notifications, parent, false);
        MyViewHolder myViewHolder= new MyViewHolder(view);


        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tv_date_time.setText(parkingsArrayList.get(position).getDate());
        holder.tv_address.setText(parkingsArrayList.get(position).getLocation());
        holder.linear_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onListClickListener.onListClick(parkingsArrayList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return parkingsArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tv_address,tv_date_time;
        LinearLayout linear_layout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_address=itemView.findViewById(R.id.tv_address);
            tv_date_time=itemView.findViewById(R.id.tv_date_time);
            linear_layout=itemView.findViewById(R.id.linear_layout);

        }
    }

    public interface onListClickListener{
        void onListClick(Parkings p);
    }
}
