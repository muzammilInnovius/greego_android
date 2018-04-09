package com.greegoapp.Adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.greegoapp.Interface.RecyclerViewItemClickListener;
import com.greegoapp.Model.VehicleDetailModel;
import com.greegoapp.R;
import com.greegoapp.databinding.RowTripHistoryBinding;
import com.greegoapp.databinding.RowVehicleDetailBinding;

import java.util.ArrayList;
import java.util.List;

public class VehicleDetailAdapter extends RecyclerView.Adapter<VehicleDetailAdapter.TripHistoryViewHolder> {

    private List<VehicleDetailModel> VehicleModelList = new ArrayList<>();
    RecyclerViewItemClickListener mListener;
    Context context;

    public VehicleDetailAdapter(@NonNull List<VehicleDetailModel> users, Context context, RecyclerViewItemClickListener mListener) {
        VehicleModelList=users;
        this.context=context;
        this.mListener=mListener;
    }

    public VehicleDetailAdapter(RecyclerViewItemClickListener mListener) {
        this.mListener=mListener;
    }

    @Override
    public TripHistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        RowVehicleDetailBinding binding = DataBindingUtil.inflate(inflater, R.layout.row_vehicle_detail, parent, false);
        return new TripHistoryViewHolder(binding.getRoot(),mListener);
    }

    @Override
    public void onBindViewHolder(TripHistoryViewHolder holder, int position) {
        VehicleDetailModel VehicleDetailModel = VehicleModelList.get(position);
           holder.year.setText(VehicleDetailModel.getYear());
    }

    public VehicleDetailAdapter() {
    }

    @Override
    public int getItemCount() {
        return VehicleModelList.size();
    }
    public class TripHistoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private RowVehicleDetailBinding mBinding;

        private TextView year;
        RecyclerViewItemClickListener mListener;

        public TripHistoryViewHolder(View view, RecyclerViewItemClickListener mListener) {
            super(view);
            this.mListener = mListener;
            mBinding = DataBindingUtil.bind(view);
            year=mBinding.tvYear;
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListener.onClick(v, getAdapterPosition());
        }

    }

}
