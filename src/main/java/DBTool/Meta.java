package DBTool;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.*;

public class Meta {


    public static HashSet<String> getColumns(ResultSet rs) throws SQLException {
        HashSet<String> columns = new HashSet<String>();
        ResultSetMetaData rsmd = rs.getMetaData();

        int count = rsmd.getColumnCount();
        for (int x = 1; x <= count; x++) {
            columns.add(rsmd.getColumnName(x));
        }
        return columns;
    }

    public static TreeSet<String> getTColumns(ResultSet rs) throws SQLException {
        TreeSet<String> columns = new TreeSet<>();
        ResultSetMetaData rsmd = rs.getMetaData();
        int count = rsmd.getColumnCount();
        for (int x = 1; x <= count; x++) {
            String s=rsmd.getColumnName(x);
            int size=rsmd.getColumnDisplaySize(x);
            System.out.printf("name:%s,size:%d",s,size);
            columns.add(s);
        }
        return columns;
    }

    public static ArrayList<String> getAColumns(ResultSet rs) throws SQLException {
        ArrayList<String> columns = new ArrayList<>();
        ResultSetMetaData rsmd = rs.getMetaData();
        int count = rsmd.getColumnCount();
        for (int x = 1; x <= count; x++) {
            String s=rsmd.getColumnName(x);
            int size=rsmd.getColumnDisplaySize(x);
            //System.out.printf("name:%s,size:%d",s,size);
            columns.add(s);
        }
        return columns;
    }

    /**
     *
     * @param rs
     * @return 回傳Map,Key為欄位名稱,值為欄位型態
     * @throws SQLException
     */
    public static Vector<Column> getVColumns(ResultSet rs) throws SQLException {
        Vector<Column>columns=new Vector<>();
        ResultSetMetaData rsmd=rs.getMetaData();
        for(int i=1;i<=rsmd.getColumnCount();i++){

//            columns.add(rsmd.getColumnName(i),rsmd.getColumnType(i));
            columns.add(new Column(rsmd.getColumnName(i),rsmd.getColumnType(i)));

        }
        return columns;

    }




    public static class Column{

        private String columnName;
        private int columnType;

        public Column(String columnName, int columnType) {
            this.columnName = columnName;
            this.columnType = columnType;
        }

        public String getColumnName() {
            return columnName;
        }

        public void setColumnName(String columnName) {
            this.columnName = columnName;
        }

        public int getColumnType() {
            return columnType;
        }

        public void setColumnType(int columnType) {
            this.columnType = columnType;
        }
    }

    public static class Table{
        private String table_name;
        private List<Column>columns;

        public Table(List<Column> columns) {
            this.columns = columns;
        }

        public String getTable_name() {
            return table_name;
        }

        public void setTable_name(String table_name) {
            this.table_name = table_name;
        }

        public List<Column> getColumns() {
            return columns;
        }

        public void setColumns(List<Column> columns) {
            this.columns = columns;
        }
    }


}
