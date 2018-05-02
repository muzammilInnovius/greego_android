package com.greegoapp.Adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.greegoapp.Interface.RecyclerViewItemClickListener;
import com.greegoapp.Model.TripHistoryModel;
import com.greegoapp.R;
import com.greegoapp.databinding.RowTripHistoryBinding;

import java.util.ArrayList;
import java.util.UUID;

public class TripHistoryAdapter extends RecyclerView.Adapter<TripHistoryAdapter.TripHistoryViewHolder> {

    private ArrayList<TripHistoryModel.DataBean> historyModelList;
    RecyclerViewItemClickListener mListener;
    Context context;

    public TripHistoryAdapter(Context context, ArrayList<TripHistoryModel.DataBean> users, RecyclerViewItemClickListener mListener) {
        historyModelList = users;
        this.context = context;
        this.mListener = mListener;
    }

    public TripHistoryAdapter(RecyclerViewItemClickListener mListener) {
        this.mListener = mListener;
    }

    @Override
    public TripHistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        RowTripHistoryBinding binding = DataBindingUtil.inflate(inflater, R.layout.row_trip_history, parent, false);
        return new TripHistoryViewHolder(binding.getRoot(), mListener);
    }

    @Override
    public void onBindViewHolder(TripHistoryViewHolder holder, int position) {
        TripHistoryModel.DataBean tripHistoryModel = historyModelList.get(position);

        if (tripHistoryModel != null) {

            String strTotalTime= String.valueOf(tripHistoryModel.getTotal_estimated_travel_time());
            String strTotalCost = String.valueOf(tripHistoryModel.getTotal_estimated_trip_cost());

            holder.tvTripDate.setText(tripHistoryModel.getCreated_at());
            holder.tvTripTime.setText(strTotalTime);
            holder.tvTripTotalCost.setText(strTotalCost);

            String profilePicUrl = "http://kroslinkstech.in/greego/storage/app/" +tripHistoryModel.getProfile_pic();

            if (profilePicUrl != null) {
                Glide.clear(holder.ivProPicTripHistory);
                Glide.with(context)
                        .load(profilePicUrl)
                        .centerCrop()
                        .signature(new StringSignature(UUID.randomUUID().toString()))
                        .crossFade().skipMemoryCache(true)
                        .into(holder.ivProPicTripHistory);

            } else {
                holder.ivProPicTripHistory.setImageResource(R.mipmap.ic_user_profile);
            }
        }
    }


    @Override
    public int getItemCount() {
        return historyModelList.size();
    }

    public class TripHistoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private RowTripHistoryBinding mBinding;

        private TextView tvTripDate, tvTripTime, tvTripTotalCost;
        private ImageView ivProPicTripHistory;
        RecyclerViewItemClickListener mListener;

        public TripHistoryViewHolder(View view, RecyclerViewItemClickListener mListener) {
            super(view);
            this.mListener = mListener;
            mBinding = DataBindingUtil.bind(view);

            tvTripDate = mBinding.tvTripDate;
            tvTripTime = mBinding.tvTripTime;
            tvTripTotalCost = mBinding.tvTripTotalCost;
            ivProPicTripHistory = mBinding.ivProPicTripHistory;

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListener.onClick(v, getAdapterPosition());
        }

    }

}