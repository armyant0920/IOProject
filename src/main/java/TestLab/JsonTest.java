package TestLab;

import com.google.gson.JsonArray;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Random;

public class JsonTest {

    public static void main(String[] args) {

        String[] data={"A","B","C","d"};
        JSONArray jsonArray=new JSONArray(data);
        System.out.println(jsonArray.toString());
        ArrayList list=new ArrayList();
        list.add("E");
        list.add("F");
        list.add("G");
        list.add("G");
        JSONArray arr=new JSONArray(list);
        System.out.println(arr.toString());
        ArrayList dataList=new ArrayList();
        Random rnd=new Random();
        for(int i=0;i<10;i++){
            Object o[]=new Object[5];
            for(int j=0;j<o.length;j++){
                o[j]=rnd.nextInt(1000);

            }
            dataList.add(o);




        }
      JSONArray arr2=new JSONArray(dataList);

        System.out.println(arr2.toString());



    }
}
