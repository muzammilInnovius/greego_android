package com.greegoapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class GetUserData implements Parcelable{


    /**
     * data : {"id":29,"name":"muzzamil123","email":"abc@gmail.com","lastname":"modan","contact_number":"+919998841576","city":null,"profile_pic":"http://innoviussoftware.com/greego/storage/app/","promocode":null,"verified":"0","is_iphone":"0","is_agreed":"1","device_id":null,"created_at":"2018-04-17 10:30:12","updated_at":"2018-04-18 05:39:03","cards":[{"id":7,"user_id":"29","card_number":"Z2dnaCB0eXVpIHl0cmU=","exp_month_year":"09/18","zipcode":"896600","created_at":"2018-04-17 12:00:54","updated_at":"2018-04-17 12:00:54"},{"id":8,"user_id":"29","card_number":"Z2dnaCB0eXVpIHl0cmU=","exp_month_year":"09/18","zipcode":"896600","created_at":"2018-04-17 12:16:40","updated_at":"2018-04-17 12:16:40"},{"id":12,"user_id":"29","card_number":"MTIzMTIzMTIzMTMxMTExMQ==","exp_month_year":"09/18","zipcode":"896600","created_at":"2018-04-17 13:34:58","updated_at":"2018-04-17 13:34:58"},{"id":13,"user_id":"29","card_number":"MTIzMTIzMTIzMTMxMTExMQ==","exp_month_year":"09/18","zipcode":"896600","created_at":"2018-04-17 13:46:05","updated_at":"2018-04-17 13:46:05"},{"id":14,"user_id":"29","card_number":"MTIzMTIzMTIzMTMxMTExMQ==","exp_month_year":"09/18","zipcode":"896600","created_at":"2018-04-17 13:47:02","updated_at":"2018-04-17 13:47:02"},{"id":15,"user_id":"29","card_number":"MTIzMTIzMTIzMTMxMTExMQ==","exp_month_year":"09/18","zipcode":"896600","created_at":"2018-04-17 13:55:38","updated_at":"2018-04-17 13:55:38"},{"id":16,"user_id":"29","card_number":"MTIzMTIzMTIzMTMxMTExMQ==","exp_month_year":"09/18","zipcode":"896600","created_at":"2018-04-17 14:23:22","updated_at":"2018-04-17 14:23:22"},{"id":20,"user_id":"29","card_number":"MjUzNjIzNTY5ODU2","exp_month_year":"guhh","zipcode":"896600","created_at":"2018-04-18 05:41:42","updated_at":"2018-04-18 05:41:42"},{"id":21,"user_id":"29","card_number":"MjUzNjIzNTY5ODU2","exp_month_year":"nsjsj","zipcode":"896600","created_at":"2018-04-18 05:53:03","updated_at":"2018-04-18 05:53:03"}],"vehicles":[{"id":21,"user_id":"29","vehicle_id":"160","year":"2017","type":"2","color":"white","transmission_type":"0","created_at":"2018-04-17 11:51:02","updated_at":"2018-04-17 11:51:02"},{"id":22,"user_id":"29","vehicle_id":"13","year":"2018","type":"1","color":"white","transmission_type":"0","created_at":"2018-04-17 12:15:57","updated_at":"2018-04-17 12:15:57"},{"id":23,"user_id":"29","vehicle_id":"13","year":"2588","type":"1","color":"red","transmission_type":"0","created_at":"2018-04-17 13:02:41","updated_at":"2018-04-17 13:02:41"},{"id":24,"user_id":"29","vehicle_id":"14","year":"2051","type":"2","color":"ref","transmission_type":"0","created_at":"2018-04-17 13:54:14","updated_at":"2018-04-17 13:54:14"},{"id":25,"user_id":"29","vehicle_id":"13","year":"2580","type":"1","color":"red","transmission_type":"0","created_at":"2018-04-17 14:23:09","updated_at":"2018-04-17 14:23:09"},{"id":28,"user_id":"29","vehicle_id":"30","year":"2017","type":"0","color":"white","transmission_type":"0","created_at":"2018-04-18 05:33:09","updated_at":"2018-04-18 05:33:09"}]}
     * error_code : 0
     * message :
     */

    private DataBean data;
    private int error_code;
    private String message;

    protected GetUserData(Parcel in) {
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
         * id : 29
         * name : muzzamil123
         * email : abc@gmail.com
         * lastname : modan
         * contact_number : +919998841576
         * city : null
         * profile_pic : http://innoviussoftware.com/greego/storage/app/
         * promocode : null
         * verified : 0
         * is_iphone : 0
         * is_agreed : 1
         * device_id : null
         * created_at : 2018-04-17 10:30:12
         * updated_at : 2018-04-18 05:39:03
         * cards : [{"id":7,"user_id":"29","card_number":"Z2dnaCB0eXVpIHl0cmU=","exp_month_year":"09/18","zipcode":"896600","created_at":"2018-04-17 12:00:54","updated_at":"2018-04-17 12:00:54"},{"id":8,"user_id":"29","card_number":"Z2dnaCB0eXVpIHl0cmU=","exp_month_year":"09/18","zipcode":"896600","created_at":"2018-04-17 12:16:40","updated_at":"2018-04-17 12:16:40"},{"id":12,"user_id":"29","card_number":"MTIzMTIzMTIzMTMxMTExMQ==","exp_month_year":"09/18","zipcode":"896600","created_at":"2018-04-17 13:34:58","updated_at":"2018-04-17 13:34:58"},{"id":13,"user_id":"29","card_number":"MTIzMTIzMTIzMTMxMTExMQ==","exp_month_year":"09/18","zipcode":"896600","created_at":"2018-04-17 13:46:05","updated_at":"2018-04-17 13:46:05"},{"id":14,"user_id":"29","card_number":"MTIzMTIzMTIzMTMxMTExMQ==","exp_month_year":"09/18","zipcode":"896600","created_at":"2018-04-17 13:47:02","updated_at":"2018-04-17 13:47:02"},{"id":15,"user_id":"29","card_number":"MTIzMTIzMTIzMTMxMTExMQ==","exp_month_year":"09/18","zipcode":"896600","created_at":"2018-04-17 13:55:38","updated_at":"2018-04-17 13:55:38"},{"id":16,"user_id":"29","card_number":"MTIzMTIzMTIzMTMxMTExMQ==","exp_month_year":"09/18","zipcode":"896600","created_at":"2018-04-17 14:23:22","updated_at":"2018-04-17 14:23:22"},{"id":20,"user_id":"29","card_number":"MjUzNjIzNTY5ODU2","exp_month_year":"guhh","zipcode":"896600","created_at":"2018-04-18 05:41:42","updated_at":"2018-04-18 05:41:42"},{"id":21,"user_id":"29","card_number":"MjUzNjIzNTY5ODU2","exp_month_year":"nsjsj","zipcode":"896600","created_at":"2018-04-18 05:53:03","updated_at":"2018-04-18 05:53:03"}]
         * vehicles : [{"id":21,"user_id":"29","vehicle_id":"160","year":"2017","type":"2","color":"white","transmission_type":"0","created_at":"2018-04-17 11:51:02","updated_at":"2018-04-17 11:51:02"},{"id":22,"user_id":"29","vehicle_id":"13","year":"2018","type":"1","color":"white","transmission_type":"0","created_at":"2018-04-17 12:15:57","updated_at":"2018-04-17 12:15:57"},{"id":23,"user_id":"29","vehicle_id":"13","year":"2588","type":"1","color":"red","transmission_type":"0","created_at":"2018-04-17 13:02:41","updated_at":"2018-04-17 13:02:41"},{"id":24,"user_id":"29","vehicle_id":"14","year":"2051","type":"2","color":"ref","transmission_type":"0","created_at":"2018-04-17 13:54:14","updated_at":"2018-04-17 13:54:14"},{"id":25,"user_id":"29","vehicle_id":"13","year":"2580","type":"1","color":"red","transmission_type":"0","created_at":"2018-04-17 14:23:09","updated_at":"2018-04-17 14:23:09"},{"id":28,"user_id":"29","vehicle_id":"30","year":"2017","type":"0","color":"white","transmission_type":"0","created_at":"2018-04-18 05:33:09","updated_at":"2018-04-18 05:33:09"}]
         */

        private int id;
        private String name;
        private String email;
        private String lastname;
        private String contact_number;
        private Object city;
        private String profile_pic;
        private Object promocode;
        private String verified;
        private String is_iphone;
        private String is_agreed;
        private Object device_id;
        private String created_at;
        private String updated_at;
        private List<CardsBean> cards;
        private List<VehiclesBean> vehicles;

        protected DataBean(Parcel in) {
            id = in.readInt();
            name = in.readString();
            email = in.readString();
            lastname = in.readString();
            contact_number = in.readString();
            profile_pic = in.readString();
            verified = in.readString();
            is_iphone = in.readString();
            is_agreed = in.readString();
            created_at = in.readString();
            updated_at = in.readString();
            cards = in.createTypedArrayList(CardsBean.CREATOR);
            vehicles = in.createTypedArrayList(VehiclesBean.CREATOR);
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

        public Object getDevice_id() {
            return device_id;
        }

        public void setDevice_id(Object device_id) {
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

        public List<CardsBean> getCards() {
            return cards;
        }

        public void setCards(List<CardsBean> cards) {
            this.cards = cards;
        }

        public List<VehiclesBean> getVehicles() {
            return vehicles;
        }

        public void setVehicles(List<VehiclesBean> vehicles) {
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
            parcel.writeString(lastname);
            parcel.writeString(contact_number);
            parcel.writeString(profile_pic);
            parcel.writeString(verified);
            parcel.writeString(is_iphone);
            parcel.writeString(is_agreed);
            parcel.writeString(created_at);
            parcel.writeString(updated_at);
            parcel.writeTypedList(cards);
            parcel.writeTypedList(vehicles);
        }

        public static class CardsBean implements Parcelable{
            /**
             * id : 7
             * user_id : 29
             * card_number : Z2dnaCB0eXVpIHl0cmU=
             * exp_month_year : 09/18
             * zipcode : 896600
             * created_at : 2018-04-17 12:00:54
             * updated_at : 2018-04-17 12:00:54
             */

            private int id;
            private String user_id;
            private String card_number;
            private String exp_month_year;
            private String zipcode;
            private String created_at;
            private String updated_at;

            protected CardsBean(Parcel in) {
                id = in.readInt();
                user_id = in.readString();
                card_number = in.readString();
                exp_month_year = in.readString();
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

            public void setExp_month_year(String exp_month_year) {
                this.exp_month_year = exp_month_year;
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
                parcel.writeString(zipcode);
                parcel.writeString(created_at);
                parcel.writeString(updated_at);
            }
        }

        public static class VehiclesBean implements Parcelable{
            /**
             * id : 21
             * user_id : 29
             * vehicle_id : 160
             * year : 2017
             * type : 2
             * color : white
             * transmission_type : 0
             * created_at : 2018-04-17 11:51:02
             * updated_at : 2018-04-17 11:51:02
             */

            private int id;
            private String user_id;
            private String vehicle_id;
            private String year;

            public String getSelected() {
                return selected;
            }

            public void setSelected(String selected) {
                this.selected = selected;
            }

            private String type;
            private String color;
            private String transmission_type;
            private String selected;
            private String created_at;
            private String updated_at;

            protected VehiclesBean(Parcel in) {
                id = in.readInt();
                user_id = in.readString();
                vehicle_id = in.readString();
                year = in.readString();
                type = in.readString();
                color = in.readString();
                selected = in.readString();
                transmission_type = in.readString();
                created_at = in.readString();
                updated_at = in.readString();
            }

            public static final Creator<VehiclesBean> CREATOR = new Creator<VehiclesBean>() {
                @Override
                public VehiclesBean createFromParcel(Parcel in) {
                    return new VehiclesBean(in);
                }

                @Override
                public VehiclesBean[] newArray(int size) {
                    return new VehiclesBean[size];
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
                parcel.writeString(vehicle_id);
                parcel.writeString(year);
                parcel.writeString(type);
                parcel.writeString(color);
                parcel.writeString(transmission_type);
                parcel.writeString(selected);
                parcel.writeString(created_at);
                parcel.writeString(updated_at);
            }
        }
    }
}
