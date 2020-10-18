package DBTool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static java.sql.DriverManager.getConnection;

public class DBAccess {
    private static String path = "localhost:";//如果不是本地端,就是IP4網路位置 ex:192.168.196.46:
    private static String port = "1433";
    private static String databaseName = "Project";
    private static String user = "sa";
    private static String password = "manager";

    public static String getPath() {
        return path;
    }

    public static void setPath(String path) {
        DBAccess.path = path;
    }

    public static String getPort() {
        return port;
    }

    public static void setPort(String port) {
        DBAccess.port = port;
    }

    public static String getDatabaseName() {
        return databaseName;
    }

    public static void setDatabaseName(String databaseName) {
        DBAccess.databaseName = databaseName;
    }

    public static String getUser() {
        return user;
    }

    public static void setUser(String user) {
        DBAccess.user = user;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        DBAccess.password = password;
    }

    public static void setting(){


    }

    public static Connection openDB() {

    /*    try {
            return  getConnection("jdbc:sqlserver://localhost:1433;databaseName=Project", "sa", "manager");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }*/

        try {


            return  getConnection("jdbc:sqlserver://" + path + port + ";databaseName=" + databaseName, user, password);


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;


    }

    public static void close(Connection conn) {
        if (conn != null) ;
        try {
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


}
