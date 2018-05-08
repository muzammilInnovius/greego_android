package com.greegoapp.Activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.google.gson.Gson;
import com.greegoapp.AppController.AppController;
import com.greegoapp.GlobleFields.GlobalValues;
import com.greegoapp.Model.CardData;
import com.greegoapp.Model.GetUserData;
import com.greegoapp.R;
import com.greegoapp.SessionManager.SessionManager;
import com.greegoapp.Utils.Applog;
import com.greegoapp.Utils.ConnectivityDetector;
import com.greegoapp.Utils.KeyboardUtility;
import com.greegoapp.Utils.MYEditCard;
import com.greegoapp.Utils.MyProgressDialog;
import com.greegoapp.Utils.SnackBar;
import com.greegoapp.Utils.WebFields;
import com.greegoapp.databinding.ActivityAddPaymentMethodBinding;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.exception.AuthenticationException;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.stripe.model.Charge;
import com.stripe.model.Customer;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static com.greegoapp.Activity.PaymentActivity.ADD_CARD_PAYMENT_METHOD;
import static com.greegoapp.Fragment.MapHomeFragment.REQUEST_ADD_PAYMENT;

public class AddPaymentMethodActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityAddPaymentMethodBinding binding;
    private View snackBarView;
    Context context;
    DatePicker datePicker;

    TextInputEditText edtTvExpDate;

    EditText edtTvCvv, edtTvZipCode;
    Button save;
    int mYear, mMonth, mDay;
    ImageButton ibback;
    Button btnSavePaymentMethod;
    // Required empty public constructor
    ArrayList<GetUserData.DataBean.CardsBean> cardData;
    TextView tvEditPaymentTitle;
    ImageButton ibBack;
    String strCardNo, cardToken;
    MYEditCard edtTvCardNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_payment_method);

        snackBarView = findViewById(android.R.id.content);
        context = AddPaymentMethodActivity.this;
        cardData = new ArrayList<>();

        bindViews();
        setListner();

//        callUserMeApi();

    }


    String stripPay() {
        strCardNo = edtTvCardNumber.getText().toString();
        String date = edtTvExpDate.getText().toString();
        String cvvno = edtTvCvv.getText().toString();
        String zipcode = edtTvZipCode.getText().toString();

        Stripe stripe;

        Card card = new Card(strCardNo, Integer.parseInt(date.substring(0, 2)), Integer.parseInt("20" + date.substring(3)), cvvno);
// Remember to validate the card object before you use it to save time.
        if (!card.validateCard()) {
            // Do not continue token creation.
            edtTvCardNumber.requestFocus();
            SnackBar.showValidationError(context, snackBarView, getString(R.string.card_not_valid));
//            Toast.makeText(this, "card not valid", Toast.LENGTH_SHORT).show();
        } else {


            try {
                MyProgressDialog.showProgressDialog(context);
                stripe = new Stripe("pk_test_GF0y48SCqViKdCSA8LwOPFVj");
//            googleMap.clear();
//            customGoogleMap.clear();
                stripe.createToken(
                        card,
                        new TokenCallback() {
                            @SuppressLint("StaticFieldLeak")
                            public void onSuccess(final Token token) {

                                Log.e("check token", ":::::" + token.getId());
                                Log.e("check token all data", ":::::" + token);


//                                final Map<String, Object> parames = new HashMap<>();
//                                parames.put("amount", 88);
//                                parames.put("currency", "usd");
//                                parames.put("description", "ajit payment");
//                                parames.put("source", token.getId());

                                try {
                                    new AsyncTask<Void, Void, Void>() {

                                        Charge charge;

                                        @Override
                                        protected Void doInBackground(
                                                Void... params) {
                                            try {

                                                com.stripe.Stripe.apiKey = "sk_test_BXg8CAB9wtNTm1lNBe3HMK2X";


                                                Map<String, Object> chargeParams = new HashMap<>();
                                                chargeParams.put("source", token.getId());
                                                chargeParams.put("email", "paying.user@example.com");

                                                Customer customer = Customer.create(chargeParams);

                                                Log.e("aj", customer.getId());


                                                cardToken = customer.getId();

                                                if (cardToken != null) {
                                                    callSavePaymentMethodAPI();
                                                }

                                        /*charge = Charge
                                                .create(parames);
*/

                                            } catch (Exception e) {
                                                // TODO Auto-generated catch block
                                                e.printStackTrace();
                                            }
                                            return null;
                                        }

                                        protected void onPostExecute(Void result) {


                                        }

                                        ;

                                    }.execute();

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                            }

                            public void onError(Exception error) {
                                // Show localized error message
                                Log.e("check", "erorr" + error.getMessage());

                            }
                        }
                );


            } catch (AuthenticationException e) {
                e.printStackTrace();
            }
        }

        return cardToken;
    }

    private void setListner() {
        ibBack.setOnClickListener(this);
        btnSavePaymentMethod.setOnClickListener(this);
        edtTvCvv.setOnClickListener(this);
        edtTvCardNumber.addTextChangedListener(new CreditCardNumberFormattingTextWatcher());
//        edtTvCardNumber.addTextChangedListener(new CreditCardFormattingTextWatcher(edtTvCardNumber));
//
//        edtTvExpDate.addTextChangedListener(mDateEntryWatcher);
//
//

        edtTvCardNumber.getCardNumber(); // Get the card number
        edtTvCardNumber.isValid(); // Is the card number valid
        edtTvCardNumber.getCardType();
        edtTvExpDate.addTextChangedListener(mDateEntryWatcher);

    }


    private TextWatcher mDateEntryWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String working = s.toString();
            boolean isValid = true;
            if (working.length() == 2 && before == 0) {
                if (Integer.parseInt(working) < 1 || Integer.parseInt(working) > 12) {
                    isValid = false;
                } else {
                    working += "/";
                    edtTvExpDate.setText(working);
                    edtTvExpDate.setSelection(working.length());
                }
            } else if (working.length() == 5 && before == 0) {
                String enteredYear = working.substring(3);
                int currentYear = Calendar.getInstance().get(Calendar.YEAR);
                if (Integer.parseInt(enteredYear) < currentYear) {
                    isValid = false;
                }
            } else if (working.length() != 7) {
                isValid = false;
            }


        }

        @Override
        public void afterTextChanged(Editable s) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

    };

    private void bindViews() {
        edtTvCardNumber = binding.edtTvCardNumber;
        edtTvExpDate = binding.edtTvExpDate;
        edtTvCvv = binding.edtTvCvv;
        edtTvZipCode = binding.edtTvZipCode;
        btnSavePaymentMethod = binding.btnSavePaymentMethod;
        tvEditPaymentTitle = binding.tvEditPaymentTitle;
        ibBack = binding.ibBack;
    }


    public static class CreditCardNumberFormattingTextWatcher implements TextWatcher {

        private boolean lock;

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (lock || s.length() > 16) {
                return;
            }
            lock = true;
            for (int i = 4; i < s.length(); i += 5) {
                if (s.toString().charAt(i) != ' ') {
                    s.insert(i, "-");
                }
            }
            lock = false;
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ibBack:
//                Intent  in = new Intent(AddPaymentMethodActivity.this, HomeActivity.class);
//               startActivity(in);
                finish();
//                Fragment fragment = new PaymentFragment();
//                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.containerBody, fragment);
//                fragmentTransaction.addToBackStack(null);
//                fragmentTransaction.commit();
                break;
            case R.id.btnSavePaymentMethod:
                KeyboardUtility.hideKeyboard(context, view);
//                if (isValid()) {

                if (ConnectivityDetector
                        .isConnectingToInternet(context)) {
                    stripPay();

                } else {
                    SnackBar.showInternetError(context, snackBarView);
                }
//                }
                break;

        }
    }

    private void getDate() {

        try {
            // Get Current Date
            Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            mYear = year;
                            mMonth = monthOfYear;
                            mDay = dayOfMonth;

                            edtTvExpDate.setText(new StringBuilder().append(mMonth + 1).append("/").append(year).append(" "));
                            //strFrom = txtVwWrkgSncFrom.getText().toString();
                        }
                    }, mDay, mMonth, mYear);

            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            datePickerDialog.show();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void callSavePaymentMethodAPI() {

        try {
            JSONObject jsonObject = new JSONObject();

            strCardNo = edtTvCardNumber.getText().toString();
            strCardNo = strCardNo.replace("-", "");
            String strExpDate = edtTvExpDate.getText().toString();
            String strzipCode = edtTvZipCode.getText().toString();

            jsonObject.put(WebFields.UPDATE_CARD.PARAM_CARD_NUMBER, strCardNo);
            jsonObject.put(WebFields.UPDATE_CARD.PARAM_EXP_MONTH_YEAR, strExpDate);
            jsonObject.put(WebFields.UPDATE_CARD.PARAM_ZIPCODE, strzipCode);
            jsonObject.put(WebFields.UPDATE_CARD.PARAM_CARD_TOKEN, cardToken);

            Applog.E("request UpdateCard=> " + jsonObject.toString());
//            MyProgressDialog.showProgressDialog(context);

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    WebFields.BASE_URL + WebFields.UPDATE_CARD.MODE, jsonObject, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    Applog.E("success: " + response.toString());
                    try {
                        CardData cardDetails = new Gson().fromJson(String.valueOf(response), CardData.class);
                        MyProgressDialog.hideProgressDialog();

                        if (cardDetails.getError_code() == 0) {
                            SessionManager.setIsUserLoggedin(context, true);
                            Applog.E("UserDetails" + cardDetails);
                            String card_number = cardDetails.getData().getCard_number();

                            SessionManager.saveCardDetails(context, cardDetails);

                            if (REQUEST_ADD_PAYMENT == 110) {
                                Intent data = new Intent();
                                setResult(REQUEST_ADD_PAYMENT, data);
                            } else if (ADD_CARD_PAYMENT_METHOD == 1001) {
                                Intent data = new Intent();
                                data.putExtra("cardNumber", card_number);
                                setResult(ADD_CARD_PAYMENT_METHOD, data);
                            }


//                            callUserMeApi();
                            finish();


                            SnackBar.showSuccess(context, snackBarView, "Add card successfully.");
                        } else {
                            MyProgressDialog.hideProgressDialog();
                            SnackBar.showError(context, snackBarView, response.getString("message"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    CardData cardDetails = new Gson().fromJson(String.valueOf(response), CardData.class);
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    MyProgressDialog.hideProgressDialog();
                    Applog.E("Error: " + error.getMessage());

                    SnackBar.showError(context, snackBarView, getResources().getString(R.string.something_went_wrong));
                }
            }) {
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> params = new HashMap<String, String>();

                    params.put(WebFields.PARAM_ACCEPT, "application/json");
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

//        Fragment fragment = new PaymentFragment();
//        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.containerBody, fragment);
//        fragmentTransaction.addToBackStack(null);
//        fragmentTransaction.commit();
    }

    private boolean isValid() {
        strCardNo = edtTvCardNumber.getText().toString();
        String date = edtTvExpDate.getText().toString();
        String cvvno = edtTvCvv.getText().toString();
        String zipcode = edtTvZipCode.getText().toString();
//
//        if (strCardNo.length() < 19) {
//            edtTvCardNumber.requestFocus();
//            SnackBar.showValidationError(context, snackBarView, getString(R.string.enter_valid_card_no));
//            return false;
//        } else
        if (date.isEmpty()) {
            edtTvExpDate.requestFocus();
            SnackBar.showValidationError(context, snackBarView, getString(R.string.enter_exp_date));
            return false;
        }/* else if (cvvno.isEmpty()) {
            edtTvCvv.requestFocus();
            SnackBar.showValidationError(context, snackBarView, getString(R.string.enter_cvv_no));
            return false;
        } else if (cvvno.length() < 3) {
            edtTvCvv.requestFocus();
            SnackBar.showValidationError(context, snackBarView, getString(R.string.enter_valid_cvv_no));
            return false;
        }*/ else if (zipcode.isEmpty()) {
            edtTvZipCode.requestFocus();
            SnackBar.showValidationError(context, snackBarView, getString(R.string.enter_zipcode));
            return false;
        } else if (zipcode.length() < 5) {
            edtTvZipCode.requestFocus();
            SnackBar.showValidationError(context, snackBarView, getString(R.string.enter_valid_zipcode));
            return false;
        }
        return true;
    }

    String cardNo;

    private void callUserMeApi() {
        try {

            JSONObject jsonObject = new JSONObject();

            Applog.E("request: " + jsonObject.toString());
            MyProgressDialog.showProgressDialog(context);

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    WebFields.BASE_URL + WebFields.USER_ME.MODE, jsonObject, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    Applog.E("success: " + response.toString());

                    GetUserData userDetails = new Gson().fromJson(String.valueOf(response), GetUserData.class);

                    try {
                        MyProgressDialog.hideProgressDialog();
//
                        if (userDetails.getError_code() == 0) {
                            cardData.addAll(userDetails.getData().getCards());

                            if (userDetails.getData() == null) {
                                tvEditPaymentTitle.setText(getResources().getString(R.string.new_payment));
                            } else {
                                tvEditPaymentTitle.setText(getResources().getString(R.string.text_edit_payment_title));
                                for (GetUserData.DataBean.CardsBean cardsBean : cardData) {

                                    byte[] data = Base64.decode(cardsBean.getCard_number(), Base64.DEFAULT);
                                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                                        cardNo = new String(data, StandardCharsets.UTF_8);
                                    }
//                                    String decordNumber = cardsBean.getCard_number();

//                                    try {
//                                        cardNo = URLDecoder.decode(data, "UTF-8");
//                                    } catch (UnsupportedEncodingException e) {
//                                        e.printStackTrace();
//                                    }
                                    Applog.E("Card No===> " + cardNo);
                                    String s = cardNo;
                                    String s1 = s.substring(0, 4);
                                    String s2 = s.substring(4, 8);
                                    String s3 = s.substring(8, 12);
                                    String s4 = s.substring(12, 16);
                                    String strCardNumber = s1 + " " + s2 + " " + s3 + " " + s4;
                                    edtTvCardNumber.setText(strCardNumber);
                                    edtTvExpDate.setText(cardsBean.getExp_month_year());
                                    edtTvZipCode.setText(cardsBean.getZipcode());
                                }
                            }
//                            edtTvProfileFname.setText(userDetails.getData().getName());
//                            edtTvProfileLname.setText(userDetails.getData().getLastname());
//                            strEmail = userDetails.getData().getEmail();
//                            SessionManager.saveUserData(context, userDetails);
//                            SnackBar.showSuccess(context, snackBarView, "Profile Update successfully.");
//
                            //getIs_agreed = 0 new user

//
                        } else {
                            MyProgressDialog.hideProgressDialog();
                            SnackBar.showError(context, snackBarView, response.getString("message"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    MyProgressDialog.hideProgressDialog();
                    Applog.E("Error: " + error.getMessage());

                    SnackBar.showError(context, snackBarView, getResources().getString(R.string.something_went_wrong));
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
