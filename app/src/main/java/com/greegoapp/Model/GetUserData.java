package com.greegoapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class GetUserData implements Parcelable{


    /**
     * data : {"id":2,"name":"vishal","email":"vishal@gmail.com","lastname":null,"contact_number":"+918460003300","city":null,"profile_pic":null,"promocode":null,"verified":"1","is_iphone":"0","is_agreed":"1","created_at":"2018-04-08 23:47:47","updated_at":"2018-04-11 04:39:32","cards":[{"id":1,"user_id":"2","card_number":"MTExMTIyMjIzMzMzNDQ0NA==","exp_month":"12","exp_year":"2017","cvv_number":"123","zipcode":"1234","created_at":"2018-04-10 07:06:09","updated_at":"2018-04-10 07:06:09"},{"id":2,"user_id":"2","card_number":"MTExMTIyMjIzMzMzNTU1NQ==","exp_month":"12","exp_year":"2017","cvv_number":"123","zipcode":"1234","created_at":"2018-04-10 07:16:51","updated_at":"2018-04-10 07:16:51"},{"id":3,"user_id":"2","card_number":"MTExMTIyMjIzMzMzNDQ0NA==","exp_month":"12","exp_year":"2017","cvv_number":"123","zipcode":"1234","created_at":"2018-04-10 09:41:52","updated_at":"2018-04-10 09:41:52"},{"id":4,"user_id":"2","card_number":"MTExMTIyMjIzMzMzNDQ0NA==","exp_month":"12","exp_year":"2017","cvv_number":"123","zipcode":"1234","created_at":"2018-04-10 09:45:06","updated_at":"2018-04-10 09:45:06"},{"id":5,"user_id":"2","card_number":"MTExMTIyMjIzMzMzNDQ0NA==","exp_month":"12","exp_year":"2017","cvv_number":"123","zipcode":"1234","created_at":"2018-04-11 04:39:51","updated_at":"2018-04-11 04:39:51"},{"id":6,"user_id":"2","card_number":"MTExMTIyMjIzMzMzNDQ0NA==","exp_month":"12","exp_year":"2017","cvv_number":"123","zipcode":"1234","created_at":"2018-04-11 04:46:59","updated_at":"2018-04-11 04:46:59"}],"vehicles":[]}
     * error_code : 0
     * message :
     */

    private DataBean data;
    private int error_code;
    private String message;

    public GetUserData(){}

    public GetUserData(Parcel in) {
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

    public static final Creator<GetUserData> CREATOR = new Creator<GetUserData>() {
        @Override
        public GetUserData createFromParcel(Parcel in) {
            return new GetUserData(in);
        }

        @Override
        public GetUserData[] newArray(int size) {
            return new GetUserData[size];
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
         * id : 2
         * name : vishal
         * email : vishal@gmail.com
         * lastname : null
         * contact_number : +918460003300
         * city : null
         * profile_pic : null
         * promocode : null
         * verified : 1
         * is_iphone : 0
         * is_agreed : 1
         * created_at : 2018-04-08 23:47:47
         * updated_at : 2018-04-11 04:39:32
         * cards : [{"id":1,"user_id":"2","card_number":"MTExMTIyMjIzMzMzNDQ0NA==","exp_month":"12","exp_year":"2017","cvv_number":"123","zipcode":"1234","created_at":"2018-04-10 07:06:09","updated_at":"2018-04-10 07:06:09"},{"id":2,"user_id":"2","card_number":"MTExMTIyMjIzMzMzNTU1NQ==","exp_month":"12","exp_year":"2017","cvv_number":"123","zipcode":"1234","created_at":"2018-04-10 07:16:51","updated_at":"2018-04-10 07:16:51"},{"id":3,"user_id":"2","card_number":"MTExMTIyMjIzMzMzNDQ0NA==","exp_month":"12","exp_year":"2017","cvv_number":"123","zipcode":"1234","created_at":"2018-04-10 09:41:52","updated_at":"2018-04-10 09:41:52"},{"id":4,"user_id":"2","card_number":"MTExMTIyMjIzMzMzNDQ0NA==","exp_month":"12","exp_year":"2017","cvv_number":"123","zipcode":"1234","created_at":"2018-04-10 09:45:06","updated_at":"2018-04-10 09:45:06"},{"id":5,"user_id":"2","card_number":"MTExMTIyMjIzMzMzNDQ0NA==","exp_month":"12","exp_year":"2017","cvv_number":"123","zipcode":"1234","created_at":"2018-04-11 04:39:51","updated_at":"2018-04-11 04:39:51"},{"id":6,"user_id":"2","card_number":"MTExMTIyMjIzMzMzNDQ0NA==","exp_month":"12","exp_year":"2017","cvv_number":"123","zipcode":"1234","created_at":"2018-04-11 04:46:59","updated_at":"2018-04-11 04:46:59"}]
         * vehicles : []
         */

        private int id;
        private String name;
        private String email;
        private String lastname;
        private String contact_number;
        private String city;
        private String profile_pic;
        private String promocode;
        private String verified;
        private String is_iphone;
        private String is_agreed;
        private String created_at;
        private String updated_at;
        private List<CardsBean> cards;
        private List<?> vehicles;

        protected DataBean(Parcel in) {
            id = in.readInt();
            name = in.readString();
            email = in.readString();
            contact_number = in.readString();
            verified = in.readString();
            is_iphone = in.readString();
            is_agreed = in.readString();
            created_at = in.readString();
            updated_at = in.readString();
            cards = in.createTypedArrayList(CardsBean.CREATOR);
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

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
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

        public List<CardsBean> getCards() {
            return cards;
        }

        public void setCards(List<CardsBean> cards) {
            this.cards = cards;
        }

        public List<?> getVehicles() {
            return vehicles;
        }

        public void setVehicles(List<?> vehicles) {
            this.vehicles = vehicles;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeInt(id);
            parcel.writeString(name);
            parcel.writeString(email);
            parcel.writeString(contact_number);
            parcel.writeString(verified);
            parcel.writeString(is_iphone);
            parcel.writeString(is_agreed);
            parcel.writeString(created_at);
            parcel.writeString(updated_at);
            parcel.writeTypedList(cards);
        }

        public static class CardsBean implements Parcelable{
            /**
             * id : 1
             * user_id : 2
             * card_number : MTExMTIyMjIzMzMzNDQ0NA==
             * exp_month : 12
             * exp_year : 2017
             * cvv_number : 123
             * zipcode : 1234
             * created_at : 2018-04-10 07:06:09
             * updated_at : 2018-04-10 07:06:09
             */

            private int id;
            private String user_id;
            private String card_number;
            private String exp_month_year;
//            private String exp_year;
            private String cvv_number;
            private String zipcode;
            private String created_at;
            private String updated_at;

            protected CardsBean(Parcel in) {
                id = in.readInt();
                user_id = in.readString();
                card_number = in.readString();
                exp_month_year = in.readString();
                cvv_number = in.readString();
                zipcode = in.readString();
                created_at = in.readString();
                updated_at = in.readString();
            }

            public static final Creator<CardsBean> CREATOR = new Creator<CardsBean>() {
                @Override
                public CardsBean createFromParcel(Parcel in) {
                    return new CardsBean(in);
                }

                @Override
                public CardsBean[] newArray(int size) {
                    return new CardsBean[size];
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

            public String getCard_number() {
                return card_number;
            }

            public void setCard_number(String card_number) {
                this.card_number = card_number;
            }

            public String getExp_month_year() {
                return exp_month_year;
            }

            public void setExp_month_year(String exp_month) {
                this.exp_month_year = exp_month;
            }


            public String getCvv_number() {
                return cvv_number;
            }

            public void setCvv_number(String cvv_number) {
                this.cvv_number = cvv_number;
            }

            public String getZipcode() {
                return zipcode;
            }

            public void setZipcode(String zipcode) {
                this.zipcode = zipcode;
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
            public void writeToParcel(Parcel parcel, int i) {
                parcel.writeInt(id);
                parcel.writeString(user_id);
                parcel.writeString(card_number);
                parcel.writeString(exp_month_year);
                parcel.writeString(cvv_number);
                parcel.writeString(zipcode);
                parcel.writeString(created_at);
                parcel.writeString(updated_at);
            }
        }
    }
}
