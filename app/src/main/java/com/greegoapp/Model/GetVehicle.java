package com.greegoapp.Model;

import java.util.List;

public class GetVehicle {

    /**
     * data : [{"id":1,"model":"2-Litre"},{"id":2,"model":"427"},{"id":3,"model":"428"},{"id":4,"model":"Ace"},{"id":5,"model":"Aceca"},{"id":6,"model":"Aceca-Bristol"},{"id":7,"model":"Cobra"},{"id":8,"model":"Greyhound"}]
     * error_code : 0
     * message :
     */

    private int error_code;
    private String message;
    private List<DataBean> data;

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

    public static class DataBean {
        /**
         * id : 1
         * model : 2-Litre
         */

        private int id;
        private String model;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getModel() {
            return model;
        }

        public void setModel(String model) {
            this.model = model;
        }

        public String toString() {
            return model;
        }
    }
}
