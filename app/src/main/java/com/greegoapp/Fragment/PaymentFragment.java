package com.greegoapp.Fragment;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.greegoapp.Activity.HomeActivity;
import com.greegoapp.Interface.BackPressedFragment;
import com.greegoapp.Interface.CallFragmentInterface;
import com.greegoapp.R;
import com.greegoapp.databinding.FragmentPaymentBinding;
/**
 * A simple {@link Fragment} subclass.
 */
public class PaymentFragment extends Fragment implements View.OnClickListener {
    FragmentPaymentBinding binding;
    private View snackBarView;
    Context context;
    TextView tvAddPaymentMethod, tvAddPromoCode;
    ImageButton ibBack;
    public BackPressedFragment backPressed;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    CallFragmentInterface callMyFragment;

    public static PaymentFragment newInstance(String param1, String param2) {
        PaymentFragment fragment = new PaymentFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_payment, container, false);

        View view = binding.getRoot();
        snackBarView = getActivity().findViewById(android.R.id.content);
        context = getActivity();

        bindViews();
        setListner();

        return view;
    }

    private void setListner() {
        tvAddPromoCode.setOnClickListener(this);
        tvAddPaymentMethod.setOnClickListener(this);
        ibBack.setOnClickListener(this);
    }

    private void bindViews() {
        tvAddPaymentMethod = binding.tvAddPaymentMethod;
        tvAddPromoCode = binding.tvAddPromoCode;
        ibBack = binding.ibBack;

    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvAddPaymentMethod:
                Fragment fragment = new AddPaymentMethodFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.containerBody, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
            case R.id.tvAddPromoCode:
                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                alert.setView(R.layout.dialog_add_promocode);
              /*  alert.setCancelable(false);
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                alert.setPositiveButton("Done", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        //Toast.makeText(getContext(), "Username: " + user + " Email: " + pass, Toast.LENGTH_SHORT).show();
                    }
                });*/
                AlertDialog dialog = alert.create();
                dialog.show();
                break;
            case R.id.ibBack:
                backPressed.onBackPressed(getActivity());
                break;

        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CallFragmentInterface) {
            callMyFragment = (CallFragmentInterface) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement CallFragmentInterface");
        }
        if (context instanceof BackPressedFragment) {
            backPressed = (BackPressedFragment) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement CallFragmentInterface");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callMyFragment = null;
        backPressed = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}