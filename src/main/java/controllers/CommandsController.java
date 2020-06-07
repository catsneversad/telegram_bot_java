package controllers;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class CommandsController {
    SendMessage sendMessage = new SendMessage();
    public SendMessage commands (String text, Long chatId, Integer messageId) {
        sendMessage.setChatId(chatId);

        if (text.equals("/start")) {
            sendMessage.setText ("Hello\nPress /help to see the all commands");
            sendMessage.setParseMode("Markdown");
        } else if (text.equals("/help")) {

        }
        return sendMessage;
    }
}
