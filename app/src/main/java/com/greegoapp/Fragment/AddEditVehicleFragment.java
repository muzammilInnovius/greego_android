package com.greegoapp.Fragment;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.greegoapp.AppController.AppController;
import com.greegoapp.GlobleFields.GlobalValues;
import com.greegoapp.Interface.BackPressedFragment;
import com.greegoapp.Interface.CallFragmentInterface;
import com.greegoapp.Model.GetManufactures;
import com.greegoapp.Model.GetVehicle;
import com.greegoapp.R;
import com.greegoapp.SessionManager.SessionManager;
import com.greegoapp.Utils.Applog;
import com.greegoapp.Utils.ConnectivityDetector;
import com.greegoapp.Utils.KeyboardUtility;
import com.greegoapp.Utils.MyProgressDialog;
import com.greegoapp.Utils.SnackBar;
import com.greegoapp.Utils.WebFields;
import com.greegoapp.databinding.FragmentAddEditVehicleBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class AddEditVehicleFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private EditText edtTvMake, edtTvModel, edtTvYear, edtTvColor, edtTvCarType;
    private Spinner spinnerMake, spinnerModel, spinnerYear, spinnerColor, spinnerCarType;
    private TextView tvAutoMatic, tvManual;
    private Button btnSaveVehicle;
    FragmentAddEditVehicleBinding binding;
    private View snackBarView;
    Context context;


    private ArrayAdapter<GetManufactures.DataBean> ManufacturesBeanArrayAdapter;
    private ArrayAdapter<GetVehicle.DataBean> vehicalModelListArrayAdapter;

    private ArrayList<GetManufactures.DataBean> alManufactur;
    private ArrayList<GetVehicle.DataBean> alVehical;


    String strManufactur, strVehicle;
    int manufacturIds, modelIds;
    private ArrayAdapter<String> carTypeArrayAdapter;
    private String[] carType;
    private int carTypeIds;
    String vehCarType;
    ImageButton ibBack;
    private BackPressedFragment backPressed;
    CallFragmentInterface callMyFragment;
    public AddEditVehicleFragment() {
        // Required empty public constructor
    }


    public static AddEditVehicleFragment newInstance(String param1, String param2) {
        AddEditVehicleFragment fragment = new AddEditVehicleFragment();
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
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_edit_vehicle, container, false);

        View view = binding.getRoot();
        snackBarView = getActivity().findViewById(android.R.id.content);
        context = getActivity();
        bindViews();
        //Mujju
        callGetVehicalAPI();
        setListners();
        getCarTypedArray();

        return view;
    }

    private void callGetVehicalAPI() {
        try {

            JSONObject jsonObject = new JSONObject();

            Applog.E("request: " + jsonObject.toString());
            MyProgressDialog.showProgressDialog(context);

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    WebFields.BASE_URL + WebFields.GET_VEHICLES.GET_MANUFACTURES_MODE, jsonObject, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    Applog.E("success: " + response.toString());
                    GetManufactures manufactures = new Gson().fromJson(String.valueOf(response), GetManufactures.class);

                    try {
                        MyProgressDialog.hideProgressDialog();
//
                        if (manufactures.getError_code() == 0) {
                            alManufactur = new ArrayList<>();

                            alManufactur.addAll(manufactures.getData());

                            GetManufactures.DataBean makeData = new GetManufactures.DataBean();
                            makeData.setId(0);
                            makeData.setName("CHOOSE VEHICLE MAKE");
                            alManufactur.add(0, makeData);


                            ManufacturesBeanArrayAdapter = new ArrayAdapter<GetManufactures.DataBean>(context, android.R.layout.simple_spinner_dropdown_item, alManufactur) {
                                @Override
                                public boolean isEnabled(int position) {
                                    if (position == 0) {
                                        // Disable the first item from Spinner
                                        // First item will be use for hint
                                        return false;
                                    } else {
                                        return true;
                                    }
                                }

                                @Override
                                public View getDropDownView(int position, View convertView,
                                                            ViewGroup parent) {
                                    View view = super.getDropDownView(position, convertView, parent);
                                    TextView tv = (TextView) view;
                                    if (position == 0) {
                                        // Set the hint text color gray
                                        tv.setTextColor(Color.GRAY);
                                    } else {
                                        tv.setTextColor(Color.BLACK);
                                    }
                                    return view;
                                }
                            };

                            ManufacturesBeanArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                            spinnerMake.setAdapter(ManufacturesBeanArrayAdapter);
                            spinnerMake.setOnItemSelectedListener(makeListBeanListener);


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
            })
//            {
//                @Override
//                public Map<String, String> getHeaders() throws AuthFailureError {
//                    Map<String, String> params = new HashMap<String, String>();
//
//                    params.put(WebFields.PARAM_ACCEPT, "application/json");
//                    Applog.E("Token==>" + SessionManager.getToken(context));
//                    params.put(WebFields.PARAM_AUTHOTIZATION, GlobalValues.BEARER_TOKEN + SessionManager.getToken(context));
//
//                    return params;
//                }
//            }
                    ;
            jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                    GlobalValues.TIME_OUT,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            AppController.getInstance().addToRequestQueue(jsonObjReq);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setListners() {
        edtTvCarType.setOnClickListener(this);
        edtTvMake.setOnClickListener(this);
        edtTvModel.setOnClickListener(this);
        edtTvYear.setOnClickListener(this);
        edtTvColor.setOnClickListener(this);
        btnSaveVehicle.setOnClickListener(this);
        tvManual.setOnClickListener(this);
        tvAutoMatic.setOnClickListener(this);
        ibBack.setOnClickListener(this);
    }

    private void bindViews() {
        ibBack = binding.ibBack;
        edtTvCarType = binding.edtTvCarType;
        edtTvMake = binding.edtTvMake;
        edtTvModel = binding.edtTvModel;
        edtTvYear = binding.edtTvYear;
        edtTvColor = binding.edtTvColor;

        spinnerMake = binding.spinnerMake;
        spinnerModel = binding.spinnerModel;
//        spinnerYear = binding.spinnerYear;
//        spinnerColor = binding.spinnerColor;
        spinnerCarType = binding.spinnerCarType;

        tvAutoMatic = binding.tvAutoMatic;
        tvManual = binding.tvManual;
        btnSaveVehicle = binding.btnSaveVehicle;
    }

    int transmissionType;
    boolean autoType;
    boolean manualType;

    @Override

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.edtTvMake:
                edtTvMake.requestFocus();

                if (alManufactur != null) {
                    spinnerMake.performClick();
                } else {
                    if (ConnectivityDetector
                            .isConnectingToInternet(context)) {
                        callVehicalModelApi(manufacturIds);
                    } else {
                        SnackBar.showInternetError(context, snackBarView);
                    }
                }
                break;

            case R.id.edtTvModel:
                KeyboardUtility.hideKeyboard(context, view);
                edtTvModel.requestFocus();
                if (strManufactur == null) {
                    edtTvModel.requestFocus();
                    SnackBar.showValidationError(context, snackBarView, getString(R.string.vehicle_make_empty));
                    return;
                }
                if (alVehical != null) {
                    spinnerModel.performClick();
                } else {
//                    if (ConnectivityDetector
//                            .isConnectingToInternet(context)) {
////                        callGetModelApi(strManufactur);
//                    } else {
//                        SnackBar.showInternetError(context, snackBarView);
//                    }

                }
                break;

            case R.id.ibBack:
                backPressed.onBackPressed(getActivity());
                break;
            case R.id.edtTvYear:
                break;

            case R.id.edtTvColor:
                break;

            case R.id.edtTvCarType:
                edtTvCarType.requestFocus();
                if (carType != null) {
                    spinnerCarType.performClick();
                }
                break;


            case R.id.tvAutoMatic:
                if (autoType) {
                    tvAutoMatic.setTextColor(getResources().getColor(R.color.hint_color));
                    autoType = false;
                } else {
                    transmissionType = 0;
                    autoType = true;
                    tvAutoMatic.setTextColor(getResources().getColor(R.color.app_bg));
                }
                break;
            case R.id.tvManual:
                if (manualType) {
                    tvManual.setTextColor(getResources().getColor(R.color.hint_color));
                    manualType = false;
                } else {
                    manualType = true;
                    transmissionType = 1;
                    tvManual.setTextColor(getResources().getColor(R.color.app_bg));
                }
                break;

            case R.id.btnSaveVehicle:
                if (isValid()) {
                    if (ConnectivityDetector
                            .isConnectingToInternet(context)) {

                        callSaveVehicleAPI();

                    } else {
                        SnackBar.showInternetError(context, snackBarView);
                    }
                }


                break;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callMyFragment = null;
        backPressed = null;
    }

    private boolean isValid() {
        String make = edtTvMake.getText().toString().trim();
        String model = edtTvModel.getText().toString().trim();
        String year = edtTvYear.getText().toString().trim();
        String type = edtTvCarType.getText().toString().trim();
        String color = edtTvColor.getText().toString().trim();

        if (make.isEmpty()) {
            edtTvMake.requestFocus();
            SnackBar.showValidationError(context, snackBarView, getString(R.string.vehicle_make_empty));
            return false;
        } else if (model.isEmpty()) {
            edtTvModel.requestFocus();
            SnackBar.showValidationError(context, snackBarView, getString(R.string.vehicle_model_empty));
            return false;
        } else if (year.isEmpty()) {
            edtTvYear.requestFocus();
            SnackBar.showValidationError(context, snackBarView, getString(R.string.vehicle_year_empty));
            return false;
        } else if (color.isEmpty()) {
            edtTvColor.requestFocus();
            SnackBar.showValidationError(context, snackBarView, getString(R.string.vehicle_color_empty));
            return false;
        } else if (type.isEmpty()) {
            edtTvCarType.requestFocus();
            SnackBar.showValidationError(context, snackBarView, getString(R.string.vehicle_car_empty));
            return false;
        }
        return true;
    }

    private void callSaveVehicleAPI() {
        try {
            String year = edtTvYear.getText().toString().trim();
            String color = edtTvColor.getText().toString().trim();

            JSONObject jsonObject = new JSONObject();
            jsonObject.put(WebFields.ADD_VEHICLE.PARAM_MANUFACTURER_ID, manufacturIds);
            jsonObject.put(WebFields.ADD_VEHICLE.PARAM_MODEL_ID, modelIds);
            jsonObject.put(WebFields.ADD_VEHICLE.PARAM_YEAR, year);
            jsonObject.put(WebFields.ADD_VEHICLE.PARAM_COLOR, color);
            jsonObject.put(WebFields.ADD_VEHICLE.PARAM_CAR_TYPE, carTypeIds);
            jsonObject.put(WebFields.ADD_VEHICLE.PARAM_TRANSMISSION_TYPE, transmissionType);
            Applog.E("Request Vehicle" + jsonObject);
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    WebFields.BASE_URL + WebFields.ADD_VEHICLE.MODE, jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Applog.E("Save vehicle success: " + response.toString());

                    SnackBar.showSuccess(context, snackBarView, "Vehicle Add successfully.");
//                    GetVehicle vehicleData = new Gson().fromJson(String.valueOf(response), GetVehicle.class);
                    //                hidepDialog();
//                    if (vehicleData.getError_code() == 0) {
//
//
//                    } else
//
//                    {
//                        SnackBar.showError(context, snackBarView, vehicleData.getMessage());
//                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Applog.E("Error: " + error.getMessage());

                    SnackBar.showError(context, snackBarView, getResources().getString(R.string.something_went_wrong));
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
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
            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(jsonObjReq);
        } catch (
                Exception e)

        {
            e.printStackTrace();
        }

    }


    private void getCarTypedArray() {
        carType = getResources().getStringArray(R.array.typeOfModel);
        carTypeArrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, carType) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        carTypeArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerCarType.setAdapter(carTypeArrayAdapter);
        spinnerCarType.setOnItemSelectedListener(carTypeListner);
    }

    private AdapterView.OnItemSelectedListener carTypeListner = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            try {

//                edtBusinessState.setText("" + spinnerBusinessType.getItemAtPosition(position));


                if (position > 0) {
                    vehCarType = spinnerCarType.getItemAtPosition(position).toString();
                    edtTvCarType.setText(vehCarType);
                    Applog.E(" onItemSelected: businessType: " + vehCarType);
                    if (vehCarType.equalsIgnoreCase(carType[1])) {
                        carTypeIds = 0;
                    } else if (vehCarType.equalsIgnoreCase(carType[2])) {
                        carTypeIds = 1;
                    } else if (vehCarType.equalsIgnoreCase(carType[3])) {
                        carTypeIds = 2;
                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private void callVehicalModelApi(int manufId) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(WebFields.GET_VEHICLES.PARAM_VEHICAL_MANUF_ID, manufId);

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    WebFields.BASE_URL + WebFields.GET_VEHICLES.GET_VEHICAL_MODE, jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Applog.E("VEHICLE MODEL success: " + response.toString());

                    GetVehicle vehicleData = new Gson().fromJson(String.valueOf(response), GetVehicle.class);
                    //                hidepDialog();
                    if (vehicleData.getError_code() == 0) {
                        alVehical = new ArrayList<>();
                        alVehical.addAll(vehicleData.getData());

                        GetVehicle.DataBean questionDatum1 = new GetVehicle.DataBean();
                        questionDatum1.setId(0);
                        questionDatum1.setModel("CHOOSE VEHICLE MODEL");
                        alVehical.add(0, questionDatum1);


                        vehicalModelListArrayAdapter = new ArrayAdapter<GetVehicle.DataBean>(context, android.R.layout.simple_spinner_dropdown_item, alVehical) {
                            @Override
                            public boolean isEnabled(int position) {
                                if (position == 0) {
                                    // Disable the first item from Spinner
                                    // First item will be use for hint
                                    return false;
                                } else {
                                    return true;
                                }
                            }

                            @Override
                            public View getDropDownView(int position, View convertView,
                                                        ViewGroup parent) {
                                View view = super.getDropDownView(position, convertView, parent);
                                TextView tv = (TextView) view;
                                if (position == 0) {
                                    // Set the hint text color gray
                                    tv.setTextColor(Color.GRAY);
                                } else {
                                    tv.setTextColor(Color.BLACK);
                                }
                                return view;
                            }
                        };
                        vehicalModelListArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        spinnerModel.setAdapter(vehicalModelListArrayAdapter);
                        spinnerModel.setOnItemSelectedListener(modelItemSelectedListener);


                    } else

                    {
                        SnackBar.showError(context, snackBarView, vehicleData.getMessage());
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Applog.E("Error: " + error.getMessage());

                    SnackBar.showError(context, snackBarView, getResources().getString(R.string.something_went_wrong));
                }
            });
            jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                    GlobalValues.TIME_OUT,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(jsonObjReq);
        } catch (
                Exception e)

        {
            e.printStackTrace();
        }

    }


    private AdapterView.OnItemSelectedListener modelItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            try {
//                spinnerQue1.setVisibility(View.INVISIBLE);
                if (position > 0) {
                    final GetVehicle.DataBean questionDatum = (GetVehicle.DataBean) spinnerModel.getItemAtPosition(position);
                    Applog.E(" onItemSelected: zipcode: " + questionDatum.getId());
                    edtTvModel.setText(questionDatum.getModel());
                    strVehicle = questionDatum.getModel();
                    modelIds = questionDatum.getId();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
    private AdapterView.OnItemSelectedListener makeListBeanListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            try {
//                spinnerQue1.setVisibility(View.INVISIBLE);
                if (position > 0) {
                    final GetManufactures.DataBean questionDatum = (GetManufactures.DataBean) spinnerMake.getItemAtPosition(position);
                    Applog.E("SpinnerCountry onItemSelected: state: " + questionDatum.getId());
                    edtTvMake.setText(questionDatum.getName());
//                edtTxtState.setBackground(ContextCompat.getDrawable(context,R.drawable.green_rounded_edittext));
                    strManufactur = questionDatum.getName();
                    manufacturIds = questionDatum.getId();
                    strVehicle = null;
                    alVehical = null;
                    spinnerMake.setSelection(0);
                    spinnerModel.setSelection(0);
                    edtTvModel.setText("CHOOSE VEHICLE MODEL");
                    callVehicalModelApi(questionDatum.getId());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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



    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
