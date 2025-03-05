package com.example.outsourcing.domain.common.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class SearchKeywordRankingService {

    private final RedisTemplate<String, Object> redisTemplate;

    public void increaseCount(String keyword){

        LocalDateTime now = LocalDateTime.now();
        int hour = now.getHour();
        int minute = now.getMinute();
        // 15분 단위로 변환 (00, 15, 30, 45)
        int min = (minute / 15) * 15;
        String key = "search_ranking:" + String.format("%02d:%02d", hour,min);

        redisTemplate.opsForZSet().incrementScore(key, keyword, 1);
        //1시간후에 만료되도록 설정
        redisTemplate.expire(key, 1, TimeUnit.DAYS);
    }

    public double getScore(String keyword){
        LocalDateTime now = LocalDateTime.now();
        int hour = now.getHour();
        int minute = now.getMinute();
        // 15분 단위로 변환 (00, 15, 30, 45)
        int min = (minute / 15) * 15;
        String key = "search_ranking:" + String.format("%02d:%02d", hour,min);
        ZSetOperations<String, Object> zSetOperations = redisTemplate.opsForZSet();
        Double searchRanking = zSetOperations.score(key, keyword);
        return searchRanking == null ? 0 : searchRanking;
    }

    public List<String> getTop10SearchKeyword(){
        LocalDateTime now = LocalDateTime.now();
        int hour = now.getHour();
        int minute = now.getMinute();
        // 15분 단위로 변환 (00, 15, 30, 45)
        int min = (minute / 15) * 15;
        String key = "search_ranking:" + String.format("%02d:%02d", hour,min);
        Set<Object> searchRanking = redisTemplate.opsForZSet().reverseRange(key, 0, 9);

        return searchRanking.stream()
                .map(v -> (String) v)
                .toList();
    }
}
