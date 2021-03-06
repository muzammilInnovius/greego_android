package com.greegoapp.Activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.android.volley.request.SimpleMultiPartRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.google.gson.Gson;
import com.greegoapp.Adapter.VehicleDetailAdapter;
import com.greegoapp.AppController.AppController;
import com.greegoapp.GlobleFields.GlobalValues;
import com.greegoapp.Interface.CallFragmentInterface;
import com.greegoapp.Interface.RecyclerViewItemClickListener;
import com.greegoapp.Model.GetUserData;
import com.greegoapp.Model.UserData;
import com.greegoapp.R;
import com.greegoapp.SessionManager.SessionManager;
import com.greegoapp.Utils.Applog;
import com.greegoapp.Utils.ConnectivityDetector;
import com.greegoapp.Utils.MyProgressDialog;
import com.greegoapp.Utils.SnackBar;
import com.greegoapp.Utils.WebFields;
import com.greegoapp.databinding.ActivityUserProfileBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.greegoapp.Activity.HomeActivity.HOME_SLIDER_PROFILE_UPDATE;
import static com.greegoapp.Activity.HomeActivity.MAP_HOME_PROFILE_UPDATE;
import static com.greegoapp.Activity.SettingActivity.SETTING_PROFILE_UPDATE;

public class UserProfileActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityUserProfileBinding binding;
    private View snackBarView;
    Context context;
    ImageButton ibback;
    RecyclerView recyclerView;
    ArrayList<GetUserData.DataBean.VehiclesBean> alVehicleDetail;
    public static VehicleDetailAdapter adapter;
    RecyclerViewItemClickListener mListener;
    ImageView ivProPic;
    private static final int SELECT_GALLERY_PIC = 101;
    private static final int SELECT_CAMERA_PIC = 99;
    public final int MY_PERMISSIONS_REQUEST_CAMERA = 108;
    public static final int SELECT_CHOISE_VEHICLE = 1100;
    //  private OnFragmentInteractionListener mListener;
    GetUserData userDetails;
    EditText edtTvProfileFname, edtTvProfileLname;
    Button btnUpdate, tvAdd;
    String strEmail, profilePicUrl;

    Intent in;
    String filePath_driving_license;
    private Uri mImageUri;
    private Bitmap bitmap = null;
    private String strProPicBase64 = "";
    private CallFragmentInterface callMyFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_profile);

        snackBarView = findViewById(android.R.id.content);
        context = UserProfileActivity.this;

        alVehicleDetail = new ArrayList<>();

//        setHeaderbar();
        bindViews();
        setListner();
        if (ConnectivityDetector.isConnectingToInternet(context)) {
            callUserMeApi();

        } else {
            SnackBar.showInternetError(context, snackBarView);
        }


    }

    private void setRecyclerView() {
        RecyclerViewItemClickListener mListener = new RecyclerViewItemClickListener() {
            @Override
            public void onClick(View view, int position) {
//                Fragment fragment = new TripHistoryDetailFragment();
//                FragmentManager fragmentManager = UserProfileActivity.this.getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.containerBody, fragment);
//                fragmentTransaction.addToBackStack(null);
//                fragmentTransaction.commit();

            }
        };


        adapter = new VehicleDetailAdapter(context, alVehicleDetail, mListener);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    private void setListner() {
        ivProPic.setOnClickListener(this);
        ibback.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);
        tvAdd.setOnClickListener(this);
    }

    private void bindViews() {
        ibback = binding.ibBack;
        recyclerView = binding.rcVwVehicle;
        ivProPic = binding.ivProPic;
        edtTvProfileLname = binding.edtTvProfileLname;
        edtTvProfileFname = binding.edtTvProfileFname;
        btnUpdate = binding.btnUpdate;
        tvAdd = binding.tvAdd;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ibBack:
                if (HOME_SLIDER_PROFILE_UPDATE == 1000) {
                    Intent data = new Intent();
                    String userName = edtTvProfileFname.getText().toString();
                    data.putExtra("profilePic", profilePicUrl);
                    data.putExtra("name", userName);
//                    callMyFragment.callUpdateVehicalMethod(alVehicleDetail);
                    setResult(HOME_SLIDER_PROFILE_UPDATE, data);
                } else if (MAP_HOME_PROFILE_UPDATE == 1002) {
                    Intent data = new Intent();
                    String userName = edtTvProfileFname.getText().toString();
//                    data.putExtra("profilePic", profilePicUrl);
//                    data.putExtra("name", userName);
                    setResult(MAP_HOME_PROFILE_UPDATE, data);
                } else if (SETTING_PROFILE_UPDATE == 1001) {
                    Intent data = new Intent();
                    String userName = edtTvProfileFname.getText().toString();
                    data.putExtra("profilePic", profilePicUrl);
                    data.putExtra("name", userName);
                    setResult(SETTING_PROFILE_UPDATE, data);
                }
                finish();
                break;

            case R.id.ivProPic:
                checkPermission();
                break;

            case R.id.btnUpdate:
                if (ConnectivityDetector.isConnectingToInternet(context)) {
                    callUpdateProfileAPI();

                } else {
                    SnackBar.showInternetError(context, snackBarView);
                }

//                callUpdateProAPI();
                break;

            case R.id.tvAdd:
                in = new Intent(context, AddEditVehicleActivity.class);
                startActivityForResult(in, SELECT_CHOISE_VEHICLE);
                break;

        }

    }

    private void callUpdateProAPI() {
        SimpleMultiPartRequest smr = new SimpleMultiPartRequest(Request.Method.POST, WebFields.BASE_URL + WebFields.SIGN_UP.MODE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);
                        try {
                            JSONObject jObj = new JSONObject(response);
                            String message = jObj.getString("message");

                            UserData userDetails = new Gson().fromJson(String.valueOf(response), UserData.class);

                            MyProgressDialog.hideProgressDialog();
//
                            if (userDetails.getError_code() == 0) {

                                Applog.E("UserDetails" + userDetails);
//                            callUserMeApi();
                                String userName = userDetails.getData().getName().toString();
                                Intent data = new Intent();
                                data.putExtra("name", userName);
                                setResult(HOME_SLIDER_PROFILE_UPDATE, data);

                                SessionManager.setIsUserLoggedin(context, true);
                                SnackBar.showSuccess(context, snackBarView, "Profile Update.");
                            } else {
                                MyProgressDialog.hideProgressDialog();
                                SnackBar.showError(context, snackBarView, getResources().getString(R.string.something_went_wrong));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                SnackBar.showError(context, snackBarView, getResources().getString(R.string.something_went_wrong));
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put(WebFields.PARAM_ACCEPT, "application/json");
                params.put(WebFields.PARAM_AUTHOTIZATION, GlobalValues.BEARER_TOKEN + SessionManager.getToken(context));

                return params;
            }
        };

        String strFName = edtTvProfileFname.getText().toString();
        String strLName = edtTvProfileLname.getText().toString();

        smr.addFile(WebFields.SIGN_UP.PARAM_PRO_PIC, filePath_driving_license);
        smr.addFile(WebFields.SIGN_UP.PARAM_FIRST_NAME, strFName);
        smr.addFile(WebFields.SIGN_UP.PARAM_LAST_NAME, strLName);
        smr.addFile(WebFields.SIGN_UP.PARAM_EMAIL, strEmail);
        smr.addFile(WebFields.SIGN_UP.PARAM_CITY, "");
        try {
            AppController.getInstance().addToRequestQueue(smr);
        } catch (Exception e) {
            SnackBar.showError(context, snackBarView, getResources().getString(R.string.something_went_wrong));
        }
    }

    private void checkPermission() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(context,
                        Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(context,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(context,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED)

                {
                    ActivityCompat.requestPermissions(UserProfileActivity.this,
                            new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_REQUEST_CAMERA);


                } else {
                    showPicProfileDialog();
                }
            } else {
                showPicProfileDialog();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showPicProfileDialog() {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(context)
                    .setTitle("Greego App").setMessage("Choose profile pic");


            builder.setNegativeButton("Gallery", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                  /*  Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent,
                            "Select Picture"), SELECT_GALLERY_PIC);*/
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, SELECT_GALLERY_PIC);
                }
            });

            builder.setPositiveButton("Camera", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    openCamera();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

//        Applog.e("pick image", "");
//        final String[] items = new String[]{"Choose existing", "Take photo"};
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(UserProfileActivity.this,
//                R.layout.custom_camera_layout, items);
//        AlertDialog.Builder builder = new AlertDialog.Builder(UserProfileActivity.this);
//        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int item) {
//
//                if (item == 0) {
//                    Intent intent = new Intent();
//                    intent.setType("image/*");
//                    intent.setAction(Intent.ACTION_GET_CONTENT);
//                    startActivityForResult(Intent.createChooser(intent,
//                            "Select Picture"), 99);
//                    // startActivityForResult(intent, 99);
//                } else {
//                    openCamera();
//                }
//            }
//        });
//
//        final AlertDialog dialog = builder.create();
//
//        dialog.show();
    }

    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, SELECT_CAMERA_PIC);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

//            // user chose an image from the gallery
//            // loadAsync(data.getData());
            if (requestCode == SELECT_GALLERY_PIC) {

//                Uri picUri = data.getData();
//                ivProPic.setImageURI(picUri);
//                filePath_driving_license = getPath(picUri);
                mImageUri = data.getData();
                try {
                    bitmap = getBitmapFromUri(mImageUri);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                ivProPic.setImageURI(mImageUri);
                strProPicBase64 = encodeTobase64(bitmap);

                if (ConnectivityDetector.isConnectingToInternet(context)) {
                    callUserProUpdate();

                } else {
                    SnackBar.showInternetError(context, snackBarView);
                }

//
            } else if (requestCode == SELECT_CAMERA_PIC) {
//
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                ivProPic.setImageURI(getImageUri(photo));
                strProPicBase64 = encodeTobase64(photo);

                if (ConnectivityDetector.isConnectingToInternet(context)) {
                    callUserProUpdate();

                } else {
                    SnackBar.showInternetError(context, snackBarView);
                }

            }
        } else if (requestCode == SELECT_CHOISE_VEHICLE) {
            if (ConnectivityDetector.isConnectingToInternet(context)) {
                callUserMeApi();
            } else {
                SnackBar.showInternetError(context, snackBarView);
            }

        }
    }

    private void callUserProUpdate() {
        try {
            JSONObject jsonObject = new JSONObject();

            jsonObject.put(WebFields.PROFILE_UPDATE.PARAM_IMAGE, strProPicBase64);

            Applog.E("request Profile Pic=> " + jsonObject.toString());
            MyProgressDialog.showProgressDialog(context);

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    WebFields.BASE_URL + WebFields.PROFILE_UPDATE.MODE, jsonObject, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    Applog.E("success: " + response.toString());

                    UserData userDetails = new Gson().fromJson(String.valueOf(response), UserData.class);
                    try {
                        MyProgressDialog.hideProgressDialog();
//
                        if (userDetails.getError_code() == 0) {

                            Applog.E("UserDetails" + userDetails);
//                            callUserMeApi();
                            profilePicUrl = userDetails.getData().getProfile_pic();


                            if (profilePicUrl != null) {
                                Glide.clear(ivProPic);
                                Glide.with(getApplicationContext())
                                        .load(profilePicUrl)
                                        .centerCrop()
                                        .signature(new StringSignature(UUID.randomUUID().toString()))
                                        .crossFade().skipMemoryCache(true)
                                        .into(ivProPic);

                            } else {
                                ivProPic.setImageResource(R.mipmap.ic_place_holder);
                            }


                            String userName = userDetails.getData().getName().toString();
                            Intent data = new Intent();
                            data.putExtra("profilePic", profilePicUrl);
                            data.putExtra("name", userName);
                            setResult(HOME_SLIDER_PROFILE_UPDATE, data);

                            SessionManager.setIsUserLoggedin(context, true);

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

    private String getPath(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(getApplicationContext(), contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        Log.e("result path", result);
        return result;
    }

    String path;
    public Uri getImageUri(Bitmap inImage) {

        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "Title", null);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return Uri.parse(path);
    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                context.getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }

    public static String encodeTobase64(Bitmap image) {
        Bitmap immagex = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        Applog.E(imageEncoded);
        return imageEncoded;
    }

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
                    alVehicleDetail = new ArrayList<>();
                    try {
                        MyProgressDialog.hideProgressDialog();
//
                        if (userDetails.getError_code() == 0) {

                            edtTvProfileFname.setText(userDetails.getData().getName());
                            edtTvProfileLname.setText(userDetails.getData().getLastname());
                            strEmail = userDetails.getData().getEmail();

//                            ivProPic.setImageURI(Uri.parse(userDetails.getData().getProfile_pic()));

                            profilePicUrl = userDetails.getData().getProfile_pic();


                            if (profilePicUrl != null) {
                                Glide.clear(ivProPic);
                                Glide.with(getApplicationContext())
                                        .load(profilePicUrl)
                                        .centerCrop()
                                        .signature(new StringSignature(UUID.randomUUID().toString()))
                                        .crossFade().skipMemoryCache(true)
                                        .into(ivProPic);

                            } else {
                                ivProPic.setImageResource(R.mipmap.ic_user_profile);
                            }


                            alVehicleDetail.addAll(userDetails.getData().getVehicles());
                            Applog.E("vehicle size==>" + alVehicleDetail.size());

                            setRecyclerView();

//                            adapter.notifyDataSetChanged();
//
                        } else {
                            MyProgressDialog.hideProgressDialog();
                            SnackBar.showError(context, snackBarView, getResources().getString(R.string.something_went_wrong));
                        }
                    } catch (Exception e) {
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

    private void callUpdateProfileAPI() {
        try {
            JSONObject jsonObject = new JSONObject();

            String strFName = edtTvProfileFname.getText().toString();
            String strLName = edtTvProfileLname.getText().toString();

            jsonObject.put(WebFields.SIGN_UP.PARAM_FIRST_NAME, strFName);
            jsonObject.put(WebFields.SIGN_UP.PARAM_LAST_NAME, strLName);
            Applog.E("Email==>" + strEmail);
            jsonObject.put(WebFields.SIGN_UP.PARAM_EMAIL, strEmail);
            jsonObject.put(WebFields.SIGN_UP.PARAM_CITY, "");
            jsonObject.put(WebFields.SIGN_UP.PARAM_PRO_PIC, "");

            Applog.E("request DigitCode=> " + jsonObject.toString());
            MyProgressDialog.showProgressDialog(context);

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    WebFields.BASE_URL + WebFields.SIGN_UP.MODE, jsonObject, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    Applog.E("success: " + response.toString());

                    UserData userDetails = new Gson().fromJson(String.valueOf(response), UserData.class);
                    try {
                        MyProgressDialog.hideProgressDialog();
//
                        if (userDetails.getError_code() == 0) {

                            Applog.E("UserDetails" + userDetails);
//                            callUserMeApi();
                            String userName = userDetails.getData().getName().toString();
                            Intent data = new Intent();
                            data.putExtra("name", userName);
                            setResult(HOME_SLIDER_PROFILE_UPDATE, data);

                            SessionManager.setIsUserLoggedin(context, true);

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (HOME_SLIDER_PROFILE_UPDATE == 1000) {
            Intent data = new Intent();
//            String userName = edtTvProfileFname.getText().toString();
//            data.putExtra("name", userName);
            setResult(HOME_SLIDER_PROFILE_UPDATE, data);
        } else if (SETTING_PROFILE_UPDATE == 1001) {
            Intent data = new Intent();
            String userName = edtTvProfileFname.getText().toString();
            data.putExtra("profilePic", profilePicUrl);
            data.putExtra("name", userName);
            setResult(SETTING_PROFILE_UPDATE, data);
        }
        finish();
    }

//    public static void adapterChange() {
//        adapter.notifyDataSetChanged();
//    }


}
