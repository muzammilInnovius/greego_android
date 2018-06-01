package com.greegoapp.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
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

public class VehicleDetailAdapter extends RecyclerView.Adapter<VehicleDetailAdapter.VehicleDetailViewHolder> {

    private ArrayList<GetUserData.DataBean.VehiclesBean> alVehicleModelList;
    private View snackBarView;

    RecyclerViewItemClickListener mListener;
    Context context;
    private int lastSelectedPosition = -1;
    int oldPosition = -1;
    boolean isChecked;
    GetUserData.DataBean.VehiclesBean vehicleDetailModel;
    private boolean isCheck = false;


    public VehicleDetailAdapter(Context context, ArrayList<GetUserData.DataBean.VehiclesBean> users, RecyclerViewItemClickListener mListener) {
        alVehicleModelList = users;
        this.context = context;
        this.mListener = mListener;

        if (getItemCount() == 1) {
            lastSelectedPosition = 0;
            callVehicleSelectApi(alVehicleModelList.get(0).getVehicle_id());
        }
    }


    @Override
    public VehicleDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        RowVehicleDetailBinding binding = DataBindingUtil.inflate(inflater, R.layout.row_vehicle_detail, parent, false);

        return new VehicleDetailViewHolder(binding.getRoot(), mListener);
    }

    public class VehicleDetailViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private RowVehicleDetailBinding mBinding;

        private TextView tvYear, tvModel, tvMake, tvColor;
        RecyclerViewItemClickListener mListener;
        ImageView imgVwDelete;
        RadioButton mCheckBox;

        public VehicleDetailViewHolder(View view, RecyclerViewItemClickListener mListener) {
            super(view);
            this.mListener = mListener;
            mBinding = DataBindingUtil.bind(view);
            tvYear = mBinding.tvYear;
            tvModel = mBinding.tvModel;
            tvMake = mBinding.tvMake;
            tvColor = mBinding.tvColor;
            imgVwDelete = mBinding.imgVwDelete;
            mCheckBox = mBinding.chkVehicleSet;
            view.setOnClickListener(this);

            mCheckBox.setOnClickListener(this);
            imgVwDelete.setOnClickListener(this);


        }


        @Override
        public void onClick(View v) {
            mListener.onClick(v, getAdapterPosition());
            switch (v.getId()) {
                case R.id.imgVwDelete:
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                    alertDialogBuilder.setMessage("Are you sure, You wanted to delete vehicle ?");
                    alertDialogBuilder.setPositiveButton("yes",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    callDeleteVehicleAPI(alVehicleModelList.get(getLayoutPosition()).getVehicle_id());
                                    alVehicleModelList.remove(getAdapterPosition());
                                    notifyItemRemoved(getAdapterPosition());
                                    notifyItemRangeChanged(getAdapterPosition(), alVehicleModelList.size());
                                }
                            });

                    alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();


                    break;
                case R.id.chkVehicleSet:

                    lastSelectedPosition = getAdapterPosition();

                    callVehicleSelectApi(alVehicleModelList.get(getLayoutPosition()).getVehicle_id());

                    notifyItemRangeChanged(0, alVehicleModelList.size());

                    break;
            }
        }

    }


    @Override
    public void onBindViewHolder(final VehicleDetailViewHolder holder, final int position) {

        vehicleDetailModel = alVehicleModelList.get(position);

        final int vehicle_id = vehicleDetailModel.getVehicle_id();
        final int vehicle_select_id = vehicleDetailModel.getSelected();

        if (lastSelectedPosition == position) {
            holder.mCheckBox.setChecked(true);
        } else {
            if (oldPosition == position) {
                holder.mCheckBox.setChecked(false);
            } else {
                holder.mCheckBox.setChecked(false);
            }
        }


        if (vehicleDetailModel != null) {
            holder.tvYear.setText(String.valueOf(vehicleDetailModel.getYear()));
            holder.tvMake.setText(vehicleDetailModel.getVehicle_name());
            holder.tvColor.setText(vehicleDetailModel.getColor());
            holder.tvModel.setText(vehicleDetailModel.getVehicle_model());
            if (vehicle_select_id == 1 && oldPosition == -1) {
                oldPosition = holder.getAdapterPosition();
                holder.mCheckBox.setChecked(true);
            }
        }
    }


    private void callVehicleSelectApi(int vehicle_id) {
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
                    notifyDataSetChanged();


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
        return alVehicleModelList.size();
    }


    private void callDeleteVehicleAPI(int vehicle_id) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(WebFields.DELETE_VEHICLE.PARAM_VEHICLE_ID, vehicle_id);
            Applog.E("request: " + jsonObject.toString());
            MyProgressDialog.showProgressDialog(context);

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    WebFields.BASE_URL + WebFields.DELETE_VEHICLE.MODE, jsonObject, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    Applog.E("success: " + response.toString());
                    MyProgressDialog.hideProgressDialog();
//                    SnackBar.showError(context, snackBarView, "Vehicle deleted sucessfully.");
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

}
