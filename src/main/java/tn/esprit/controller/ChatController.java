package tn.esprit.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import tn.esprit.api.XAiApiService;

public class ChatController {
    @FXML private TextField messageInput;
    @FXML private TextArea chatArea;

    @FXML
    private void initialize() {
        // Listen for Enter key press in the TextField
        messageInput.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                sendMessage(); // Call sendMessage when Enter is pressed
                event.consume(); // Prevent new line in TextField
            }
        });
    }

    @FXML
    private void sendMessage() {
        String userInput = messageInput.getText();
        if (!userInput.isEmpty()) {
            chatArea.appendText("You: " + userInput + "\n\n");
            String response = XAiApiService.sendMessage(userInput);
            chatArea.appendText("AI: " + response + "\n");
            messageInput.clear();
        }
    }


}