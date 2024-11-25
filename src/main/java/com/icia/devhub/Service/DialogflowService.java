package com.icia.devhub.Service;

import com.google.cloud.dialogflow.v2.DetectIntentRequest;
import com.google.cloud.dialogflow.v2.DetectIntentResponse;
import com.google.cloud.dialogflow.v2.QueryInput;
import com.google.cloud.dialogflow.v2.QueryResult;
import com.google.cloud.dialogflow.v2.SessionsClient;
import com.google.cloud.dialogflow.v2.TextInput;
import com.icia.devhub.config.DialogflowConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.UUID;

@Service
public class DialogflowService {

    private final SessionsClient sessionsClient;
    private final String projectId;

    @Autowired
    public DialogflowService(SessionsClient sessionsClient, DialogflowConfig dialogflowConfig) {
        this.sessionsClient = sessionsClient;
        this.projectId = dialogflowConfig.getProjectId();
    }

    public String detectIntentTexts(String text) {
        String sessionId = UUID.randomUUID().toString();
        String responseText = "";

        try {
            // Set the session
            String session = String.format("projects/%s/locations/global/agent/sessions/%s", projectId, sessionId);

            // Set the text and language code for the query
            TextInput.Builder textInput = TextInput.newBuilder().setText(text).setLanguageCode("ko");

            // Build the query with the TextInput
            QueryInput queryInput = QueryInput.newBuilder().setText(textInput).build();

            // Build the DetectIntentRequest
            DetectIntentRequest request = DetectIntentRequest.newBuilder()
                    .setSession(session)
                    .setQueryInput(queryInput)
                    .build();

            // Performs the detect intent request
            DetectIntentResponse response = sessionsClient.detectIntent(request);

            // Display the query result
            QueryResult queryResult = response.getQueryResult();
            responseText = queryResult.getFulfillmentText();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return responseText;
    }
}
