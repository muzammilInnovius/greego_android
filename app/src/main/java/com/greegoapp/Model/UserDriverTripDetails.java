package com.greegoapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class UserDriverTripDetails implements Parcelable {


    /**
     * data : {"id":47,"request_id":"55","user_id":"101","driver_id":"161","status":"1","actual_trip_amount":"0","tip_amount":"10","total_trip_amount":"10","actual_trip_travel_time":"10","payment_status":"0","trip_driver_rating":"2","trip_user_rating":"3","created_at":"2018-04-25 07:46:26","updated_at":"2018-04-25 07:46:26","driver":{"id":161,"name":"sapan","lastname":"shah","email":"abc@abc.com","dob":"2018-12-04","contact_number":"+919408742287","city":null,"profile_pic":"profile_pic/U1vDcvKxnfsAcmtV5tUbqe4ik2toJO3k7O6cuswU.jpeg","promocode":"0","legal_firstname":"sapan","legal_middlename":"m","legal_lastname":"shah","social_security_number":"123456789","is_sedan":"1","is_suv":"1","is_van":"0","is_auto":"1","is_manual":"0","current_status":"0","verified":"0","is_agreed":"1","is_iphone":"0","profile_status":"7","is_approve":"0","device_id":"fpJBjHFJnag:APA91bHaxDmSR7FVZK1x6jkqKdquZW0jAnzt08DogekxVD0wjINrC035q5b7ygC5Z5on-TtzEsKz-rDjf6plxdSMktL0_SQefq7gHFc2mLdWEZ0Z0Qd8WRaRUhl9elOvvnt9iy3ZqxI5","created_at":"2018-04-21 04:55:42","updated_at":"2018-04-25 06:52:43"},"request":{"id":55,"user_id":"101","user_vehicle_id":"123","from_address":"28/2, Mahalaxmi Society, Paldi, Ahmedabad, Gujarat 380007, India","from_lat":"23.0105","from_lng":"72.5611","to_address":"LOADING TRASPORT SERVICES, Block J, Sainik Farm, New Delhi, Delhi, India","to_lat":"28.4998","to_lng":"77.2146","total_estimated_travel_time":"12","total_estimated_trip_cost":"100","request_status":"1","created_at":"2018-04-25 07:46:14","updated_at":"2018-04-25 07:46:26","user":{"id":101,"name":"John","email":"muza@gmail.com","lastname":"bxhdh","contact_number":"+919998841576","city":null,"profile_pic":null,"promocode":null,"verified":"0","is_iphone":"0","is_agreed":"1","device_id":"ep-Zoewj644:APA91bGqo0_yjeTsI9XFvgvob5JhGQdUrOue3PX1SwBSi5w1PY04l9xt30Ovqu9OjGD7Aa_OcmwgU7PDEzwB7FSFXWeCJ9GO-21mTcWGJV4HoeZciXySJv8CUgXUgZbWsBiHU-AUBwEW","created_at":"2018-04-20 10:47:07","updated_at":"2018-04-25 06:49:14"},"user_vehicle":{"id":123,"user_id":"101","vehicle_id":"2","year":"2017","type":"0","color":"red","transmission_type":"1","selected":"0","created_at":"2018-04-23 13:10:57","updated_at":"2018-04-25 06:48:07","vehiclemodel":{"id":2,"vehicle_manufacturer_id":"1","model":"427","updated_at":"2018-04-11 01:39:54","created_at":"2018-04-11 01:39:54","vmake":{"id":1,"name":"ac","created_at":"2018-04-11 01:39:52","updated_at":"2018-04-11 01:39:52"}}}}}
     * error_code : 0
     * message :
     */

    private DataBean data;
    private int error_code;
    private String message;

    protected UserDriverTripDetails(Parcel in) {
        error_code = in.readInt();
        message = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(error_code);
        dest.writeString(message);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<UserDriverTripDetails> CREATOR = new Creator<UserDriverTripDetails>() {
        @Override
        public UserDriverTripDetails createFromParcel(Parcel in) {
            return new UserDriverTripDetails(in);
        }

        @Override
        public UserDriverTripDetails[] newArray(int size) {
            return new UserDriverTripDetails[size];
        }
    };

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static class DataBean implements Parcelable{
        /**
         * id : 47
         * request_id : 55
         * user_id : 101
         * driver_id : 161
         * status : 1
         * actual_trip_amount : 0
         * tip_amount : 10
         * total_trip_amount : 10
         * actual_trip_travel_time : 10
         * payment_status : 0
         * trip_driver_rating : 2
         * trip_user_rating : 3
         * created_at : 2018-04-25 07:46:26
         * updated_at : 2018-04-25 07:46:26
         * driver : {"id":161,"name":"sapan","lastname":"shah","email":"abc@abc.com","dob":"2018-12-04","contact_number":"+919408742287","city":null,"profile_pic":"profile_pic/U1vDcvKxnfsAcmtV5tUbqe4ik2toJO3k7O6cuswU.jpeg","promocode":"0","legal_firstname":"sapan","legal_middlename":"m","legal_lastname":"shah","social_security_number":"123456789","is_sedan":"1","is_suv":"1","is_van":"0","is_auto":"1","is_manual":"0","current_status":"0","verified":"0","is_agreed":"1","is_iphone":"0","profile_status":"7","is_approve":"0","device_id":"fpJBjHFJnag:APA91bHaxDmSR7FVZK1x6jkqKdquZW0jAnzt08DogekxVD0wjINrC035q5b7ygC5Z5on-TtzEsKz-rDjf6plxdSMktL0_SQefq7gHFc2mLdWEZ0Z0Qd8WRaRUhl9elOvvnt9iy3ZqxI5","created_at":"2018-04-21 04:55:42","updated_at":"2018-04-25 06:52:43"}
         * request : {"id":55,"user_id":"101","user_vehicle_id":"123","from_address":"28/2, Mahalaxmi Society, Paldi, Ahmedabad, Gujarat 380007, India","from_lat":"23.0105","from_lng":"72.5611","to_address":"LOADING TRASPORT SERVICES, Block J, Sainik Farm, New Delhi, Delhi, India","to_lat":"28.4998","to_lng":"77.2146","total_estimated_travel_time":"12","total_estimated_trip_cost":"100","request_status":"1","created_at":"2018-04-25 07:46:14","updated_at":"2018-04-25 07:46:26","user":{"id":101,"name":"John","email":"muza@gmail.com","lastname":"bxhdh","contact_number":"+919998841576","city":null,"profile_pic":null,"promocode":null,"verified":"0","is_iphone":"0","is_agreed":"1","device_id":"ep-Zoewj644:APA91bGqo0_yjeTsI9XFvgvob5JhGQdUrOue3PX1SwBSi5w1PY04l9xt30Ovqu9OjGD7Aa_OcmwgU7PDEzwB7FSFXWeCJ9GO-21mTcWGJV4HoeZciXySJv8CUgXUgZbWsBiHU-AUBwEW","created_at":"2018-04-20 10:47:07","updated_at":"2018-04-25 06:49:14"},"user_vehicle":{"id":123,"user_id":"101","vehicle_id":"2","year":"2017","type":"0","color":"red","transmission_type":"1","selected":"0","created_at":"2018-04-23 13:10:57","updated_at":"2018-04-25 06:48:07","vehiclemodel":{"id":2,"vehicle_manufacturer_id":"1","model":"427","updated_at":"2018-04-11 01:39:54","created_at":"2018-04-11 01:39:54","vmake":{"id":1,"name":"ac","created_at":"2018-04-11 01:39:52","updated_at":"2018-04-11 01:39:52"}}}}
         */

        private int id;
        private String request_id;
        private String user_id;
        private String driver_id;
        private String status;
        private String actual_trip_amount;
        private String tip_amount;
        private String total_trip_amount;
        private String actual_trip_travel_time;
        private String payment_status;
        private String trip_driver_rating;
        private String trip_user_rating;
        private String created_at;
        private String updated_at;
        private DriverBean driver;
        private RequestBean request;

        protected DataBean(Parcel in) {
            id = in.readInt();
            request_id = in.readString();
            user_id = in.readString();
            driver_id = in.readString();
            status = in.readString();
            actual_trip_amount = in.readString();
            tip_amount = in.readString();
            total_trip_amount = in.readString();
            actual_trip_travel_time = in.readString();
            payment_status = in.readString();
            trip_driver_rating = in.readString();
            trip_user_rating = in.readString();
            created_at = in.readString();
            updated_at = in.readString();
            driver = in.readParcelable(DriverBean.class.getClassLoader());
            request = in.readParcelable(RequestBean.class.getClassLoader());
        }

        public static final Creator<DataBean> CREATOR = new Creator<DataBean>() {
            @Override
            public DataBean createFromParcel(Parcel in) {
                return new DataBean(in);
            }

            @Override
            public DataBean[] newArray(int size) {
                return new DataBean[size];
            }
        };

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getRequest_id() {
            return request_id;
        }

        public void setRequest_id(String request_id) {
            this.request_id = request_id;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getDriver_id() {
            return driver_id;
        }

        public void setDriver_id(String driver_id) {
            this.driver_id = driver_id;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getActual_trip_amount() {
            return actual_trip_amount;
        }

        public void setActual_trip_amount(String actual_trip_amount) {
            this.actual_trip_amount = actual_trip_amount;
        }

        public String getTip_amount() {
            return tip_amount;
        }

        public void setTip_amount(String tip_amount) {
            this.tip_amount = tip_amount;
        }

        public String getTotal_trip_amount() {
            return total_trip_amount;
        }

        public void setTotal_trip_amount(String total_trip_amount) {
            this.total_trip_amount = total_trip_amount;
        }

        public String getActual_trip_travel_time() {
            return actual_trip_travel_time;
        }

        public void setActual_trip_travel_time(String actual_trip_travel_time) {
            this.actual_trip_travel_time = actual_trip_travel_time;
        }

        public String getPayment_status() {
            return payment_status;
        }

        public void setPayment_status(String payment_status) {
            this.payment_status = payment_status;
        }

        public String getTrip_driver_rating() {
            return trip_driver_rating;
        }

        public void setTrip_driver_rating(String trip_driver_rating) {
            this.trip_driver_rating = trip_driver_rating;
        }

        public String getTrip_user_rating() {
            return trip_user_rating;
        }

        public void setTrip_user_rating(String trip_user_rating) {
            this.trip_user_rating = trip_user_rating;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }

        public DriverBean getDriver() {
            return driver;
        }

        public void setDriver(DriverBean driver) {
            this.driver = driver;
        }

        public RequestBean getRequest() {
            return request;
        }

        public void setRequest(RequestBean request) {
            this.request = request;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(id);
            dest.writeString(request_id);
            dest.writeString(user_id);
            dest.writeString(driver_id);
            dest.writeString(status);
            dest.writeString(actual_trip_amount);
            dest.writeString(tip_amount);
            dest.writeString(total_trip_amount);
            dest.writeString(actual_trip_travel_time);
            dest.writeString(payment_status);
            dest.writeString(trip_driver_rating);
            dest.writeString(trip_user_rating);
            dest.writeString(created_at);
            dest.writeString(updated_at);
            dest.writeParcelable(driver, flags);
            dest.writeParcelable(request, flags);
        }

        public static class DriverBean implements Parcelable{
            /**
             * id : 161
             * name : sapan
             * lastname : shah
             * email : abc@abc.com
             * dob : 2018-12-04
             * contact_number : +919408742287
             * city : null
             * profile_pic : profile_pic/U1vDcvKxnfsAcmtV5tUbqe4ik2toJO3k7O6cuswU.jpeg
             * promocode : 0
             * legal_firstname : sapan
             * legal_middlename : m
             * legal_lastname : shah
             * social_security_number : 123456789
             * is_sedan : 1
             * is_suv : 1
             * is_van : 0
             * is_auto : 1
             * is_manual : 0
             * current_status : 0
             * verified : 0
             * is_agreed : 1
             * is_iphone : 0
             * profile_status : 7
             * is_approve : 0
             * device_id : fpJBjHFJnag:APA91bHaxDmSR7FVZK1x6jkqKdquZW0jAnzt08DogekxVD0wjINrC035q5b7ygC5Z5on-TtzEsKz-rDjf6plxdSMktL0_SQefq7gHFc2mLdWEZ0Z0Qd8WRaRUhl9elOvvnt9iy3ZqxI5
             * created_at : 2018-04-21 04:55:42
             * updated_at : 2018-04-25 06:52:43
             */

            private int id;
            private String name;
            private String lastname;
            private String email;
            private String dob;
            private String contact_number;
            private Object city;
            private String profile_pic;
            private String promocode;
            private String legal_firstname;
            private String legal_middlename;
            private String legal_lastname;
            private String social_security_number;
            private String is_sedan;
            private String is_suv;
            private String is_van;
            private String is_auto;
            private String is_manual;
            private String current_status;
            private String verified;
            private String is_agreed;
            private String is_iphone;
            private String profile_status;
            private String is_approve;
            private String device_id;
            private String created_at;
            private String updated_at;

            protected DriverBean(Parcel in) {
                id = in.readInt();
                name = in.readString();
                lastname = in.readString();
                email = in.readString();
                dob = in.readString();
                contact_number = in.readString();
                profile_pic = in.readString();
                promocode = in.readString();
                legal_firstname = in.readString();
                legal_middlename = in.readString();
                legal_lastname = in.readString();
                social_security_number = in.readString();
                is_sedan = in.readString();
                is_suv = in.readString();
                is_van = in.readString();
                is_auto = in.readString();
                is_manual = in.readString();
                current_status = in.readString();
                verified = in.readString();
                is_agreed = in.readString();
                is_iphone = in.readString();
                profile_status = in.readString();
                is_approve = in.readString();
                device_id = in.readString();
                created_at = in.readString();
                updated_at = in.readString();
            }

            public static final Creator<DriverBean> CREATOR = new Creator<DriverBean>() {
                @Override
                public DriverBean createFromParcel(Parcel in) {
                    return new DriverBean(in);
                }

                @Override
                public DriverBean[] newArray(int size) {
                    return new DriverBean[size];
                }
            };

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getLastname() {
                return lastname;
            }

            public void setLastname(String lastname) {
                this.lastname = lastname;
            }

            public String getEmail() {
                return email;
            }

            public void setEmail(String email) {
                this.email = email;
            }

            public String getDob() {
                return dob;
            }

            public void setDob(String dob) {
                this.dob = dob;
            }

            public String getContact_number() {
                return contact_number;
            }

            public void setContact_number(String contact_number) {
                this.contact_number = contact_number;
            }

            public Object getCity() {
                return city;
            }

            public void setCity(Object city) {
                this.city = city;
            }

            public String getProfile_pic() {
                return profile_pic;
            }

            public void setProfile_pic(String profile_pic) {
                this.profile_pic = profile_pic;
            }

            public String getPromocode() {
                return promocode;
            }

            public void setPromocode(String promocode) {
                this.promocode = promocode;
            }

            public String getLegal_firstname() {
                return legal_firstname;
            }

            public void setLegal_firstname(String legal_firstname) {
                this.legal_firstname = legal_firstname;
            }

            public String getLegal_middlename() {
                return legal_middlename;
            }

            public void setLegal_middlename(String legal_middlename) {
                this.legal_middlename = legal_middlename;
            }

            public String getLegal_lastname() {
                return legal_lastname;
            }

            public void setLegal_lastname(String legal_lastname) {
                this.legal_lastname = legal_lastname;
            }

            public String getSocial_security_number() {
                return social_security_number;
            }

            public void setSocial_security_number(String social_security_number) {
                this.social_security_number = social_security_number;
            }

            public String getIs_sedan() {
                return is_sedan;
            }

            public void setIs_sedan(String is_sedan) {
                this.is_sedan = is_sedan;
            }

            public String getIs_suv() {
                return is_suv;
            }

            public void setIs_suv(String is_suv) {
                this.is_suv = is_suv;
            }

            public String getIs_van() {
                return is_van;
            }

            public void setIs_van(String is_van) {
                this.is_van = is_van;
            }

            public String getIs_auto() {
                return is_auto;
            }

            public void setIs_auto(String is_auto) {
                this.is_auto = is_auto;
            }

            public String getIs_manual() {
                return is_manual;
            }

            public void setIs_manual(String is_manual) {
                this.is_manual = is_manual;
            }

            public String getCurrent_status() {
                return current_status;
            }

            public void setCurrent_status(String current_status) {
                this.current_status = current_status;
            }

            public String getVerified() {
                return verified;
            }

            public void setVerified(String verified) {
                this.verified = verified;
            }

            public String getIs_agreed() {
                return is_agreed;
            }

            public void setIs_agreed(String is_agreed) {
                this.is_agreed = is_agreed;
            }

            public String getIs_iphone() {
                return is_iphone;
            }

            public void setIs_iphone(String is_iphone) {
                this.is_iphone = is_iphone;
            }

            public String getProfile_status() {
                return profile_status;
            }

            public void setProfile_status(String profile_status) {
                this.profile_status = profile_status;
            }

            public String getIs_approve() {
                return is_approve;
            }

            public void setIs_approve(String is_approve) {
                this.is_approve = is_approve;
            }

            public String getDevice_id() {
                return device_id;
            }

            public void setDevice_id(String device_id) {
                this.device_id = device_id;
            }

            public String getCreated_at() {
                return created_at;
            }

            public void setCreated_at(String created_at) {
                this.created_at = created_at;
            }

            public String getUpdated_at() {
                return updated_at;
            }

            public void setUpdated_at(String updated_at) {
                this.updated_at = updated_at;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeInt(id);
                dest.writeString(name);
                dest.writeString(lastname);
                dest.writeString(email);
                dest.writeString(dob);
                dest.writeString(contact_number);
                dest.writeString(profile_pic);
                dest.writeString(promocode);
                dest.writeString(legal_firstname);
                dest.writeString(legal_middlename);
                dest.writeString(legal_lastname);
                dest.writeString(social_security_number);
                dest.writeString(is_sedan);
                dest.writeString(is_suv);
                dest.writeString(is_van);
                dest.writeString(is_auto);
                dest.writeString(is_manual);
                dest.writeString(current_status);
                dest.writeString(verified);
                dest.writeString(is_agreed);
                dest.writeString(is_iphone);
                dest.writeString(profile_status);
                dest.writeString(is_approve);
                dest.writeString(device_id);
                dest.writeString(created_at);
                dest.writeString(updated_at);
            }
        }

        public static class RequestBean implements Parcelable{
            /**
             * id : 55
             * user_id : 101
             * user_vehicle_id : 123
             * from_address : 28/2, Mahalaxmi Society, Paldi, Ahmedabad, Gujarat 380007, India
             * from_lat : 23.0105
             * from_lng : 72.5611
             * to_address : LOADING TRASPORT SERVICES, Block J, Sainik Farm, New Delhi, Delhi, India
             * to_lat : 28.4998
             * to_lng : 77.2146
             * total_estimated_travel_time : 12
             * total_estimated_trip_cost : 100
             * request_status : 1
             * created_at : 2018-04-25 07:46:14
             * updated_at : 2018-04-25 07:46:26
             * user : {"id":101,"name":"John","email":"muza@gmail.com","lastname":"bxhdh","contact_number":"+919998841576","city":null,"profile_pic":null,"promocode":null,"verified":"0","is_iphone":"0","is_agreed":"1","device_id":"ep-Zoewj644:APA91bGqo0_yjeTsI9XFvgvob5JhGQdUrOue3PX1SwBSi5w1PY04l9xt30Ovqu9OjGD7Aa_OcmwgU7PDEzwB7FSFXWeCJ9GO-21mTcWGJV4HoeZciXySJv8CUgXUgZbWsBiHU-AUBwEW","created_at":"2018-04-20 10:47:07","updated_at":"2018-04-25 06:49:14"}
             * user_vehicle : {"id":123,"user_id":"101","vehicle_id":"2","year":"2017","type":"0","color":"red","transmission_type":"1","selected":"0","created_at":"2018-04-23 13:10:57","updated_at":"2018-04-25 06:48:07","vehiclemodel":{"id":2,"vehicle_manufacturer_id":"1","model":"427","updated_at":"2018-04-11 01:39:54","created_at":"2018-04-11 01:39:54","vmake":{"id":1,"name":"ac","created_at":"2018-04-11 01:39:52","updated_at":"2018-04-11 01:39:52"}}}
             */

            private int id;
            private String user_id;
            private String user_vehicle_id;
            private String from_address;
            private String from_lat;
            private String from_lng;
            private String to_address;
            private String to_lat;
            private String to_lng;
            private String total_estimated_travel_time;
            private String total_estimated_trip_cost;
            private String request_status;
            private String created_at;
            private String updated_at;
            private UserBean user;
            private UserVehicleBean user_vehicle;

            protected RequestBean(Parcel in) {
                id = in.readInt();
                user_id = in.readString();
                user_vehicle_id = in.readString();
                from_address = in.readString();
                from_lat = in.readString();
                from_lng = in.readString();
                to_address = in.readString();
                to_lat = in.readString();
                to_lng = in.readString();
                total_estimated_travel_time = in.readString();
                total_estimated_trip_cost = in.readString();
                request_status = in.readString();
                created_at = in.readString();
                updated_at = in.readString();
                user = in.readParcelable(UserBean.class.getClassLoader());
                user_vehicle = in.readParcelable(UserVehicleBean.class.getClassLoader());
            }

            public static final Creator<RequestBean> CREATOR = new Creator<RequestBean>() {
                @Override
                public RequestBean createFromParcel(Parcel in) {
                    return new RequestBean(in);
                }

                @Override
                public RequestBean[] newArray(int size) {
                    return new RequestBean[size];
                }
            };

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getUser_id() {
                return user_id;
            }

            public void setUser_id(String user_id) {
                this.user_id = user_id;
            }

            public String getUser_vehicle_id() {
                return user_vehicle_id;
            }

            public void setUser_vehicle_id(String user_vehicle_id) {
                this.user_vehicle_id = user_vehicle_id;
            }

            public String getFrom_address() {
                return from_address;
            }

            public void setFrom_address(String from_address) {
                this.from_address = from_address;
            }

            public String getFrom_lat() {
                return from_lat;
            }

            public void setFrom_lat(String from_lat) {
                this.from_lat = from_lat;
            }

            public String getFrom_lng() {
                return from_lng;
            }

            public void setFrom_lng(String from_lng) {
                this.from_lng = from_lng;
            }

            public String getTo_address() {
                return to_address;
            }

            public void setTo_address(String to_address) {
                this.to_address = to_address;
            }

            public String getTo_lat() {
                return to_lat;
            }

            public void setTo_lat(String to_lat) {
                this.to_lat = to_lat;
            }

            public String getTo_lng() {
                return to_lng;
            }

            public void setTo_lng(String to_lng) {
                this.to_lng = to_lng;
            }

            public String getTotal_estimated_travel_time() {
                return total_estimated_travel_time;
            }

            public void setTotal_estimated_travel_time(String total_estimated_travel_time) {
                this.total_estimated_travel_time = total_estimated_travel_time;
            }

            public String getTotal_estimated_trip_cost() {
                return total_estimated_trip_cost;
            }

            public void setTotal_estimated_trip_cost(String total_estimated_trip_cost) {
                this.total_estimated_trip_cost = total_estimated_trip_cost;
            }

            public String getRequest_status() {
                return request_status;
            }

            public void setRequest_status(String request_status) {
                this.request_status = request_status;
            }

            public String getCreated_at() {
                return created_at;
            }

            public void setCreated_at(String created_at) {
                this.created_at = created_at;
            }

            public String getUpdated_at() {
                return updated_at;
            }

            public void setUpdated_at(String updated_at) {
                this.updated_at = updated_at;
            }

            public UserBean getUser() {
                return user;
            }

            public void setUser(UserBean user) {
                this.user = user;
            }

            public UserVehicleBean getUser_vehicle() {
                return user_vehicle;
            }

            public void setUser_vehicle(UserVehicleBean user_vehicle) {
                this.user_vehicle = user_vehicle;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeInt(id);
                dest.writeString(user_id);
                dest.writeString(user_vehicle_id);
                dest.writeString(from_address);
                dest.writeString(from_lat);
                dest.writeString(from_lng);
                dest.writeString(to_address);
                dest.writeString(to_lat);
                dest.writeString(to_lng);
                dest.writeString(total_estimated_travel_time);
                dest.writeString(total_estimated_trip_cost);
                dest.writeString(request_status);
                dest.writeString(created_at);
                dest.writeString(updated_at);
                dest.writeParcelable(user, flags);
                dest.writeParcelable(user_vehicle, flags);
            }

            public static class UserBean implements Parcelable{
                /**
                 * id : 101
                 * name : John
                 * email : muza@gmail.com
                 * lastname : bxhdh
                 * contact_number : +919998841576
                 * city : null
                 * profile_pic : null
                 * promocode : null
                 * verified : 0
                 * is_iphone : 0
                 * is_agreed : 1
                 * device_id : ep-Zoewj644:APA91bGqo0_yjeTsI9XFvgvob5JhGQdUrOue3PX1SwBSi5w1PY04l9xt30Ovqu9OjGD7Aa_OcmwgU7PDEzwB7FSFXWeCJ9GO-21mTcWGJV4HoeZciXySJv8CUgXUgZbWsBiHU-AUBwEW
                 * created_at : 2018-04-20 10:47:07
                 * updated_at : 2018-04-25 06:49:14
                 */

                private int id;
                private String name;
                private String email;
                private String lastname;
                private String contact_number;
                private Object city;
                private Object profile_pic;
                private Object promocode;
                private String verified;
                private String is_iphone;
                private String is_agreed;
                private String device_id;
                private String created_at;
                private String updated_at;

                protected UserBean(Parcel in) {
                    id = in.readInt();
                    name = in.readString();
                    email = in.readString();
                    lastname = in.readString();
                    contact_number = in.readString();
                    verified = in.readString();
                    is_iphone = in.readString();
                    is_agreed = in.readString();
                    device_id = in.readString();
                    created_at = in.readString();
                    updated_at = in.readString();
                }

                public static final Creator<UserBean> CREATOR = new Creator<UserBean>() {
                    @Override
                    public UserBean createFromParcel(Parcel in) {
                        return new UserBean(in);
                    }

                    @Override
                    public UserBean[] newArray(int size) {
                        return new UserBean[size];
                    }
                };

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getEmail() {
                    return email;
                }

                public void setEmail(String email) {
                    this.email = email;
                }

                public String getLastname() {
                    return lastname;
                }

                public void setLastname(String lastname) {
                    this.lastname = lastname;
                }

                public String getContact_number() {
                    return contact_number;
                }

                public void setContact_number(String contact_number) {
                    this.contact_number = contact_number;
                }

                public Object getCity() {
                    return city;
                }

                public void setCity(Object city) {
                    this.city = city;
                }

                public Object getProfile_pic() {
                    return profile_pic;
                }

                public void setProfile_pic(Object profile_pic) {
                    this.profile_pic = profile_pic;
                }

                public Object getPromocode() {
                    return promocode;
                }

                public void setPromocode(Object promocode) {
                    this.promocode = promocode;
                }

                public String getVerified() {
                    return verified;
                }

                public void setVerified(String verified) {
                    this.verified = verified;
                }

                public String getIs_iphone() {
                    return is_iphone;
                }

                public void setIs_iphone(String is_iphone) {
                    this.is_iphone = is_iphone;
                }

                public String getIs_agreed() {
                    return is_agreed;
                }

                public void setIs_agreed(String is_agreed) {
                    this.is_agreed = is_agreed;
                }

                public String getDevice_id() {
                    return device_id;
                }

                public void setDevice_id(String device_id) {
                    this.device_id = device_id;
                }

                public String getCreated_at() {
                    return created_at;
                }

                public void setCreated_at(String created_at) {
                    this.created_at = created_at;
                }

                public String getUpdated_at() {
                    return updated_at;
                }

                public void setUpdated_at(String updated_at) {
                    this.updated_at = updated_at;
                }

                @Override
                public int describeContents() {
                    return 0;
                }

                @Override
                public void writeToParcel(Parcel dest, int flags) {
                    dest.writeInt(id);
                    dest.writeString(name);
                    dest.writeString(email);
                    dest.writeString(lastname);
                    dest.writeString(contact_number);
                    dest.writeString(verified);
                    dest.writeString(is_iphone);
                    dest.writeString(is_agreed);
                    dest.writeString(device_id);
                    dest.writeString(created_at);
                    dest.writeString(updated_at);
                }
            }

            public static class UserVehicleBean implements Parcelable{
                /**
                 * id : 123
                 * user_id : 101
                 * vehicle_id : 2
                 * year : 2017
                 * type : 0
                 * color : red
                 * transmission_type : 1
                 * selected : 0
                 * created_at : 2018-04-23 13:10:57
                 * updated_at : 2018-04-25 06:48:07
                 * vehiclemodel : {"id":2,"vehicle_manufacturer_id":"1","model":"427","updated_at":"2018-04-11 01:39:54","created_at":"2018-04-11 01:39:54","vmake":{"id":1,"name":"ac","created_at":"2018-04-11 01:39:52","updated_at":"2018-04-11 01:39:52"}}
                 */

                private int id;
                private String user_id;
                private String vehicle_id;
                private String year;
                private String type;
                private String color;
                private String transmission_type;
                private String selected;
                private String created_at;
                private String updated_at;
                private VehiclemodelBean vehiclemodel;

                protected UserVehicleBean(Parcel in) {
                    id = in.readInt();
                    user_id = in.readString();
                    vehicle_id = in.readString();
                    year = in.readString();
                    type = in.readString();
                    color = in.readString();
                    transmission_type = in.readString();
                    selected = in.readString();
                    created_at = in.readString();
                    updated_at = in.readString();
                    vehiclemodel = in.readParcelable(VehiclemodelBean.class.getClassLoader());
                }

                public static final Creator<UserVehicleBean> CREATOR = new Creator<UserVehicleBean>() {
                    @Override
                    public UserVehicleBean createFromParcel(Parcel in) {
                        return new UserVehicleBean(in);
                    }

                    @Override
                    public UserVehicleBean[] newArray(int size) {
                        return new UserVehicleBean[size];
                    }
                };

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public String getUser_id() {
                    return user_id;
                }

                public void setUser_id(String user_id) {
                    this.user_id = user_id;
                }

                public String getVehicle_id() {
                    return vehicle_id;
                }

                public void setVehicle_id(String vehicle_id) {
                    this.vehicle_id = vehicle_id;
                }

                public String getYear() {
                    return year;
                }

                public void setYear(String year) {
                    this.year = year;
                }

                public String getType() {
                    return type;
                }

                public void setType(String type) {
                    this.type = type;
                }

                public String getColor() {
                    return color;
                }

                public void setColor(String color) {
                    this.color = color;
                }

                public String getTransmission_type() {
                    return transmission_type;
                }

                public void setTransmission_type(String transmission_type) {
                    this.transmission_type = transmission_type;
                }

                public String getSelected() {
                    return selected;
                }

                public void setSelected(String selected) {
                    this.selected = selected;
                }

                public String getCreated_at() {
                    return created_at;
                }

                public void setCreated_at(String created_at) {
                    this.created_at = created_at;
                }

                public String getUpdated_at() {
                    return updated_at;
                }

                public void setUpdated_at(String updated_at) {
                    this.updated_at = updated_at;
                }

                public VehiclemodelBean getVehiclemodel() {
                    return vehiclemodel;
                }

                public void setVehiclemodel(VehiclemodelBean vehiclemodel) {
                    this.vehiclemodel = vehiclemodel;
                }

                @Override
                public int describeContents() {
                    return 0;
                }

                @Override
                public void writeToParcel(Parcel dest, int flags) {
                    dest.writeInt(id);
                    dest.writeString(user_id);
                    dest.writeString(vehicle_id);
                    dest.writeString(year);
                    dest.writeString(type);
                    dest.writeString(color);
                    dest.writeString(transmission_type);
                    dest.writeString(selected);
                    dest.writeString(created_at);
                    dest.writeString(updated_at);
                    dest.writeParcelable(vehiclemodel, flags);
                }

                public static class VehiclemodelBean implements Parcelable{
                    /**
                     * id : 2
                     * vehicle_manufacturer_id : 1
                     * model : 427
                     * updated_at : 2018-04-11 01:39:54
                     * created_at : 2018-04-11 01:39:54
                     * vmake : {"id":1,"name":"ac","created_at":"2018-04-11 01:39:52","updated_at":"2018-04-11 01:39:52"}
                     */

                    private int id;
                    private String vehicle_manufacturer_id;
                    private String model;
                    private String updated_at;
                    private String created_at;
                    private VmakeBean vmake;

                    protected VehiclemodelBean(Parcel in) {
                        id = in.readInt();
                        vehicle_manufacturer_id = in.readString();
                        model = in.readString();
                        updated_at = in.readString();
                        created_at = in.readString();
                        vmake = in.readParcelable(VmakeBean.class.getClassLoader());
                    }

                    public static final Creator<VehiclemodelBean> CREATOR = new Creator<VehiclemodelBean>() {
                        @Override
                        public VehiclemodelBean createFromParcel(Parcel in) {
                            return new VehiclemodelBean(in);
                        }

                        @Override
                        public VehiclemodelBean[] newArray(int size) {
                            return new VehiclemodelBean[size];
                        }
                    };

                    public int getId() {
                        return id;
                    }

                    public void setId(int id) {
                        this.id = id;
                    }

                    public String getVehicle_manufacturer_id() {
                        return vehicle_manufacturer_id;
                    }

                    public void setVehicle_manufacturer_id(String vehicle_manufacturer_id) {
                        this.vehicle_manufacturer_id = vehicle_manufacturer_id;
                    }

                    public String getModel() {
                        return model;
                    }

                    public void setModel(String model) {
                        this.model = model;
                    }

                    public String getUpdated_at() {
                        return updated_at;
                    }

                    public void setUpdated_at(String updated_at) {
                        this.updated_at = updated_at;
                    }

                    public String getCreated_at() {
                        return created_at;
                    }

                    public void setCreated_at(String created_at) {
                        this.created_at = created_at;
                    }

                    public VmakeBean getVmake() {
                        return vmake;
                    }

                    public void setVmake(VmakeBean vmake) {
                        this.vmake = vmake;
                    }

                    @Override
                    public int describeContents() {
                        return 0;
                    }

                    @Override
                    public void writeToParcel(Parcel dest, int flags) {
                        dest.writeInt(id);
                        dest.writeString(vehicle_manufacturer_id);
                        dest.writeString(model);
                        dest.writeString(updated_at);
                        dest.writeString(created_at);
                        dest.writeParcelable(vmake, flags);
                    }

                    public static class VmakeBean implements Parcelable{
                        /**
                         * id : 1
                         * name : ac
                         * created_at : 2018-04-11 01:39:52
                         * updated_at : 2018-04-11 01:39:52
                         */

                        private int id;
                        private String name;
                        private String created_at;
                        private String updated_at;

                        protected VmakeBean(Parcel in) {
                            id = in.readInt();
                            name = in.readString();
                            created_at = in.readString();
                            updated_at = in.readString();
                        }

                        public static final Creator<VmakeBean> CREATOR = new Creator<VmakeBean>() {
                            @Override
                            public VmakeBean createFromParcel(Parcel in) {
                                return new VmakeBean(in);
                            }

                            @Override
                            public VmakeBean[] newArray(int size) {
                                return new VmakeBean[size];
                            }
                        };

                        public int getId() {
                            return id;
                        }

                        public void setId(int id) {
                            this.id = id;
                        }

                        public String getName() {
                            return name;
                        }

                        public void setName(String name) {
                            this.name = name;
                        }

                        public String getCreated_at() {
                            return created_at;
                        }

                        public void setCreated_at(String created_at) {
                            this.created_at = created_at;
                        }

                        public String getUpdated_at() {
                            return updated_at;
                        }

                        public void setUpdated_at(String updated_at) {
                            this.updated_at = updated_at;
                        }

                        @Override
                        public int describeContents() {
                            return 0;
                        }

                        @Override
                        public void writeToParcel(Parcel dest, int flags) {
                            dest.writeInt(id);
                            dest.writeString(name);
                            dest.writeString(created_at);
                            dest.writeString(updated_at);
                        }
                    }
                }
            }
        }
    }
}
