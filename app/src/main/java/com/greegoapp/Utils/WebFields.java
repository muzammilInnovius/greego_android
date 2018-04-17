package com.greegoapp.Utils;

public class WebFields {

    public static final String BASE_URL = "http://innoviussoftware.com/greego/public/api/";

    public static final String USERID = "userId";
    public static final String STATUS = "status";
    public static final String MESSAGE = "message";
    public static final String PARAM_ACCEPT = "Accept";
    public static final String PARAM_AUTHOTIZATION = "Authorization";

    public static final class SIGN_IN {
        public static final String MODE = "login";
        public static final String PARAM_CONTACT_NO = "contact_number";
        public static final String PARAM_IS_PHONE_NO = "is_iphone";
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
}
