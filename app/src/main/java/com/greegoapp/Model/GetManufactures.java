package com.greegoapp.Model;

import java.util.List;

public class GetManufactures {

    /**
     * data : [{"id":1,"name":"ac"},{"id":2,"name":"acura"},{"id":3,"name":"allard"},{"id":4,"name":"alpina"},{"id":5,"name":"amc"},{"id":6,"name":"ariel"},{"id":7,"name":"aston-martin"},{"id":8,"name":"audi"},{"id":9,"name":"austin"},{"id":10,"name":"austin-healey"},{"id":11,"name":"avanti"},{"id":12,"name":"bentley"},{"id":13,"name":"berkeley"},{"id":14,"name":"bitter"},{"id":15,"name":"bizzarrini"},{"id":16,"name":"bmw"},{"id":17,"name":"bugatti"},{"id":18,"name":"buick"},{"id":19,"name":"cadillac"},{"id":20,"name":"caterham"},{"id":21,"name":"checker"},{"id":22,"name":"chevrolet"},{"id":23,"name":"chrysler"},{"id":24,"name":"daewoo"},{"id":25,"name":"daimler"},{"id":26,"name":"datsun"},{"id":27,"name":"de-tomaso"},{"id":28,"name":"dodge"},{"id":29,"name":"eagle"},{"id":30,"name":"ferrari"},{"id":31,"name":"fiat"},{"id":32,"name":"fisker"},{"id":33,"name":"ford"},{"id":34,"name":"gmc"},{"id":35,"name":"honda"},{"id":36,"name":"hudson"},{"id":37,"name":"hummer"},{"id":38,"name":"hyundai"},{"id":39,"name":"infiniti"},{"id":40,"name":"isuzu"},{"id":41,"name":"italdesign"},{"id":42,"name":"jaguar"},{"id":43,"name":"jeep"},{"id":44,"name":"kia"},{"id":45,"name":"koenigsegg"},{"id":46,"name":"lamborghini"},{"id":47,"name":"land-rover"},{"id":48,"name":"lexus"},{"id":49,"name":"lincoln"},{"id":50,"name":"lotec"},{"id":51,"name":"lotus"},{"id":52,"name":"marcos"},{"id":53,"name":"maserati"},{"id":54,"name":"maybach"},{"id":55,"name":"mazda"},{"id":56,"name":"mcc"},{"id":57,"name":"mclaren"},{"id":58,"name":"mercedes-benz"},{"id":59,"name":"mercury"},{"id":60,"name":"mg"},{"id":61,"name":"mini"},{"id":62,"name":"mitsubishi"},{"id":63,"name":"moretti"},{"id":64,"name":"morgan"},{"id":65,"name":"nissan"},{"id":66,"name":"noble"},{"id":67,"name":"oldsmobile"},{"id":68,"name":"packard"},{"id":69,"name":"pagani"},{"id":70,"name":"panoz"},{"id":71,"name":"plymouth"},{"id":72,"name":"pontiac"},{"id":73,"name":"porsche"},{"id":74,"name":"rolls-royce"},{"id":75,"name":"saab"},{"id":76,"name":"saleen"},{"id":77,"name":"saturn"},{"id":78,"name":"scion"},{"id":79,"name":"smart"},{"id":80,"name":"spyker"},{"id":81,"name":"ssc"},{"id":82,"name":"studebaker"},{"id":83,"name":"subaru"},{"id":84,"name":"suzuki"},{"id":85,"name":"tesla"},{"id":86,"name":"toyota"},{"id":87,"name":"triumph"},{"id":88,"name":"vector"},{"id":89,"name":"volkswagen"},{"id":90,"name":"volvo"},{"id":91,"name":"wartburg"},{"id":92,"name":"willys-overland"},{"id":93,"name":"zenvo"}]
     * error_code : 0
     * message : VehicleManufacturersanu List
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
         * name : ac
         */

        private int id;
        private String name;

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

        public String toString() {
            return name;
        }
    }
}
