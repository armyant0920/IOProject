package TestLab;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateFormat {

    public static void main(String[] args) {

        SimpleDateFormat sd=new SimpleDateFormat("YYYY/MM/DD");

        String s="2020/10/12";

        try {
            System.out.println("test1: "+sd.parse(s));
        } catch (ParseException e) {
            e.printStackTrace();
        }


        Date d=new Date(2020,10,15);
        String d1s="2020-10-15";
        Date d2=new Date();
        System.out.println(d);
        System.out.println(sd.format(d));


        System.out.println(sd.format(d2));

        Date today=new Date();
        String todayS=String.format(Locale.TAIWAN,"%tc",today);
        System.out.println("今天是: "+todayS);

        try (Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=project", "sa", "manager");
             Statement st=conn.createStatement();
             PreparedStatement ps=conn.prepareStatement("insert into demotable values(?,?,?,?) ")
        ) {
            ps.setInt(1,5);
            ps.setString(2,"EEE");
            ps.setDate(3, new java.sql.Date((2020-1900),10,15));
            ps.setString(4,"測試JAVA匯入日期2");
            ps.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }




    }


}