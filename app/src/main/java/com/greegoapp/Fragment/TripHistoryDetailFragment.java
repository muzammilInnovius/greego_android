package com.greegoapp.Fragment;

import android.app.FragmentTransaction;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.greegoapp.Interface.BackPressedFragment;
import com.greegoapp.Interface.CallFragmentInterface;
import com.greegoapp.R;
import com.greegoapp.Utils.HeaderBar;
import com.greegoapp.databinding.FragmentTripHistoryBinding;
import com.greegoapp.databinding.FragmentTripHistoryDetailBinding;


public class TripHistoryDetailFragment extends Fragment implements View.OnClickListener {
    FragmentTripHistoryDetailBinding binding;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private View snackBarView;
    TextView tvdate;
    Context context;
    ImageButton ibback;
    //  int unicode = 0x1F62D;
    //String code;
    private BackPressedFragment backPressed;
    CallFragmentInterface callMyFragment;
    private OnFragmentInteractionListener mListener;


    public static TripHistoryDetailFragment newInstance(String param1, String param2) {
        TripHistoryDetailFragment fragment = new TripHistoryDetailFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_trip_history_detail, container, false);

        View view = binding.getRoot();
        snackBarView = getActivity().findViewById(android.R.id.content);
        context = getActivity();

        bindViews();

        setListner();


        return view;
    }

    private HeaderBar headerBar;

    private void setHeaderbar() {
        try {
//            headerBar = (HeaderBar) findViewById(R.id.headerBar);
            headerBar.ivLeft.setImageResource(R.mipmap.ic_profile);
            headerBar.ivLeft.setOnClickListener(this);

            headerBar.rrHomeBtn.setVisibility(View.GONE);

            headerBar.ivRight.setVisibility(View.GONE);
            headerBar.ivRightOfLeft.setVisibility(View.GONE);

            headerBar.tvTitle.setVisibility(View.VISIBLE);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setListner() {
        ibback.setOnClickListener(this);
    }

    private void bindViews() {
        ibback = binding.ibBack;
        tvdate = binding.tvTripHistoryTitle;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        callMyFragment = null;
        backPressed = null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ibBack:
                backPressed.onBackPressed(getActivity());
                break;
        }
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
