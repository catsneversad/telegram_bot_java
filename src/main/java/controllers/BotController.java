package controllers;

import domain.AccessToken;
import domain.UserLoginData;
import domain.models.Student;
import domain.models.User;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.File;
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
    private User currentUser = new User();
    private boolean isAuthorised=false;

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
            sendMsg(sendMessage.setText("/help - all needed commands"));
        }

        if (receivedMessage.getText().equals("/help")) {
            if (isAuthorised==false) {
                sendMsg(sendMessage.setText("To sign in you have to confirm your login and password \n commands:\n" +
                        "/set_username <<your login>>\n" +
                        "/set_password <<your password>>\n\n\n" +
                        "press /login after settings to sign in\n"));
            } else {
                sendMsg(sendMessage.setText("Commands:\n" +
                        "/mygroup - show's your group\n" +
                        "/myfaculty - show's your faculty\n" +
                        "/myschedule - give's you schedule\n"));
            }
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
                    sendMsg(sendMessage.setText("Username has been established"));
                }
            }
        }


        if (receivedMessage.getText().startsWith("/set_password")) {
            String text = receivedMessage.getText();
            if (text.length() == 13) {
                sendMsg(sendMessage.setText("Please enter non-empty password"));
            } else {
                String[] takenmsg = text.split(" ");
                if (takenmsg[1].isEmpty()) {
                    sendMsg(sendMessage.setText("Please enter non-empty password"));
                } else {
                    user.setPassword(takenmsg[1]);
                    sendMsg(sendMessage.setText("Password has been established"));
                    sendMsg(sendMessage.setText(deleteMsg(chatId, messageId)));
                }
            }
        }

        if (receivedMessage.getText().equals("/login")) {
            UserLoginData userLoginData = new UserLoginData();
            userLoginData.setUsername(user.getUsername());
            userLoginData.setPassword(user.getPassword());
            User studentData = userService.getUserByUsername(user.getUsername());
            if (login(userLoginData)) {
                sendMsg(sendMessage.setText("Welcome " + studentData.getName() + " " + studentData.getSurname()));
                currentUser.setUsername(user.getUsername());
                currentUser.setPassword(user.getPassword());
                isAuthorised=true;
            } else {
                sendMsg(sendMessage.setText("Incorrect username or password!"));
            }
        }

        if (receivedMessage.getText().equals("/myuserdata")) {
            sendMessage.setText(userService.getUserByUsername(user.getUsername()).toString());
        }
        if (receivedMessage.getText().equals("/mygroup")) {
            sendMessage.setText("HELLLOO!");
            User student = userService.getStudentDataByUsername(currentUser);
        }
        if (receivedMessage.getText().equals("/myfaculty")) {
            User student = userService.getStudentDataByUsername(currentUser);
            sendMessage.setText(((Student) student).getOwnFaculty());
        }


        if (receivedMessage.getText().equals("/mySchedule")) {
            if (currentUser == null) {
                sendMsg(sendMessage.setText("Firstly SignUp please"));
            } else if (currentUser.getRole() != "Teacher") {
                String path = "C:\\Users\\didef\\IdeaProjects\\telegram_bot\\src\\main\\java\\DataBase\\schedules\\";
                User studentData = userService.getStudentDataByUsername(currentUser);
                path += ((Student) studentData).getOwnClass() + " " + ((Student) studentData).getOwnFaculty() + " " + ".xlsx";
                SendDocument sendDocumentRequest = new SendDocument();
                sendDocumentRequest.setChatId(receivedMessage.getChatId());
                sendDocumentRequest.setDocument(path);
                sendDocumentRequest.setCaption("This is your schedule");
                sendDocumentRequest.setDocument(path);
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
