package Json;


import DBTool.DBAccess;

import org.json.JSONArray;
import org.json.JSONObject;


import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import java.io.BufferedReader;
import java.io.IOException;

import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.*;
import java.util.ArrayList;

public class Test {

    private static ArrayList<SpotPojo> myList;


    public static void main(String[] args) {
        myList = new ArrayList<>();
        System.out.println("更新資料");
        resetData(myList);
        insertTable();
        readData(myList);
    }


    private static void resetData(ArrayList<SpotPojo> list) {


        try {
            URL url = new URL("https://data.boch.gov.tw/opendata/assetsCase/3.1.json");
            URLConnection conn = url.openConnection();

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String jsonLine = null;
            StringBuilder sb = new StringBuilder();
            while ((jsonLine = br.readLine()) != null) {
                sb.append(jsonLine);

            }
            String jsonData = sb.toString();

            br.close();
            //
            list.clear();
            JSONArray arr = new JSONArray(jsonData);
 /*           int index=0;

PropertyChangeSupport support=new PropertyChangeSupport(index);

support.addPropertyChangeListener(new PropertyChangeListener() {
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        System.out.println("new "+evt.getNewValue());
    }
});*/

            for (int i = 0; i < arr.length(); i++) {
//                Object o=arr.getJSONObject(i);
//                 index=i;
                JSONObject obj = arr.getJSONObject(i);
//                System.out.printf("index:%d %s\n",i,obj);
                String caseId = obj.optString("caseId").replaceAll("'", "");
                String caseName = obj.optString("caseName");
                String registerDate = obj.optString("registerDate");
                String belongCity = obj.optString("belongCity");
                double longitude = Double.parseDouble(String.valueOf(obj.optDouble("longitude")));
                double latitude = Double.parseDouble(String.valueOf(obj.optDouble("latitude")));
                String representImage = obj.optString("representImage");
                String briefDescribe = obj.optString("briefDescribe");
                list.add(new SpotPojo(
                        caseId,
                        caseName,
                        registerDate,
                        belongCity,
                        longitude,
                        latitude,
                        representImage,
                        briefDescribe));
                System.out.printf("index:%d,progress:%.2f%%\n", i, (float) i * 100 / arr.length());
               /* try{JSONArray temp=obj.getJSONArray("computeType");
                    for(int j=0;j<temp.length();j++){
                        JSONObject jb=temp.getJSONObject(i);
                        System.out.println(jb);

                    }
                }
                catch (JSONException e){e.printStackTrace();}*/


//                System.out.printf("index:%d name:%s\n",i,obj.getString("recorder"));

            }
            System.out.println("資料載入完畢");


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private class ProgressListener implements PropertyChangeListener {
        private ProgressListener() {
        }

        private ProgressListener(JProgressBar bar) {

        }


        @Override
        public void propertyChange(PropertyChangeEvent evt) {

        }
    }


    private static void insertTable() {

        try (Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=project", "sa", "armyant0920");
             Statement st = conn.createStatement();
             //ResultSet rs=st.executeQuery("");
             PreparedStatement ps = conn.prepareStatement("insert into Spot values(?,?,?,?,?,?,?,?) ")
        ) {
            for (int i = 0; i < myList.size(); i++) {
                ps.setString(1, myList.get(i).getCaseId());
                ps.setString(2, myList.get(i).getCaseName());
                ps.setDate(3, myList.get(i).getSqlDate());
                ps.setString(4, myList.get(i).getBelongCity());
                ps.setDouble(5, myList.get(i).getLongitude());
                ps.setDouble(6, myList.get(i).getLatitude());
                ps.setString(7, myList.get(i).getRepresentImage());
                ps.setString(8, myList.get(i).getBriefDescribe());
                ps.addBatch();
                ps.clearParameters();

            }
            ps.executeBatch();
            /*int updated[]=ps.executeBatch();
            for(int i=0;i<updated.length;i++){

                System.out.println("result "+i+" ="+updated[i]);
            }*/
            ps.clearBatch();



        } catch (SQLException e) {
            e.printStackTrace();
        }


        System.out.println("資料匯入資料庫完畢");

    }

    private static void readData(ArrayList list) {
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i));
        }
    }


}
