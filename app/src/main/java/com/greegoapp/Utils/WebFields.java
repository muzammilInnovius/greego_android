package com.greegoapp.Utils;

public class WebFields {

    public static final String BASE_URL = "http://innoviussoftware.com/greego/public/api/";

    public static final String USERID = "userId";
    public static final String STATUS = "status";
    public static final String MESSAGE = "message";


    public static final class SIGN_IN{
        public static final String MODE = "login";
        public static final String PARAM_CONTACT_NO= "contact_number";
        public static final String PARAM_IS_PHONE_NO= "is_iphone";
    }


    public static final class VERIFY_OTP{
        public static final String MODE = "verify/otp";
        public static final String PARAM_CONTACT_NO= "contact_number";
        public static final String PARAM_OTP= "otp";
    }



    public static final class SIGN_UP{
        public static final String MODE = "user/update";

        public static final String PARAM_ACCEPT= "Accept";
        public static final String PARAM_AUTHOTIZATION= "Authorization";
        public static final String PARAM_FIRST_NAME= "name";
        public static final String PARAM_EMAIL= "email";
        public static final String PARAM_LAST_NAME= "lastname";
        public static final String PARAM_CITY= "city";
        public static final String PARAM_PRO_PIC= "profile_pic";
        public static final String PARAM_IS_AGGREED= "is_agreed";
    }



}
