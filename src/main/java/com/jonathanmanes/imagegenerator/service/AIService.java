package com.jonathanmanes.imagegenerator.service;

import com.theokanning.openai.image.CreateImageRequest;
import com.theokanning.openai.service.OpenAiService;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AIService {

    @Resource(name = "getOpenAiService")
    private final OpenAiService openAiService;

    public String generatePicture(String prompt) {
        CreateImageRequest createImageRequest = CreateImageRequest.builder()
                .prompt(prompt)
                .size("1024x1024")
                .n(1)
                .build();

        return openAiService.createImage(createImageRequest).getData().get(0).getUrl();
    }
}
