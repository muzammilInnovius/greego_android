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
import android.widget.RelativeLayout;

import com.greegoapp.Activity.HomeActivity;
import com.greegoapp.Adapter.TripHistoryAdapter;
import com.greegoapp.Interface.RecyclerViewItemClickListener;
import com.greegoapp.Model.TripHistoryModel;
import com.greegoapp.R;
import com.greegoapp.databinding.FragmentAddPaymentMethodBinding;
import com.greegoapp.databinding.FragmentTripHistoryBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class TripHistoryFragment extends Fragment implements View.OnClickListener {
    FragmentTripHistoryBinding binding;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private View snackBarView;
    Context context;
    RecyclerView recyclerView;
    ImageButton ibback;
    List<TripHistoryModel> tripHistoryList;
    TripHistoryAdapter tripHistoryAdapter;
    TripHistoryModel tripHistoryModel;
    RecyclerViewItemClickListener mListener;



    public TripHistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_trip_history, container, false);

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
        tripHistoryModel.setDate("45");
        tripHistoryList.add(tripHistoryModel);
        recyclerView.setAdapter(new TripHistoryAdapter(tripHistoryList, context,mListener));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

    }

    private void setListner() {
        ibback.setOnClickListener(this);


    }

    private void bindViews() {
        recyclerView=binding.rcVwTripHistory;
        ibback=binding.ibBack;

        tripHistoryModel = new TripHistoryModel();
        tripHistoryList = new ArrayList<TripHistoryModel>();
        tripHistoryAdapter = new TripHistoryAdapter();

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

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.ibBack:
                Intent i=new Intent(getActivity(), HomeActivity.class);
                startActivity(i);
                break;

        }
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
