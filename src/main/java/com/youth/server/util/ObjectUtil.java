package com.youth.server.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class ObjectUtil {


    public static Map<?,?> toMap(Object obj) {
        return new ObjectMapper().convertValue(obj,Map.class);
    }
}
