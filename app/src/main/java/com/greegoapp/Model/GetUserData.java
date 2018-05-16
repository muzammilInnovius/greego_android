package com.greegoapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class GetUserData implements Parcelable{


    /**
     * data : {"id":244,"name":"Priyanka","email":"priyanka.innovius@gmail.com","lastname":"patel","contact_number":"+917874412012","city":null,"state":null,"profile_pic":"http://kroslinkstech.in/greego/storage/app/profile_pic/9Ad7LDsTSFdWUno5kQ6h3ZQHrzohKJ9zpSpODQpW.png","promocode":null,"invitecode":"JDGgXQ","verified":0,"is_iphone":0,"is_agreed":1,"is_deactivated":0,"created_at":"2018-04-27 10:59:24","updated_at":"2018-05-03 06:24:19","cards":[{"id":64,"user_id":244,"card_number":"MTIzNDEyMzQxMjM0MTIzNA==","card_token":"1234","exp_month_year":"12/12","zipcode":12121,"selected":0,"created_at":"2018-05-03 06:40:05","updated_at":"2018-05-03 06:40:05"},{"id":65,"user_id":244,"card_number":"NDMyMTQzMjE0MzIxNDMyMQ==","card_token":"1234","exp_month_year":"02/2","zipcode":12345,"selected":0,"created_at":"2018-05-03 06:40:39","updated_at":"2018-05-03 06:40:39"}],"vehicles":[{"id":220,"user_id":244,"vehicle_id":1,"year":2010,"type":0,"color":"red","transmission_type":"0","selected":1,"created_at":"2018-05-02 12:38:16","updated_at":"2018-05-02 12:42:15","vehicle_name":"ac","vehicle_model":"2-Litre","vehiclemodel":{"id":1,"vehicle_manufacturer_id":1,"model":"2-Litre","updated_at":"2018-04-11 07:09:54","created_at":"2018-04-11 07:09:54","vmake":{"id":1,"name":"ac","created_at":"2018-04-11 07:09:52","updated_at":"2018-04-11 07:09:52"}}},{"id":221,"user_id":244,"vehicle_id":147,"year":2002,"type":0,"color":"red","transmission_type":"0","selected":0,"created_at":"2018-05-02 12:41:36","updated_at":"2018-05-02 12:42:15","vehicle_name":"berkeley","vehicle_model":"Twosome","vehiclemodel":{"id":147,"vehicle_manufacturer_id":13,"model":"Twosome","updated_at":"2018-04-11 07:10:05","created_at":"2018-04-11 07:10:05","vmake":{"id":13,"name":"berkeley","created_at":"2018-04-11 07:10:05","updated_at":"2018-04-11 07:10:05"}}}]}
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

    public static class DataBean {
        /**
         * id : 244
         * name : Priyanka
         * email : priyanka.innovius@gmail.com
         * lastname : patel
         * contact_number : +917874412012
         * city : null
         * state : null
         * profile_pic : http://kroslinkstech.in/greego/storage/app/profile_pic/9Ad7LDsTSFdWUno5kQ6h3ZQHrzohKJ9zpSpODQpW.png
         * promocode : null
         * invitecode : JDGgXQ
         * verified : 0
         * is_iphone : 0
         * is_agreed : 1
         * is_deactivated : 0
         * created_at : 2018-04-27 10:59:24
         * updated_at : 2018-05-03 06:24:19
         * cards : [{"id":64,"user_id":244,"card_number":"MTIzNDEyMzQxMjM0MTIzNA==","card_token":"1234","exp_month_year":"12/12","zipcode":12121,"selected":0,"created_at":"2018-05-03 06:40:05","updated_at":"2018-05-03 06:40:05"},{"id":65,"user_id":244,"card_number":"NDMyMTQzMjE0MzIxNDMyMQ==","card_token":"1234","exp_month_year":"02/2","zipcode":12345,"selected":0,"created_at":"2018-05-03 06:40:39","updated_at":"2018-05-03 06:40:39"}]
         * vehicles : [{"id":220,"user_id":244,"vehicle_id":1,"year":2010,"type":0,"color":"red","transmission_type":"0","selected":1,"created_at":"2018-05-02 12:38:16","updated_at":"2018-05-02 12:42:15","vehicle_name":"ac","vehicle_model":"2-Litre","vehiclemodel":{"id":1,"vehicle_manufacturer_id":1,"model":"2-Litre","updated_at":"2018-04-11 07:09:54","created_at":"2018-04-11 07:09:54","vmake":{"id":1,"name":"ac","created_at":"2018-04-11 07:09:52","updated_at":"2018-04-11 07:09:52"}}},{"id":221,"user_id":244,"vehicle_id":147,"year":2002,"type":0,"color":"red","transmission_type":"0","selected":0,"created_at":"2018-05-02 12:41:36","updated_at":"2018-05-02 12:42:15","vehicle_name":"berkeley","vehicle_model":"Twosome","vehiclemodel":{"id":147,"vehicle_manufacturer_id":13,"model":"Twosome","updated_at":"2018-04-11 07:10:05","created_at":"2018-04-11 07:10:05","vmake":{"id":13,"name":"berkeley","created_at":"2018-04-11 07:10:05","updated_at":"2018-04-11 07:10:05"}}}]
         */

        private int id;
        private String name;
        private String email;
        private String lastname;
        private String contact_number;
        private Object city;
        private Object state;
        private String profile_pic;
        private Object promocode;
        private String invitecode;
        private int verified;
        private int is_iphone;
        private int is_agreed;
        private int is_deactivated;
        private String created_at;
        private String updated_at;
        private int email_verified;
        private List<CardsBean> cards;
        private List<VehiclesBean> vehicles;

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

        public int getEmail_verified() {
            return email_verified;
        }

        public void setEmail_verified(int email_verified) {
            this.email_verified = email_verified;
        }

        public Object getCity() {
            return city;
        }

        public void setCity(Object city) {
            this.city = city;
        }

        public Object getState() {
            return state;
        }

        public void setState(Object state) {
            this.state = state;
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

        public String getInvitecode() {
            return invitecode;
        }

        public void setInvitecode(String invitecode) {
            this.invitecode = invitecode;
        }

        public int getVerified() {
            return verified;
        }

        public void setVerified(int verified) {
            this.verified = verified;
        }

        public int getIs_iphone() {
            return is_iphone;
        }

        public void setIs_iphone(int is_iphone) {
            this.is_iphone = is_iphone;
        }

        public int getIs_agreed() {
            return is_agreed;
        }

        public void setIs_agreed(int is_agreed) {
            this.is_agreed = is_agreed;
        }

        public int getIs_deactivated() {
            return is_deactivated;
        }

        public void setIs_deactivated(int is_deactivated) {
            this.is_deactivated = is_deactivated;
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

        public static class CardsBean {
            /**
             * id : 64
             * user_id : 244
             * card_number : MTIzNDEyMzQxMjM0MTIzNA==
             * card_token : 1234
             * exp_month_year : 12/12
             * zipcode : 12121
             * selected : 0
             * created_at : 2018-05-03 06:40:05
             * updated_at : 2018-05-03 06:40:05
             */

            private int id;
            private int user_id;
            private String card_number;
            private String card_token;
            private String exp_month_year;
            private int zipcode;
            private int selected;
            private String created_at;
            private String updated_at;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

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

            public String getCard_token() {
                return card_token;
            }

            public void setCard_token(String card_token) {
                this.card_token = card_token;
            }

            public String getExp_month_year() {
                return exp_month_year;
            }

            public void setExp_month_year(String exp_month_year) {
                this.exp_month_year = exp_month_year;
            }

            public int getZipcode() {
                return zipcode;
            }

            public void setZipcode(int zipcode) {
                this.zipcode = zipcode;
            }

            public int getSelected() {
                return selected;
            }

            public void setSelected(int selected) {
                this.selected = selected;
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

        public static class VehiclesBean {
            /**
             * id : 220
             * user_id : 244
             * vehicle_id : 1
             * year : 2010
             * type : 0
             * color : red
             * transmission_type : 0
             * selected : 1
             * created_at : 2018-05-02 12:38:16
             * updated_at : 2018-05-02 12:42:15
             * vehicle_name : ac
             * vehicle_model : 2-Litre
             * vehiclemodel : {"id":1,"vehicle_manufacturer_id":1,"model":"2-Litre","updated_at":"2018-04-11 07:09:54","created_at":"2018-04-11 07:09:54","vmake":{"id":1,"name":"ac","created_at":"2018-04-11 07:09:52","updated_at":"2018-04-11 07:09:52"}}
             */

            private int id;
            private int user_id;
            private int vehicle_id;
            private int year;
            private int type;
            private String color;
            private String transmission_type;
            private int selected;
            private String created_at;
            private String updated_at;
            private String vehicle_name;
            private String vehicle_model;
            private VehiclemodelBean vehiclemodel;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getUser_id() {
                return user_id;
            }

            public void setUser_id(int user_id) {
                this.user_id = user_id;
            }

            public int getVehicle_id() {
                return vehicle_id;
            }

            public void setVehicle_id(int vehicle_id) {
                this.vehicle_id = vehicle_id;
            }

            public int getYear() {
                return year;
            }

            public void setYear(int year) {
                this.year = year;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
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

            public int getSelected() {
                return selected;
            }

            public void setSelected(int selected) {
                this.selected = selected;
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

            public String getVehicle_name() {
                return vehicle_name;
            }

            public void setVehicle_name(String vehicle_name) {
                this.vehicle_name = vehicle_name;
            }

            public String getVehicle_model() {
                return vehicle_model;
            }

            public void setVehicle_model(String vehicle_model) {
                this.vehicle_model = vehicle_model;
            }

            public VehiclemodelBean getVehiclemodel() {
                return vehiclemodel;
            }

            public void setVehiclemodel(VehiclemodelBean vehiclemodel) {
                this.vehiclemodel = vehiclemodel;
            }

            public static class VehiclemodelBean {
                /**
                 * id : 1
                 * vehicle_manufacturer_id : 1
                 * model : 2-Litre
                 * updated_at : 2018-04-11 07:09:54
                 * created_at : 2018-04-11 07:09:54
                 * vmake : {"id":1,"name":"ac","created_at":"2018-04-11 07:09:52","updated_at":"2018-04-11 07:09:52"}
                 */

                private int id;
                private int vehicle_manufacturer_id;
                private String model;
                private String updated_at;
                private String created_at;
                private VmakeBean vmake;

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public int getVehicle_manufacturer_id() {
                    return vehicle_manufacturer_id;
                }

                public void setVehicle_manufacturer_id(int vehicle_manufacturer_id) {
                    this.vehicle_manufacturer_id = vehicle_manufacturer_id;
                }

                public String getModel() {
                    return model;
                }

                public void setModel(String model) {
                    this.model = model;
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

                public VmakeBean getVmake() {
                    return vmake;
                }

                public void setVmake(VmakeBean vmake) {
                    this.vmake = vmake;
                }

                public static class VmakeBean {
                    /**
                     * id : 1
                     * name : ac
                     * created_at : 2018-04-11 07:09:52
                     * updated_at : 2018-04-11 07:09:52
                     */

                    private int id;
                    private String name;
                    private String created_at;
                    private String updated_at;

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
        }
    }
}
