package DBTool;


import Json.SpotPojo;


import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;

import static javax.swing.JOptionPane.*;

/**
 * CURD
 * 插入、讀取、查詢、刪除
 */

class ProjectGUI {
    private static JFrame frame;// 定義窗體
    private static JMenuBar bar;// 定義選單欄
    private static JTextArea textOutput;
    private static JMenu importMenu, sqlMenu, editMenu;// 定義"檔案"和"子選單"選單
    private static JMenuItem openCSV, openJSON, openURL, clearAll;// 定義條目"退出"和"子條目"選單項
    private static JMenuItem DB_Connect;
    private static JButton exportBtn;
    private static JMenuItem fontItem;//格式Menu的項目
    private static FileDialog openDia, saveDia;// 定義"開啟 儲存"對話方塊
    private static File file;//定義檔案
    private String current;
    private int fontSize = 12;//預設值12
    private static final int Default_size[] = {12, 16, 20, 24, 28, 32};
    private static final Dimension DEFAULT_SIZE = new Dimension(600, 500);
    private static ArrayList<SpotPojo> spotList;
    private static final String WARNING = "警告";
    private static final String INFORMATION = "訊息";
    private static final String WARNING_TIP = "無可輸出的資料,請重新匯入有效的檔案來源";
    private static final String INSERT_COMPLETE = "匯入資料完畢";
    private static final String CSV_COMPLETE = "CSV檔讀取完成";
    private static final String JSON_COMPLETE = "JSON檔讀取完成";
    private static final String URL_COMPLETE = "URL讀取完成";
    private static final String EXCEPTION_FILEMISS = "系統找不到指定的檔案";
    private static final String INPUT_HINT = "請輸入目標網址";
    private static final String CLEAR_HINT = "確定清除目前資料?";
    private static final String CLEAR_COMPLETE = "資料清除完畢";
    private static final String INSERT_CHECK="將目前資料匯入資料庫?";

    private static final String FRAME_NAME = "專案GUI";
    private static final String IMPORT_MENU = "匯入資料";
    private static final String CSV_FILE = "CSV檔";
    private static final String JSON_FILE = "JSON檔";
    private static final String URL_File = "URL";
    private static final String CLEAR = "清除";
    private static final String DB_TEXT = "連接資料庫";

    ProjectGUI() {
        init();
    }

    //圖形使用者介面組建初始化
    public void init() {
        spotList = new ArrayList<>();
        frame = new JFrame(FRAME_NAME);//建立窗體物件

        frame.setVisible(true); //設定窗體可見
        bar = new JMenuBar();// 建立選單欄

        textOutput = new JTextArea();// 建立文字域
        textOutput.setLineWrap(true);//自動換行設定
        textOutput.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(textOutput);

        importMenu = new JMenu(IMPORT_MENU);// 建立"匯入"選單

        openCSV = new JMenuItem(CSV_FILE);//建立"CSV"選單項
        openJSON = new JMenuItem(JSON_FILE);//建立"JSON"選單項
        openURL = new JMenuItem(URL_File);//URL
        clearAll = new JMenuItem(CLEAR);


        DB_Connect = new JMenuItem("連接資料庫");//設定DB連線參數

        importMenu.add(openCSV);//將 CSV 項新增到 檔案 選單上
        importMenu.add(openJSON);//將 JSON 項新增到 檔案 選單上
        importMenu.add(openURL);//將 URL 項新增到 檔案 選單上
        importMenu.add(clearAll);//清除

        bar.add(importMenu);//將 匯入 新增到選單欄上
        sqlMenu = new JMenu("資料庫操作");
        sqlMenu.add(DB_Connect);
        editMenu = new JMenu("格式");

        fontItem = new JMenuItem("字體大小");


        editMenu.add(fontItem);//增加字體大小調整

        bar.add(sqlMenu);


        bar.add(editMenu);


        frame.setJMenuBar(bar);//將此窗體的選單欄設定為指定的選單欄.
        openDia = new FileDialog(frame, "Open", FileDialog.LOAD);
        saveDia = new FileDialog(frame, "Save", FileDialog.SAVE);
        //frame.add(textOutput);// 將文字域新增到窗體內
        exportBtn = new JButton("匯入資料庫");
        frame.add(exportBtn, BorderLayout.SOUTH);
        frame.add(scrollPane);
        textOutput.requestFocus();//Kevin:focus後文字才有正常顯示,調查中
        textOutput.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                int len = e.getLength();
                int len2 = textOutput.getText().length();
                String s = textOutput.getText().substring(len2 - len, len2);
                System.out.println("change:" + s);//+e.getLength()
                current = textOutput.getText();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                System.out.println("change:" + e.getLength());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                System.out.println("change:" + e.getLength());
            }
        });

        textOutput.getDocument().putProperty("text", current);

        myEvent();//載入事件處理

        frame.pack();
        frame.setBounds(300, 100, 600, 500);//設定窗體位置和大小

    }

    private void myEvent() {
        //這邊重複增加大量ActionListener好像有點太冗長,
        //之後可能會共用一個,但by case執行不同動作
        openCSV.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                openDia.setVisible(true); //顯得開啟檔案對話方塊
                String dirpath = openDia.getDirectory();//獲取開啟檔案路徑並且儲存
                String fileName = openDia.getFile();//獲取檔名
                System.out.println(dirpath + fileName);

                if (dirpath == null || fileName == null) //判斷路徑和檔案是否為空
                {
                    return;
                } else {
                    textOutput.setText(null); //檔案不為空 清除原來檔案內容

                }
                file = new File(dirpath, fileName); //建立新的路徑和名稱
                try {
                    BufferedReader bufr = new BufferedReader(new FileReader(file));//嘗試從檔案中讀東西
                    String line = null; //變數字串初始化為空
                    bufr.readLine();//跳過第一行
                    while ((line = bufr.readLine()) != null) {
                        String data[] = line.split(",");


                        SpotPojo spot = new SpotPojo(
                                data[0],//caseID
                                data[1],//caseName
                                data[2],//registerDate
                                data[3],//belongCity
                                Double.parseDouble(data[4]),//longitude
                                Double.parseDouble(data[5]),//latitude
                                data[6],//representImage
                                data[7]//briefDescribe
                        );
                        spotList.add(spot);
                        textOutput.append(line + "\r\n");//顯示每一行內容
                    }

                    bufr.close();//關閉檔案+
                    current = textOutput.getText();
                    JOptionPane.showMessageDialog(frame, CSV_COMPLETE, INFORMATION, INFORMATION_MESSAGE);


                } catch (FileNotFoundException e1) {
                    JOptionPane.showMessageDialog(null, e1.getClass(), EXCEPTION_FILEMISS, JOptionPane.ERROR_MESSAGE);
                    System.out.println(EXCEPTION_FILEMISS);
                    e1.printStackTrace(); // 丟擲檔案路徑找不到異常

                } catch (IOException e2) {
                    e2.printStackTrace();// 丟擲IO異常
                }
            }
        });

        openJSON.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                openDia.setVisible(true); //顯示開啟檔案對話方塊
                String dirpath = openDia.getDirectory();//獲取開啟檔案路徑並且儲存
                String fileName = openDia.getFile();//獲取檔名
                System.out.println(dirpath + fileName);

                if (dirpath == null || fileName == null) //判斷路徑和檔案是否為空
                {
                    return;
                } else {
                    textOutput.setText(null); //檔案不為空 清除原來檔案內容

                }
                file = new File(dirpath, fileName); //建立新的路徑和名稱
                try (BufferedReader bufr = new BufferedReader(new FileReader(file));) {

                    String line = null;
                    StringBuilder sb = new StringBuilder();
                    while ((line = bufr.readLine()) != null) {
                        sb.append(line);
                        textOutput.append(line + "\r\n");//顯示每一行內容
                    }
                    bufr.close();//關閉檔案
                    current = textOutput.getText();
                    spotList = readFromJson(sb.toString());
                    JOptionPane.showMessageDialog(frame, JSON_COMPLETE, INFORMATION, INFORMATION_MESSAGE);


                } catch (FileNotFoundException e1) {
                    JOptionPane.showMessageDialog(null, e1.getClass(), EXCEPTION_FILEMISS, JOptionPane.ERROR_MESSAGE);

                    e1.printStackTrace(); // 丟擲檔案路徑找不到異常

                } catch (IOException e2) {
                    e2.printStackTrace();// 丟擲IO異常
                }


            }
        });

        openURL.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                String inputValue = JOptionPane.showInputDialog(INPUT_HINT);
                URL url = null;
                try {
                    url = new URL(inputValue);

                } catch (MalformedURLException malformedURLException) {
                    malformedURLException.printStackTrace();
                }
                try (InputStream is = url.openStream();
                     BufferedReader br = new BufferedReader(new InputStreamReader(is));
                ) {
                    StringBuilder sb = new StringBuilder();
                    while (br.ready()) {
                        String line = br.readLine(); //變數字串初始化為空
                        sb.append(line);
                        System.out.println(line);
                        textOutput.append(line + "\r\n");
                    }
                    String jsonData = sb.toString();
                    spotList = readFromJson(jsonData);
                    JOptionPane.showMessageDialog(frame, URL_COMPLETE, INFORMATION, INFORMATION_MESSAGE);


                } catch (MalformedURLException malformedURLException) {
                    System.err.println("url路徑錯誤");
                    malformedURLException.printStackTrace();
                } catch (IOException ioException) {
                    System.err.println("IO錯誤");
                    ioException.printStackTrace();
                }
            }
        });

        clearAll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int check = showConfirmDialog(frame,CLEAR_HINT , WARNING,
                        YES_NO_OPTION, WARNING_MESSAGE);
                if (check == YES_OPTION) {
                    spotList.clear();
                    textOutput.setText("");
                }

            }
        });

        fontItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                ArrayList<Integer> sizeList = new ArrayList<>();
                for (int i : Default_size) {
                    sizeList.add(i);

                }
                Object selectedValue = JOptionPane.showInputDialog(null, "請選擇字體大小", "更改文字大小",
                        JOptionPane.INFORMATION_MESSAGE, null, sizeList.toArray(), sizeList.get(0));
                if (selectedValue != null) {
                    fontSize = (Integer) selectedValue;
                    textOutput.setFont(new Font("", Font.PLAIN, fontSize));
                    frame.pack();
                    frame.repaint();
                    frame.setSize(DEFAULT_SIZE);

//                    frame.setBounds(300, 100, 600, 500);//設定窗體位置和大小

                }
            }
        });

        exportBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (spotList != null && spotList.size()>0) {
                    int check = showConfirmDialog(frame, INSERT_CHECK, WARNING,
                            YES_NO_OPTION, WARNING_MESSAGE);
                    if (check == YES_OPTION) {
                        insertToDataBasae(spotList);
                    }

                } else {
                    JOptionPane.showMessageDialog(frame, WARNING_TIP, WARNING, ERROR_MESSAGE);
                }

            }
        });
        DB_Connect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DBRecorder.getInstance();
                AddressDialog dialog=new AddressDialog(frame,false);
                dialog.setSize(500, 250);
                dialog.setLocationRelativeTo(null);
                dialog.setVisible(true);
            }
        });

        //窗體關閉監聽
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

    }

    private static ArrayList readFromJson(String s) {
        ArrayList list = new ArrayList();
        JSONArray arr = new JSONArray(s);
        for (int i = 0; i < arr.length(); i++) {
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


        }
        return list;
    }


    private static void insertToDataBasae(ArrayList<SpotPojo> list) {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=project", "sa", "armyant0920");

             //ResultSet rs=st.executeQuery("");
             PreparedStatement ps = conn.prepareStatement("insert into Spot values(?,?,?,?,?,?,?,?) ")
        ) {
            for (int i = 0; i < list.size(); i++) {
                ps.setString(1, list.get(i).getCaseId());
                ps.setString(2, list.get(i).getCaseName());
                ps.setDate(3, list.get(i).getSqlDate());
                ps.setString(4, list.get(i).getBelongCity());
                ps.setDouble(5, list.get(i).getLongitude());
                ps.setDouble(6, list.get(i).getLatitude());
                ps.setString(7, list.get(i).getRepresentImage());
                ps.setString(8, list.get(i).getBriefDescribe());
                ps.addBatch();
                ps.clearParameters();

            }
            ps.executeBatch();

            ps.clearBatch();

        } catch (SQLException e) {
            e.printStackTrace();
        }


        JOptionPane.showMessageDialog(frame, INSERT_COMPLETE, INFORMATION, INFORMATION_MESSAGE);

    }

    public static void main(String[] args) {


        new ProjectGUI();
        System.out.println("┴┬┴┬／￣＼＿／￣＼\r\n" +
                "┬┴┬┴▏　　▏▔▔▔▔＼\r\n" +
                "┴┬┴／＼　／　　　　　　﹨\r\n" +
                "┬┴∕　　　　　　　／　　　）\r\n" +
                "┴┬▏　　　　　　　　●　　▏\r\n" +
                "┬┴▏　　　　　　　　　　　▔█◤\r\n" +
                "┴◢██◣　　　　　　 ＼＿＿／\r\n" +
                "┬█████◣　　　　　　　／\r\n" +
                "┴█████████████◣\r\n" +
                "◢██████████████▆▄\r\n" +
                "◢████████████");
        System.out.println("===============程式已啟動==================");
    }


}
