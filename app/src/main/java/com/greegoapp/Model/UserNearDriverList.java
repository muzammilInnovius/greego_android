package com.greegoapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class UserNearDriverList implements Parcelable {

    /**
     * data : [{"id":"5","driver_id":"4","lat":"23.0497","lng":"72.5117","distance":"4.74628461961922"},{"id":"3","driver_id":"4","lat":"23.0304","lng":"72.5178","distance":"4.082848754183807"},{"id":"1","driver_id":"1","lat":"23.0134","lng":"72.5624","distance":"1.5294123390024332"}]
     * error_code : 0
     * message :
     */

    private int error_code;
    private String message;
    private List<DataBean> data;

    protected UserNearDriverList(Parcel in) {
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

    public static final Creator<UserNearDriverList> CREATOR = new Creator<UserNearDriverList>() {
        @Override
        public UserNearDriverList createFromParcel(Parcel in) {
            return new UserNearDriverList(in);
        }

        @Override
        public UserNearDriverList[] newArray(int size) {
            return new UserNearDriverList[size];
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
         * id : 5
         * driver_id : 4
         * lat : 23.0497
         * lng : 72.5117
         * distance : 4.74628461961922
         */

        private String id;
        private String driver_id;
        private String lat;
        private String lng;
        private String distance;

        protected DataBean(Parcel in) {
            id = in.readString();
            driver_id = in.readString();
            lat = in.readString();
            lng = in.readString();
            distance = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(id);
            dest.writeString(driver_id);
            dest.writeString(lat);
            dest.writeString(lng);
            dest.writeString(distance);
        }

        @Override
        public int describeContents() {
            return 0;
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

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getDriver_id() {
            return driver_id;
        }

        public void setDriver_id(String driver_id) {
            this.driver_id = driver_id;
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getLng() {
            return lng;
        }

        public void setLng(String lng) {
            this.lng = lng;
        }

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }
    }
}
