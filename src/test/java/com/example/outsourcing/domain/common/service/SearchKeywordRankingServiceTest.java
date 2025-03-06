package com.example.outsourcing.domain.common.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.mockito.BDDMockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SearchKeywordRankingServiceTest {

    @InjectMocks
    private SearchKeywordRankingService searchKeywordRankingService;

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private ZSetOperations<String, Object> zSetOperations;

    private final String keyword = "Pizza";
    private final String key = "search_ranking:12:00";

    @Test
    void increaseCount_키워드검색횟수증가성공() {
        // given
        given(redisTemplate.opsForZSet()).willReturn(zSetOperations);

        // when
        searchKeywordRankingService.increaseCount(keyword);

        // then
        verify(zSetOperations, times(1)).incrementScore(anyString(), eq(keyword), eq(1.0d));
        verify(redisTemplate, times(1)).expire(anyString(), eq(1L), eq(TimeUnit.DAYS));

    }

    @Test
    void getScore_키워드점수조회성공() {
        // given
        given(redisTemplate.opsForZSet()).willReturn(zSetOperations);
        given(zSetOperations.score(anyString(), eq(keyword))).willReturn(5.0);

        // when
        double score = searchKeywordRankingService.getScore(keyword);

        // then
        assertEquals(5.0, score);
    }

    @Test
    void getScore_키워드점수조회점수가없을경우_0반환() {
        // given
        given(redisTemplate.opsForZSet()).willReturn(zSetOperations);
        given(zSetOperations.score(anyString(), eq(keyword))).willReturn(null); // 점수가 없는 경우

        // when
        double score = searchKeywordRankingService.getScore(keyword);

        // then
        assertEquals(0.0, score); // 기본값 0 반환
    }

    @Test
    void getTop10SearchKeyword_상위10개키워드조회성공() {
        // given
        Set<Object> mockResult = Set.of("Pizza", "Burgers", "Sushi");
        given(redisTemplate.opsForZSet()).willReturn(zSetOperations);
        given(zSetOperations.reverseRange(anyString(), eq(0L), eq(9L))).willReturn(mockResult);

        // when
        var topKeywords = searchKeywordRankingService.getTop10SearchKeyword();

        // then
        assertNotNull(topKeywords);
        assertEquals(3, topKeywords.size());
        assertTrue(topKeywords.contains("Pizza"));
        assertTrue(topKeywords.contains("Burgers"));
        assertTrue(topKeywords.contains("Sushi"));
    }
}
