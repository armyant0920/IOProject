package DBLab;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;

public class Lab4 {


    public static void main(String[] args) {
        File file = new File("C:\\Users\\user\\OneDrive\\桌面\\example.csv");
        StringBuilder sb = new StringBuilder();
        try (Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=project", "sa", "armyant0920");
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("select*from Spot");
             FileWriter fr = new FileWriter(file);
        ) {
            ResultSetMetaData metaData = rs.getMetaData();
            for (int i = 1; i <= metaData.getColumnCount(); i++) {

                fr.write(metaData.getColumnName(i) + ",");

            }
            fr.write("\n");
            while (rs.next()) {
                for (int i = 1; i <= metaData.getColumnCount(); i++) {

                    fr.write(rs.getString(metaData.getColumnName(i)) + ",");
                }
                fr.write("\n");


            }
            System.out.println("完成輸出");


        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
