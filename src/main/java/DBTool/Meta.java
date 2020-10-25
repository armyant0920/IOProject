package DBTool;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
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




    public static Vector<Column> getVColumns(ResultSet rs) throws SQLException {

        Vector<Column>columns=new Vector<>();
        ResultSetMetaData rsmd=rs.getMetaData();
//        String table_name=rsmd.getTableName(0);
//        System.out.println("測試!!!!"+table_name);
        for(int i=1;i<=rsmd.getColumnCount();i++){

//            columns.add(rsmd.getColumnName(i),rsmd.getColumnType(i));
            columns.add(new Column(rsmd.getColumnName(i),""));//rsmd.getColumnType(i)

        }
        return columns;

    }



    public static class Column{

        private String column_name;
        private String data_type;

        public Column(String name, String type) {
            this.column_name = name;
            this.data_type = type;
        }

        public String getColumn_name() {
            return column_name;
        }

        public void setColumn_name(String column_name) {
            this.column_name = column_name;
        }

        public String getData_type() {
            return data_type;
        }
        //原則上不會去改原始資料表的結構,而且也不見得好改
      /*  public void setData_type(String data_type) {
            this.data_type = data_type;
        }*/
    }

    public static class Table{


        private String table_name;
        private Vector<Column>columns;

        public Table(String name){
            this.table_name=name;
            this.columns=new Vector<>();

        }

       public void addColumn(Column c){

            this.columns.add(c);

       }

        public String getTable_name() {
            return table_name;
        }

        public void setTable_name(String table_name) {
            this.table_name = table_name;
        }

        public Vector<Column> getColumns() {
            return columns;
        }

        public void setColumns(Vector<Column> columns) {
            this.columns = columns;
        }
    }


}
