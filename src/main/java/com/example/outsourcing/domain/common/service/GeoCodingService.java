package com.example.outsourcing.domain.common.service;

import com.example.outsourcing.domain.common.dto.KaKaoLatLngResponse;
import com.example.outsourcing.domain.common.dto.LatLng;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Slf4j
@Service
public class GeoCodingService {

    RestClient restClient;
    private static final String URL = "https://dapi.kakao.com/v2/local/search/address.json";
    @Value("${KAKAO_API_KEY}")
    private String apiKey;

    public GeoCodingService() {
        restClient = RestClient.builder().build();
    }

    public LatLng getLatLng(String address){
        String url = URL + "?query=" + address;

        KaKaoLatLngResponse response = restClient.get()
                .uri(url)
                .header(HttpHeaders.ACCEPT, "application/json")
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .header("Authorization", "KakaoAK " + apiKey)
                .retrieve()
                .body(KaKaoLatLngResponse.class);
        if(response != null){
            log.debug("Kakao response: {}", response);
            return response.getDocuments().get(0);
        }

        throw new RuntimeException("잘못된 주소입니다.");
    }

}
