package DBTool;

import Json.JsonMethod;
import Json.SpotPojo;
import Tool.DownloadImage;
import com.google.gson.Gson;
import org.apache.commons.dbcp2.BasicDataSource;

import javax.swing.*;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

import java.net.URI;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import static java.sql.DriverManager.getConnection;
import static javax.swing.JOptionPane.*;
import static javax.swing.JOptionPane.YES_OPTION;

public class DBRecorder {
    private String dirPath;//未來考慮做一個設定選項讓使用者自訂路徑


    private static BasicDataSource ds;
    private final String recordPath = "Login.txt";
    private final String SYS_MESSAGE = "Login";
    private static final String DEFAULT_SERVER = "localhost";
    private static final String DEFAULT_PORT = "1433";
    private static final String DEFAULT_DATABASE = "Demolab";
    private static final String DEFAULT_USER = "sa";
    private static final String DEFAULT_PASSWORD = "manager";
    private static DBRecorder instance;
    private static File recordFile;
    private static String server;
    private static String port;
    private static String database;
    private static String user;
    private static String password;
    private static boolean remember = false;//預設不儲存登入資訊

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

    public static boolean getRemember() {
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

    private static void setParamDefault() {
        server = DEFAULT_SERVER;
        port = DEFAULT_PORT;
        database = DEFAULT_DATABASE;
        user = DEFAULT_USER;
        password = DEFAULT_PASSWORD;

    }

    DBRecorder() {

        dirPath = "C:\\DBRecord\\";
        recordFile = new File(dirPath + recordPath);
        System.out.println(dirPath + recordFile);

        if (recordFile.exists()) {

            System.out.println(recordFile.getAbsolutePath());
            //如果record存在,讀取資料
            try {
                String s = readRecord(recordFile);
                System.out.println(s);
                Gson gson=new Gson();

                DBParam param = gson.fromJson(s, DBParam.class);
                if (param.getRemember() == true) {
                    server = param.getServer();
                    port = param.getPort();
                    database = param.getDatabae();
                    user = param.getUser();
                    password = param.getPassword();
                    remember = param.getRemember();
                    System.out.println("讀取上次連線資料:");
                    System.out.println(server);
                    System.out.println(port);
                    System.out.println(database);
                    System.out.println(user);
                    System.out.println(password);
                    System.out.println(remember);
                } else {
                    System.out.println("記憶狀態為false,使用預設資料");
                    setParamDefault();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            //如果檔案不存在,使用預設登入資料,並建立新檔案
            setParamDefault();

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
        ds = new BasicDataSource();
        ds.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        ds.setUrl("jdbc:sqlserver://" + server + ":" + port + ";databaseName=" + database);
        ds.setUsername(user);
        ds.setPassword(password);
        ds.setMaxTotal(50);
        ds.setMaxIdle(20);
    }

    public static void updateParams(String[] params) {
        server = params[0];
        port = params[1];
        database = params[2];
        user = params[3];
        password = params[4];
        ds = new BasicDataSource();
        ds.setUrl("jdbc:sqlserver://" + server + ":" + port + ";databaseName=" + database);
        ds.setUsername(user);
        ds.setPassword(password);
        //保留針對使用者設定調整的可能
        ds.setMaxTotal(50);
        ds.setMaxIdle(20);


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

    public static void updateFile() {

        if (!recordFile.exists()) {
            createFile(recordFile);
        }
        DBParam param = new DBParam(server.replace(":", ""), port, database, user, password, remember);
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

    /*public static void deleteFile() {
        File file = recordFile;
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
    }*/

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

    /**
     * @return 嘗試直接返回Statement
     */
    public static Statement getStatement() {

        try {
            System.out.printf("URL:%s\nuser:%s\npassword:%s\n", ds.getUrl(), ds.getUsername(), ds.getPassword());
            return ds.getConnection().createStatement();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getClass(), Dialog_Message.CONNECT_FAIL, JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return null;
    }

    public static Connection getConnect(String server, String port, String database, String user, String password) {
        try {

            return getConnection("jdbc:sqlserver://"
                    + server + ":"
                    + port
                    + ";databaseName=" + database, user, password);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;

    }

    public static Connection getConnect() {
        try {
            return getConnection("jdbc:sqlserver://"
                    + server + ":"
                    + port
                    + ";databaseName=" + database, user, password);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;

    }

    public static void executeUpdate(Connection conn) {

        String sql = JOptionPane.showInputDialog("input update sql");

        try (Statement st = conn.createStatement();

        ) {
            int i = st.executeUpdate(sql);
            JOptionPane.showMessageDialog(null, i + Dialog_Message.UPDATE_MSG, Dialog_MsgTitle.UPDATED, JOptionPane.PLAIN_MESSAGE);


        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    /**
     * @param pickTable 根據pickTable的名稱,取得對應資料
     */
    public static void executeQuery(Meta.Table pickTable) {

        //String sql = JOptionPane.showInputDialog("input query sql");
        String sql = "select*from " + pickTable.getTable_name();
        try (Statement st = ds.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql);

        ) {
            Vector<Meta.Column> columns = pickTable.getColumns();
            Vector<String> columnNames = new Vector<>();
            for (int i = 0; i < columns.size(); i++) {
                columnNames.add(columns.get(i).getColumn_name() + "@" + columns.get(i).getData_type());

            }


            Vector rowData = getData(rs, columns);


            createTable(rowData, columnNames, pickTable.getTable_name());


        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), e.getClass().getName(), JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    /**
     * @return
     */
    private static Vector getData(ResultSet rs, Vector<Meta.Column> columns) throws SQLException {

        Vector<Vector<Object>> rowData = new Vector<>();

        while (rs.next()) {
            Vector<Object> row = new Vector<>();
            for (int i = 0; i < columns.size(); i++) {
                row.add(rs.getString(columns.get(i).getColumn_name()));
            }
            for (Object o : row) {
                System.out.println((String) o);
            }
            rowData.add(row);
        }

        return rowData;
    }

    /**
     * 將資料庫回傳資料存成CSV格式
     *
     * @param rs Result set
     */

    private static String csvFormat(ArrayList<String> columns, ResultSet rs) throws SQLException {
        StringBuilder sb = new StringBuilder();
        while (rs.next()) {
            for (int j = 0; j < columns.size(); j++) {
                sb.append(rs.getString(columns.get(j)) + ",");

            }
            sb.append("\n");

        }
        return sb.toString();

    }

    private static String jsonFormat(ArrayList<String> columns, ResultSet rs) throws SQLException {
        StringBuilder sb = new StringBuilder();
        while (rs.next()) {

            sb.append("");
        }
        return sb.toString();
    }

    /**
     * @param rowData
     * @param columns
     */
    private static void createTable(Vector rowData, Vector columns, String table_name) {

//特化功能部分,考慮選擇到某一列時,自動抓URL欄位顯示圖片
        JFrame frame = new JFrame(table_name);

        frame.setDefaultLookAndFeelDecorated(true);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setSize(500, 500);
        frame.setLocationRelativeTo(null);

        Container cp = frame.getContentPane();
        DefaultTableModel dtm = new DefaultTableModel(rowData, columns);
        JTable table = new JTable(dtm);


        dtm.setColumnIdentifiers(columns);
        TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(dtm);
        table.setRowSorter(sorter);

        table.setPreferredScrollableViewportSize(new Dimension(500, 500));
        table.setCellSelectionEnabled(true);
        table.setRowSelectionAllowed(true);
        table.setColumnSelectionAllowed(true);

        cp.add(new JScrollPane(table), BorderLayout.CENTER);
        //
        JPanel southjPanel = new JPanel(new GridLayout(0, 3));
        //
        JButton btn_InsertCSV = new JButton("insert CSV");
        btn_InsertCSV.setBackground(Color.GREEN);
        southjPanel.add(btn_InsertCSV);
        //
        JButton btn_ExportCSV = new JButton("export CSV");
        btn_ExportCSV.setBackground(Color.GREEN);
        southjPanel.add(btn_ExportCSV);
        //
        JButton btn_ExportJson2 = new JButton("export JSON_ver2");
        btn_ExportJson2.setBackground(Color.GREEN);
        southjPanel.add(btn_ExportJson2);
        //
        JButton btn_QSQL = new JButton("input QuerySQL");
        btn_QSQL.setBackground(Color.YELLOW);
        southjPanel.add(btn_QSQL);
        //
        JButton btn_USQL = new JButton("input DML SQL");
        btn_USQL.setBackground(Color.YELLOW);
        southjPanel.add(btn_USQL);
        //
        JButton btn_Truncate = new JButton("truncate");
        btn_Truncate.setBackground(Color.YELLOW);
        southjPanel.add(btn_Truncate);
        //
        JButton btn_ExportJson = new JButton("export JSON");
        btn_ExportJson.setBackground(Color.RED);
        southjPanel.add(btn_ExportJson);
        //
        JButton btn_Image = new JButton("imageURL");
        btn_Image.setBackground(Color.RED);
        southjPanel.add(btn_Image);
        //
        JButton btn_Add = new JButton("add");
//        southjPanel.add(btn_Add);
        //
        JButton btn_MAP = new JButton("map");
        btn_MAP.setBackground(Color.RED);
        southjPanel.add(btn_MAP);
        //

        cp.add(southjPanel, BorderLayout.SOUTH);

        btn_Add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dtm.addRow((Object[]) null);

            }
        });
        //以CSV文件匯入資料,極端狀況下.欄位名稱是保留字或純數字時會失敗
        btn_InsertCSV.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                File file = showLoadDialog(frame);



                    insertFromCSV(table_name, file);
                    JOptionPane.showMessageDialog(frame, Dialog_Message.CSV_INSERTED, Dialog_MsgTitle.MSG, JOptionPane.INFORMATION_MESSAGE);
                    refreshTable(table_name, table, dtm);


            }
        });
        //輸出CSV文件,缺點是必須自行指定副檔名
        btn_ExportCSV.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                File file = showSaveDialog(frame);//檔案不為空，新建一個路徑和名稱


                int row = table.getRowCount();
                int col = table.getColumnCount();
                //append設定為false,避免使用者用同樣的檔案路徑存新資料
                try (BufferedWriter bwr = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, false), "utf-8"))) {
                    //取得表格欄位
//                        List<String>columnName=new ArrayList<>();
                    for (int i = 0; i < table.getColumnCount(); i++) {
                        String s = table.getColumnName(i);
                        s = s.substring(0, s.indexOf("@"));
                        if (i < table.getColumnCount() - 1) {
                            bwr.write(s + ",");
                        } else {
                            bwr.write(s);
                        }
//                            columnName.add(s);

                    }
                    bwr.write("\n");

                    //寫入目前GUI表格上的資料
                    for (int i = 0; i < row; i++) {
                        for (int j = 0; j < col; j++) {
                            Object val = table.getValueAt(i, j);
                            if (val != null) {//如果不是空值
                                bwr.write((String) val);//寫入資料
                            }
                            if (j < col - 1) {
                                bwr.write(",");//如果不是最後一欄,加上","

                            } else {//如果是最後一欄,加上換行
                                bwr.write("\n");
                            }

                        }

                    }

                    bwr.flush();
                    bwr.close();
                    JOptionPane.showMessageDialog(frame, Dialog_Message.CSV_COMPLETE, Dialog_MsgTitle.MSG, JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }

            }
        });
        //特化功能:輸出JSON檔
        btn_ExportJson.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                File file = showSaveDialog(frame);

                ArrayList dataList = new ArrayList();
                int row = table.getRowCount();
                int col = table.getColumnCount();

                for (int i = 0; i < row; i++) {
                    List data = new ArrayList();

                    for (int j = 0; j < col; j++) {
                        data.add(table.getValueAt(i, j));
                    }
                    SpotPojo spot = new SpotPojo();
                    spot.setCaseId((String) data.get(0));
                    spot.setCaseName((String) data.get(1));
                    spot.setRegisterDate((String) data.get(2));
                    spot.setBelongCity((String) data.get(3));
                    spot.setLongitude(Double.parseDouble((String) data.get(4)));
                    spot.setLatitude(Double.parseDouble((String) data.get(5)));
                    spot.setRepresentImage((String) data.get(6));
                    spot.setBriefDescribe((String) data.get(7));
                    dataList.add(spot);
                }
                String jsonOutput = JsonMethod.parseToJson(dataList);
                try (
                        BufferedWriter bwr = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true), "utf-8"))) {
                    bwr.write(jsonOutput);
                    JOptionPane.showMessageDialog(frame, Dialog_Message.JSON_OUTPUT_COMPLETE, Dialog_MsgTitle.MSG, JOptionPane.INFORMATION_MESSAGE);
//                        bwr.flush();


                } catch (
                        Exception err) {
                    JOptionPane.showMessageDialog(frame, err.getMessage(), Dialog_MsgTitle.ERROR, JOptionPane.INFORMATION_MESSAGE);

                    err.printStackTrace();
                }

            }
        });
        //轉出共通的JSON文件

        btn_ExportJson2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                File file = showSaveDialog(frame);


                String columnName[] = getColumnName(table).split(",");


                int row = table.getRowCount();
                int col = table.getColumnCount();
                //append設定為false,避免使用者用同樣的檔案路徑存新資料
                try (BufferedWriter bwr = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, false), "utf-8"))) {

                    bwr.write("[");
                    //寫入目前GUI表格上的資料
                    for (int i = 0; i < row; i++) {
                        bwr.write("{");
                        for (int j = 0; j < col; j++) {
                            Object val = table.getValueAt(i, j);
                            if (val != null) {//如果不是空值
                                bwr.write(addMark(columnName[j])+":" + addMark((String) val));//寫入資料
                            } else {
                                bwr.write(addMark(columnName[j]) +":"+ addMark("null"));

                            }
                            if (j < col - 1) {
                                bwr.write(",");

                            } else {//如果是最後一欄,加上"}"
                                bwr.append("}");
                            }
                        }
                        if (i < row - 1) {//如果不是最後一行,加上","
                            bwr.append(",");
                        }


                    }
                    bwr.write("]");

                    bwr.flush();
                    bwr.close();
                    JOptionPane.showMessageDialog(frame, Dialog_Message.JSON_OUTPUT_COMPLETE, Dialog_MsgTitle.MSG, JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }


            }
        });

        //下Query SQL指令
        btn_QSQL.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String sql = JOptionPane.showInputDialog(Dialog_Message.SQL_QUERY);

                try (Statement st = ds.getConnection().createStatement();
                     ResultSet rs = st.executeQuery(sql);

                ) {
                    Meta.getVColumns(rs);
                    Vector title = Meta.getVColumns(rs);
                    Vector rowData = getData(rs, title);


                    dtm.setDataVector(rowData, title);
                    dtm.fireTableStructureChanged();

                } catch (SQLException err) {
                    JOptionPane.showMessageDialog(null, err.getMessage(), e.getClass().getName(), JOptionPane.ERROR_MESSAGE);
                    err.printStackTrace();
                }
            }
        });

        //含insert、update、delete等DML語言
        btn_USQL.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String sql = JOptionPane.showInputDialog(Dialog_Message.SQL_UPDATE);
                try (Statement st = ds.getConnection().createStatement();

                ) {
                    int result = st.executeUpdate(sql);
                    JOptionPane.showMessageDialog(frame, result, Dialog_MsgTitle.MSG, INFORMATION_MESSAGE);
                    refreshTable(table_name, table, dtm);


                } catch (SQLException err) {
                    JOptionPane.showMessageDialog(null, err.getMessage(), e.getClass().getName(), JOptionPane.ERROR_MESSAGE);
                    err.printStackTrace();
                }
            }
        });
//特化功能:打開圖片瀏覽
        btn_Image.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = table.getSelectedRow();
                int column = table.getSelectedColumn();
                String s = (String) table.getValueAt(row, column);
                System.out.println("按鈕" + s);

                URL url = DownloadImage.convertURL(s);

                DownloadImage.readURL(url);


            }
        });
//清空資料表
        btn_Truncate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int check = showConfirmDialog(frame, Dialog_Message.CONFIRM_TRUNCATE, Dialog_MsgTitle.CONFIRM,
                        YES_NO_OPTION, WARNING_MESSAGE);
                if (check == YES_OPTION) {

                    try (Connection conn = DBRecorder.getConnect();
                         Statement st = conn.createStatement();) {
                        st.executeUpdate("truncate table " + table_name);
                        JOptionPane.showMessageDialog(frame, Dialog_Message.TRUNCATE_COMPLETE, Dialog_MsgTitle.MSG, INFORMATION_MESSAGE);
                        refreshTable( table_name, table, dtm);



                    } catch (SQLException throwables) {

                        JOptionPane.showMessageDialog(frame, throwables, Dialog_MsgTitle.ERROR, ERROR_MESSAGE);
                        throwables.printStackTrace();
                    }

                }

            }
        });
//特化功能:打開經緯度地址
        btn_MAP.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Desktop dt = Desktop.getDesktop();
                int pickRow = table.getSelectedRow();
                String longitude = (String) table.getValueAt(pickRow, 4);
                String latitude = (String) table.getValueAt(pickRow, 5);
                String url = "https://www.google.com.tw/maps/@" + latitude + "," + longitude + ",15z";
                System.out.println(url);
                URI uri = URI.create(url);
                try {
                    dt.browse(uri);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }

            }
        });
        frame.setVisible(true);

        frame.toFront();

    }

    private static void insertFromCSV(String table, File csv) {

        //參數1.table name
        //參數2.多個欄位名稱,
        //參數3.多個值
//原則上2與3的配合下,只要資料正確,即使CSV檔資料順序與資料庫不一致也可以匯入

        String sql = "insert into " + table + " (";

        String values = (" values(");

        try (
                BufferedReader br = new BufferedReader(new FileReader(csv));
        ) {
            //處理CSV的標題,拼成完整SQL語句

            //讀CSV的第一行,預設用半形","區隔，注意碰到不按牌理出牌的格式有死掉的可能
            String columnNames[] = br.readLine().split(",");
            for (int i = 0; i < columnNames.length; i++) {
                sql += columnNames[i];
                values += "?";

                if (i < columnNames.length - 1) {
                    sql += ",";
                    values += ",";
                } else {
                    sql += ")";
                    values += ")";
                }
            }


            sql += values;
            System.out.println(sql);

            try (Connection conn = DBRecorder.getConnect();
                 PreparedStatement ps = conn.prepareStatement(sql);) {

                String line = null;
                while ((line = br.readLine()) != null) {
                    List<String> data=new ArrayList();
                    String content[] = line.split(",");//最後一個如果是空抓不到?
                    for(int i=0;i<content.length;i++){
                        data.add(content[i]);
                    }
                    if(data.size()<columnNames.length){

                        for(int i=0;i<=(columnNames.length-data.size());i++){
                            data.add("");

                        }
                        System.out.println(data.size());
                    }
                    for(int i=0;i<content.length;i++){
                        System.out.print(content[i]+",\t");
                    }
                    System.out.println();
                    for (int i = 1; i <=columnNames.length; i++) {
                        ps.setString(i, data.get(i-1));
                    }
                    ps.addBatch();
                    ps.clearParameters();
                }
                int[] result = ps.executeBatch();
                ps.clearBatch();
                for (int i = 0; i < result.length; i++) {
                    System.out.printf("index %d result=%s\n", i, result[i]);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void refreshTable(String tableName, JTable table, DefaultTableModel dtm) {

        String sql = "select " + getColumnName(table) + " from " + tableName;


        try (Statement st = ds.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql);

        ) {
            Vector<Meta.Column> columns = Meta.getVColumns(rs);
            Vector<String> columnNames = new Vector<>();
            for (int i = 0; i < columns.size(); i++) {

                columnNames.add(columns.get(i).getColumn_name() + "@" + columns.get(i).getData_type());
            }
            Vector title = columns;
            Vector rowData = getData(rs, title);

            dtm.setDataVector(rowData, columnNames);
            dtm.fireTableStructureChanged();

        } catch (SQLException err) {
            JOptionPane.showMessageDialog(null, err.getMessage(), err.getClass().getName(), JOptionPane.ERROR_MESSAGE);
            err.printStackTrace();
        }


    }


    private static String getColumnName(JTable table) {

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < table.getColumnCount(); i++) {
            String s = table.getColumnName(i);
            s = s.substring(0, s.indexOf("@"));
            sb.append(s);
            if (i < table.getColumnCount() - 1) {
                sb.append(",");
            }

        }

        return sb.toString();

    }

    private static File showLoadDialog(JFrame frame) {
        FileDialog dialog = new FileDialog(frame, Dialog_MsgTitle.LOAD, FileDialog.LOAD);
        dialog.setVisible(true);
        String dirpath = dialog.getDirectory();//獲取儲存檔案路徑並儲存到字串中。
        String fileName = dialog.getFile();////獲取打儲存檔名稱並儲存到字串中
        if (dirpath == null || fileName == null) //判斷路徑和檔案是否為空
        {
            return null;
        }
        return new File(dirpath, fileName);


    }

    private static File showSaveDialog(JFrame frame) {
        FileDialog dialog = new FileDialog(frame, Dialog_MsgTitle.SAVE, FileDialog.SAVE);
        dialog.setVisible(true);
        String dirpath = dialog.getDirectory();//獲取儲存檔案路徑並儲存到字串中。
        String fileName = dialog.getFile();////獲取打儲存檔名稱並儲存到字串中
        if (dirpath == null || fileName == null)//判斷路徑和檔案是否為空
        {
            return null;//空操作
        } else {
            return new File(dirpath, fileName);//檔案不為空，新建一個路徑和名稱
        }

    }

    private static String addMark(String s) {

        return "\"" + s + "\"";
    }


}



