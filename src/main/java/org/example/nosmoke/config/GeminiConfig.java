package org.example.nosmoke.config;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.vertexai.VertexAiGeminiChatModel;
import lombok.Getter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class GeminiConfig {
    @Bean
    public ChatLanguageModel chatLanguageModel() {
        // Langchain4j 제공 Gemini 모델 빌더
        return VertexAiGeminiChatModel.builder().build();
    }
}
