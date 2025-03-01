package com.example.outsourcing.domain.common.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class SearchKeywordRankingService {

    private final RedisTemplate<String, Object> redisTemplate;

    public void increaseCount(String keyword){
        redisTemplate.opsForZSet().incrementScore("search_ranking", keyword, 1);
    }

    public List<String> getTop10SearchKeyword(){
        Set<Object> searchRanking = redisTemplate.opsForZSet().reverseRange("search_ranking", 0, 9);

        return searchRanking.stream()
                .map(v -> (String) v)
                .toList();
    }

    @Scheduled(fixedRate = 5*60*1000)
    public void decreaseCount(){
        log.info("decreaseCount ranking service");
        Set<ZSetOperations.TypedTuple<Object>> searchRanking = redisTemplate.opsForZSet().rangeWithScores("search_ranking", 0, -1);
        if(searchRanking == null) return;
        for (ZSetOperations.TypedTuple<Object> typedTuple : searchRanking) {
            Double score = typedTuple.getScore();
            String keyword = (String) typedTuple.getValue();
            if(score == null || keyword == null) continue;

            double decreasedScore = score * 0.9;
            if(decreasedScore < 1){
                redisTemplate.opsForZSet().remove("search_ranking", keyword);
            }else {
                redisTemplate.opsForZSet().add("search_ranking", keyword, decreasedScore);
            }

        }
    }

}
