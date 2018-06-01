package com.greegoapp.Utils;

public class WebFields {

//    Local Url
//    public static final String BASE_URL = "http://innoviussoftware.com/greego/public/api/";

    //    Live URl
//    public static final String BASE_URL = "https://www.kroslinkstech.in/greego/public/api/";

//    Client live url(Amazon)
    public static final String BASE_URL = "http://54.144.110.21/api/";


    public static final String USERID = "userId";
    public static final String STATUS = "status";
    public static final String MESSAGE = "message";
    public static final String PARAM_ACCEPT = "Accept";
    public static final String PARAM_AUTHOTIZATION = "Authorization";

    public static final class SIGN_IN {
        public static final String MODE = "login";
        public static final String PARAM_CONTACT_NO = "contact_number";
        public static final String PARAM_IS_PHONE_NO = "is_iphone";
        public static final String PARAM_DEVICE_ID = "device_id";
    }


    public static final class VERIFY_OTP {
        public static final String MODE = "verify/otp";
        public static final String PARAM_CONTACT_NO = "contact_number";
        public static final String PARAM_OTP = "otp";
    }


    public static final class SIGN_UP {
        public static final String MODE = "user/update";

        public static final String PARAM_FIRST_NAME = "name";
        public static final String PARAM_EMAIL = "email";
        public static final String PARAM_LAST_NAME = "lastname";
        public static final String PARAM_CITY = "city";
        public static final String PARAM_PRO_PIC = "profile_pic";
        public static final String PARAM_IS_AGGREED = "is_agreed";
    }


    public static final class USER_ME {
        public static final String MODE = "user/me";
    }


    public static final class UPDATE_CARD {
        public static final String MODE = "user/update/card";

        public static final String PARAM_CARD_NUMBER = "card_number";
        public static final String PARAM_EXP_MONTH_YEAR = "exp_month_year";
        public static final String PARAM_ZIPCODE = "zipcode";
        public static final String PARAM_CARD_TOKEN = "card_token";

    }

    public static final class GET_VEHICLES {
        public static final String GET_MANUFACTURES_MODE = "get/manufacturers";


        public static final String GET_VEHICAL_MODE = "get/vehicles";
        public static final String PARAM_VEHICAL_MANUF_ID = "vehicle_manufacturer_id";
    }

    public static final class ADD_VEHICLE {
        public static final String MODE = "user/add/vehicle";

        public static final String PARAM_MANUFACTURER_ID = "vehicle_manufacturer_id";
        public static final String PARAM_MODEL_ID = "vehicle_id";
        public static final String PARAM_YEAR = "year";
        public static final String PARAM_COLOR = "color";
        public static final String PARAM_CAR_TYPE = "type";
        public static final String PARAM_TRANSMISSION_TYPE = "transmission_type";

    }

    public static final class SELECT_VEHICLE {
        public static final String MODE = "user/select/vehicle";
        public static final String PARAM_VEHICLE_ID = "vehicle_id";
    }

    public static final class TRIP {
        public static final String MODE = "get/rates";
        public static final String PARAM_STATE = "state";
    }

    public static final class USER_NEAR_DRIVER_LIST {
        public static final String MODE = "user/get/drivers";

        public static final String PARAM_USER_LAT = "lat";
        public static final String PARAM_USER_LONG = "lng";
    }


    public static final class USER_ADD_REQUEST {
        public static final String MODE = "user/add/request";

        public static final String PARAM_USER_VEHICLE_ID = "user_vehicle_id";
        public static final String PARAM_FROM_ADDRESS = "from_address";
        public static final String PARAM_FROM_LAT = "from_lat";
        public static final String PARAM_FROM_LNG = "from_lng";
        public static final String PARAM_TO_ADDRESS = "to_address";
        public static final String PARAM_TO_LAT = "to_lat";
        public static final String PARAM_TO_LNG = "to_lng";
        public static final String PARAM_TOTAL_EST_TRAVEL_TIME = "total_estimated_travel_time";
        public static final String PARAM_TOTAL_EST_TRIP_COST = "total_estimated_trip_cost";

    }

    public static final class GET_USER_TRIP_DETAILS {
        public static final String MODE = "user/get/tripdetails";

        public static final String PARAM_TRIP_ID = "trip_id";
        public static final String PARAM_TRIP_STATUS = "STATUS";
    }

    public static final class DELETE_VEHICLE {
        public static final String MODE = "user/delete/vehicle";

        public static final String PARAM_VEHICLE_ID = "vehicle_id";
    }


    public static final class PROFILE_UPDATE {
        public static final String MODE = "user/add/profile_pic_base64";

        public static final String PARAM_IMAGE = "image";
    }

    public static final class SELECT_DELETE_CARD {
        public static final String SELECT_CARD_MODE = "user/select/card";
        public static final String DELETE_CARD_MODE = "user/delete/card";
        public static final String PARAM_CARD_ID = "card_id";
    }


    public static final class CANCLE_TRIP_REQUEST {
        public static final String MODE = "user/cancle/request";

        public static final String PARAM_REQUEST_ID = "request_id";

    }

//    https://www.kroslinkstech.in/greego/public/api/user/add/driverrating

    public static final class DRIVER_RATING {
        public static final String MODE = "user/add/driverrating";

        public static final String PARAM_RATING = "rating";
        public static final String PARAM_TRIP_ID = "trip_id";
    }


    public static final class GET_TRIP_HISTORY {
        public static final String MODE = "user/get/triphistory";

        public static final String PARAM_USER_ID = "user_id";
    }


    public static final class DIST_URL {
        //        https://maps.googleapis.com/maps/api/directions/json?origin=\(origin)&destination=\(destination)&mode=driving&key=AIzaSyDuLTaJL-tMzdBoTZtCQfCz4m66iEZ1eQc
//        public static final String MODE="https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&";
        public static final String MODE = "https://maps.googleapis.com/maps/api/directions/json?";

        public static final String ORIGINS = "origin=";
        public static final String DESTINATION = "&destination=";
    }

//    http://kroslinkstech.in/greego/public/api/user/verify/email

    public static final class EMAIL_VERIFY {
        public static final String MODE = "user/verify/email";
    }

    public static final class TERMS_CONDITION {
        public static final String MODE = "get/texts";
    }



    public static final class TIP_UPDATE{
        public static final String MODE = "user/get/tipupdate";

        public static final String TRIP_ID = "trip_id";
        public static final String TIP_AMOUNT= "tip_amount";
    }



}
