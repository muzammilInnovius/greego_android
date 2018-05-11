package com.greegoapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class TripHistoryModel implements Parcelable {


    /**
     * data : [{"request_id":186,"user_id":244,"driver_id":227,"status":5,"from_address":"paldi","from_lat":23.0101,"from_lng":72.5626,"to_address":"Naroda","to_lat":23.0035,"to_lng":72.5616,"total_estimated_travel_time":10,"total_estimated_trip_cost":100,"created_at":"2018-04-27 12:51:10","updated_at":"2018-04-27 12:52:36","name":"vishal","lastname":"gosia","profile_pic":"profile_pic/8PO4ua9qvRpVfqmNmnvjudkiaJv59dF5Rozp21YD.png","contact_number":"+918460003300","email":"v@gmail.com","promocode":null}]
     * error_code : 0
     * message : The trip details
     */

    private int error_code;
    private String message;
    private List<DataBean> data;

    protected TripHistoryModel(Parcel in) {
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

    public static final Creator<TripHistoryModel> CREATOR = new Creator<TripHistoryModel>() {
        @Override
        public TripHistoryModel createFromParcel(Parcel in) {
            return new TripHistoryModel(in);
        }

        @Override
        public TripHistoryModel[] newArray(int size) {
            return new TripHistoryModel[size];
        }
    };

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Parcelable{
        /**
         * request_id : 186
         * user_id : 244
         * driver_id : 227
         * status : 5
         * from_address : paldi
         * from_lat : 23.0101
         * from_lng : 72.5626
         * to_address : Naroda
         * to_lat : 23.0035
         * to_lng : 72.5616
         * total_estimated_travel_time : 10
         * total_estimated_trip_cost : 100
         * created_at : 2018-04-27 12:51:10
         * updated_at : 2018-04-27 12:52:36
         * name : vishal
         * lastname : gosia
         * profile_pic : profile_pic/8PO4ua9qvRpVfqmNmnvjudkiaJv59dF5Rozp21YD.png
         * contact_number : +918460003300
         * email : v@gmail.com
         * promocode : null
         */

        private int request_id;
        private int user_id;
        private int driver_id;
        private int status;
        private String from_address;
        private double from_lat;
        private double from_lng;
        private String to_address;
        private double to_lat;
        private double to_lng;
        private int total_estimated_travel_time;
        private float total_estimated_trip_cost;
        private String created_at;
        private String updated_at;
        private String name;
        private String lastname;
        private String profile_pic;
        private String contact_number;
        private String email;
        private String promocode;

        protected DataBean(Parcel in) {
            request_id = in.readInt();
            user_id = in.readInt();
            driver_id = in.readInt();
            status = in.readInt();
            from_address = in.readString();
            from_lat = in.readDouble();
            from_lng = in.readDouble();
            to_address = in.readString();
            to_lat = in.readDouble();
            to_lng = in.readDouble();
            total_estimated_travel_time = in.readInt();
            total_estimated_trip_cost = in.readFloat();
            created_at = in.readString();
            updated_at = in.readString();
            name = in.readString();
            lastname = in.readString();
            profile_pic = in.readString();
            contact_number = in.readString();
            email = in.readString();
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

        public int getRequest_id() {
            return request_id;
        }

        public void setRequest_id(int request_id) {
            this.request_id = request_id;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public int getDriver_id() {
            return driver_id;
        }

        public void setDriver_id(int driver_id) {
            this.driver_id = driver_id;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getFrom_address() {
            return from_address;
        }

        public void setFrom_address(String from_address) {
            this.from_address = from_address;
        }

        public double getFrom_lat() {
            return from_lat;
        }

        public void setFrom_lat(double from_lat) {
            this.from_lat = from_lat;
        }

        public double getFrom_lng() {
            return from_lng;
        }

        public void setFrom_lng(double from_lng) {
            this.from_lng = from_lng;
        }

        public String getTo_address() {
            return to_address;
        }

        public void setTo_address(String to_address) {
            this.to_address = to_address;
        }

        public double getTo_lat() {
            return to_lat;
        }

        public void setTo_lat(double to_lat) {
            this.to_lat = to_lat;
        }

        public double getTo_lng() {
            return to_lng;
        }

        public void setTo_lng(double to_lng) {
            this.to_lng = to_lng;
        }

        public int getTotal_estimated_travel_time() {
            return total_estimated_travel_time;
        }

        public void setTotal_estimated_travel_time(int total_estimated_travel_time) {
            this.total_estimated_travel_time = total_estimated_travel_time;
        }

        public float getTotal_estimated_trip_cost() {
            return total_estimated_trip_cost;
        }

        public void setTotal_estimated_trip_cost(float total_estimated_trip_cost) {
            this.total_estimated_trip_cost = total_estimated_trip_cost;
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

        public String getProfile_pic() {
            return profile_pic;
        }

        public void setProfile_pic(String profile_pic) {
            this.profile_pic = profile_pic;
        }

        public String getContact_number() {
            return contact_number;
        }

        public void setContact_number(String contact_number) {
            this.contact_number = contact_number;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPromocode() {
            return promocode;
        }

        public void setPromocode(String promocode) {
            this.promocode = promocode;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(request_id);
            dest.writeInt(user_id);
            dest.writeInt(driver_id);
            dest.writeInt(status);
            dest.writeString(from_address);
            dest.writeDouble(from_lat);
            dest.writeDouble(from_lng);
            dest.writeString(to_address);
            dest.writeDouble(to_lat);
            dest.writeDouble(to_lng);
            dest.writeInt(total_estimated_travel_time);
            dest.writeFloat(total_estimated_trip_cost);
            dest.writeString(created_at);
            dest.writeString(updated_at);
            dest.writeString(name);
            dest.writeString(lastname);
            dest.writeString(profile_pic);
            dest.writeString(contact_number);
            dest.writeString(email);
        }
    }
}
