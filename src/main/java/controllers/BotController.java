package controllers;

import domain.AccessToken;
import domain.UserLoginData;
import domain.models.AituInfo;
import domain.models.Student;
import domain.models.User;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import services.AuthorizationService;
import services.UserService;
import services.interfaces.IAuthorizationService;
import services.interfaces.IUserService;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class BotController extends TelegramLongPollingBot {
    private final IUserService userService = new UserService();
    private final IAuthorizationService authService = new AuthorizationService();
    private final String TOKEN = "1196395361:AAHB_vvnGRj2HkjGctut9Kf3vaAy7tSQLb4";
    private final String USERNAME = "aituit1908ver2_bot";

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
                        "/myschedule - give's you schedule\n" +
                        "/aitu - give's info about ASTANA IT UNIVERSITY"));
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

        if (receivedMessage.getText().equals("/aitu")) {
            AituInfo info = new AituInfo();
            sendMsg(sendMessage.setText("Commands:\n" +
                    "/aitu_info - general information about AITU\n" +
                    "/aitu_infrastructure - infrastructure of AITU\n" +
                    "/educational_programs - educational programs\n"));
        }
        if (receivedMessage.getText().equals("/aitu_info")) {
            AituInfo info = new AituInfo();
            sendMsg(sendMessage.setText(info.getInfo()));
        }
        if (receivedMessage.getText().equals("/mygroup")) {
            User student = userService.getStudentDataByUsername(currentUser);
            sendMsg(sendMessage.setText(((Student) student).getOwnClass()));
        }
        if (receivedMessage.getText().equals("/myfaculty")) {
            User student = userService.getStudentDataByUsername(currentUser);
            sendMsg(sendMessage.setText(((Student) student).getOwnFaculty()));
        }
        if (receivedMessage.getText().equals("/aitu_infrastructure")) {
            AituInfo info = new AituInfo();
            sendMsg(sendMessage.setText(info.getInfrastructure()));
        }
        if (receivedMessage.getText().equals("/educational_programs")) {
            AituInfo info = new AituInfo();
            sendMsg(sendMessage.setText(info.getEducationalPrograms()));
        }

        if (receivedMessage.getText().equals("/mySchedule")) {
            System.out.println(currentUser);
            if (currentUser == null) {
                sendMsg(sendMessage.setText("Firstly SignUp please"));
            } else if (currentUser != null && currentUser.getRole() != "Teacher") {
                String path = "C:\\Users\\magzhan\\Desktop\\schedules.zip";

//                Student studentData = (Student)userService.getStudentDataByUsername(currentUser);
//                System.out.println(studentData);
                //path += studentData.getOwnFaculty() + " "  + studentData.getOwnClass() + ".xlsx";
                System.out.println(path);

                File file = new File(path);

                InputFile a = new InputFile ();
                a.setMedia(file, "zip");

                SendDocument sendDocument = new SendDocument();
                sendDocument.setChatId(chatId);
                sendDocument.setDocument(a);
                sendDocument.setCaption("Hello");

                try {
                    execute(sendDocument);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }

        if (currentUser != null && receivedMessage.getText().equals("/edit_info")) {
            sendMsg(sendMessage.setText("If you wanna change your password send new password in this format: </change_pass: <yourPassword>>"));
        } else if (currentUser != null && receivedMessage.getText().startsWith("/change_pass:")) {
            String text = receivedMessage.getText();
            if (text.length() == 13) {
                sendMsg(sendMessage.setText("Please enter non-empty password"));
            } else {
                String[] takenmsg = text.split(" ");
                if (takenmsg[1].isEmpty()) {
                    sendMsg(sendMessage.setText("Please enter non-empty password"));
                } else {
                    currentUser.setPassword(takenmsg[1]);
                    userService.updateUser(currentUser);
                    sendMsg(sendMessage.setText("new Password has been established"));
                }
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
