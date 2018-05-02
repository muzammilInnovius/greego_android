package com.greegoapp.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.greegoapp.databinding.RowCardNumberBinding;

import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CardsNumberAdapter extends RecyclerView.Adapter<CardsNumberAdapter.ViewHolder> {
    private Context context;
    //private ArrayList<String> arrayList;
    private String cardNo;
    GetUserData.DataBean.CardsBean cardDetailModel;
    private ArrayList<GetUserData.DataBean.CardsBean> alCardData;
    private String strCard;
    int card_id;
    private int lastSelectedPosition = -1;
    RecyclerViewItemClickListener mListener;
    private boolean isSelect = true;

    public CardsNumberAdapter() {
    }

    public CardsNumberAdapter(Context context, ArrayList<GetUserData.DataBean.CardsBean> cardData, RecyclerViewItemClickListener mListener) {
        this.context = context;
        this.alCardData = cardData;
        this.mListener = mListener;
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //        private RadioButton rbCardNumber;
        private TextView tvCardDetail;
        private RowCardNumberBinding mBinding;
        private ImageView ivCardNumber;
        private RecyclerViewItemClickListener mListener;
        private ImageView ivCardLogo, ivDeleteCard;


        public ViewHolder(View itemView, RecyclerViewItemClickListener mListener) {
            super(itemView);
            this.mListener = mListener;
            mBinding = DataBindingUtil.bind(itemView);
            ivCardNumber = mBinding.ivCardNumber;
            tvCardDetail = mBinding.tvCardDetail;
            ivCardLogo = mBinding.ivCardLogo;
            ivDeleteCard = mBinding.ivDeleteCard;
            ivDeleteCard.setOnClickListener(this);
            ivCardNumber.setOnClickListener(this);

            ivCardNumber.setImageResource(R.mipmap.ic_un_tick);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.ivCardNumber:

                    if (isSelect) {
                        isSelect = false;
                        ivCardNumber.setImageResource(R.mipmap.ic_tick);

                        lastSelectedPosition = getAdapterPosition();
                        selectCardForUser(alCardData.get(getLayoutPosition()).getId());

                        notifyItemRangeChanged(getAdapterPosition(), alCardData.size());
                    } else {
                        ivCardNumber.setImageResource(R.mipmap.ic_un_tick);
                        isSelect = true;
                    }

                    break;
                case R.id.ivDeleteCard:
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                    alertDialog.setTitle("Delete");
                    alertDialog.setMessage("Are you sure you want delete this payment method?");
                    // Setting Positive "Yes" Button
                    alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            deleteCardDetails(card_id);
                            alCardData.remove(getAdapterPosition());
                            notifyItemRemoved(getAdapterPosition());
                            notifyItemRangeChanged(getAdapterPosition(), alCardData.size());
                        }
                    });

                    alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    alertDialog.show();

                    break;
            }
        }


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        RowCardNumberBinding binding = DataBindingUtil.inflate(inflater, R.layout.row_card_number, parent, false);
        return new ViewHolder(binding.getRoot(), mListener);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        if (lastSelectedPosition == position) {
            isSelect = true;
            holder.ivCardNumber.setImageResource(R.mipmap.ic_tick);
        } else {
            isSelect = false;
            holder.ivCardNumber.setImageResource(R.mipmap.ic_un_tick);
        }

        cardDetailModel = alCardData.get(position);
        byte[] data = Base64.decode(alCardData.get(position).getCard_number(), Base64.DEFAULT);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            cardNo = new String(data, StandardCharsets.UTF_8);
        }



        card_id = alCardData.get(position).getId();
        String number = setCardNumber(cardNo);
        holder.tvCardDetail.setText(number);
    }

    @Override
    public int getItemCount() {
        return alCardData.size();
    }

    private String setCardNumber(String cardNumber) {


        String strCardNumber;
        int length = cardNumber.length();
        if (length > 0) {
            String s = cardNumber;
            String s4 = s.substring(12, 16);
            strCardNumber = "*** *** **** " + s4;

        } else {
            strCardNumber = "*** *** **** ****";
        }
        return strCardNumber;
    }


    private void deleteCardDetails(int id) {

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(WebFields.SELECT_DELETE_CARD.PARAM_CARD_ID, String.valueOf(id));
            Applog.E("request: " + jsonObject.toString());
            MyProgressDialog.showProgressDialog(context);

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    WebFields.BASE_URL + WebFields.SELECT_DELETE_CARD.DELETE_CARD_MODE, jsonObject, new Response.Listener<JSONObject>() {

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

    private void selectCardForUser(int id) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(WebFields.SELECT_DELETE_CARD.PARAM_CARD_ID, String.valueOf(id));
            Applog.E("request: " + jsonObject.toString());
            MyProgressDialog.showProgressDialog(context);

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    WebFields.BASE_URL + WebFields.SELECT_DELETE_CARD.SELECT_CARD_MODE, jsonObject, new Response.Listener<JSONObject>() {

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


}
