package com.careerpath.interfaces.api;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleNotFound_returns404Response() {
        // given
        NoSuchElementException ex = new NoSuchElementException("Not found");

        // when
        ResponseEntity<Map<String, Object>> response = handler.handleNotFound(ex);

        // then
        assertThat(response.getStatusCode().value()).isEqualTo(404);
        assertThat(response.getBody()).containsEntry("status", 404);
        assertThat(response.getBody()).containsEntry("error", "Not Found");
        assertThat(response.getBody()).containsEntry("message", "Not found");
    }

    @Test
    void handleBadRequest_returns400Response() {
        // given
        IllegalArgumentException ex = new IllegalArgumentException("Bad request");

        // when
        ResponseEntity<Map<String, Object>> response = handler.handleBadRequest(ex);

        // then
        assertThat(response.getStatusCode().value()).isEqualTo(400);
        assertThat(response.getBody()).containsEntry("status", 400);
        assertThat(response.getBody()).containsEntry("error", "Bad Request");
        assertThat(response.getBody()).containsEntry("message", "Bad request");
    }

    @Test
    void handleServerError_returns500Response_andGenericMessage() {
        // given
        Exception ex = new RuntimeException("Boom");

        // when
        ResponseEntity<Map<String, Object>> response = handler.handleServerError(ex);

        // then
        assertThat(response.getStatusCode().value()).isEqualTo(500);
        assertThat(response.getBody()).containsEntry("status", 500);
        assertThat(response.getBody()).containsEntry("error", "Internal Server Error");
        assertThat(response.getBody()).containsEntry(
                "message",
                "Something went wrong on our side."
        );
    }
}
