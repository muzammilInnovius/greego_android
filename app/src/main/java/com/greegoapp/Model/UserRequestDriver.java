package com.greegoapp.Model;

public class UserRequestDriver {


    /**
     * data : {"user_id":140,"user_vehicle_id":"1","from_address":"test","from_lat":"23.0134","from_lng":"23.0134","to_address":"to test","to_lat":"23.033863","to_lng":"72.492711","total_estimated_travel_time":"12","total_estimated_trip_cost":100,"request_status":0,"updated_at":"2018-04-21 12:56:24","created_at":"2018-04-21 12:56:24","id":4}
     * error_code : 0
     * message : Request Sent
     */

    private String data;
    private int error_code;
    private String message;

    public String getData() {
        return data;
    }

    public void setData(String data) {
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
         * user_id : 140
         * user_vehicle_id : 1
         * from_address : test
         * from_lat : 23.0134
         * from_lng : 23.0134
         * to_address : to test
         * to_lat : 23.033863
         * to_lng : 72.492711
         * total_estimated_travel_time : 12
         * total_estimated_trip_cost : 100
         * request_status : 0
         * updated_at : 2018-04-21 12:56:24
         * created_at : 2018-04-21 12:56:24
         * id : 4
         */

        private int user_id;
        private String user_vehicle_id;
        private String from_address;
        private String from_lat;
        private String from_lng;
        private String to_address;
        private String to_lat;
        private String to_lng;
        private String total_estimated_travel_time;
        private int total_estimated_trip_cost;
        private int request_status;
        private String updated_at;
        private String created_at;
        private int id;

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
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

        public int getTotal_estimated_trip_cost() {
            return total_estimated_trip_cost;
        }

        public void setTotal_estimated_trip_cost(int total_estimated_trip_cost) {
            this.total_estimated_trip_cost = total_estimated_trip_cost;
        }

        public int getRequest_status() {
            return request_status;
        }

        public void setRequest_status(int request_status) {
            this.request_status = request_status;
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
