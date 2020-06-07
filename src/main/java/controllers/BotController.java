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
    private final String TOKEN = "1196395361:AAHB_vvnGRj2HkjGctut9Kf3vaAy7tSQLb4";
    private final String USERNAME = "aitubotit1908ver2";

    private String username = null;
    private String password = null;
    private User currentUser = null;


    private CommandsController commandsController;

    public BotController() {
        this.commandsController = new CommandsController();
    }


    @Override
    public void onUpdateReceived(Update update) {
        Message receivedMessage = update.getMessage();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(receivedMessage.getChatId());
        String name = receivedMessage.getFrom().getFirstName();
        String surname = receivedMessage.getFrom().getLastName();
        surname = (surname != null ? surname : "");

        String userInfo = name + " " + surname;

        String text = receivedMessage.getText();
        Integer messageId = receivedMessage.getMessageId();

        String takenMessage[];

        if (text.equals("/start")) {
            this.sendMsg(this.commandsController.commands(text, receivedMessage.getChatId(), messageId));
        } else if (text.startsWith("/set_username_for_login")) {
            takenMessage = text.split(" ");
            username = takenMessage[1];
            this.sendMsg(sendMessage.setText("Successfully updated."));
        } else if (text.startsWith("/set_password_for_login")) {
            takenMessage = text.split( " ");
            password = takenMessage[1];
            this.sendMsg(sendMessage.setText("Successfully updated."));
        } else if (text.equals("/info")) {
            if (currentUser == null) {
                if (username != null && password != null) {
                    UserLoginData userLoginData = new UserLoginData();
                    userLoginData.setUsername(username);
                    userLoginData.setPassword(password);
                    if (login(userLoginData)) {
                        this.sendMsg(sendMessage.setText("Congrats!\n" + userService.getUserByUsername(username).toString()));
                    } else {
                        this.sendMsg(sendMessage.setText("notCongrats!\n"));
                    }
                } else {
                    System.out.println("HELLO2");
                    this.sendMsg(sendMessage.setText("SORRY U R DUMB xD"));
                }
            } else {
                this.sendMsg(sendMessage.setText(userService.getUserByUsername(username).toString()));
            }
        }
//        if(currentUser==null) {
//            sendMessage.setText("Sign in:" + "\n" + "username:<<your username>>");
//            while (true) {
//                if (!receivedMessage.getText().isEmpty() && receivedMessage.getText().startsWith("username:")) {
//                    String[] takenmsg = receivedMessage.getText().split(":");
//                    username = takenmsg[1];
//                    sendMessage.setText("Write your password:" + "\n" + "password:<<your password>>");
//                }
//                if (username != null) {
//                    if (receivedMessage.getText().startsWith("password:")) {
//                        String[] takenmsg = receivedMessage.getText().split(":");
//                        password = takenmsg[1];
//                        UserLoginData userLoginData = new UserLoginData();
//                        userLoginData.setUsername(username);
//                        userLoginData.setPassword(password);
//                        if (login(userLoginData)) {
//                            sendMessage.setText("Congrats!");
//                            break;
//                        } else {
//                            sendMessage.setText("NotCongrats!");
//                            break;
//                        }
//                    }
//                }
//            }
//        }
//        else {
//            SendMessage message = new SendMessage();
//            if(receivedMessage.getText().equals("/myuserdata")){
//                message.setText(userService.getUserByUsername(username).toString());
//            }
//        }
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

    public void sendMsg(SendMessage sendMessage) {
        try {
            execute(sendMessage);
            return;
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public boolean login(UserLoginData data) {
        try {
            AccessToken token = authService.authenticateUser(data);
            currentUser = authService.getUserByUsername(data.getUsername());
            return true;
        } catch (Exception e) {
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
