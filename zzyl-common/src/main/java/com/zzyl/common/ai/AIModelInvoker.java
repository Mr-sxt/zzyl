package com.zzyl.common.ai;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.ResponseFormatJsonObject;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionCreateParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AIModelInvoker {
    @Autowired
    private TongYiAIProperties tongYiAIProperties;

    public String TongYiInvoker(String prompt){
        OpenAIClient client = OpenAIOkHttpClient.builder()
                .apiKey(System.getenv(tongYiAIProperties.getApiKey()))
                .baseUrl(tongYiAIProperties.getBaseUrl())
                .build();

        ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
                .addUserMessage(prompt)
                .model(tongYiAIProperties.getModel())
                .build();


            ChatCompletion chatCompletion = client.chat().completions().create(params);
            return chatCompletion.choices().get(0).message().content().orElseGet(()->"");

    }

}
