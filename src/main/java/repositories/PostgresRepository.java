package repositories;

import repositories.interfaces.IDBRepository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgresRepository implements IDBRepository {
    @Override
    public Connection getConnection() {
        try {
            String conStr = "jdbc:postgresql://localhost:5432/TelegramBotDb";
            Connection con = DriverManager.getConnection(conStr, "postgres", "lbvfi2281");
            return con;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
