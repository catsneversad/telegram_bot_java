package controllers;

import domain.AccessToken;
import domain.UserLoginData;
import domain.models.User;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import services.AuthorizationService;
import services.UserService;
import services.interfaces.IAuthorizationService;
import services.interfaces.IUserService;

import java.util.ArrayList;
import java.util.List;


public class BotController extends TelegramLongPollingBot {
    private final IUserService userService = new UserService();
    private final IAuthorizationService authService = new AuthorizationService();
    private final String TOKEN = "922444330:AAGvjm9Fyh9BJ9iSeelDbVvUN32rU7o9Sus";
    private final String USERNAME = "DimkeksBot";

    private UserLoginData user = new UserLoginData();
    private User currentUser = null;

    SendMessage sendMessage = new SendMessage();
    static int cnt = 0;

    @Override
    public void onUpdateReceived(Update update) {
        Message receivedMessage = update.getMessage();
        sendMessage.setChatId(receivedMessage.getChatId());
        String name = receivedMessage.getFrom().getFirstName();
        String surname = receivedMessage.getFrom().getLastName();
        surname = (surname != null ? surname : "");

        Long chatId = receivedMessage.getChatId();
        Integer messageId = receivedMessage.getMessageId();

        if (receivedMessage.getText().equals("/start")) {
            sendMsg(sendMessage.setText("If you don't know how to use please go to /help"));
        }

        if (receivedMessage.getText().equals("/help")) {
            sendMsg(sendMessage.setText("To sign in you have to confirm your login and password \n commands:\n" +
                    "/set_username <<your login>>\n" +
                    "/set_password <<your password>>\n\n\n" +
                    "press /login after settings\n" +
                    "press /data is you wanna see your data"));
        }

        if (receivedMessage.getText().startsWith("/set_username")) {
            String text = receivedMessage.getText();
            if (text.length() == 13) {
                sendMsg(sendMessage.setText("Please enter non-empty username"));
            } else {
                String[] takenmsg = text.split(" ");
                if (takenmsg[1].isEmpty()) {
                    sendMsg(sendMessage.setText("Please enter non-empty username"));
                } else {
                    user.setUsername(takenmsg[1]);
                    sendMsg(sendMessage.setText("Username was successfully updated"));
                }
            }
        }


        if (receivedMessage.getText().startsWith("/set_password")) {
            String text = receivedMessage.getText();
            if (text.length() == 13) {
                sendMsg(sendMessage.setText("Please enter non-empty username"));
            } else {
                String[] takenmsg = text.split(" ");
                if (takenmsg[1].isEmpty()) {
                    sendMsg(sendMessage.setText("Please enter non-empty username"));
                } else {
                    user.setPassword(takenmsg[1]);
                    sendMsg(sendMessage.setText("Password was successfully updated"));
                    sendMsg(sendMessage.setText(deleteMsg(chatId, messageId)));
                }
            }
        }

        if (receivedMessage.getText().equals("/login")) {
            UserLoginData userLoginData = new UserLoginData();
            userLoginData.setUsername(user.getUsername());
            userLoginData.setPassword(user.getPassword());
            if (login(userLoginData)) {
                sendMsg(sendMessage.setText("Successful login"));
            } else {
                sendMsg(sendMessage.setText("Incorrect username or password!"));
            }
        }

        if (receivedMessage.getText().equals("/data")) {
            if (currentUser == null) {
                sendMsg(sendMessage.setText("To sign in you have to confirm your login and password \n commands:\n" +
                        "/set_username <<your login>>\n" +
                        "/set_password <<your password>>"));
            } else {
                sendMsg(sendMessage.setText(currentUser.toString()));
            }
        }
    }

    public String deleteMsg (long chatId, Integer messageId) {
        DeleteMessage delete = new DeleteMessage();
        delete.setChatId(chatId);
        delete.setMessageId(messageId);
        try {
            execute(delete);
            return "Your password was hide";
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean login(UserLoginData data) {
        try {
            AccessToken token = authService.authenticateUser(data);
            currentUser = userService.getUserByUsername(data.getUsername());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void sendMsg(SendMessage sendMessage) {
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }



    @Override
    public String getBotUsername() {
        return USERNAME;
    }

    @Override
    public String getBotToken() {
        return TOKEN;
    }
}