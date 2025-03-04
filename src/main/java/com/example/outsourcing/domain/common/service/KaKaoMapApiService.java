package com.example.outsourcing.domain.common.service;

import com.example.outsourcing.domain.common.dto.KaKaoMapResponse;
import com.example.outsourcing.domain.common.dto.LatLng;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Slf4j
@Service
public class KaKaoMapApiService {

    private final GeometryFactory geometryFactory;

    RestClient restClient;
    private static final String URL = "https://dapi.kakao.com/v2/local/search/address.json";
    @Value("${KAKAO_API_KEY}")
    private String apiKey;

    public KaKaoMapApiService(GeometryFactory geometryFactory) {
        this.geometryFactory = geometryFactory;
        restClient = RestClient.builder().build();
    }

    public LatLng getLatLng(String address){
        String url = URL + "?query=" + address;

        KaKaoMapResponse response = restClient.get()
                .uri(url)
                .header(HttpHeaders.ACCEPT, "application/json")
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .header("Authorization", "KakaoAK " + apiKey)
                .retrieve()
                .body(KaKaoMapResponse.class);
        if(response != null){
            log.debug("Kakao response: {}", response);
            return response.getDocuments().get(0);
        }

        throw new RuntimeException("잘못된 주소입니다.");
    }

    public Point getPoint(String address){
        LatLng latLng = getLatLng(address);
        Point point = geometryFactory.createPoint(new Coordinate(latLng.getLongitude(), latLng.getLatitude()));
        point.setSRID(4326);
        return point;
    }

}
