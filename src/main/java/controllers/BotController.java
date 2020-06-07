package controllers;

import domain.AccessToken;
import domain.UserLoginData;
import domain.models.User;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import services.AuthorizationService;
import services.UserService;
import services.interfaces.IAuthorizationService;
import services.interfaces.IUserService;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

public class BotController extends TelegramLongPollingBot {
    private final IUserService userService = new UserService();
    private final IAuthorizationService authService = new AuthorizationService();
    private final String TOKEN = "1251502840:AAFNkleaB9Bs3DkwXraO1lfUifcZN5MTv8Q";
    private final String USERNAME = "aitubotit1908";

    private String username;
    private String password;
    private boolean currentUser=false;



    @Override
    public void onUpdateReceived(Update update) {
        Message receivedMessage = update.getMessage();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(receivedMessage.getChatId());
        String name = receivedMessage.getFrom().getFirstName();
        String surname = receivedMessage.getFrom().getLastName();
        surname = (surname != null ? surname : "");

        if(currentUser==false) {
            if (receivedMessage.getText().equals("/start")) {
                sendMessage.setText("Sign in:" + "\n" + "username:<<your username>>");
            }
            if (receivedMessage.getText().startsWith("username:")) {
                String[] takenmsg = receivedMessage.getText().split(":");
                username = takenmsg[1];
                sendMessage.setText("Write your password:" + "\n" + "password:<<your password>>");
            }
            if(username!=null) {
                if (receivedMessage.getText().startsWith("password:")) {
                    String[] takenmsg = receivedMessage.getText().split(":");
                    password = takenmsg[1];
                    UserLoginData userLoginData = new UserLoginData();
                    userLoginData.setUsername(username);
                    userLoginData.setPassword(password);
                    if (login(userLoginData)) {
                        sendMessage.setText("Congrats!");
                    } else {
                        sendMessage.setText("NotCongrats!");
                    }

                }
            }
        }
        else {
            SendMessage message = new SendMessage();
            if(receivedMessage.getText().equals("/myuserdata")){
                message.setText(userService.getUserByUsername(username).toString());
            }
        }
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            System.out.println(e.getMessage());
        }
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            System.out.println(e.getMessage());
        }
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public boolean login(UserLoginData data) {
        try {
            AccessToken token = authService.authenticateUser(data);
            currentUser=true;
            return true;
        } catch (Exception e) {
            currentUser=false;
            return false;
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
