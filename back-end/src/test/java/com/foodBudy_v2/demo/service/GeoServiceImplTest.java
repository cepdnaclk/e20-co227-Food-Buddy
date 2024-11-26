package com.foodBudy_v2.demo.service;

import org.assertj.core.api.Assertions;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
class GeoServiceImplTest {
    private GeoService geoService;

    @BeforeEach
    void setUp() {
        geoService = new GeoServiceImpl();
    }

    @Test
    void calculateDistance_test() {
        // arrange
        double baseLat = 7.261444320940439, baseLon = 80.57678163526387;

        double lat1 = 7.267722191447389, lon1 = 80.57599708914255, expectedDistance1 = 0.738;
        double lat2 = 7.265920921192404, lon2 = 80.56427315377036, expectedDistance2 = 1.47;
        double lat3 = 7.293179992033037, lon3 = 80.63675783930738, expectedDistance3 = 7.5;
        double lat4 = 7.284044469763235, lon4 = 80.78349377788307, expectedDistance4 = 22.94;
        double lat5 = 6.9499108184465905, lon5 = 79.99332976063563, expectedDistance5 = 73.11;

        // act
        double calculatedDistance1 = geoService.calculateDistance(baseLat, baseLon, lat1, lon1);
        double calculatedDistance2 = geoService.calculateDistance(baseLat, baseLon, lat2, lon2);
        double calculatedDistance3 = geoService.calculateDistance(baseLat, baseLon, lat3, lon3);
        double calculatedDistance4 = geoService.calculateDistance(baseLat, baseLon, lat4, lon4);
        double calculatedDistance5 = geoService.calculateDistance(baseLat, baseLon, lat5, lon5);

        // assert
        double offset = 0.05;
        Assertions.assertThat(calculatedDistance1).isCloseTo(expectedDistance1, Offset.offset(offset));
        Assertions.assertThat(calculatedDistance2).isCloseTo(expectedDistance2, Offset.offset(offset));
        Assertions.assertThat(calculatedDistance3).isCloseTo(expectedDistance3, Offset.offset(offset));
        Assertions.assertThat(calculatedDistance4).isCloseTo(expectedDistance4, Offset.offset(offset));
        Assertions.assertThat(calculatedDistance5).isCloseTo(expectedDistance5, Offset.offset(offset));
    }
}