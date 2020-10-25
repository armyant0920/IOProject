package Tool;

import java.io.*;

import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.showMessageDialog;

public class Reading {

    public static void main(String[] args) {
    progress1();




    }

    private static void progress1(){

        File in_file = new File("C:\\Users\\user\\OneDrive\\桌面\\AWT.csv"); //建立新的路徑和名稱
        File out_file = new File("C:\\Users\\user\\OneDrive\\桌面\\Result.csv"); //建立新的路徑和名稱




        try (
                FileReader fr = new FileReader(in_file);
                FileWriter out=new FileWriter(out_file);

        ) {
            int c;

            while((c=fr.read())!=-1){
                out.write(c);

            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void progress2(){

        File in_file = new File("C:\\Users\\user\\OneDrive\\桌面\\Result.csv"); //建立新的路徑和名稱
        File out_file = new File("C:\\Users\\user\\OneDrive\\桌面\\BBB.csv"); //建立新的路徑和名稱

        StringBuilder sb = new StringBuilder();


        try (
                FileReader fr = new FileReader(in_file);
                FileWriter out=new FileWriter(out_file);

        ) {
            int c;

            while((c=fr.read())!=-1){
                out.write(c);

            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }



    }
}
