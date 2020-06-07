package domain.models;

import java.sql.Date;
import java.util.ArrayList;

public class Student extends User {
    private String ownClass;
    private Faculty ownFaculty;

    public Student(String ownClass, Faculty ownFaculty) {
        this.ownClass = ownClass;
        this.ownFaculty = ownFaculty;
    }

    public Student(String name, String surname, String username, String password, Date birthday, String role, String ownClass, Faculty ownFaculty) {
        super(name, surname, username, password, birthday, role);
        this.ownClass = ownClass;
        this.ownFaculty = ownFaculty;
    }

    public Student(long id, String name, String surname, String username, String password, Date birthday, String role, String ownClass, Faculty ownFaculty) {
        super(id, name, surname, username, password, birthday, role);
        this.ownClass = ownClass;
    }

    public Student(long id, String name, String surname, String username, Date birthday, String role, String ownClass, Faculty ownFaculty) {
        super(id, name, surname, username, birthday, role);
        this.ownClass = ownClass;
    }

    public String getOwnClass() {
        return ownClass;
    }

    public void setOwnClass(String ownClass) { this.ownClass = ownClass; }
}
