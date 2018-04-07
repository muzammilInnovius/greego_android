package com.greegoapp.Fragment;


import android.app.DatePickerDialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.greegoapp.R;
import com.greegoapp.Utils.ConnectivityDetector;
import com.greegoapp.Utils.KeyboardUtility;
import com.greegoapp.Utils.SnackBar;
import com.greegoapp.databinding.FragmentAddPaymentMethodBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddPaymentMethodFragment extends Fragment implements View.OnClickListener {
    FragmentAddPaymentMethodBinding binding;
    private View snackBarView;
    Context context;
     DatePicker datePicker;


     TextInputEditText cardNo,expDate,cvv,zip;
     Button save;
     int mYear, mMonth, mDay;
    ImageButton ibback;
    public AddPaymentMethodFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_payment_method, container, false);

        View view = binding.getRoot();
        snackBarView = getActivity().findViewById(android.R.id.content);
        context = getActivity();

        bindViews();
        setListner();
        expDate.setInputType(InputType.TYPE_NULL);
        return view;
    }

    private void setListner() {
        ibback.setOnClickListener(this);
        save.setOnClickListener(this);
        expDate.setOnClickListener(this);
    }

    private void bindViews() {
        cardNo=binding.edtTvCardNumber;
        expDate=binding.edtTvExpDate;
        cvv=binding.edtTvCvv;
        zip=binding.edtTvZipCode;
        ibback=binding.ibBack;
        save=binding.btnSavePaymentMethod;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.ibBack:
                Fragment fragment = new PaymentFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.containerBody, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
            case R.id.btnSavePaymentMethod:
                KeyboardUtility.hideKeyboard(context, view);
                if (isValid()) {
                    if (ConnectivityDetector
                            .isConnectingToInternet(context)) {

                        callSavePaymentMethodAPI();

                    } else {
                        SnackBar.showInternetError(context, snackBarView);
                    }
                }
                break;
            case R.id.edtTvExpDate:
                getDate();
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

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                mYear = year;
                                mMonth = monthOfYear;
                                mDay = dayOfMonth;

                                expDate.setText(new StringBuilder().append(mMonth + 1).append("/").append(year).append(" "));
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
        Fragment fragment = new PaymentFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.containerBody, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private boolean isValid() {
        String cardno = cardNo.getText().toString();
        String date = expDate.getText().toString();
        String cvvno = cvv.getText().toString();
        String zipcode = zip.getText().toString();
        if (cardno.length() < 12 || cardno.length() > 12)
        {
            cardNo.requestFocus();
            SnackBar.showValidationError(context, snackBarView, getString(R.string.enter_valid_card_no));
            return false;
        }
        else if(date.isEmpty())
        {
            expDate.requestFocus();
            SnackBar.showValidationError(context, snackBarView, getString(R.string.enter_exp_date));
            return false;
        }
        else if(cvvno.isEmpty())
        {
            cvv.requestFocus();
            SnackBar.showValidationError(context, snackBarView, getString(R.string.enter_cvv_no));
            return false;
        }
        else if(cvvno.length()<3)
        {
            cvv.requestFocus();
            SnackBar.showValidationError(context, snackBarView, getString(R.string.enter_valid_cvv_no));
            return false;
        }
        else if(zipcode.isEmpty())
        {
            zip.requestFocus();
            SnackBar.showValidationError(context, snackBarView, getString(R.string.enter_zipcode));
            return false;
        }
        else if(zipcode.length()<6 || zipcode.length()>6)
        {
            zip.requestFocus();
            SnackBar.showValidationError(context, snackBarView, getString(R.string.enter_valid_zipcode));
            return false;
        }
        return true;
    }
}
