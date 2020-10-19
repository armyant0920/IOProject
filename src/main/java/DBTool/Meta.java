package DBTool;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.TreeSet;
import java.util.Vector;

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

    public static Vector<String> getVColumns(ResultSet rs) throws SQLException {
        Vector<String>columns=new Vector<>();
        ResultSetMetaData rsmd=rs.getMetaData();
        for(int i=1;i<=rsmd.getColumnCount();i++){
            columns.add(rsmd.getColumnName(i));
        }
        return columns;

    }


}
