//package com.youth.server.util;
//
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.stereotype.Service;
//
//import java.util.concurrent.TimeUnit;
//
//@Service
//public class RedisUtil {
//    private final RedisTemplate<String, Object> redisTemplate;
//
//    public RedisUtil(RedisTemplate<String, Object> redisTemplate) {
//        this.redisTemplate = redisTemplate;
//    }
//
//    public void setData(String key, String value,Long expiredTime){
//        redisTemplate.opsForValue().set(key, value, expiredTime, TimeUnit.MILLISECONDS);
//    }
//
//    public String getData(String key){
//        return (String) redisTemplate.opsForValue().get(key);
//    }
//
//    public boolean checkExistsValue(String value) {
//        return value != null && !value.isEmpty();
//    }
//
//    public void deleteData(String key){
//        redisTemplate.delete(key);
//    }
//}
