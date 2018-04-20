package com.greegoapp.Adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.greegoapp.AppController.AppController;
import com.greegoapp.GlobleFields.GlobalValues;
import com.greegoapp.Interface.RecyclerViewItemClickListener;
import com.greegoapp.Model.GetUserData;
import com.greegoapp.R;
import com.greegoapp.SessionManager.SessionManager;
import com.greegoapp.Utils.Applog;
import com.greegoapp.Utils.MyProgressDialog;
import com.greegoapp.Utils.WebFields;
import com.greegoapp.databinding.RowVehicleDetailBinding;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class VehicleDetailAdapter extends RecyclerView.Adapter<VehicleDetailAdapter.TripHistoryViewHolder> {

    private ArrayList<GetUserData.DataBean.VehiclesBean> VehicleModelList;
    private View snackBarView;

    RecyclerViewItemClickListener mListener;
    Context context;
    private int selectedPosition = -1;
    boolean isChecked;
    private RadioButton lastCheckedRB = null;
    private boolean isCheck = false;

    public VehicleDetailAdapter(Context context, ArrayList<GetUserData.DataBean.VehiclesBean> users, RecyclerViewItemClickListener mListener) {
        VehicleModelList = users;
        this.context = context;
        this.mListener = mListener;
    }


    @Override
    public TripHistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        RowVehicleDetailBinding binding = DataBindingUtil.inflate(inflater, R.layout.row_vehicle_detail, parent, false);
        return new TripHistoryViewHolder(binding.getRoot(), mListener);
    }

    @Override
    public void onBindViewHolder(final TripHistoryViewHolder holder, int position) {
        final GetUserData.DataBean.VehiclesBean vehicleDetailModel = VehicleModelList.get(position);
        final String vehicle_id = vehicleDetailModel.getVehicle_id();
        final String vehicle_select_id = vehicleDetailModel.getSelected();
        if (vehicleDetailModel != null) {
            holder.tvYear.setText(vehicleDetailModel.getYear());
            holder.tvMake.setText(vehicleDetailModel.getTransmission_type());
            holder.tvColor.setText(vehicleDetailModel.getColor());
            holder.tvModel.setText(vehicleDetailModel.getVehicle_id());

            if (vehicle_select_id.matches("0")) {
                holder.mCheckBox.setChecked(true);
            } else {
                holder.mCheckBox.setChecked(false);
            }
        }


        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (isCheck) {
                    isCheck = false;
                    holder.mCheckBox.setChecked(false);
                } else {
                    isCheck = true;
                    holder.mCheckBox.setChecked(true);
                    Log.i("Vehicle id ", vehicle_id);
                    callVehicleSelectApi(vehicle_id);
                }

            }
        });

//        holder.mCheckBox.setSelected(vehicleDetailModel.isSelected());
//
//        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if(isChecked){
//                    holder.mCheckBox.setChecked(false);
////                    vehicleDetailModel.setSelected(true);
//                }else {
////                    vehicleDetailModel.setSelected(false);
//                    holder.mCheckBox.setChecked(true);
//                }
//            }
//        });
//        holder.mCheckBox.setChecked(vehicleDetailModel.isSelected());

    }

    private void callVehicleSelectApi(String vehicle_id) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(WebFields.SELECT_VEHICLE.PARAM_VEHICLE_ID, vehicle_id);
            Applog.E("request: " + jsonObject.toString());
            MyProgressDialog.showProgressDialog(context);

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    WebFields.BASE_URL + WebFields.SELECT_VEHICLE.MODE, jsonObject, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    Applog.E("success: " + response.toString());
                    MyProgressDialog.hideProgressDialog();
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    MyProgressDialog.hideProgressDialog();
                    Applog.E("Error: " + error.getMessage());


                }
            }) {
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> params = new HashMap<String, String>();

                    params.put(WebFields.PARAM_ACCEPT, "application/json");
                    Applog.E("Token==>" + SessionManager.getToken(context));
                    params.put(WebFields.PARAM_AUTHOTIZATION, GlobalValues.BEARER_TOKEN + SessionManager.getToken(context));

                    return params;
                }
            };
            jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                    GlobalValues.TIME_OUT,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            AppController.getInstance().addToRequestQueue(jsonObjReq);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public int getItemCount() {
        return VehicleModelList.size();
    }

    public class TripHistoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private RowVehicleDetailBinding mBinding;

        private TextView tvYear, tvModel, tvMake, tvColor;
        RecyclerViewItemClickListener mListener;
        ImageView imgVwnext;
        RadioButton mCheckBox;

        public TripHistoryViewHolder(View view, RecyclerViewItemClickListener mListener) {
            super(view);
            this.mListener = mListener;
            mBinding = DataBindingUtil.bind(view);
            tvYear = mBinding.tvYear;
            tvModel = mBinding.tvModel;
            tvMake = mBinding.tvMake;
            tvColor = mBinding.tvColor;
            imgVwnext = mBinding.imgVwnext;
            mCheckBox = mBinding.chkVehicleSet;
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListener.onClick(v, getAdapterPosition());
        }

    }

}
