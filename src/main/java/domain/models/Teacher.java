package domain.models;

import java.sql.Date;
import java.util.ArrayList;

public class Teacher extends User{
    private ArrayList<String> ownClass;
    private String discipline;

    public Teacher(String discipline) {
        this.discipline = discipline;
    }

    public Teacher(String name, String surname, String username, String password, Date birthday, String role, String discipline) {
        super(name, surname, username, password, birthday, role);
        this.discipline = discipline;
    }

    public Teacher(long id, String name, String surname, String username, String password, Date birthday, String role, String discipline) {
        super(id, name, surname, username, password, birthday, role);
        this.discipline = discipline;
    }

    public Teacher(long id, String name, String surname, String username, Date birthday, String role, String discipline) {
        super(id, name, surname, username, birthday, role);
        this.discipline = discipline;
    }

    public ArrayList<String> getOwnClass() {
        return ownClass;
    }

    public void setOwnClass(ArrayList<String> ownClass) {
        this.ownClass = ownClass;
    }

    public String getDiscipline() {
        return discipline;
    }

    public void setDiscipline(String discipline) {
        this.discipline = discipline;
    }
}
