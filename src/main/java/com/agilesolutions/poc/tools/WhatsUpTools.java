package com.agilesolutions.poc.tools;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class WhatsUpTools {

    @Value("${twilio.account.sid}")
    private String accountSid;

    @Value("${twilio.auth.token}")
    private String authToken;

    @Value("${twilio.whatsapp.number}")
    private String fromWhatsAppNumber;

    public WhatsUpTools() {
        // Initialize Twilio with account credentials
        Twilio.init(accountSid, authToken);
    }

    @Tool(description = "Send out WhatsUp message")
    public String sendWhatsAppMessage(@ToolParam(description = "Generative response") String messageBody) {
        // Send a message via Twilio's API
        Message message = Message.creator(
                        new PhoneNumber("whatsapp:" + fromWhatsAppNumber),   // Recipient's WhatsApp number
                        new PhoneNumber(fromWhatsAppNumber), // Twilio WhatsApp number
                        messageBody)                         // Message body
                .create();

        return message.getSid(); // Return message SID to track status
    }
}