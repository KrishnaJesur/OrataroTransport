package com.edusunsoft.transport.orataro.ui.activitymaps;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class DirectionsResModel implements Serializable {

    @SerializedName("geocoded_waypoints")
    @Expose
    private List<GeocodedWaypoint> geocodedWaypoints = null;
    @SerializedName("routes")
    @Expose
    private List<Route> routes = null;
    @SerializedName("status")
    @Expose
    private String status;
    private final static long serialVersionUID = -4023571478346116893L;

    public List<GeocodedWaypoint> getGeocodedWaypoints() {
        return geocodedWaypoints;
    }

    public void setGeocodedWaypoints(List<GeocodedWaypoint> geocodedWaypoints) {
        this.geocodedWaypoints = geocodedWaypoints;
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public class Bounds implements Serializable {

        @SerializedName("northeast")
        @Expose
        private Northeast northeast;
        @SerializedName("southwest")
        @Expose
        private Southwest southwest;
        private final static long serialVersionUID = -6853458718051117874L;

        public Northeast getNortheast() {
            return northeast;
        }

        public void setNortheast(Northeast northeast) {
            this.northeast = northeast;
        }

        public Southwest getSouthwest() {
            return southwest;
        }

        public void setSouthwest(Southwest southwest) {
            this.southwest = southwest;
        }

    }


    public class Distance implements Serializable {

        @SerializedName("text")
        @Expose
        private String text;
        @SerializedName("value")
        @Expose
        private int value;
        private final static long serialVersionUID = -3810008782554087890L;

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


    public class Distance_ implements Serializable {

        @SerializedName("text")
        @Expose
        private String text;
        @SerializedName("value")
        @Expose
        private int value;
        private final static long serialVersionUID = -2514986748814802819L;

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


    public class Duration implements Serializable {

        @SerializedName("text")
        @Expose
        private String text;
        @SerializedName("value")
        @Expose
        private int value;
        private final static long serialVersionUID = 6061694072011681309L;

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

    public class Duration_ implements Serializable {

        @SerializedName("text")
        @Expose
        private String text;
        @SerializedName("value")
        @Expose
        private int value;
        private final static long serialVersionUID = 8479920090100406832L;

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


    public class EndLocation implements Serializable {

        @SerializedName("lat")
        @Expose
        private float lat;
        @SerializedName("lng")
        @Expose
        private float lng;
        private final static long serialVersionUID = 6848943772265073097L;

        public float getLat() {
            return lat;
        }

        public void setLat(float lat) {
            this.lat = lat;
        }

        public float getLng() {
            return lng;
        }

        public void setLng(float lng) {
            this.lng = lng;
        }

    }


    public class EndLocation_ implements Serializable {

        @SerializedName("lat")
        @Expose
        private float lat;
        @SerializedName("lng")
        @Expose
        private float lng;
        private final static long serialVersionUID = -737792453064458661L;

        public float getLat() {
            return lat;
        }

        public void setLat(float lat) {
            this.lat = lat;
        }

        public float getLng() {
            return lng;
        }

        public void setLng(float lng) {
            this.lng = lng;
        }

    }


    public class GeocodedWaypoint implements Serializable {

        @SerializedName("geocoder_status")
        @Expose
        private String geocoderStatus;
        @SerializedName("place_id")
        @Expose
        private String placeId;
        @SerializedName("types")
        @Expose
        private List<String> types = null;
        private final static long serialVersionUID = -92605610094315157L;

        public String getGeocoderStatus() {
            return geocoderStatus;
        }

        public void setGeocoderStatus(String geocoderStatus) {
            this.geocoderStatus = geocoderStatus;
        }

        public String getPlaceId() {
            return placeId;
        }

        public void setPlaceId(String placeId) {
            this.placeId = placeId;
        }

        public List<String> getTypes() {
            return types;
        }

        public void setTypes(List<String> types) {
            this.types = types;
        }

    }


    public class Leg implements Serializable {

        @SerializedName("distance")
        @Expose
        private Distance distance;
        @SerializedName("duration")
        @Expose
        private Duration duration;
        @SerializedName("end_address")
        @Expose
        private String endAddress;
        @SerializedName("end_location")
        @Expose
        private EndLocation endLocation;
        @SerializedName("start_address")
        @Expose
        private String startAddress;
        @SerializedName("start_location")
        @Expose
        private StartLocation startLocation;
        @SerializedName("steps")
        @Expose
        private List<Step> steps = null;
        @SerializedName("traffic_speed_entry")
        @Expose
        private List<Object> trafficSpeedEntry = null;
        @SerializedName("via_waypoint")
        @Expose
        private List<Object> viaWaypoint = null;
        private final static long serialVersionUID = -2561717530701602612L;

        public Distance getDistance() {
            return distance;
        }

        public void setDistance(Distance distance) {
            this.distance = distance;
        }

        public Duration getDuration() {
            return duration;
        }

        public void setDuration(Duration duration) {
            this.duration = duration;
        }

        public String getEndAddress() {
            return endAddress;
        }

        public void setEndAddress(String endAddress) {
            this.endAddress = endAddress;
        }

        public EndLocation getEndLocation() {
            return endLocation;
        }

        public void setEndLocation(EndLocation endLocation) {
            this.endLocation = endLocation;
        }

        public String getStartAddress() {
            return startAddress;
        }

        public void setStartAddress(String startAddress) {
            this.startAddress = startAddress;
        }

        public StartLocation getStartLocation() {
            return startLocation;
        }

        public void setStartLocation(StartLocation startLocation) {
            this.startLocation = startLocation;
        }

        public List<Step> getSteps() {
            return steps;
        }

        public void setSteps(List<Step> steps) {
            this.steps = steps;
        }

        public List<Object> getTrafficSpeedEntry() {
            return trafficSpeedEntry;
        }

        public void setTrafficSpeedEntry(List<Object> trafficSpeedEntry) {
            this.trafficSpeedEntry = trafficSpeedEntry;
        }

        public List<Object> getViaWaypoint() {
            return viaWaypoint;
        }

        public void setViaWaypoint(List<Object> viaWaypoint) {
            this.viaWaypoint = viaWaypoint;
        }

    }


    public class Northeast implements Serializable {

        @SerializedName("lat")
        @Expose
        private float lat;
        @SerializedName("lng")
        @Expose
        private float lng;
        private final static long serialVersionUID = -5170827947181689687L;

        public float getLat() {
            return lat;
        }

        public void setLat(float lat) {
            this.lat = lat;
        }

        public float getLng() {
            return lng;
        }

        public void setLng(float lng) {
            this.lng = lng;
        }

    }

    public class OverviewPolyline implements Serializable {

        @SerializedName("points")
        @Expose
        private String points;
        private final static long serialVersionUID = 6052532207009513303L;

        public String getPoints() {
            return points;
        }

        public void setPoints(String points) {
            this.points = points;
        }

    }


    public class Polyline implements Serializable {

        @SerializedName("points")
        @Expose
        private String points;
        private final static long serialVersionUID = -6072724779537989677L;

        public String getPoints() {
            return points;
        }

        public void setPoints(String points) {
            this.points = points;
        }

    }


    public class Route implements Serializable {

        @SerializedName("bounds")
        @Expose
        private Bounds bounds;
        @SerializedName("copyrights")
        @Expose
        private String copyrights;
        @SerializedName("legs")
        @Expose
        private List<Leg> legs = null;
        @SerializedName("overview_polyline")
        @Expose
        private OverviewPolyline overviewPolyline;
        @SerializedName("summary")
        @Expose
        private String summary;
        @SerializedName("warnings")
        @Expose
        private List<Object> warnings = null;
        @SerializedName("waypoint_order")
        @Expose
        private List<Object> waypointOrder = null;
        private final static long serialVersionUID = 5212405337481769216L;

        public Bounds getBounds() {
            return bounds;
        }

        public void setBounds(Bounds bounds) {
            this.bounds = bounds;
        }

        public String getCopyrights() {
            return copyrights;
        }

        public void setCopyrights(String copyrights) {
            this.copyrights = copyrights;
        }

        public List<Leg> getLegs() {
            return legs;
        }

        public void setLegs(List<Leg> legs) {
            this.legs = legs;
        }

        public OverviewPolyline getOverviewPolyline() {
            return overviewPolyline;
        }

        public void setOverviewPolyline(OverviewPolyline overviewPolyline) {
            this.overviewPolyline = overviewPolyline;
        }

        public String getSummary() {
            return summary;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }

        public List<Object> getWarnings() {
            return warnings;
        }

        public void setWarnings(List<Object> warnings) {
            this.warnings = warnings;
        }

        public List<Object> getWaypointOrder() {
            return waypointOrder;
        }

        public void setWaypointOrder(List<Object> waypointOrder) {
            this.waypointOrder = waypointOrder;
        }

    }


    public class Southwest implements Serializable {

        @SerializedName("lat")
        @Expose
        private float lat;
        @SerializedName("lng")
        @Expose
        private float lng;
        private final static long serialVersionUID = -1296364183740695509L;

        public float getLat() {
            return lat;
        }

        public void setLat(float lat) {
            this.lat = lat;
        }

        public float getLng() {
            return lng;
        }

        public void setLng(float lng) {
            this.lng = lng;
        }

    }


    public class StartLocation implements Serializable {

        @SerializedName("lat")
        @Expose
        private float lat;
        @SerializedName("lng")
        @Expose
        private float lng;
        private final static long serialVersionUID = -2432666631042341051L;

        public float getLat() {
            return lat;
        }

        public void setLat(float lat) {
            this.lat = lat;
        }

        public float getLng() {
            return lng;
        }

        public void setLng(float lng) {
            this.lng = lng;
        }

    }


    public class StartLocation_ implements Serializable {

        @SerializedName("lat")
        @Expose
        private float lat;
        @SerializedName("lng")
        @Expose
        private float lng;
        private final static long serialVersionUID = -5089228782894522183L;

        public float getLat() {
            return lat;
        }

        public void setLat(float lat) {
            this.lat = lat;
        }

        public float getLng() {
            return lng;
        }

        public void setLng(float lng) {
            this.lng = lng;
        }

    }


    public class Step implements Serializable {

        @SerializedName("distance")
        @Expose
        private Distance_ distance;
        @SerializedName("duration")
        @Expose
        private Duration_ duration;
        @SerializedName("end_location")
        @Expose
        private EndLocation_ endLocation;
        @SerializedName("html_instructions")
        @Expose
        private String htmlInstructions;
        @SerializedName("polyline")
        @Expose
        private Polyline polyline;
        @SerializedName("start_location")
        @Expose
        private StartLocation_ startLocation;
        @SerializedName("travel_mode")
        @Expose
        private String travelMode;
        @SerializedName("maneuver")
        @Expose
        private String maneuver;
        private final static long serialVersionUID = 8091645151320122827L;

        public Distance_ getDistance() {
            return distance;
        }

        public void setDistance(Distance_ distance) {
            this.distance = distance;
        }

        public Duration_ getDuration() {
            return duration;
        }

        public void setDuration(Duration_ duration) {
            this.duration = duration;
        }

        public EndLocation_ getEndLocation() {
            return endLocation;
        }

        public void setEndLocation(EndLocation_ endLocation) {
            this.endLocation = endLocation;
        }

        public String getHtmlInstructions() {
            return htmlInstructions;
        }

        public void setHtmlInstructions(String htmlInstructions) {
            this.htmlInstructions = htmlInstructions;
        }

        public Polyline getPolyline() {
            return polyline;
        }

        public void setPolyline(Polyline polyline) {
            this.polyline = polyline;
        }

        public StartLocation_ getStartLocation() {
            return startLocation;
        }

        public void setStartLocation(StartLocation_ startLocation) {
            this.startLocation = startLocation;
        }

        public String getTravelMode() {
            return travelMode;
        }

        public void setTravelMode(String travelMode) {
            this.travelMode = travelMode;
        }

        public String getManeuver() {
            return maneuver;
        }

        public void setManeuver(String maneuver) {
            this.maneuver = maneuver;
        }

    }

}