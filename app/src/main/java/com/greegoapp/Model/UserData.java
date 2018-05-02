package com.greegoapp.Model;

public class UserData {

    /**
     * data : {"id":14,"name":null,"email":null,"lastname":null,"contact_number":"+919998841576","city":null,"profile_pic":null,"promocode":null,"verified":"1","is_iphone":"0","is_agreed":null,"created_at":"2018-04-09 08:27:06","updated_at":"2018-04-09 12:46:46"}
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
         * id : 14
         * name : null
         * email : null
         * lastname : null
         * contact_number : +919998841576
         * city : null
         * profile_pic : null
         * promocode : null
         * verified : 1
         * is_iphone : 0
         * is_agreed : null
         * created_at : 2018-04-09 08:27:06
         * updated_at : 2018-04-09 12:46:46
         */

        private int id;
        private Object name;
        private Object email;
        private Object lastname;
        private String contact_number;
        private Object city;
        private String  profile_pic;
        private Object promocode;
        private String verified;
        private String is_iphone;
        private Object is_agreed;
        private String created_at;
        private String updated_at;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public Object getName() {
            return name;
        }

        public void setName(Object name) {
            this.name = name;
        }

        public Object getEmail() {
            return email;
        }

        public void setEmail(Object email) {
            this.email = email;
        }

        public Object getLastname() {
            return lastname;
        }

        public void setLastname(Object lastname) {
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

        public String getProfile_pic() {
            return profile_pic;
        }

        public void setProfile_pic(String  profile_pic) {
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

        public Object getIs_agreed() {
            return is_agreed;
        }

        public void setIs_agreed(Object is_agreed) {
            this.is_agreed = is_agreed;
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
    }
}
