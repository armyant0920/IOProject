package DBLab;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 任意TABLE匯入CSV構思
 */
public class Lab5 {


    public static void main(String[] args) {

        String table="demotable";
        File file=new File("C:\\Users\\user\\OneDrive\\桌面\\demo.csv");

        insertFromCSV(table,file);
        System.out.println("執行完成");

    }

    public static void insertFromCSV(String table, File csv)  {

        //參數1.table name
        //參數2.多個欄位名稱,
        //參數3.多個值
//原則上2與3的配合下,只要資料正確,即使CSV檔資料順序與資料庫不一致也可以匯入

        String sql = "insert into " + table + " (";

        String values = (" values(");

        try (
                BufferedReader br = new BufferedReader(new FileReader(csv));
        ) {
            //處理CSV的標題,拼成完整SQL語句

            //讀CSV的第一行,預設用半形","區隔，注意碰到不按牌理出牌的格式有死掉的可能
            String columnNames[] = br.readLine().split(",");
            for (int i = 0; i < columnNames.length; i++) {
                sql += columnNames[i];
                values+="?";

                if (i < columnNames.length - 1) {
                    sql += ",";
                    values+=",";
                }else{
                    sql+=")";
                    values+=")";
                }
            }


            sql+=values;
            System.out.println(sql);
            try(Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=project", "sa", "armyant0920");
                PreparedStatement ps = conn.prepareStatement(sql);) {

                String line = null;
                while ((line = br.readLine()) != null) {
                    String content[]=line.split(",");
                    for(int i=1;i<=columnNames.length;i++){
                        ps.setString(i,content[i-1]);


                    }
                    ps.addBatch();
                    ps.clearParameters();
                }
                int[] result=ps.executeBatch();
                ps.clearBatch();
                for(int i=0;i<result.length;i++){

                    System.out.printf("index %d result=%s\n",i,result[i]);

                }

            }


        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
