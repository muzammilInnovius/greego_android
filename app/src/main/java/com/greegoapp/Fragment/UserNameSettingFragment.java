package com.greegoapp.Fragment;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.greegoapp.Activity.HomeActivity;
import com.greegoapp.Adapter.VehicleDetailAdapter;
import com.greegoapp.Interface.RecyclerViewItemClickListener;
import com.greegoapp.Model.VehicleDetailModel;
import com.greegoapp.R;
import com.greegoapp.databinding.FragmentUserNameSettingBinding;

import java.util.ArrayList;
import java.util.List;


public class UserNameSettingFragment extends Fragment implements View.OnClickListener {
    FragmentUserNameSettingBinding binding;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private View snackBarView;
    Context context;
    ImageButton ibback;
    RecyclerView recyclerView;
    List<VehicleDetailModel> vehicleDetailList;
    VehicleDetailAdapter VehicleDetailAdapter;
    VehicleDetailModel vehicleDetailModel;
    RecyclerViewItemClickListener mListener;

  //  private OnFragmentInteractionListener mListener;

    public UserNameSettingFragment() {
    }

     public static UserNameSettingFragment newInstance(String param1, String param2) {
        UserNameSettingFragment fragment = new UserNameSettingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_name_setting, container, false);

        View view = binding.getRoot();
        snackBarView = getActivity().findViewById(android.R.id.content);
        context = getActivity();

        bindViews();
        setRecyclerView();

        setListner();

        return view;
    }

    private void setRecyclerView() {
        RecyclerViewItemClickListener mListener = new RecyclerViewItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                Fragment fragment = new TripHistoryDetailFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.containerBody, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        };
        recyclerView.setHasFixedSize(true);
        vehicleDetailModel.setYear("2015");
        vehicleDetailList.add(vehicleDetailModel);
        recyclerView.setAdapter(new VehicleDetailAdapter(vehicleDetailList, context,mListener));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

    }

    private void setListner() {
        ibback.setOnClickListener(this);
    }

    private void bindViews() {
        ibback=binding.ibBack;
        recyclerView=binding.rcVwVehicle;
        vehicleDetailModel = new VehicleDetailModel();
        vehicleDetailList = new ArrayList<VehicleDetailModel>();
        VehicleDetailAdapter = new VehicleDetailAdapter();

    }

    public void onButtonPressed(Uri uri) {
      /*  if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }*/
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ibBack:
                Fragment fragment = new SettingFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.containerBody, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
        }

    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
