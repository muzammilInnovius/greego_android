package com.greegoapp.Activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;

import com.greegoapp.Adapter.TripHistoryAdapter;
import com.greegoapp.Interface.RecyclerViewItemClickListener;
import com.greegoapp.Model.TripHistoryModel;
import com.greegoapp.R;
import com.greegoapp.databinding.ActivityTripHistoryBinding;

import java.util.ArrayList;
import java.util.List;

public class TripHistoryActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityTripHistoryBinding binding;
    Context context;
    RecyclerView rvTripHistory;
    ImageButton ibback;
    List<TripHistoryModel> tripHistoryList;
    TripHistoryAdapter tripHistoryAdapter;
    TripHistoryModel tripHistoryModel;
    View snackBarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_trip_history);

        snackBarView = findViewById(android.R.id.content);
        context = TripHistoryActivity.this;

        bindViews();
        setRecyclerView();
        setListner();


    }

    private void setRecyclerView() {
        RecyclerViewItemClickListener mListener = new RecyclerViewItemClickListener() {
            @Override
            public void onClick(View view, int position) {
//                Fragment fragment = new TripHistoryDetailFragment();
//                FragmentManager fragmentManager = getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.containerBody, fragment);
//                fragmentTransaction.addToBackStack(null);
//                fragmentTransaction.commit();

                Intent in = new Intent(context, TripHistoryDetailActivity.class);
                startActivity(in);

            }
        };
        rvTripHistory.setHasFixedSize(true);
        tripHistoryModel.setDate("45");
        tripHistoryList.add(tripHistoryModel);
        rvTripHistory.setAdapter(new TripHistoryAdapter(tripHistoryList, context, mListener));
        rvTripHistory.setLayoutManager(new LinearLayoutManager(context));

    }

    private void setListner() {
        ibback.setOnClickListener(this);


    }

    private void bindViews() {
        rvTripHistory = binding.rvTripHistory;
        ibback = binding.ibBack;

        tripHistoryModel = new TripHistoryModel();
        tripHistoryList = new ArrayList<TripHistoryModel>();
        tripHistoryAdapter = new TripHistoryAdapter();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ibBack:
                finish();
                break;

        }
    }
}
