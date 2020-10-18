package DBTool;

import com.google.gson.Gson;

import javax.swing.*;
import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import static java.sql.DriverManager.getConnection;

public class DBRecorder {
    private String dirPath;
    private final String recordPath = "Login.txt";
    private final String SYS_MESSAGE = "Login";
    private final String SYS_CREATE_NEW = "檔案不存在,重新建立";
    private final String DEFAULT_SERVER = "localhost:";
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
        System.out.println(dirPath);
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
                System.out.println(server);
                System.out.println(port);
                System.out.println(database);
                System.out.println(user);
                System.out.println(password);

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            //如果檔案不存在,使用預設登入資料,並建立新檔案
            JOptionPane.showMessageDialog(null, SYS_CREATE_NEW, SYS_MESSAGE, JOptionPane.INFORMATION_MESSAGE);
            createFile(recordFile);
            DBParam param = new DBParam(
                    DEFAULT_SERVER,
                    DEFAULT_PORT,
                    DEFAULT_DATABASE,
                    DEFAULT_USER,
                    DEFAULT_PASSWORD);
            Gson gson = new Gson();
           server = DEFAULT_SERVER;
            port = DEFAULT_PORT;
            database = DEFAULT_DATABASE;
           user = DEFAULT_USER;
           password = DEFAULT_PASSWORD;

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
        DBParam param=new DBParam(server.replace(":",""),port,database,user,password);
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

                ArrayList<String> columns = Meta.getAColumns(rs);
                //列印標題
                for (int i=0;i<columns.size() ;i++) {
                    System.out.print(columns.get(i) + "\t");
                }
                System.out.println();
                //

                while (rs.next()) {
                    for (int i=0;i<columns.size();i++) {
                        System.out.print(rs.getString(columns.get(i)) + "\t");
                    }
                    System.out.println();




            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


}
