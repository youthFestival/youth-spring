package com.youth.server.dto;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * API 응답 데이터를 담는 클래스

 */
@Data
public class Payload {
    private final Map<String, String> data = new HashMap<>();

    public Map<String,String> put(String key, String value){
        data.put(key,value);
        return data;
    }

    public String get(String key){
        return data.getOrDefault(key,"");
    }
}
