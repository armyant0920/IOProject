package DBTool;



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
import java.util.ArrayList;

    /**
     *CURD
     * 插入、讀取、查詢、刪除
     */

    class DBGUI {

        private JFrame frame;// 定義窗體
        private JMenuBar bar;// 定義選單欄
        private JTextArea textOutput;
        private JMenu fileMenu,sqlMenu, editMenu;// 定義"檔案"和"子選單"選單
        private JMenuItem openCSV, openJSON,openURL;// 定義條目"退出"和"子條目"選單項
        private JMenuItem DB_set,DB_insert,DB_update,DB_query;

        private JMenuItem fontItem;//格式Menu的項目
        private FileDialog openDia, saveDia;// 定義"開啟 儲存"對話方塊
        private File file;//定義檔案
        private JPanel panel;
        private int fontStyle;
        private String current;
        private int fontSize = 12;//預設值12
        private static int Default_size[] = {12, 16, 20, 24, 28, 32};
        private static final Dimension DEFAULT_SIZE = new Dimension(600, 500);

        DBGUI() {
            init();
        }

        //圖形使用者介面組建初始化
        public void init() {
            frame = new JFrame("專案工具");//建立窗體物件

            frame.setVisible(true); //設定窗體可見
            bar = new JMenuBar();// 建立選單欄

            textOutput = new JTextArea();// 建立文字域
            textOutput.setLineWrap(true);//自動換行設定

            JScrollPane scrollPane=new JScrollPane(textOutput);

            fileMenu = new JMenu("匯入資料");// 建立"匯入"選單

            openCSV = new JMenuItem("CSV檔");//建立"CSV"選單項
            openJSON=new JMenuItem("JSON檔");//建立"JSON"選單項
            openURL = new JMenuItem("開啟URL");//console


           DB_set=new JMenuItem("設定參數");//設定DB連線參數
            DB_query=new JMenuItem("查詢Table");//新增查詢選項

//            saveItem = new JMenuItem("儲存");//建立"儲存"選單項
//            saveOtherItem = new JMenuItem("另存");
//            closeItem = new JMenuItem("退出");//建立“退出"選單項

            fileMenu.add(openCSV);//將 開啟 選單項新增到 檔案 選單上
            fileMenu.add(openURL);//將 開啟 選單項新增到 檔案 選單上
//            fileMenu.add(saveItem);//將 儲存 選單項新增到 檔案 選單上
//            fileMenu.add(saveOtherItem);
//            fileMenu.add(closeItem);//將 退出 選單項新增到 檔案 選單上

            bar.add(fileMenu);//將檔案新增到選單欄上
            sqlMenu=new JMenu("資料庫操作");
            editMenu = new JMenu("格式");

            fontItem = new JMenuItem("字體大小");


            editMenu.add(fontItem);//增加字體大小調整

            bar.add(sqlMenu);


            bar.add(editMenu);


            frame.setJMenuBar(bar);//將此窗體的選單欄設定為指定的選單欄.
            openDia = new FileDialog(frame, "Open", FileDialog.LOAD);
            saveDia = new FileDialog(frame, "Save", FileDialog.SAVE);
            //frame.add(textOutput);// 將文字域新增到窗體內
            frame.add(scrollPane);
            textOutput.requestFocus();//Kevin:focus後文字才有正常顯示,調查中
            textOutput.getDocument().addDocumentListener(new DocumentListener() {

                @Override
                public void insertUpdate(DocumentEvent e) {
                    int len=e.getLength();
                    int len2=textOutput.getText().length();
                    String s=textOutput.getText().substring(len2-len,len2);
                    System.out.println("change:"+s);//+e.getLength()
                    current=textOutput.getText();
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    System.out.println("change:"+e.getLength());
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    System.out.println("change:"+e.getLength());
                }
            });

            textOutput.getDocument().putProperty("text",current);


        /*textOutput.addPropertyChangeListener("", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {

            }
        });*/
        /*textOutput.addTextListener(new TextListener() {
            @Override
            public void textValueChanged(TextEvent e) {
//                TextArea t=(TextArea) e.getSource();
//                t.select(t.getCaretPosition() - 1, t.getCaretPosition());
//                String s = t.getSelectedText();
//                System.out.print(s);
//                t.setCaretPosition(t.getCaretPosition() + 1);
                current = textOutput.getText();

                if (current.length() == 0)
                    current = null;
            }
        });*/



            myEvent();//載入事件處理

            frame.pack();
            frame.setBounds(300, 100, 600, 500);//設定窗體位置和大小

        }

        private void readTest(File file) {

            try (BufferedReader bufr = new BufferedReader(new FileReader(file))) {

                ;//嘗試從檔案中讀東西
                String line = null; //變數字串初始化為空

                while ((line = bufr.readLine()) != null) {
                    System.out.println(line);//顯示每一行內容

                }
                System.out.println(bufr.ready());


            } catch (FileNotFoundException e) {

                JOptionPane.showMessageDialog(null, e.getClass(), "系統找不到指定的檔案。", JOptionPane.ERROR_MESSAGE);
                System.out.println("系統找不到指定的檔案。");
                e.printStackTrace(); // 丟擲檔案路徑找不到異常

            } catch (IOException e) {
                e.printStackTrace();
            }


        }


        private void myEvent() {


            //開啟選單項監聽
            /**
             * 這邊重複增加大量ActionListener好像有點太冗長,
             * 之後可能會共用一個,但by case執行不同動作
             */
            openCSV.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    openDia.setVisible(true); //顯得開啟檔案對話方塊
                    String dirpath = openDia.getDirectory();//獲取開啟檔案路徑並且儲存
                    String fileName = openDia.getFile();//獲取檔名並且儲存
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
                        while ((line = bufr.readLine()) != null) {
                            textOutput.append(line + "\r\n");
                        } //顯示每一行內容
//                    System.out.println(textOutput.getText());
                        bufr.close();//關閉檔案+
                        current = textOutput.getText();

                    } catch (FileNotFoundException e1) {
                        JOptionPane.showMessageDialog(null, e1.getClass(), "系統找不到指定的檔案。", JOptionPane.ERROR_MESSAGE);
                        System.out.println("系統找不到指定的檔案。");
                        e1.printStackTrace(); // 丟擲檔案路徑找不到異常

                    } catch (IOException e2) {
                        e2.printStackTrace();// 丟擲IO異常
                    }
                }
            });

            openURL.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    String inputValue = JOptionPane.showInputDialog("請輸入目標網址");
                    try {
                        URL url = new URL(inputValue);
                        InputStream is = url.openStream();
                        BufferedReader br = new BufferedReader(new InputStreamReader(is));
                        while (br.ready()) {
                            String line = br.readLine(); //變數字串初始化為空

                            System.out.println(line);
                            textOutput.append(line + "\r\n");
                        }
                        br.close();

                    } catch (MalformedURLException malformedURLException) {
                        System.err.println("url路徑錯誤");
                        malformedURLException.printStackTrace();
                    } catch (IOException ioException) {
                        System.err.println("IO錯誤");
                        ioException.printStackTrace();
                    }
                }
            });

           /* saveItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (file == null) {
                        saveDia.setVisible(true);//顯示儲存檔案對話方塊
                        String dirpath = saveDia.getDirectory();//獲取儲存檔案路徑並儲存到字串中。
                        String fileName = saveDia.getFile() + ".txt";////獲取打儲存檔名稱並儲存到字串中

                        if (dirpath == null || fileName == null)//判斷路徑和檔案是否為空
                            return;//空操作
                        else
                            file = new File(dirpath, fileName);//檔案不為空，新建一個路徑和名稱
                    }
                    try {
                        BufferedWriter bufw = new BufferedWriter(new FileWriter(file));
                        String text = textOutput.getText();//獲取文字內容
                        bufw.write(text);//將獲取文字內容寫入到字元輸出流
                        bufw.close();//關閉檔案
                    } catch (IOException e1) {
                        e1.printStackTrace();//丟擲IO異常
                    }
                }
            });*/

            /*saveOtherItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {

                    saveDia.setVisible(true);//顯示儲存檔案對話方塊
                    String dirpath = saveDia.getDirectory();//獲取儲存檔案路徑並儲存到字串中。
                    String fileName = saveDia.getFile() + ".txt";////獲取打儲存檔名稱並儲存到字串中

                    if (dirpath == null || fileName == null)//判斷路徑和檔案是否為空
                        return;//空操作
                    else
                        file = new File(dirpath, fileName);//檔案不為空，新建一個路徑和名稱

                    try {
                        BufferedWriter bufw = new BufferedWriter(new FileWriter(file));
                        String text = textOutput.getText();//獲取文字內容
                        bufw.write(text);//將獲取文字內容寫入到字元輸出流
                        bufw.close();//關閉檔案
                    } catch (IOException e1) {
                        e1.printStackTrace();//丟擲IO異常
                    }
                }
            });*/


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


            //窗體關閉監聽
            frame.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    System.exit(0);
                }
            });

            //退出選單項監聽
//            closeItem.addActionListener(new ActionListener() {
//                public void actionPerformed(ActionEvent e) {
//                    System.exit(0);
//                }



       /* public static void main(String[] args) {
            new GUI.FileGUI();
        }*/
    }
}
