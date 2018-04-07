package com.greegoapp.Fragment;


import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.greegoapp.Activity.HomeActivity;
import com.greegoapp.Interface.BackPressedFragment;
import com.greegoapp.R;
import com.greegoapp.databinding.FragmentPaymentBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class PaymentFragment extends Fragment implements View.OnClickListener {
    FragmentPaymentBinding binding;
    private View snackBarView;
    Context context;
    TextView tvAddPaymentMethod,tvAddPromoCode;
    ImageButton ibback;
    private BackPressedFragment backPressed;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public PaymentFragment() {
        // Required empty public constructor
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
        ibback.setOnClickListener(this);
    }

    private void bindViews() {
        tvAddPaymentMethod=binding.tvAddPaymentMethod;
        tvAddPromoCode=binding.tvAddPromoCode;
        ibback=binding.ibBack;

    }


    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
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
                Intent i=new Intent(getActivity(), HomeActivity.class);
                startActivity(i);
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

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
