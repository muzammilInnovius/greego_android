package com.greegoapp.Model;

public class CardData {

    /**
     * data : {"user_id":2,"card_number":"1111222233334444","exp_month_year":"12/2022","zipcode":"1234","updated_at":"2018-04-11 12:28:17","created_at":"2018-04-11 12:28:17","id":10}
     * error_code : 0
     * message : User Card Saved
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
         * user_id : 2
         * card_number : 1111222233334444
         * exp_month_year : 12/2022
         * zipcode : 1234
         * updated_at : 2018-04-11 12:28:17
         * created_at : 2018-04-11 12:28:17
         * id : 10
         */

        private int user_id;
        private String card_number;
        private String exp_month_year;
        private String zipcode;
        private String updated_at;
        private String created_at;
        private int id;

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public String getCard_number() {
            return card_number;
        }

        public void setCard_number(String card_number) {
            this.card_number = card_number;
        }

        public String getExp_month_year() {
            return exp_month_year;
        }

        public void setExp_month_year(String exp_month_year) {
            this.exp_month_year = exp_month_year;
        }

        public String getZipcode() {
            return zipcode;
        }

        public void setZipcode(String zipcode) {
            this.zipcode = zipcode;
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
