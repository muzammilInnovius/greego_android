package com.greegoapp.Model;

public class Login {


    /**
     * data : {"contact_number":"+918460003300","is_agreed":"0","otp":513208,"token":"eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6ImM4NmZmODg1ODMzYThmYmRkZTI3ZjZhMDBiZWNhMTQ0ZGJjNDk5ZDkzYzcxMGFhMzk4MzY5Y2M4NzliYjlmZTBkMWU0YmQ1MTlmMDhhZjZlIn0.eyJhdWQiOiIxIiwianRpIjoiYzg2ZmY4ODU4MzNhOGZiZGRlMjdmNmEwMGJlY2ExNDRkYmM0OTlkOTNjNzEwYWEzOTgzNjljYzg3OWJiOWZlMGQxZTRiZDUxOWYwOGFmNmUiLCJpYXQiOjE1MjQyOTQ3MDksIm5iZiI6MTUyNDI5NDcwOSwiZXhwIjoxNTU1ODMwNzA5LCJzdWIiOiIxMzQiLCJzY29wZXMiOltdfQ.yg-bg8qNOgRp2bSUGuYOa0Hr-5cZvJW7nKZF_qIXly18M9AxTVrLoAFBkau9RgroVOGIxxdKhe7SYQRaBn3PRq3KIe0Hqg5YmDE-tZ7-tjjrMyHKfvRvjtbGzArgheCyolUmVECvIVIDC-_nTvXVIUjY2xADgBQ7ycULBX1qVL-GY4_8ZzZtBU3RPE0RTNLnlscnd8CGhMtB3tvmr__3bTu087Y_PAfVEhMKvT0Y5N5d0ESEt2nDMHE8RyYaN_azUOcRdp3-dowK79qColpGXNOO2d-_ocvflYczRbW_K9mERC2QkeA6EU-Jl089HAotSsTBSyy5wjXs2vVuMomb_TTUCP8tL5MaoVAMi_avh9zkQxXcNEyx0qOWHAB4TNWGtepDnAHCfY8q-6IaOUq6kMGFXWY6zVr_XTEHlS46eAprTx1QVeR_glSrdi5ztKaS3DZyIxqffVadWwKYFrQhYT3_liJf_i_Zx7Tok2uXrSxce6_6ha2E3UyxxRj087zvQImrgHpj0v51s_5ym7qJqdz6IU2RZdCeplONO7RsB7-wxG55HCKgL5L_pAR82hCE9djyX6KwfJ3OQPv7q7S_aeoDQpwqfYwMg-mo0Pl1YZTL3xqUiyybbNiYAZjb6PBzYE6RySLQNBj9vcVxROvVzi6sJLrZK92bnZEn99EOyx8"}
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
         * contact_number : +918460003300
         * is_agreed : 0
         * otp : 513208
         * token : eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6ImM4NmZmODg1ODMzYThmYmRkZTI3ZjZhMDBiZWNhMTQ0ZGJjNDk5ZDkzYzcxMGFhMzk4MzY5Y2M4NzliYjlmZTBkMWU0YmQ1MTlmMDhhZjZlIn0.eyJhdWQiOiIxIiwianRpIjoiYzg2ZmY4ODU4MzNhOGZiZGRlMjdmNmEwMGJlY2ExNDRkYmM0OTlkOTNjNzEwYWEzOTgzNjljYzg3OWJiOWZlMGQxZTRiZDUxOWYwOGFmNmUiLCJpYXQiOjE1MjQyOTQ3MDksIm5iZiI6MTUyNDI5NDcwOSwiZXhwIjoxNTU1ODMwNzA5LCJzdWIiOiIxMzQiLCJzY29wZXMiOltdfQ.yg-bg8qNOgRp2bSUGuYOa0Hr-5cZvJW7nKZF_qIXly18M9AxTVrLoAFBkau9RgroVOGIxxdKhe7SYQRaBn3PRq3KIe0Hqg5YmDE-tZ7-tjjrMyHKfvRvjtbGzArgheCyolUmVECvIVIDC-_nTvXVIUjY2xADgBQ7ycULBX1qVL-GY4_8ZzZtBU3RPE0RTNLnlscnd8CGhMtB3tvmr__3bTu087Y_PAfVEhMKvT0Y5N5d0ESEt2nDMHE8RyYaN_azUOcRdp3-dowK79qColpGXNOO2d-_ocvflYczRbW_K9mERC2QkeA6EU-Jl089HAotSsTBSyy5wjXs2vVuMomb_TTUCP8tL5MaoVAMi_avh9zkQxXcNEyx0qOWHAB4TNWGtepDnAHCfY8q-6IaOUq6kMGFXWY6zVr_XTEHlS46eAprTx1QVeR_glSrdi5ztKaS3DZyIxqffVadWwKYFrQhYT3_liJf_i_Zx7Tok2uXrSxce6_6ha2E3UyxxRj087zvQImrgHpj0v51s_5ym7qJqdz6IU2RZdCeplONO7RsB7-wxG55HCKgL5L_pAR82hCE9djyX6KwfJ3OQPv7q7S_aeoDQpwqfYwMg-mo0Pl1YZTL3xqUiyybbNiYAZjb6PBzYE6RySLQNBj9vcVxROvVzi6sJLrZK92bnZEn99EOyx8
         */

        private String contact_number;
        private String is_agreed;
        private int otp;
        private String token;

        public String getContact_number() {
            return contact_number;
        }

        public void setContact_number(String contact_number) {
            this.contact_number = contact_number;
        }

        public String getIs_agreed() {
            return is_agreed;
        }

        public void setIs_agreed(String is_agreed) {
            this.is_agreed = is_agreed;
        }

        public int getOtp() {
            return otp;
        }

        public void setOtp(int otp) {
            this.otp = otp;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
}
