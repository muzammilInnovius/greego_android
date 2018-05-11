package com.greegoapp.Model;

import java.util.List;

public class GetPickUpTime {

    /**
     * geocoded_waypoints : [{"geocoder_status":"OK","place_id":"ChIJ2-W8VAGFXjkRJzjPR5wfJag","types":["premise"]},{"geocoder_status":"OK","place_id":"ChIJf-Sf8wWFXjkRMH_IwanaHlI","types":["street_address"]}]
     * routes : [{"bounds":{"northeast":{"lat":23.0098074,"lng":72.5639948},"southwest":{"lat":23.0039894,"lng":72.56047889999999}},"copyrights":"Map data ©2018 Google","legs":[{"distance":{"text":"0.9 km","value":937},"duration":{"text":"4 mins","value":215},"end_address":"C/3, Shantivan, Fatehpura, Shantivan, Motinagar, Paldi, Ahmedabad, Gujarat 380007, India","end_location":{"lat":23.0039894,"lng":72.56047889999999},"start_address":"Merlin Pentagon, Mahalaxmi Five Rd, Fatehpura, Paldi, Ahmedabad, Gujarat 380007, India","start_location":{"lat":23.0098074,"lng":72.5620871},"steps":[{"distance":{"text":"37 m","value":37},"duration":{"text":"1 min","value":9},"end_location":{"lat":23.0097246,"lng":72.5624345},"html_instructions":"Head <b>east<\/b> on <b>Vikas Gruh Rd<\/b> toward <b>Freniben Desai Marg<\/b>/<b>Mahalaxmi Five Rd<\/b>","polyline":{"points":"ibmkCah{yLJq@DQ"},"start_location":{"lat":23.0098074,"lng":72.5620871},"travel_mode":"DRIVING"},{"distance":{"text":"51 m","value":51},"duration":{"text":"1 min","value":11},"end_location":{"lat":23.0093097,"lng":72.5622339},"html_instructions":"Turn <b>right<\/b> onto <b>Freniben Desai Marg<\/b>/<b>Mahalaxmi Five Rd<\/b>","maneuver":"turn-right","polyline":{"points":"wamkCej{yL^J^NPJ"},"start_location":{"lat":23.0097246,"lng":72.5624345},"travel_mode":"DRIVING"},{"distance":{"text":"0.4 km","value":441},"duration":{"text":"1 min","value":74},"end_location":{"lat":23.0057076,"lng":72.5639948},"html_instructions":"Turn <b>left<\/b> onto <b>Sheikh Adam Abuwala Marg<\/b>","maneuver":"turn-left","polyline":{"points":"e_mkC}h{yLbDcB`Ai@n@]TMTIZKh@Mb@Kb@Kd@K`@Id@Ob@MXKRIl@UNI@?"},"start_location":{"lat":23.0093097,"lng":72.5622339},"travel_mode":"DRIVING"},{"distance":{"text":"0.4 km","value":408},"duration":{"text":"2 mins","value":121},"end_location":{"lat":23.0039894,"lng":72.56047889999999},"html_instructions":"Turn <b>right<\/b><div style=\"font-size:0.9em\">Destination will be on the right<\/div>","maneuver":"turn-right","polyline":{"points":"uhlkC}s{yLf@bALTN^b@x@N\\Xn@t@hBVr@`@lAJXj@pB@BX~@"},"start_location":{"lat":23.0057076,"lng":72.5639948},"travel_mode":"DRIVING"}],"traffic_speed_entry":[],"via_waypoint":[]}],"overview_polyline":{"points":"ibmkCah{yLPcA~@ZPJbDcBpBgAj@WdAYnCm@vBs@|@_@@?f@bA\\t@r@vAnAxCx@`CrAnE"},"summary":"Sheikh Adam Abuwala Marg","warnings":[],"waypoint_order":[]}]
     * status : OK
     */

    private String status;
    private List<GeocodedWaypointsBean> geocoded_waypoints;
    private List<RoutesBean> routes;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<GeocodedWaypointsBean> getGeocoded_waypoints() {
        return geocoded_waypoints;
    }

    public void setGeocoded_waypoints(List<GeocodedWaypointsBean> geocoded_waypoints) {
        this.geocoded_waypoints = geocoded_waypoints;
    }

    public List<RoutesBean> getRoutes() {
        return routes;
    }

    public void setRoutes(List<RoutesBean> routes) {
        this.routes = routes;
    }

    public static class GeocodedWaypointsBean {
        /**
         * geocoder_status : OK
         * place_id : ChIJ2-W8VAGFXjkRJzjPR5wfJag
         * types : ["premise"]
         */

        private String geocoder_status;
        private String place_id;
        private List<String> types;

        public String getGeocoder_status() {
            return geocoder_status;
        }

        public void setGeocoder_status(String geocoder_status) {
            this.geocoder_status = geocoder_status;
        }

        public String getPlace_id() {
            return place_id;
        }

        public void setPlace_id(String place_id) {
            this.place_id = place_id;
        }

        public List<String> getTypes() {
            return types;
        }

        public void setTypes(List<String> types) {
            this.types = types;
        }
    }

    public static class RoutesBean {
        /**
         * bounds : {"northeast":{"lat":23.0098074,"lng":72.5639948},"southwest":{"lat":23.0039894,"lng":72.56047889999999}}
         * copyrights : Map data ©2018 Google
         * legs : [{"distance":{"text":"0.9 km","value":937},"duration":{"text":"4 mins","value":215},"end_address":"C/3, Shantivan, Fatehpura, Shantivan, Motinagar, Paldi, Ahmedabad, Gujarat 380007, India","end_location":{"lat":23.0039894,"lng":72.56047889999999},"start_address":"Merlin Pentagon, Mahalaxmi Five Rd, Fatehpura, Paldi, Ahmedabad, Gujarat 380007, India","start_location":{"lat":23.0098074,"lng":72.5620871},"steps":[{"distance":{"text":"37 m","value":37},"duration":{"text":"1 min","value":9},"end_location":{"lat":23.0097246,"lng":72.5624345},"html_instructions":"Head <b>east<\/b> on <b>Vikas Gruh Rd<\/b> toward <b>Freniben Desai Marg<\/b>/<b>Mahalaxmi Five Rd<\/b>","polyline":{"points":"ibmkCah{yLJq@DQ"},"start_location":{"lat":23.0098074,"lng":72.5620871},"travel_mode":"DRIVING"},{"distance":{"text":"51 m","value":51},"duration":{"text":"1 min","value":11},"end_location":{"lat":23.0093097,"lng":72.5622339},"html_instructions":"Turn <b>right<\/b> onto <b>Freniben Desai Marg<\/b>/<b>Mahalaxmi Five Rd<\/b>","maneuver":"turn-right","polyline":{"points":"wamkCej{yL^J^NPJ"},"start_location":{"lat":23.0097246,"lng":72.5624345},"travel_mode":"DRIVING"},{"distance":{"text":"0.4 km","value":441},"duration":{"text":"1 min","value":74},"end_location":{"lat":23.0057076,"lng":72.5639948},"html_instructions":"Turn <b>left<\/b> onto <b>Sheikh Adam Abuwala Marg<\/b>","maneuver":"turn-left","polyline":{"points":"e_mkC}h{yLbDcB`Ai@n@]TMTIZKh@Mb@Kb@Kd@K`@Id@Ob@MXKRIl@UNI@?"},"start_location":{"lat":23.0093097,"lng":72.5622339},"travel_mode":"DRIVING"},{"distance":{"text":"0.4 km","value":408},"duration":{"text":"2 mins","value":121},"end_location":{"lat":23.0039894,"lng":72.56047889999999},"html_instructions":"Turn <b>right<\/b><div style=\"font-size:0.9em\">Destination will be on the right<\/div>","maneuver":"turn-right","polyline":{"points":"uhlkC}s{yLf@bALTN^b@x@N\\Xn@t@hBVr@`@lAJXj@pB@BX~@"},"start_location":{"lat":23.0057076,"lng":72.5639948},"travel_mode":"DRIVING"}],"traffic_speed_entry":[],"via_waypoint":[]}]
         * overview_polyline : {"points":"ibmkCah{yLPcA~@ZPJbDcBpBgAj@WdAYnCm@vBs@|@_@@?f@bA\\t@r@vAnAxCx@`CrAnE"}
         * summary : Sheikh Adam Abuwala Marg
         * warnings : []
         * waypoint_order : []
         */

        private BoundsBean bounds;
        private String copyrights;
        private OverviewPolylineBean overview_polyline;
        private String summary;
        private List<LegsBean> legs;
        private List<?> warnings;
        private List<?> waypoint_order;

        public BoundsBean getBounds() {
            return bounds;
        }

        public void setBounds(BoundsBean bounds) {
            this.bounds = bounds;
        }

        public String getCopyrights() {
            return copyrights;
        }

        public void setCopyrights(String copyrights) {
            this.copyrights = copyrights;
        }

        public OverviewPolylineBean getOverview_polyline() {
            return overview_polyline;
        }

        public void setOverview_polyline(OverviewPolylineBean overview_polyline) {
            this.overview_polyline = overview_polyline;
        }

        public String getSummary() {
            return summary;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }

        public List<LegsBean> getLegs() {
            return legs;
        }

        public void setLegs(List<LegsBean> legs) {
            this.legs = legs;
        }

        public List<?> getWarnings() {
            return warnings;
        }

        public void setWarnings(List<?> warnings) {
            this.warnings = warnings;
        }

        public List<?> getWaypoint_order() {
            return waypoint_order;
        }

        public void setWaypoint_order(List<?> waypoint_order) {
            this.waypoint_order = waypoint_order;
        }

        public static class BoundsBean {
            /**
             * northeast : {"lat":23.0098074,"lng":72.5639948}
             * southwest : {"lat":23.0039894,"lng":72.56047889999999}
             */

            private NortheastBean northeast;
            private SouthwestBean southwest;

            public NortheastBean getNortheast() {
                return northeast;
            }

            public void setNortheast(NortheastBean northeast) {
                this.northeast = northeast;
            }

            public SouthwestBean getSouthwest() {
                return southwest;
            }

            public void setSouthwest(SouthwestBean southwest) {
                this.southwest = southwest;
            }

            public static class NortheastBean {
                /**
                 * lat : 23.0098074
                 * lng : 72.5639948
                 */

                private double lat;
                private double lng;

                public double getLat() {
                    return lat;
                }

                public void setLat(double lat) {
                    this.lat = lat;
                }

                public double getLng() {
                    return lng;
                }

                public void setLng(double lng) {
                    this.lng = lng;
                }
            }

            public static class SouthwestBean {
                /**
                 * lat : 23.0039894
                 * lng : 72.56047889999999
                 */

                private double lat;
                private double lng;

                public double getLat() {
                    return lat;
                }

                public void setLat(double lat) {
                    this.lat = lat;
                }

                public double getLng() {
                    return lng;
                }

                public void setLng(double lng) {
                    this.lng = lng;
                }
            }
        }

        public static class OverviewPolylineBean {
            /**
             * points : ibmkCah{yLPcA~@ZPJbDcBpBgAj@WdAYnCm@vBs@|@_@@?f@bA\t@r@vAnAxCx@`CrAnE
             */

            private String points;

            public String getPoints() {
                return points;
            }

            public void setPoints(String points) {
                this.points = points;
            }
        }

        public static class LegsBean {
            /**
             * distance : {"text":"0.9 km","value":937}
             * duration : {"text":"4 mins","value":215}
             * end_address : C/3, Shantivan, Fatehpura, Shantivan, Motinagar, Paldi, Ahmedabad, Gujarat 380007, India
             * end_location : {"lat":23.0039894,"lng":72.56047889999999}
             * start_address : Merlin Pentagon, Mahalaxmi Five Rd, Fatehpura, Paldi, Ahmedabad, Gujarat 380007, India
             * start_location : {"lat":23.0098074,"lng":72.5620871}
             * steps : [{"distance":{"text":"37 m","value":37},"duration":{"text":"1 min","value":9},"end_location":{"lat":23.0097246,"lng":72.5624345},"html_instructions":"Head <b>east<\/b> on <b>Vikas Gruh Rd<\/b> toward <b>Freniben Desai Marg<\/b>/<b>Mahalaxmi Five Rd<\/b>","polyline":{"points":"ibmkCah{yLJq@DQ"},"start_location":{"lat":23.0098074,"lng":72.5620871},"travel_mode":"DRIVING"},{"distance":{"text":"51 m","value":51},"duration":{"text":"1 min","value":11},"end_location":{"lat":23.0093097,"lng":72.5622339},"html_instructions":"Turn <b>right<\/b> onto <b>Freniben Desai Marg<\/b>/<b>Mahalaxmi Five Rd<\/b>","maneuver":"turn-right","polyline":{"points":"wamkCej{yL^J^NPJ"},"start_location":{"lat":23.0097246,"lng":72.5624345},"travel_mode":"DRIVING"},{"distance":{"text":"0.4 km","value":441},"duration":{"text":"1 min","value":74},"end_location":{"lat":23.0057076,"lng":72.5639948},"html_instructions":"Turn <b>left<\/b> onto <b>Sheikh Adam Abuwala Marg<\/b>","maneuver":"turn-left","polyline":{"points":"e_mkC}h{yLbDcB`Ai@n@]TMTIZKh@Mb@Kb@Kd@K`@Id@Ob@MXKRIl@UNI@?"},"start_location":{"lat":23.0093097,"lng":72.5622339},"travel_mode":"DRIVING"},{"distance":{"text":"0.4 km","value":408},"duration":{"text":"2 mins","value":121},"end_location":{"lat":23.0039894,"lng":72.56047889999999},"html_instructions":"Turn <b>right<\/b><div style=\"font-size:0.9em\">Destination will be on the right<\/div>","maneuver":"turn-right","polyline":{"points":"uhlkC}s{yLf@bALTN^b@x@N\\Xn@t@hBVr@`@lAJXj@pB@BX~@"},"start_location":{"lat":23.0057076,"lng":72.5639948},"travel_mode":"DRIVING"}]
             * traffic_speed_entry : []
             * via_waypoint : []
             */

            private DistanceBean distance;
            private DurationBean duration;
            private String end_address;
            private EndLocationBean end_location;
            private String start_address;
            private StartLocationBean start_location;
            private List<StepsBean> steps;
            private List<?> traffic_speed_entry;
            private List<?> via_waypoint;

            public DistanceBean getDistance() {
                return distance;
            }

            public void setDistance(DistanceBean distance) {
                this.distance = distance;
            }

            public DurationBean getDuration() {
                return duration;
            }

            public void setDuration(DurationBean duration) {
                this.duration = duration;
            }

            public String getEnd_address() {
                return end_address;
            }

            public void setEnd_address(String end_address) {
                this.end_address = end_address;
            }

            public EndLocationBean getEnd_location() {
                return end_location;
            }

            public void setEnd_location(EndLocationBean end_location) {
                this.end_location = end_location;
            }

            public String getStart_address() {
                return start_address;
            }

            public void setStart_address(String start_address) {
                this.start_address = start_address;
            }

            public StartLocationBean getStart_location() {
                return start_location;
            }

            public void setStart_location(StartLocationBean start_location) {
                this.start_location = start_location;
            }

            public List<StepsBean> getSteps() {
                return steps;
            }

            public void setSteps(List<StepsBean> steps) {
                this.steps = steps;
            }

            public List<?> getTraffic_speed_entry() {
                return traffic_speed_entry;
            }

            public void setTraffic_speed_entry(List<?> traffic_speed_entry) {
                this.traffic_speed_entry = traffic_speed_entry;
            }

            public List<?> getVia_waypoint() {
                return via_waypoint;
            }

            public void setVia_waypoint(List<?> via_waypoint) {
                this.via_waypoint = via_waypoint;
            }

            public static class DistanceBean {
                /**
                 * text : 0.9 km
                 * value : 937
                 */

                private String text;
                private int value;

                public String getText() {
                    return text;
                }

                public void setText(String text) {
                    this.text = text;
                }

                public int getValue() {
                    return value;
                }

                public void setValue(int value) {
                    this.value = value;
                }
            }

            public static class DurationBean {
                /**
                 * text : 4 mins
                 * value : 215
                 */

                private String text;
                private int value;

                public String getText() {
                    return text;
                }

                public void setText(String text) {
                    this.text = text;
                }

                public int getValue() {
                    return value;
                }

                public void setValue(int value) {
                    this.value = value;
                }
            }

            public static class EndLocationBean {
                /**
                 * lat : 23.0039894
                 * lng : 72.56047889999999
                 */

                private double lat;
                private double lng;

                public double getLat() {
                    return lat;
                }

                public void setLat(double lat) {
                    this.lat = lat;
                }

                public double getLng() {
                    return lng;
                }

                public void setLng(double lng) {
                    this.lng = lng;
                }
            }

            public static class StartLocationBean {
                /**
                 * lat : 23.0098074
                 * lng : 72.5620871
                 */

                private double lat;
                private double lng;

                public double getLat() {
                    return lat;
                }

                public void setLat(double lat) {
                    this.lat = lat;
                }

                public double getLng() {
                    return lng;
                }

                public void setLng(double lng) {
                    this.lng = lng;
                }
            }

            public static class StepsBean {
                /**
                 * distance : {"text":"37 m","value":37}
                 * duration : {"text":"1 min","value":9}
                 * end_location : {"lat":23.0097246,"lng":72.5624345}
                 * html_instructions : Head <b>east</b> on <b>Vikas Gruh Rd</b> toward <b>Freniben Desai Marg</b>/<b>Mahalaxmi Five Rd</b>
                 * polyline : {"points":"ibmkCah{yLJq@DQ"}
                 * start_location : {"lat":23.0098074,"lng":72.5620871}
                 * travel_mode : DRIVING
                 * maneuver : turn-right
                 */

                private DistanceBeanX distance;
                private DurationBeanX duration;
                private EndLocationBeanX end_location;
                private String html_instructions;
                private PolylineBean polyline;
                private StartLocationBeanX start_location;
                private String travel_mode;
                private String maneuver;

                public DistanceBeanX getDistance() {
                    return distance;
                }

                public void setDistance(DistanceBeanX distance) {
                    this.distance = distance;
                }

                public DurationBeanX getDuration() {
                    return duration;
                }

                public void setDuration(DurationBeanX duration) {
                    this.duration = duration;
                }

                public EndLocationBeanX getEnd_location() {
                    return end_location;
                }

                public void setEnd_location(EndLocationBeanX end_location) {
                    this.end_location = end_location;
                }

                public String getHtml_instructions() {
                    return html_instructions;
                }

                public void setHtml_instructions(String html_instructions) {
                    this.html_instructions = html_instructions;
                }

                public PolylineBean getPolyline() {
                    return polyline;
                }

                public void setPolyline(PolylineBean polyline) {
                    this.polyline = polyline;
                }

                public StartLocationBeanX getStart_location() {
                    return start_location;
                }

                public void setStart_location(StartLocationBeanX start_location) {
                    this.start_location = start_location;
                }

                public String getTravel_mode() {
                    return travel_mode;
                }

                public void setTravel_mode(String travel_mode) {
                    this.travel_mode = travel_mode;
                }

                public String getManeuver() {
                    return maneuver;
                }

                public void setManeuver(String maneuver) {
                    this.maneuver = maneuver;
                }

                public static class DistanceBeanX {
                    /**
                     * text : 37 m
                     * value : 37
                     */

                    private String text;
                    private int value;

                    public String getText() {
                        return text;
                    }

                    public void setText(String text) {
                        this.text = text;
                    }

                    public int getValue() {
                        return value;
                    }

                    public void setValue(int value) {
                        this.value = value;
                    }
                }

                public static class DurationBeanX {
                    /**
                     * text : 1 min
                     * value : 9
                     */

                    private String text;
                    private int value;

                    public String getText() {
                        return text;
                    }

                    public void setText(String text) {
                        this.text = text;
                    }

                    public int getValue() {
                        return value;
                    }

                    public void setValue(int value) {
                        this.value = value;
                    }
                }

                public static class EndLocationBeanX {
                    /**
                     * lat : 23.0097246
                     * lng : 72.5624345
                     */

                    private double lat;
                    private double lng;

                    public double getLat() {
                        return lat;
                    }

                    public void setLat(double lat) {
                        this.lat = lat;
                    }

                    public double getLng() {
                        return lng;
                    }

                    public void setLng(double lng) {
                        this.lng = lng;
                    }
                }

                public static class PolylineBean {
                    /**
                     * points : ibmkCah{yLJq@DQ
                     */

                    private String points;

                    public String getPoints() {
                        return points;
                    }

                    public void setPoints(String points) {
                        this.points = points;
                    }
                }

                public static class StartLocationBeanX {
                    /**
                     * lat : 23.0098074
                     * lng : 72.5620871
                     */

                    private double lat;
                    private double lng;

                    public double getLat() {
                        return lat;
                    }

                    public void setLat(double lat) {
                        this.lat = lat;
                    }

                    public double getLng() {
                        return lng;
                    }

                    public void setLng(double lng) {
                        this.lng = lng;
                    }
                }
            }
        }
    }
}
