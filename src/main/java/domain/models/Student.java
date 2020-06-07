package domain.models;

import java.sql.Date;
import java.util.ArrayList;

public class Student extends User {
    private String ownClass;
    private String ownFaculty;

    public Student(String ownClass, String ownFaculty) {
        this.ownClass = ownClass;
        this.ownFaculty = ownFaculty;
    }

    public Student(String name, String surname, String username, String password, Date birthday, String role, String ownClass, String ownFaculty) {
        super(name, surname, username, password, birthday, role);
        this.ownClass = ownClass;
        this.ownFaculty = ownFaculty;
    }

    public Student(long id, String name, String surname, String username, String password, Date birthday, String role, String ownClass, String ownFaculty) {
        super(id, name, surname, username, password, birthday, role);
        this.ownClass = ownClass;
        this.ownFaculty = ownFaculty;
    }

    public Student(long id, String name, String surname, String username, Date birthday, String role, String ownClass, String ownFaculty) {
        super(id, name, surname, username, birthday, role);
        this.ownClass = ownClass;
        this.ownFaculty = ownFaculty;
    }


    public String getOwnFaculty() {
        return ownFaculty;
    }

    public void setOwnFaculty(String ownFaculty) {
        this.ownFaculty = ownFaculty;
    }

    public String getOwnClass() {
        return ownClass;
    }

    public void setOwnClass(String ownClass) { this.ownClass = ownClass; }
}
