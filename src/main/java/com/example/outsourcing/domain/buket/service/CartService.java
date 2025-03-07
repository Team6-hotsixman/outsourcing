package com.example.outsourcing.domain.buket.service;

import com.example.outsourcing.domain.buket.dto.request.MenuRequest;
import com.example.outsourcing.domain.buket.dto.response.CartResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class CartService {
    private final RedisTemplate<String, Object> redisTemplate;

    public List<CartResponseDto> addMenu(long userId, MenuRequest addMenuRequest){
        String key = "cart:"+userId;
        String hashKey = createHashKey(addMenuRequest);
        redisTemplate.opsForHash().increment(key, hashKey, addMenuRequest.getQuantity());
        redisTemplate.expire(key, 3, TimeUnit.DAYS);
        return getCart(userId);
    }

    public List<CartResponseDto> deleteMenu(long userId, MenuRequest addMenuRequest){
        String key = "cart:"+userId;
        String hashKey = createHashKey(addMenuRequest);
        Integer quantity = Integer.parseInt(redisTemplate.opsForHash().get(key, hashKey).toString());
        if(quantity > 0){
            int dcreasedQuantity = quantity - addMenuRequest.getQuantity();
            if(dcreasedQuantity <= 0){//수량이 0이하면 장바구니에서 삭제
                redisTemplate.opsForHash().delete(key, hashKey);
            }else{
                redisTemplate.opsForHash().put(key, hashKey, dcreasedQuantity);
            }
        }
        return getCart(userId);
    }

    public List<CartResponseDto> getCart(long userId){
        String key = "cart:"+userId;
        Map<Object, Object> entries = redisTemplate.opsForHash().entries(key);
        List<CartResponseDto> cart = new ArrayList<>();
        for (Object k : entries.keySet()) {
            cart.add(CartResponseDto.fromEntry(k, entries.get(k)));
        }
        return cart;
    }

    public String createHashKey(MenuRequest addMenuRequest) {
        StringBuilder key = new StringBuilder(String.valueOf(addMenuRequest.getMenuId()));
        key.append(":"+addMenuRequest.getMenuName());
        if (addMenuRequest.getOptions() != null && !addMenuRequest.getOptions().isEmpty()) {
            key.append(":[").append(String.join(",", addMenuRequest.getOptions().stream()
                    .map(String::valueOf)
                    .toList()))
                    .append("]");
        }
        return key.toString();
    }

}
