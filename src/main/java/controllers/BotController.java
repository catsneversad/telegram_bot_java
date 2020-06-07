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

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public boolean login(UserLoginData data) {
        try {
            AccessToken token = authService.authenticateUser(data);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        Message receivedMessage = update.getMessage();
        String inputText = update.getMessage().getText();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(receivedMessage.getChatId());

        String name = receivedMessage.getFrom().getFirstName();
        name = (name != null ? name : "");
        String surname = receivedMessage.getFrom().getLastName();
        surname = (surname != null ? surname : "");
        String userInfo = name + " " + surname;


    
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
