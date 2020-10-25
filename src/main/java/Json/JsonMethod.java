package Json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 將Gson提供的功能整理後調用的工具類
 */
public class JsonMethod {
    /**
     * 特化給專案使用的方法
     * @param jsonData
     * @return
     */
    public static ArrayList Project_getJsonData(String jsonData){
        ArrayList list=new ArrayList();
        JSONArray arr=new JSONArray(jsonData);
        for(int i=0;i<arr.length();i++){

            JSONObject obj=arr.getJSONObject(i);
            String caseId = obj.optString("caseId").replaceAll("'","");
            String caseName = obj.optString("caseName");
            String registerReason = obj.optString("registerReason");
            String belongCity = obj.optString("belongCity");
            String representImage = obj.optString("representImage");
            String historyDevelopment = obj.optString("historyDevelopment");
            list.add(new MyItem(caseId, caseName, registerReason, belongCity, representImage, historyDevelopment));

        }
        return  list;

    }


    /**
     *
     * @param list
     * @return
     */
    public static String parseToJson(ArrayList list){

        Gson gson=new GsonBuilder().setPrettyPrinting().create();

        return gson.toJson(list);


    }
}
