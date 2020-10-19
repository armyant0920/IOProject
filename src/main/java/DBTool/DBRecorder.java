package DBTool;

import com.google.gson.Gson;

import javax.swing.*;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Vector;

import static java.sql.DriverManager.getConnection;

public class DBRecorder {
    private String dirPath;
    private final String recordPath = "Login.txt";
    private final String SYS_MESSAGE = "Login";
    private final String SYS_CREATE_NEW = "檔案不存在,重新建立";
    private final String DEFAULT_SERVER = "localhost";
    private final String DEFAULT_PORT = "1433";
    private final String DEFAULT_DATABASE = "Demolab";
    private final String DEFAULT_USER = "sa";
    private final String DEFAULT_PASSWORD = "manager";
    private static DBRecorder instance;
    private static File recordFile;
    private static String server;
    private static String port;
    private static String database;
    private static String user;
    private static String password;
    private static boolean remember=false;//預設不儲存登入資訊
    public static void setRemember(boolean remember) {
        DBRecorder.remember = remember;
    }



    public static String getServer() {
        return server;
    }

    public static String getPort() {
        return port;
    }

    public static String getDatabase() {
        return database;
    }

    public static String getUser() {
        return user;
    }

    public static String getPassword() {
        return password;
    }

    public static boolean isRemember() {
        return remember;
    }

    public static DBRecorder getInstance() {
        if (instance == null) {
            synchronized (DBRecorder.class) {
                instance = new DBRecorder();
            }
        }
        return instance;
    }

    DBRecorder() {

//        dirPath = this.getClass().getResource("/").getPath();
        dirPath="C:\\DBRecord\\";
        System.out.println(dirPath+recordFile);
        recordFile = new File(dirPath + recordPath);
        if (recordFile.exists()) {
            System.out.println(recordFile.getAbsolutePath());
            //如果record存在,讀取資料
            try {
                String s = readRecord(recordFile);
                System.out.println(s);
                Gson gson = new Gson();
                DBParam param = gson.fromJson(s, DBParam.class);
                server = param.getServer();
                port = param.getPort();
                database = param.getDatabae();
                user = param.getUser();
                password = param.getPassword();
                remember=param.isRemember();
                System.out.println(server);
                System.out.println(port);
                System.out.println(database);
                System.out.println(user);
                System.out.println(password);
                System.out.println(remember);

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            //如果檔案不存在,使用預設登入資料,並建立新檔案
//            JOptionPane.showMessageDialog(null, SYS_CREATE_NEW, SYS_MESSAGE, JOptionPane.INFORMATION_MESSAGE);
            server = DEFAULT_SERVER;
            port = DEFAULT_PORT;
            database = DEFAULT_DATABASE;
            user = DEFAULT_USER;
            password = DEFAULT_PASSWORD;
            createFile(recordFile);
            DBParam param = new DBParam(
                    DEFAULT_SERVER,
                    DEFAULT_PORT,
                    DEFAULT_DATABASE,
                    DEFAULT_USER,
                    DEFAULT_PASSWORD,
                    false);
            Gson gson = new Gson();
            String s = gson.toJson(param);
            try {
                writeData(recordFile, s, false);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public static void updateParams(String[] params){
        server=params[0];
        port=params[1];
        database=params[2];
        user=params[3];
        password=params[4];


    }
    private static void writeData(File file, String s, boolean append) throws IOException {
        if (file.exists()) {
            //覆蓋舊紀錄,append=false
            BufferedWriter bufw = new BufferedWriter(new FileWriter(file, append));
            bufw.write(s + "\n");//將獲取文字內容寫入到字元輸出流
            bufw.close();//關閉檔案
        } else {
            throw new FileNotFoundException("檔案不存在");
        }
    }

    public static void writeData()  {



        if (!recordFile.exists()) {
            createFile(recordFile);

        }
        DBParam param=new DBParam(server.replace(":",""),port,database,user,password,remember);
        Gson gson = new Gson();
        String s = gson.toJson(param);
        try {
            writeData(recordFile, s, false);

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    /**
     * @param file 存檔文件
     */

    private static void createFile(File file) {
        if (!file.exists()) {//傳入的檔案路徑不存在檔案
            file.getParentFile().mkdirs();//避免連parent資料夾也不存在,幫它直接建立一個
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void deleteFile(){
        File file=recordFile;
        if (file.exists()) {
            if (file.isFile()) {//如果是一個標準檔案
                file.delete();
            } else {
                //如果是一整個資料夾,把資料夾下所有的文件刪除,此案例雖然用不到,但之後可以做為其他專案參考
                File fileLists[] = file.listFiles();
                for (File f : fileLists) {
                    f.delete();
                }
            }
        }
    }

    private static String readRecord(File file) throws IOException {
        if (!file.exists() || file.isDirectory() || !file.canRead()) {
            return null;
        }
        BufferedReader br = new BufferedReader(new FileReader(file));
        StringBuilder sb = new StringBuilder();
        while (br.ready()) {
            sb.append(br.readLine());
        }
        br.close();
        return sb.toString();
    }

    public static Connection getConnect() {
        try {
            return getConnection("jdbc:sqlserver://"
                    + server
                    + port
                    + ";databaseName=" + database, user, password);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;

    }

    public static void executeUpdate(Connection conn){


        String sql=JOptionPane.showInputDialog("input update sql");

        try(Statement st=conn.createStatement();

        )
        {
            int i=st.executeUpdate(sql);
            JOptionPane.showMessageDialog(null,i+"筆資料已更新","update",JOptionPane.PLAIN_MESSAGE);


        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    public static void executeQuery(Connection conn) {

        String sql = JOptionPane.showInputDialog("input query sql");
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql);
        ) {
                Vector<String> columns = Meta.getVColumns(rs);
                Vector rowData=getData(rs,columns);
                createTable(rowData,columns);

/*                //列印標題

                for (int i=0;i<columns.size() ;i++) {
                    System.out.print(columns.get(i) + "\t");
                }
                System.out.println();
                //
                StringBuilder sb=new StringBuilder();
                while (rs.next()) {
                    for(int j=0;j<columns.size();j++){
                        sb.append(rs.getString(columns.get(j))+",");

                    }
                    sb.append("\n");

                    for (int i=0;i<columns.size();i++) {

                        System.out.print(rs.getString(columns.get(i)) + "\t");
                    }
                    System.out.printf("ename=%s,empno=%d",rs.getString("ename"),rs.getInt("empno"));
                    System.out.println();
            }
            System.out.println("列印CSV格式");
            System.out.println(sb.toString());*/

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,e.getMessage(),e.getClass().getName(),JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    /**
     *
     * @return
     */
    private static Vector getData(ResultSet rs,Vector<String>columns) throws SQLException {

        Vector<Vector<Object>>rowData=new Vector<>();


        while(rs.next()){
            Vector<Object>row=new Vector<>();
            for(int i=0;i<columns.size();i++){
                row.add(rs.getString(columns.get(i)));

            }
            rowData.add(row);


        }

        return rowData;

    }

    /**
     * 將資料庫回傳資料存成CSV格式
     * @param rs Result set
     */


    private static String csvFormat(ArrayList<String> columns,ResultSet rs) throws SQLException {
        StringBuilder sb=new StringBuilder();
        while (rs.next()) {
            for(int j=0;j<columns.size();j++){
                sb.append(rs.getString(columns.get(j))+",");

            }
            sb.append("\n");

        }
        return sb.toString();

    }

    private static String jsonFormat(ArrayList<String>columns,ResultSet rs) throws SQLException {
      StringBuilder sb=new StringBuilder();
      while (rs.next()){

          sb.append("");
      }
      return sb.toString();
    }

    /**
     *
     * @param rowData
     * @param columns
     */
    private static void createTable(Vector rowData,Vector columns){

//特化功能部分,考慮選擇到某一列時,自動抓URL欄位顯示圖片
        JFrame frame=new JFrame();

        frame.setDefaultLookAndFeelDecorated(true);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setSize(500,500);
        frame.setLocationRelativeTo(null);
        Container cp=frame.getContentPane();
        DefaultTableModel dtm=new DefaultTableModel(rowData,columns);
        JTable table=new JTable(dtm);
        dtm.setColumnIdentifiers(columns);
        TableRowSorter<TableModel>sorter=new TableRowSorter<TableModel>(dtm);
        table.setRowSorter(sorter);

        table.setPreferredScrollableViewportSize(new Dimension(500,500));
        table.setCellSelectionEnabled(true);
        table.setRowSelectionAllowed(true);
        table.setColumnSelectionAllowed(true);

        cp.add(new JScrollPane(table),BorderLayout.CENTER);
        frame.setVisible(true);



    }


}
