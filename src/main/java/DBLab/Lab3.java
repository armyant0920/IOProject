package DBLab;

import java.sql.*;
import java.util.Scanner;

public class Lab3 {

    public static void main(String[] args) {
        System.out.println("\n2");
//        query1();
        //query2();
    }

    private static void query1(){

        try (Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=project", "sa", "armyant0920");
             Statement st=conn.createStatement();

             PreparedStatement ps=conn.

                     prepareStatement("select name,? from errortable");
        )
        {
            Scanner sc=new Scanner(System.in);
            System.out.println("input condition");
            String condition=sc.nextLine();
//                  condition="["+condition+"]";

            ps.setString(1,"["+condition+"]");
            try(ResultSet rs=ps.executeQuery()){

                while(rs.next()){



                    System.out.println(rs.getString("name")+rs.getString(2));

                }
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private static void query2(){

        try (Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=project", "sa", "manager");
             Statement st=conn.createStatement();
             PreparedStatement ps= conn.prepareStatement("select *  from errortable");

        )
        {
            try(ResultSet rs=ps.executeQuery()){
                ResultSetMetaData rsmd=rs.getMetaData();
                for(int i=1;i<=rsmd.getColumnCount();i++){

                    System.out.print(rsmd.getColumnName(i)+"\t");
                }
                System.out.println();
                while(rs.next()){
                    for(int j=1;j<=rsmd.getColumnCount();j++){
                        System.out.print(rs.getString(j)+"\t");

                    }
                    System.out.println();

                }
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
