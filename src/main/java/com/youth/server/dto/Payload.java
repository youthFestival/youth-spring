package com.youth.server.dto;

import lombok.Data;

import java.util.Map;

@Data
public class Payload {
    private Map<String, Object> data;

    public Object getOrDefault(String key, Object defaultValue) {
        return data.getOrDefault(key, defaultValue);
    }
}
