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
import android.widget.RadioButton;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.greegoapp.Activity.PaymentActivity;
import com.greegoapp.AppController.AppController;
import com.greegoapp.GlobleFields.GlobalValues;
import com.greegoapp.Interface.RecyclerViewItemClickListener;
import com.greegoapp.Model.GetUserData;
import com.greegoapp.R;
import com.greegoapp.SessionManager.SessionManager;
import com.greegoapp.Utils.Applog;
import com.greegoapp.Utils.MYEditCard;
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
    private String cardNo;
    GetUserData.DataBean.CardsBean cardDetailModel;
    private ArrayList<GetUserData.DataBean.CardsBean> alCardData;
    private String strCard;
    int card_id;
    private int lastSelectedPosition = -1;
    RecyclerViewItemClickListener mListener;
    private boolean isSelect = true;
    int oldPosition = -1;

    public CardsNumberAdapter() {
    }

    public CardsNumberAdapter(Context context, ArrayList<GetUserData.DataBean.CardsBean> cardData, RecyclerViewItemClickListener mListener) {
        this.context = context;
        this.alCardData = cardData;
        this.mListener = mListener;


        if (getItemCount() == 1) {
            lastSelectedPosition = 0;
            PaymentActivity.cardselected = "" + alCardData.get(lastSelectedPosition).getCard_number();
            callSelectCardForUser(alCardData.get(0).getId());
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        RowCardNumberBinding binding = DataBindingUtil.inflate(inflater, R.layout.row_card_number, parent, false);
        return new ViewHolder(binding.getRoot(), mListener);
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvCardDetail;
        private RowCardNumberBinding mBinding;
        RadioButton ivCardNumber;
        private RecyclerViewItemClickListener mListener;
        private ImageView ivDeleteCard;
        private MYEditCard ivCardLogo;

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

            //  ivCardNumber.setImageResource(R.mipmap.ic_un_tick);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.ivCardNumber:
                    lastSelectedPosition = getAdapterPosition();
                    PaymentActivity.cardselected = "" + alCardData.get(getLayoutPosition()).getCard_number();

                    callSelectCardForUser(alCardData.get(getLayoutPosition()).getId());

                    notifyItemRangeChanged(0, alCardData.size());

                    break;

                case R.id.ivDeleteCard:
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                    alertDialog.setTitle("Delete");
                    alertDialog.setMessage("Are you sure you want delete this payment method?");
                    // Setting Positive "Yes" Button
                    alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                callDeleteCardDetails(alCardData.get(getLayoutPosition()).getId());

                                alCardData.remove(getAdapterPosition());
                                notifyItemRemoved(getAdapterPosition());
//                                if (getAdapterPosition() != 0) {
                                    notifyItemRangeChanged(getAdapterPosition(), alCardData.size());
//                                }
//                                else {
//                                    notifyDataSetChanged();
//                                }


                            } catch (Exception e) {
                                e.printStackTrace();
                            }
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
    public void onBindViewHolder(ViewHolder holder, final int position) {

        cardDetailModel = alCardData.get(position);
        byte[] data = Base64.decode(alCardData.get(position).getCard_number(), Base64.DEFAULT);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            cardNo = new String(data, StandardCharsets.UTF_8);
        }
        card_id = alCardData.get(position).getId();
        int selected_card_id = cardDetailModel.getSelected();

        if (lastSelectedPosition == position) {
            holder.ivCardNumber.setChecked(true);
        } else {
            if (oldPosition == position) {
                holder.ivCardNumber.setChecked(false);
            } else {
                holder.ivCardNumber.setChecked(false);
            }
        }
        if (cardDetailModel != null) {
            String number = setCardNumber(cardNo);
            holder.ivCardLogo.getCardNumber(cardNo);
            holder.ivCardLogo.isValid(cardNo);
            holder.ivCardLogo.getCardType();

            holder.tvCardDetail.setText(number);
            if (selected_card_id == 1 && oldPosition == -1) {
                oldPosition = holder.getAdapterPosition();
                holder.ivCardNumber.setChecked(true);

            }
        }
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


    private void callDeleteCardDetails(int id) {

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

    private void callSelectCardForUser(int id) {
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

    public void updateData(String strCard) {

    }

}
