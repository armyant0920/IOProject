package DBLab;



import java.sql.*;

public class TranscationLab {

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=JDBCDB", "scott", "tiger");
             Statement st=conn.createStatement();
             ResultSet rs=st.executeQuery("select*from emp");



             )
        {
            conn.setAutoCommit(false);
            try {
                while (rs.next()){
                    int empno=rs.getInt("empno");
                    String name=rs.getString("enae");
                    System.out.println("empno:"+empno+" ename:"+name);



                }
                conn.commit();
            }catch (SQLException e){
                e.printStackTrace();
                System.out.println("rollback");
                conn.rollback();


            }catch (Exception e){
                e.printStackTrace();
                System.out.println("rollback");
                conn.rollback();
            }
            System.out.println("query finished");
        }


        catch (SQLException e) {
            e.printStackTrace();
        }

    }


}
