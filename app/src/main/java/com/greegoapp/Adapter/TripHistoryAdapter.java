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
import com.greegoapp.Model.TripHistoryModel;
import com.greegoapp.R;
import com.greegoapp.databinding.RowTripHistoryBinding;

import java.util.ArrayList;
import java.util.List;

public class TripHistoryAdapter extends RecyclerView.Adapter<TripHistoryAdapter.TripHistoryViewHolder> {

    private List<TripHistoryModel> historyModelList = new ArrayList<>();
    RecyclerViewItemClickListener mListener;
    Context context;

    public TripHistoryAdapter(@NonNull List<TripHistoryModel> users, Context context,RecyclerViewItemClickListener mListener) {
        historyModelList=users;
        this.context=context;
        this.mListener=mListener;
    }

    public TripHistoryAdapter(RecyclerViewItemClickListener mListener) {
        this.mListener=mListener;
    }

    @Override
    public TripHistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        RowTripHistoryBinding binding = DataBindingUtil.inflate(inflater, R.layout.row_trip_history, parent, false);
        return new TripHistoryViewHolder(binding.getRoot(),mListener);
    }

    @Override
    public void onBindViewHolder(TripHistoryViewHolder holder, int position) {
        TripHistoryModel tripHistoryModel = historyModelList.get(position);
        holder.date.setText(tripHistoryModel.getDate());
    }

    public TripHistoryAdapter() {
    }

    @Override
    public int getItemCount() {
        return historyModelList.size();
    }
    public class TripHistoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private RowTripHistoryBinding mBinding;

      private TextView date;
        RecyclerViewItemClickListener mListener;

        public TripHistoryViewHolder(View view, RecyclerViewItemClickListener mListener) {
            super(view);
            this.mListener = mListener;
            mBinding = DataBindingUtil.bind(view);
            date = mBinding.tvTripDate;

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListener.onClick(v, getAdapterPosition());
        }

    }

}