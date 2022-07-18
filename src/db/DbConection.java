package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConection {
    private static DbConection dbConnection =null;
    private Connection connection;
    private DbConection () throws ClassNotFoundException, SQLException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        connection  = DriverManager.getConnection("jdbc:mysql://localhost:3306/keels_supermarket", "root", "1234");
    }
    public static DbConection  getInstance() throws SQLException, ClassNotFoundException {
        if(dbConnection==null){
            dbConnection=new DbConection ();
        }
        return dbConnection;
    }
    public Connection getConnection(){
        return connection;//DBConnection.getInstance().getConnection()
    }

}

