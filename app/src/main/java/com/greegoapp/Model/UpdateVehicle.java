package com.greegoapp.Model;

public class UpdateVehicle {

    /**
     * data : {"user_id":11,"vehicle_id":"2","type":"0","year":"2017","color":"red","transmission_type":"1","updated_at":"2018-04-16 12:45:38","created_at":"2018-04-16 12:45:38","id":8}
     * error_code : 0
     * message :
     */

    private DataBean data;
    private int error_code;
    private String message;

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

    public static class DataBean {
        /**
         * user_id : 11
         * vehicle_id : 2
         * type : 0
         * year : 2017
         * color : red
         * transmission_type : 1
         * updated_at : 2018-04-16 12:45:38
         * created_at : 2018-04-16 12:45:38
         * id : 8
         */

        private int user_id;
        private String vehicle_id;
        private String type;
        private String year;
        private String color;
        private String transmission_type;
        private String updated_at;
        private String created_at;
        private int id;

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public String getVehicle_id() {
            return vehicle_id;
        }

        public void setVehicle_id(String vehicle_id) {
            this.vehicle_id = vehicle_id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
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

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}
