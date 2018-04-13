package com.greegoapp.Model;

import com.google.gson.annotations.SerializedName;

public class VerifyOtp {


    /**
     * data : {"new":1,"id":14,"contact_number":"+919998841576","token":"eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6ImU1MGIyZTg0ZWJhMjNhMWQzZjIyOWY5ZGRhZGFlN2ZiYzE2YWY5ODg3MjkwYjY4YWExM2JjODcyMDUzOGFmZTNjZjI5ZjU4OTA3YjdiZjI0In0.eyJhdWQiOiIxIiwianRpIjoiZTUwYjJlODRlYmEyM2ExZDNmMjI5ZjlkZGFkYWU3ZmJjMTZhZjk4ODcyOTBiNjhhYTEzYmM4NzIwNTM4YWZlM2NmMjlmNTg5MDdiN2JmMjQiLCJpYXQiOjE1MjMyNzEyMTAsIm5iZiI6MTUyMzI3MTIxMCwiZXhwIjoxNTU0ODA3MjEwLCJzdWIiOiIxNCIsInNjb3BlcyI6W119.iC7mJfyYae9WJOWWVfoJfgyR2-ylnhASExnujfsgrO_mFS6ZGrZBuavAlGmqmKAtsJv28Zak91frpkbkkCf6GDgRiyIGu3b_evo7sKpevIoh3bGKSQq7GbYKz_146SZWUfysyz6w6zmGyjjbYPYrQqz-f4aC4f7zNhdVOs_Y4ohOZ-kWVzpzKe5JdflEUZwHUYBpKwW2EfNBJkJ48ayCZjtjU6hoCSLHTqEeBBmqxiIJgATYiu5ZN61-YfS2tpV3hEnEGuVPs4cvDP8kLlgzliYylop4ndR2JjK448gpdHUtfG7MQlKm3-1HepwtOZurAvOuh_O2uzTv65CgkzwXH_QCIbmuGOoSig_TCric_DlJSvXphH3wzYa1TRGvXLhs2RkiK8Gafj0opIrMJpjNb_-RZ4BZ3fctlTyDeVAYo3FxjVFVi8nWRpK4jPEgMzLYjRHVpM8olmp1q_EPqgYtw4UVwcv3jXSEClc99r97n_ETVwSCRFJPwmOiZx_TU2-AB3PhcGz631jvllsfAMyuewbX9-FZ2XbT40toSRw-wzRO_-ORxgrZaW7ekGAj6HEDqN3UAXn7xw9TO1vsmQoVVKziUhsXCeRTHQrK6-rRGvgFW55kumyf8gqNJ6WxCD3lfhQvcZl58y_kldBtQKBEia9O8QYwS_7_QB05VgBlQuo"}
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
         * new : 1
         * id : 14
         * contact_number : +919998841576
         * token : eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6ImU1MGIyZTg0ZWJhMjNhMWQzZjIyOWY5ZGRhZGFlN2ZiYzE2YWY5ODg3MjkwYjY4YWExM2JjODcyMDUzOGFmZTNjZjI5ZjU4OTA3YjdiZjI0In0.eyJhdWQiOiIxIiwianRpIjoiZTUwYjJlODRlYmEyM2ExZDNmMjI5ZjlkZGFkYWU3ZmJjMTZhZjk4ODcyOTBiNjhhYTEzYmM4NzIwNTM4YWZlM2NmMjlmNTg5MDdiN2JmMjQiLCJpYXQiOjE1MjMyNzEyMTAsIm5iZiI6MTUyMzI3MTIxMCwiZXhwIjoxNTU0ODA3MjEwLCJzdWIiOiIxNCIsInNjb3BlcyI6W119.iC7mJfyYae9WJOWWVfoJfgyR2-ylnhASExnujfsgrO_mFS6ZGrZBuavAlGmqmKAtsJv28Zak91frpkbkkCf6GDgRiyIGu3b_evo7sKpevIoh3bGKSQq7GbYKz_146SZWUfysyz6w6zmGyjjbYPYrQqz-f4aC4f7zNhdVOs_Y4ohOZ-kWVzpzKe5JdflEUZwHUYBpKwW2EfNBJkJ48ayCZjtjU6hoCSLHTqEeBBmqxiIJgATYiu5ZN61-YfS2tpV3hEnEGuVPs4cvDP8kLlgzliYylop4ndR2JjK448gpdHUtfG7MQlKm3-1HepwtOZurAvOuh_O2uzTv65CgkzwXH_QCIbmuGOoSig_TCric_DlJSvXphH3wzYa1TRGvXLhs2RkiK8Gafj0opIrMJpjNb_-RZ4BZ3fctlTyDeVAYo3FxjVFVi8nWRpK4jPEgMzLYjRHVpM8olmp1q_EPqgYtw4UVwcv3jXSEClc99r97n_ETVwSCRFJPwmOiZx_TU2-AB3PhcGz631jvllsfAMyuewbX9-FZ2XbT40toSRw-wzRO_-ORxgrZaW7ekGAj6HEDqN3UAXn7xw9TO1vsmQoVVKziUhsXCeRTHQrK6-rRGvgFW55kumyf8gqNJ6WxCD3lfhQvcZl58y_kldBtQKBEia9O8QYwS_7_QB05VgBlQuo
         */

        @SerializedName("new")
        private int newX;
        private int id;
        private String contact_number;
        private String token;

        public int getNewX() {
            return newX;
        }

        public void setNewX(int newX) {
            this.newX = newX;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getContact_number() {
            return contact_number;
        }

        public void setContact_number(String contact_number) {
            this.contact_number = contact_number;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
}
