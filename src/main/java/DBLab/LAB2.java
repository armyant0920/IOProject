package DBLab;

import DBTool.DBRecorder;

import java.io.BufferedWriter;
import java.io.IOException;
import java.sql.*;

import static java.sql.DriverManager.getConnection;

public class LAB2 {

    public static void main(String[] args) {


        try (
                Connection connection = DBRecorder.getConnect("10.31.25.132", "1433", "JDBCDB", "sa", "manager");
                PreparedStatement selectPstmt = connection.prepareStatement("select * from spot");
                ResultSet rs = selectPstmt.executeQuery();

                PreparedStatement updatePstmt = connection.prepareStatement("update spot set spot_name = ? where spot_add like ?");

        ) {
            int count =0;
            while (rs.next()) {

                String address = rs.getString("spot_add");
                String name = rs.getString("spot_name");


                if (address.length() >= 3) {//	把地址取出縣市名，將值指定給第2個問號
                    updatePstmt.setString(1, address.substring(0, 3) + "_" + name);//把縣市名 + 景點名稱，將值指定給第1個問號
                    updatePstmt.setString(2, "%" + address.substring(0, 3) + "%");

                    //	System.out.println("景點名稱：" + address.substring(0,3) + "_" + name +"	"+ "景點地址：" + address);

                    updatePstmt.executeUpdate();

                    updatePstmt.clearParameters();//清除先前問號儲存的值
                } else {
                    updatePstmt.setString(1, name);
                    updatePstmt.setString(2, address);


                    updatePstmt.executeUpdate();
                    updatePstmt.clearParameters();//清除先前問號儲存的值
                }
                count++;
                if(count%500==0){
                    System.out.printf("已更新%d筆\n",count);
                }
            }

            System.out.println("更新完成");


            try (	BufferedWriter bw = IOOutputGroup.exportFile("C:\\Java\\spot_update.csv", "UTF8");
                     PreparedStatement selectFinalPstmt = connection.prepareStatement("select spot_name, spot_add from spot");
                     ResultSet finalRs = selectFinalPstmt.executeQuery();

            ) {

                StringBuilder header = new StringBuilder();
                StringBuilder data = new StringBuilder();

                header.append("景點名稱").append(",").append("景點地址").append("\n");

                while(finalRs.next()) {
                    String finalName = finalRs.getString("spot_name");
                    String finalAdd = finalRs.getString("spot_add");

                    data.append(finalName).append(",").append(finalAdd).append("\n");
                }


                bw.write(header.toString() + data.toString());
                System.out.println("寫入csv完成");


            } catch (IOException e) {

                e.printStackTrace();
            }



        } catch (SQLException e) {

            e.printStackTrace();
        }



        //////////////////////////////////////////////////////////////////////////
    }


}
