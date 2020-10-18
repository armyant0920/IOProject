package ImageTool;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.io.*;
import java.net.*;

public class DownloadImage {
    private static Image image;

    public static void main(String[] args) throws MalformedURLException {

        URL url = inputURL();

        readURL(url);


    }

    private static URL inputURL() {

        try {

//            String s=JOptionPane.showInputDialog("輸入網址");
            String s="https://data.boch.gov.tw/old_upload/_upload/Assets_new/community/270/photo/";
            s+= URLEncoder.encode("大埔聚落","utf-8");
            s+=".jpg";

            System.out.println(s);
            System.out.println(URLDecoder.decode(s,"utf-8"));
//            s=java.net.URLEncoder.encode(s,"utf-8");//避免網址中出現中文
            return new URL(s);
//            return new URL(JOptionPane.showInputDialog("輸入網址"));
        } catch (MalformedURLException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void download() {

        URL url = null;//"https://graph.facebook.com/AppStore/picture?type=large"
        try {
            url = new URL("https://pgw.udn.com.tw/gw/photo.php?u=https://uc.udn.com.tw/photo/2020/03/01/99/7537150.jpg&s=Y&x=4&y=4&sw=1272&sh=952&sl=W&fw=800&exp=3600&w=930&nt=1");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        try (InputStream is = url.openStream();
             FileOutputStream fos = new FileOutputStream("c:\\java\\a.jpg");

             BufferedInputStream bis = new BufferedInputStream(is);
             BufferedOutputStream bos = new BufferedOutputStream(fos);) {


            byte[] buff = new byte[1024];
            int length;
            while ((length = bis.read(buff)) != -1) {

                bos.write(buff, 0, length);
            }
            System.out.println("寫入OK");


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void readURL(URL url) {

        image = null;

        try {
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestProperty("User-Agent", "Mozilla/5.0");

            image = ImageIO.read(con.getInputStream());
//            image=ImageIO.read(url);

            JFrame frame = new JFrame("image");
            frame.setMinimumSize(new Dimension(500,500));
            JLabel label = new JLabel(new ImageIcon(reSizeIconImage(frame.getSize(),image)));
            frame.getContentPane().add(label, BorderLayout.CENTER);
            frame.pack();
            frame.setVisible(true);
            frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            frame.addWindowStateListener(new WindowStateListener() {
                @Override
                public void windowStateChanged(WindowEvent e) {

                    if(e.getNewState()==6){

                        label.setSize(frame.getSize());
                        label.setIcon(new ImageIcon(reSizeIconImage(frame.getSize(),image)));
                        System.out.println(label.getSize());

                    }

                }
            });


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static Image reSizeIconImage(Dimension d,Image image){


        return image.getScaledInstance(d.width,d.height,Image.SCALE_SMOOTH);

    }



}


