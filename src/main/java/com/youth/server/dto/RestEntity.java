package com.youth.server.dto;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder(builderMethodName = "hiddenBuilder")
public class RestEntity extends ResponseEntity<Map<String, Object>> {
    private HttpStatus status;
    private Map<String, Object> payload;

    public RestEntity(Map<String, Object> body, HttpStatusCode status) {
        super(body, status);
    }

    public static RestEntityBuilder builder() {
        return new RestEntityBuilder();
    }

    public ResponseEntity<Map<String, Object>> toResponseEntity(){
        return new ResponseEntity<>(this.payload, this.status);
    }

    public static class RestEntityBuilder {

        private HttpStatus status = HttpStatus.OK;
        private Map<String, Object> payload = new HashMap<>();

        RestEntityBuilder() {
            this.payload.put("message", "기본 메세지 입니다.");
        }

        /**
         * 반환할 payload에 데이터를 추가합니다.
         * @param key
         * @param value
         * @return CustomResponseHashMapEntityBuilder
         */
        public RestEntityBuilder put(String key, Object value) {
            this.payload.put(key, value);
            return this;
        }

        /**
         * 반환할 payload에 담길 message를 추가합니다.
         * @param message
         * @return CustomResponseHashMapEntityBuilder
         */
        public RestEntityBuilder message(String message){
            this.payload.put("message", message);
            return this;
        }

        /**
         * 상태를 설정합니다.
         * @param status
         * @return CustomResponseHashMapEntityBuilder
         */
        public RestEntityBuilder status(HttpStatus status) {
            this.status = status;
            return this;
        }

        public RestEntity build() {
            return new RestEntity(this.payload,this.status);
        }
    }
}
