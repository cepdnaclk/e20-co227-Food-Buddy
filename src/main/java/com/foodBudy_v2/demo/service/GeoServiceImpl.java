package com.foodBudy_v2.demo.service;

import org.springframework.stereotype.Service;

@Service
public class GeoServiceImpl implements GeoService{
    public static final double EARTH_RADIUS = 6371;

    /**
     * Equirectangular Distance Approximation
     * @param lat1
     * @param lon1
     * @param lat2
     * @param lon2
     * @return
     */
    @Override
    public double calculateDistance(double lat1, double lon1, double lat2, double lon2){
        double lat1Rad = Math.toRadians(lat1);
        double lat2Rad = Math.toRadians(lat2);
        double lon1Rad = Math.toRadians(lon1);
        double lon2Rad = Math.toRadians(lon2);

        double x = (lon2Rad - lon1Rad) * Math.cos((lat1Rad + lat2Rad) / 2);
        double y = (lat2Rad - lat1Rad);
        double distance = Math.sqrt(x * x + y * y) * EARTH_RADIUS;

        return distance;
    }
}