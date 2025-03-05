package com.example.outsourcing.domain.store.dto.response;


import com.example.outsourcing.domain.store.enums.StoreStatus;

import java.time.LocalDateTime;
import java.time.LocalTime;

public interface StoreResponseForNativeQuery {
        long getId();
        long getUserId();
        long getImageId();
        String getCategoryName();
        String getStoreName();
        StoreStatus getStoreStatus();
        String getStoreNotice();
        String getAddress();
        int getMinOrderPrice();
        LocalTime getOpenTime();
        LocalTime getCloseTime();
        LocalDateTime getCreatedAt();
        LocalDateTime getModifiedAt();
        double getDistance();
        double getRate();
}
