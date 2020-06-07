package controllers;

import domain.AccessToken;
import domain.UserLoginData;
import domain.models.Student;
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
    private final String TOKEN = "922444330:AAGvjm9Fyh9BJ9iSeelDbVvUN32rU7o9Sus";
    private final String USERNAME = "DimkeksBot";

    private User user = new User();
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
                user.setUsername(takenmsg[1]);
                sendMessage.setText("Write your password:" + "\n" + "password:<<your password>>");
            }
            if(user.getUsername()!=null) {
                if (receivedMessage.getText().startsWith("password:")) {
                    String[] takenmsg = receivedMessage.getText().split(":");
                    user.setPassword(takenmsg[1]);
                    UserLoginData userLoginData = new UserLoginData();
                    userLoginData.setUsername(user.getUsername());
                    userLoginData.setPassword(user.getPassword());
                    if (login(userLoginData)) {
                        sendMessage.setText("Congrats!");
                    } else {
                        sendMessage.setText("NotCongrats!");
                    }

                }
            }
        }
        else {
            if(receivedMessage.getText().equals("/myuserdata")){
                sendMessage.setText(userService.getUserByUsername(user.getUsername()).toString());
            }
            if(receivedMessage.getText().equals("/mygroup")){
                User student = userService.getStudentDataByUsername(user);
                sendMessage.setText(((Student) student).getOwnClass());
            }
            if(receivedMessage.getText().equals("/myfaculty")){
                User student = userService.getStudentDataByUsername(user);
                sendMessage.setText(((Student) student).getOwnFaculty());
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
