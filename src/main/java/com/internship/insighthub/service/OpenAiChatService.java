package com.internship.insighthub.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class OpenAiChatService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${openai.model:gpt-4o-mini}")
    private String model;

    public OpenAiChatService() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(10_000);
        factory.setReadTimeout(60_000);
        this.restTemplate = new RestTemplate(factory);
    }

    public String ask(String systemPrompt, String userMessage) {
        String url = "https://api.openai.com/v1/responses";

        String sys = (systemPrompt == null) ? "" : systemPrompt.trim();
        String usr = (userMessage == null) ? "" : userMessage.trim();

        String input;
        if (!sys.isBlank()) {
            input = "You are a helpful tutor.\n\n" + sys + "\n\nUser: " + usr;
        } else {
            input = "You are a helpful tutor.\n\nUser: " + usr;
        }

        ResponsesRequest body = new ResponsesRequest(model, input);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        HttpEntity<ResponsesRequest> entity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> res = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            String rawJson = res.getBody();
            if (rawJson == null || rawJson.isBlank()) return "Empty AI response.";

            return extractTextFromResponses(rawJson);

        } catch (HttpClientErrorException e) {
            return "OpenAI error: " + e.getStatusCode() + " - " + safe(e.getResponseBodyAsString());
        } catch (RestClientException e) {
            return "OpenAI call failed: " + e.getMessage();
        }
    }

    private String extractTextFromResponses(String rawJson) {
        try {
            JsonNode root = objectMapper.readTree(rawJson);

            JsonNode output = root.path("output");
            if (output.isArray() && output.size() > 0) {
                JsonNode content = output.get(0).path("content");
                if (content.isArray() && content.size() > 0) {
                    JsonNode textNode = content.get(0).path("text");
                    if (!textNode.isMissingNode() && !textNode.asText().isBlank()) {
                        return textNode.asText().trim();
                    }
                }
            }

            JsonNode outputText = root.path("output_text");
            if (!outputText.isMissingNode() && !outputText.asText().isBlank()) {
                return outputText.asText().trim();
            }

            return "No text in AI response (unexpected format).";

        } catch (Exception ex) {
            return "Failed to parse AI response: " + ex.getMessage();
        }
    }

    private String safe(String s) {
        if (s == null) return "";
        return s.length() > 600 ? s.substring(0, 600) + "..." : s;
    }

    public record ResponsesRequest(String model, String input) {}
}