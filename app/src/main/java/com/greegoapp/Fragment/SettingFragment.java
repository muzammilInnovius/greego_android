package com.greegoapp.Fragment;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.greegoapp.Activity.HomeActivity;
import com.greegoapp.R;
import com.greegoapp.databinding.FragmentSettingBinding;

public class SettingFragment extends Fragment implements View.OnClickListener {
    FragmentSettingBinding  binding;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private View snackBarView;
    Context context;
    private String mParam1;
    private String mParam2;
    ImageButton ibback;
    RelativeLayout rl_name,rl_phone,rl_email,rl_btnlogout;

    private OnFragmentInteractionListener mListener;

    public SettingFragment() {
    }

   public static SettingFragment newInstance(String param1, String param2) {
        SettingFragment fragment = new SettingFragment();
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting, container, false);

        View view = binding.getRoot();
        snackBarView = getActivity().findViewById(android.R.id.content);
        context = getActivity();

        bindViews();

        setListner();

        return view;
    }

    private void setListner() {
        ibback.setOnClickListener(this);
        rl_name.setOnClickListener(this);
        rl_phone.setOnClickListener(this);
        rl_email.setOnClickListener(this);
        rl_btnlogout.setOnClickListener(this);
    }

    private void bindViews() {
        ibback=binding.ibBack;
        rl_name=binding.rlName;
        rl_phone=binding.rlPhone;
        rl_email=binding.rlEmail;
        rl_btnlogout=binding.rlLogout;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
        switch (view.getId())
        {
            case R.id.ibBack:
                Intent i=new Intent(getActivity(), HomeActivity.class);
                startActivity(i);
                break;
            case R.id.rlName:
                Fragment fragment = new UserNameSettingFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.containerBody, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
            case R.id.rlEmail:
                Toast.makeText(getContext(),"email layout click",Toast.LENGTH_LONG).show();
                break;
            case R.id.rlLogout:
                Toast.makeText(getContext(),"logout layout click",Toast.LENGTH_LONG).show();
                break;
        }
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
