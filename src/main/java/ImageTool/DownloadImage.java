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
//        download(url);
//       downloadOnce(url);
       readURL(url);


    }
    public static void showImage(){


    }

    private static URL inputURL() {
        try {
            String s=JOptionPane.showInputDialog("輸入網址");
//            String s="https://data.boch.gov.tw/old_upload/_upload/Assets_new/cultural_tourism/226/photo/";
//            s+= URLEncoder.encode("八芝蘭番仔井","utf-8");
//            s+=".JPG";

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
    private static void downloadOnce(URL url){

        try (InputStream is = url.openStream();
             FileOutputStream fos = new FileOutputStream("c:\\java\\a.jpg");

             BufferedInputStream bis = new BufferedInputStream(is);
             BufferedOutputStream bos = new BufferedOutputStream(fos);) {
            /* byte b;
            while (bis.read() != -1) {
                bos.write(bis.readAllBytes());
            }*/
            bos.write(bis.readAllBytes());
            System.out.println("寫入OK");


        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private static void download(URL url) {



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

    public static void showURLImage(URL url){
        try {
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestProperty("User-Agent", "Mozilla/5.0");

            image = ImageIO.read(con.getInputStream());
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

                    if(e.getNewState()==0){//應該是回復初始狀態?
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
                    if(e.getNewState()==0){//應該是回復初始狀態?
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


