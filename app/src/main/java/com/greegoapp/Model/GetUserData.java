package com.greegoapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class GetUserData implements Parcelable{


    /**
     * data : {"id":140,"name":"vishal","email":"vishal@gmail.com","lastname":"sdsa","contact_number":"+918460521189","city":null,"profile_pic":"","promocode":null,"verified":"0","is_iphone":"0","is_agreed":"1","device_id":null,"created_at":"2018-04-21 09:31:22","updated_at":"2018-04-21 09:33:10","cards":[{"id":63,"user_id":"140","card_number":"MTExMTIyMjIzMzMzNDQ0NA==","exp_month_year":"12","zipcode":"1234","created_at":"2018-04-21 09:33:28","updated_at":"2018-04-21 09:33:28"}],"vehicles":[{"id":105,"user_id":"140","vehicle_id":"3","year":"2017","type":"0","color":"red","transmission_type":"1","selected":"0","created_at":"2018-04-21 09:33:38","updated_at":"2018-04-21 09:33:38","vehicle_name":"ac","vehicle_model":"428","vehiclemodel":{"id":3,"vehicle_manufacturer_id":"1","model":"428","updated_at":"2018-04-11 01:39:54","created_at":"2018-04-11 01:39:54","vmake":{"id":1,"name":"ac","created_at":"2018-04-11 01:39:52","updated_at":"2018-04-11 01:39:52"}}}]}
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
         * id : 140
         * name : vishal
         * email : vishal@gmail.com
         * lastname : sdsa
         * contact_number : +918460521189
         * city : null
         * profile_pic :
         * promocode : null
         * verified : 0
         * is_iphone : 0
         * is_agreed : 1
         * device_id : null
         * created_at : 2018-04-21 09:31:22
         * updated_at : 2018-04-21 09:33:10
         * cards : [{"id":63,"user_id":"140","card_number":"MTExMTIyMjIzMzMzNDQ0NA==","exp_month_year":"12","zipcode":"1234","created_at":"2018-04-21 09:33:28","updated_at":"2018-04-21 09:33:28"}]
         * vehicles : [{"id":105,"user_id":"140","vehicle_id":"3","year":"2017","type":"0","color":"red","transmission_type":"1","selected":"0","created_at":"2018-04-21 09:33:38","updated_at":"2018-04-21 09:33:38","vehicle_name":"ac","vehicle_model":"428","vehiclemodel":{"id":3,"vehicle_manufacturer_id":"1","model":"428","updated_at":"2018-04-11 01:39:54","created_at":"2018-04-11 01:39:54","vmake":{"id":1,"name":"ac","created_at":"2018-04-11 01:39:52","updated_at":"2018-04-11 01:39:52"}}}]
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
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(id);
            dest.writeString(name);
            dest.writeString(email);
            dest.writeString(lastname);
            dest.writeString(contact_number);
            dest.writeString(profile_pic);
            dest.writeString(verified);
            dest.writeString(is_iphone);
            dest.writeString(is_agreed);
            dest.writeString(created_at);
            dest.writeString(updated_at);
            dest.writeTypedList(cards);
            dest.writeTypedList(vehicles);
        }

        public static class CardsBean implements Parcelable{
            /**
             * id : 63
             * user_id : 140
             * card_number : MTExMTIyMjIzMzMzNDQ0NA==
             * exp_month_year : 12
             * zipcode : 1234
             * created_at : 2018-04-21 09:33:28
             * updated_at : 2018-04-21 09:33:28
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
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeInt(id);
                dest.writeString(user_id);
                dest.writeString(card_number);
                dest.writeString(exp_month_year);
                dest.writeString(zipcode);
                dest.writeString(created_at);
                dest.writeString(updated_at);
            }
        }

        public static class VehiclesBean implements Parcelable {
            /**
             * id : 105
             * user_id : 140
             * vehicle_id : 3
             * year : 2017
             * type : 0
             * color : red
             * transmission_type : 1
             * selected : 0
             * created_at : 2018-04-21 09:33:38
             * updated_at : 2018-04-21 09:33:38
             * vehicle_name : ac
             * vehicle_model : 428
             * vehiclemodel : {"id":3,"vehicle_manufacturer_id":"1","model":"428","updated_at":"2018-04-11 01:39:54","created_at":"2018-04-11 01:39:54","vmake":{"id":1,"name":"ac","created_at":"2018-04-11 01:39:52","updated_at":"2018-04-11 01:39:52"}}
             */

            private int id;
            private String user_id;
            private String vehicle_id;
            private String year;
            private String type;
            private String color;
            private String transmission_type;
            private String selected;
            private String created_at;
            private String updated_at;
            private String vehicle_name;
            private String vehicle_model;
            private VehiclemodelBean vehiclemodel;

            public boolean isSelected() {
                return isSelected;
            }

            public void setSelected(boolean selected) {
                isSelected = selected;
            }

            private boolean isSelected = false;

            protected VehiclesBean(Parcel in) {
                id = in.readInt();
                user_id = in.readString();
                vehicle_id = in.readString();
                year = in.readString();
                type = in.readString();
                color = in.readString();
                transmission_type = in.readString();
                selected = in.readString();
                created_at = in.readString();
                updated_at = in.readString();
                vehicle_name = in.readString();
                vehicle_model = in.readString();
                vehiclemodel = in.readParcelable(VehiclemodelBean.class.getClassLoader());
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

            public String getSelected() {
                return selected;
            }

            public void setSelected(String selected) {
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

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeInt(id);
                dest.writeString(user_id);
                dest.writeString(vehicle_id);
                dest.writeString(year);
                dest.writeString(type);
                dest.writeString(color);
                dest.writeString(transmission_type);
                dest.writeString(selected);
                dest.writeString(created_at);
                dest.writeString(updated_at);
                dest.writeString(vehicle_name);
                dest.writeString(vehicle_model);
                dest.writeParcelable(vehiclemodel, flags);
            }


            public static class VehiclemodelBean implements Parcelable {
                /**
                 * id : 3
                 * vehicle_manufacturer_id : 1
                 * model : 428
                 * updated_at : 2018-04-11 01:39:54
                 * created_at : 2018-04-11 01:39:54
                 * vmake : {"id":1,"name":"ac","created_at":"2018-04-11 01:39:52","updated_at":"2018-04-11 01:39:52"}
                 */

                private int id;
                private String vehicle_manufacturer_id;
                private String model;
                private String updated_at;
                private String created_at;
                private VmakeBean vmake;

                protected VehiclemodelBean(Parcel in) {
                    id = in.readInt();
                    vehicle_manufacturer_id = in.readString();
                    model = in.readString();
                    updated_at = in.readString();
                    created_at = in.readString();
                    vmake = in.readParcelable(VmakeBean.class.getClassLoader());
                }

                public static final Creator<VehiclemodelBean> CREATOR = new Creator<VehiclemodelBean>() {
                    @Override
                    public VehiclemodelBean createFromParcel(Parcel in) {
                        return new VehiclemodelBean(in);
                    }

                    @Override
                    public VehiclemodelBean[] newArray(int size) {
                        return new VehiclemodelBean[size];
                    }
                };

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public String getVehicle_manufacturer_id() {
                    return vehicle_manufacturer_id;
                }

                public void setVehicle_manufacturer_id(String vehicle_manufacturer_id) {
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

                @Override
                public int describeContents() {
                    return 0;
                }

                @Override
                public void writeToParcel(Parcel dest, int flags) {
                    dest.writeInt(id);
                    dest.writeString(vehicle_manufacturer_id);
                    dest.writeString(model);
                    dest.writeString(updated_at);
                    dest.writeString(created_at);
                    dest.writeParcelable(vmake, flags);
                }

                public static class VmakeBean implements Parcelable {
                    /**
                     * id : 1
                     * name : ac
                     * created_at : 2018-04-11 01:39:52
                     * updated_at : 2018-04-11 01:39:52
                     */

                    private int id;
                    private String name;
                    private String created_at;
                    private String updated_at;

                    protected VmakeBean(Parcel in) {
                        id = in.readInt();
                        name = in.readString();
                        created_at = in.readString();
                        updated_at = in.readString();
                    }

                    public static final Creator<VmakeBean> CREATOR = new Creator<VmakeBean>() {
                        @Override
                        public VmakeBean createFromParcel(Parcel in) {
                            return new VmakeBean(in);
                        }

                        @Override
                        public VmakeBean[] newArray(int size) {
                            return new VmakeBean[size];
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
                    public void writeToParcel(Parcel dest, int flags) {
                        dest.writeInt(id);
                        dest.writeString(name);
                        dest.writeString(created_at);
                        dest.writeString(updated_at);
                    }
                }
            }
        }
    }
}
