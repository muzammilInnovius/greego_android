package com.greegoapp.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.support.annotation.RequiresApi;
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
import com.greegoapp.Utils.ConnectivityDetector;
import com.greegoapp.Utils.MyProgressDialog;
import com.greegoapp.Utils.SnackBar;
import com.greegoapp.Utils.WebFields;
import com.greegoapp.databinding.RowVehicleDetailBinding;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class VehicleDetailAdapter extends RecyclerView.Adapter<VehicleDetailAdapter.VehicleDetailViewHolder> {

    private ArrayList<GetUserData.DataBean.VehiclesBean> VehicleModelList;
    private View snackBarView;

    RecyclerViewItemClickListener mListener;
    Context context;
    private int lastSelectedPosition = -1;
    GetUserData.DataBean.VehiclesBean vehicleDetailModel;
    private boolean isCheck = false;
    int position, layoutPosition;

    String vehicle_select_id, vehicle_id;

    public VehicleDetailAdapter(Context context, ArrayList<GetUserData.DataBean.VehiclesBean> users, RecyclerViewItemClickListener mListener) {
        VehicleModelList = users;
        this.context = context;
        this.mListener = mListener;
    }


    @Override
    public VehicleDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        RowVehicleDetailBinding binding = DataBindingUtil.inflate(inflater, R.layout.row_vehicle_detail, parent, false);
        return new VehicleDetailViewHolder(binding.getRoot(), mListener);
    }


    @Override
    public void onBindViewHolder(final VehicleDetailViewHolder holder, final int position) {

        vehicleDetailModel = VehicleModelList.get(position);
        vehicle_id = vehicleDetailModel.getVehicle_id();
        vehicle_select_id = vehicleDetailModel.getSelected();


        if (vehicleDetailModel != null) {
            holder.tvYear.setText(vehicleDetailModel.getYear());
            holder.tvMake.setText(vehicleDetailModel.getVehicle_name());
            holder.tvColor.setText(vehicleDetailModel.getColor());
            holder.tvModel.setText(vehicleDetailModel.getVehicle_model());

            if (vehicle_select_id.matches("1")) {
                holder.mCheckBox.setChecked(Integer.parseInt(vehicle_id) == lastSelectedPosition);
//                lastSelectedPosition = position;
                holder.mCheckBox.setChecked(true);
            } else {
                holder.mCheckBox.setChecked(false);
            }
        }


//        holder.mCheckBox.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                CheckBox cb = (CheckBox) v;
//                int clickedPos = ((Integer) cb.getTag()).intValue();
//
//                if (cb.isChecked()) {
//                    if (lastChecked != null) {
//                        lastChecked.setChecked(false);
//                        VehicleModelList.get(lastCheckedPos).setSelected(false);
//                        Toast.makeText(context, "1111111", Toast.LENGTH_SHORT).show();
//                    }
//                    Toast.makeText(context, "2222222", Toast.LENGTH_SHORT).show();
//                    lastChecked = cb;
//                    lastCheckedPos = clickedPos;
//                } else
//                    Toast.makeText(context, "333333", Toast.LENGTH_SHORT).show();
//                    lastChecked = null;
//
//                VehicleModelList.get(clickedPos).setSelected(cb.isChecked());
//                notifyDataSetChanged();
//            }
//        });
    }


//
//        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if(isChecked){
//                    holder.mCheckBox.setChecked(true);
////                    vehicleDetailModel.setSelected(true);
//                }else {
////                    vehicleDetailModel.setSelected(false);
//                    holder.mCheckBox.setChecked(false);
//                }
//            }
//        });
//        holder.mCheckBox.setChecked(vehicleDetailModel.isSelected());

//    }


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

    /*public void update(int po,ArrayList<GetUserData.DataBean.VehiclesBean> List){
                 lastSelectedPosition =po;
                VehicleModelList =List;
        Toast.makeText(context,
                "sele "+List,
                Toast.LENGTH_LONG).show();

        Toast.makeText(context,
                "sele "+po,
                Toast.LENGTH_LONG).show();


    //             notifyDataSetChanged();



    }*/
    @Override
    public int getItemCount() {
        return VehicleModelList.size();
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

           /* mCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    lastSelectedPosition = getAdapterPosition();
                    *//*VehicleModelList.get(lastSelectedPosition).setSelected(false);*//*
                    //      vehicleDetailModel.setSelected(lastSelectedPosition+"");
                    callVehicleSelectApi(VehicleModelList.get(getLayoutPosition()).getVehicle_id());
                    VehicleModelList.get(getLayoutPosition()).setSelected(true);
                    notifyDataSetChanged();
                *//*    Toast.makeText(context,getLayoutPosition()+
                            "selected offer is ",
                            Toast.LENGTH_LONG).show();*//*
                }
            });*/
//            mCheckBox.setOnClickListener(this);
            imgVwDelete.setOnClickListener(this);

            mCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {



                    lastSelectedPosition = getAdapterPosition();
                    mCheckBox.setChecked(Integer.parseInt(vehicle_id) == lastSelectedPosition);
                    mCheckBox.setChecked(true);
                    //because of this blinking problem occurs so
                    //i have a suggestion to add notifyDataSetChanged();
                    //   notifyItemRangeChanged(0, list.length);//blink list problem
                    callVehicleSelectApi(vehicle_id);
                    notifyDataSetChanged();

                }
            });
        }


        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onClick(View v) {
            mListener.onClick(v, getAdapterPosition());
            switch (v.getId()) {
                case R.id.imgVwDelete:
                    position = getAdapterPosition();
                    layoutPosition = getLayoutPosition();
                    showCheckUserUpdateData("Are you sure you want to Delete vehicle?", position);

                    break;
//                case R.id.chkVehicleSet:
//                    lastSelectedPosition = getAdapterPosition();
//                    callVehicleSelectApi(VehicleModelList.get(getLayoutPosition()).getVehicle_id());
//                    VehicleModelList.get(getLayoutPosition()).setSelected(true);
//                    notifyItemRangeChanged(getAdapterPosition(), VehicleModelList.size());
//                    break;
            }
        }

    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void showCheckUserUpdateData(String msg, final int position) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(context)
                    .setTitle("Greego").setMessage(msg);


            builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    if (ConnectivityDetector
                            .isConnectingToInternet(context)) {

                        callDeleteVehicleAPI(VehicleModelList.get(layoutPosition).getVehicle_id());
                        VehicleModelList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, VehicleModelList.size());
                        dialog.dismiss();

                    } else {
                        SnackBar.showInternetError(context, snackBarView);
                    }
                }
            });

            builder.setPositiveButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void callDeleteVehicleAPI(String vehicle_id) {
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
