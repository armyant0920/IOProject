package Json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

public class JsonTest {

    public static void main(String[] args) {
        Gson gson=new Gson();


        Car car1=new Car(100,"福特","A");
        Car car2=new Car(120,"馬自達","B");
        String jsonString=gson.toJson(car1);
        System.out.printf("沒排版的結果:%s\n",jsonString);
        Gson gsonPretty=new GsonBuilder().setPrettyPrinting().create();
        String prettyJson=gsonPretty.toJson(car2);
        System.out.printf("排版結果:%s\n",prettyJson);
        ArrayList<Car>carList=new ArrayList<>();
        carList.add(car1);
        carList.add(car2);

        String myJSON=gsonPretty.toJson(carList);
        System.out.printf("陣列資料%s\n",myJSON);


    }
    public static class Car{
        private int speed;
        private String brand;
        private String owner;

        public Car(int speed, String brand, String owner) {
            this.speed = speed;
            this.brand = brand;
            this.owner = owner;
        }

        public int getSpeed() {
            return speed;
        }

        public void setSpeed(int speed) {
            this.speed = speed;
        }

        public String getBrand() {
            return brand;
        }

        public void setBrand(String brand) {
            this.brand = brand;
        }

        public String getOwner() {
            return owner;
        }

        public void setOwner(String owner) {
            this.owner = owner;
        }


    }


}
