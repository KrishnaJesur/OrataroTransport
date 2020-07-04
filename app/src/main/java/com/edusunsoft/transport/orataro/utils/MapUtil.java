package com.edusunsoft.transport.orataro.utils;

import android.animation.ValueAnimator;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;
import java.util.List;

public class MapUtil {

    public static void animateMarker(Marker myMarkerLOC, final LatLng location) {
        if (myMarkerLOC != null) {
            final LatLngInterpolatorV2 latLngInterpolator = new LatLngInterpolatorV2.LinearFixed();
            ValueAnimator valueAnimator = new ValueAnimator();
            final LatLng startPosition = myMarkerLOC.getPosition();

            final float startRotation = myMarkerLOC.getRotation();
            final float bearing = bearingBetweenLocations(startPosition, location);
            final float angle = 180 - Math.abs(Math.abs(startRotation - bearing) - 180);
            final float right = WhichWayToTurn(startRotation, bearing);

            valueAnimator.addUpdateListener(animation -> {
                try {
                    float v = animation.getAnimatedFraction();
                    LatLng newPosition = latLngInterpolator.interpolate(v, startPosition, location);
                    float rotation = startRotation + right * v * angle;
                    myMarkerLOC.setRotation(rotation);
                    myMarkerLOC.setPosition(newPosition);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
            valueAnimator.setFloatValues(0, 1);
            valueAnimator.setDuration(300);
            valueAnimator.start();
        }
    }

    private static float bearingBetweenLocations(LatLng latLng1, LatLng latLng2) {
        double PI = 3.14159;
        double lat1 = latLng1.latitude * PI / 180;
        double long1 = latLng1.longitude * PI / 180;
        double lat2 = latLng2.latitude * PI / 180;
        double long2 = latLng2.longitude * PI / 180;

        double dLon = (long2 - long1);

        double y = Math.sin(dLon) * Math.cos(lat2);
        double x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1)
                * Math.cos(lat2) * Math.cos(dLon);

        float brng = (float) Math.atan2(y, x);

        brng = (float) Math.toDegrees(brng);
        brng = (brng + 360) % 360;

        return brng;
    }

    public static List<LatLng> decodePoly(String encoded) {
        List<LatLng> poly = new ArrayList<>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {

            int b;
            int shift = 0;
            int result = 0;

            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);

            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;

            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);

            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)), (((double) lng / 1E5)));

            poly.add(p);
        }
        return poly;
    }

    private static float WhichWayToTurn(float currentDirection, float targetDirection) {
        float diff = targetDirection - currentDirection;
        if (Math.abs(diff) == 0) {
            return 0;
        }
        if (diff > 180) {
            return -1;
        } else {
            return 1;
        }
    }

    /*private static LatLng toLatLng(final Location location) {
        return new LatLng(location.getLatitude(), location.getLongitude());
    }*/
}

interface LatLngInterpolatorV2 {
    LatLng interpolate(float fraction, LatLng a, LatLng b);

    /*class Linear implements LatLngInterpolatorV2 {
        @Override
        public LatLng interpolate(float fraction, LatLng a, LatLng b) {
            double lat = (b.latitude - a.latitude) * fraction + a.latitude;
            double lng = (b.longitude - a.longitude) * fraction + a.longitude;
            return new LatLng(lat, lng);
        }
    }*/

    class LinearFixed implements LatLngInterpolatorV2 {
        @Override
        public LatLng interpolate(float fraction, LatLng a, LatLng b) {
            double lat = (b.latitude - a.latitude) * fraction + a.latitude;
            double lngDelta = b.longitude - a.longitude;
            // Take the shortest path across the 180th meridian.
            if (Math.abs(lngDelta) > 180) {
                lngDelta -= Math.signum(lngDelta) * 360;
            }
            double lng = lngDelta * fraction + a.longitude;
            return new LatLng(lat, lng);
        }
    }
}