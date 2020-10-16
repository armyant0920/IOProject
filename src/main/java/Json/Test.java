package Json;


import DBTool.DBAccess;
import com.google.gson.JsonArray;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.*;
import java.util.ArrayList;

public class Test {

    private static ArrayList<MyItem> myList;


    public static void main(String[] args) {
        myList = new ArrayList<>();
        System.out.println("更新資料");
        resetData(myList);
        for(int i=0;i<myList.size();i++){

            System.out.println(myList.get(i));
        }

        insertTable();


    }

    private static void resetData(ArrayList<MyItem> list) {


        try {
            URL url = new URL("https://data.boch.gov.tw/data/opendata/v2/assetsCase/4.2.json");
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
            for (int i = 0; i < arr.length(); i++) {
//                Object o=arr.getJSONObject(i);

                JSONObject obj = arr.getJSONObject(i);
//                System.out.printf("index:%d %s\n",i,obj);
                String caseId = obj.optString("caseId").replaceAll("'","");
                String caseName = obj.optString("caseName");
                String registerReason = obj.optString("registerReason");
                String belongCity = obj.optString("belongCity");
                String representImage = obj.optString("representImage");
                String historyDevelopment = obj.optString("historyDevelopment");
                list.add(new MyItem(caseId, caseName, registerReason, belongCity, representImage, historyDevelopment));

               /* try{JSONArray temp=obj.getJSONArray("computeType");
                    for(int j=0;j<temp.length();j++){
                        JSONObject jb=temp.getJSONObject(i);
                        System.out.println(jb);

                    }
                }
                catch (JSONException e){e.printStackTrace();}*/


//                System.out.printf("index:%d name:%s\n",i,obj.getString("recorder"));


            }
            System.out.println("end");


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("資料載入完畢");
    }


    private static void insertTable() {
        Connection conn = DBAccess.openDB();

        try (

                PreparedStatement ps = conn.prepareStatement("insert into relics values(?,?,?,?,?,?) ")

        ) {
            for(int i=0;i<myList.size();i++){
                ps.setString(1,myList.get(i).getCaseId());
                ps.setString(2,myList.get(i).getCaseName());
                ps.setString(3,myList.get(i).getRegisterReason());
                ps.setString(4,myList.get(i).getBelongCity());
                ps.setString(5,myList.get(i).getRepresentImage());
                ps.setString(6,myList.get(i).getHistoryDevelopment());
                ps.addBatch();
                ps.clearParameters();

            }
                ps.executeBatch();
            /*int updated[]=ps.executeBatch();
            for(int i=0;i<updated.length;i++){

                System.out.println("result "+i+" ="+updated[i]);
            }*/
            ps.clearBatch();
            conn.close();




        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("資料匯入資料庫完畢");

    }


}
