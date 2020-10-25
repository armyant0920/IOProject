/*
package DBTool;



import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.*;
import java.util.Vector;

import javax.swing.*;

*/
/**
 * 1.帳號密碼記憶功能
 * 2.讀取資料進度條
 *//*


class AddressDialog extends JDialog implements ActionListener, ItemListener {

    JLabel label1 = new JLabel("Server");
    JLabel label2 = new JLabel("Port");
    JLabel label3 = new JLabel("Database");
    JLabel label4 = new JLabel("User");
    JLabel label5 = new JLabel("Password");
    JTextField serverField = new JTextField();
    JTextField portField = new JTextField();
    JTextField databaseField = new JTextField();
    JTextField userField = new JTextField();

    JPasswordField passwordField = new JPasswordField();
    String[] address = new String[5];
    JButton loginin = new JButton("login");
    JButton cancel = new JButton("cancel");
    JRadioButton radio = new JRadioButton("記憶帳號密碼");
    private boolean remember = false;

    public AddressDialog(Frame owner, boolean modal) {
        super(owner, modal);
        init();

    }

    private void init() {
        this.setTitle("Address Dialog");
        this.setLayout(new GridLayout(7, 2));
        //
        this.add(label1);
        this.add(serverField);

        serverField.setText(DBRecorder.getServer());
        System.out.println(DBRecorder.getServer());
        //
        this.add(label2);
        this.add(portField);
        portField.setText(DBRecorder.getPort());
        System.out.println(DBRecorder.getPort());
        //
        this.add(label3);
        this.add(databaseField);
        databaseField.setText(DBRecorder.getDatabase());
        System.out.println(DBRecorder.getDatabase());
        //
        this.add(label4);
        this.add(userField);
        userField.setText(DBRecorder.getUser());
        System.out.println(DBRecorder.getUser());
        //
        this.add(label5);
        this.add(passwordField);
        passwordField.setText(DBRecorder.getPassword());
        System.out.println(DBRecorder.getPassword());
        //
        this.add(loginin);
        loginin.addActionListener(this);
        this.add(cancel);
        cancel.addActionListener(this);
        //
        this.add(radio);
        radio.setSelected(DBRecorder.getRemember());
        radio.addItemListener(this);

    }


    public String[] getAddress() {
        address[0] = serverField.getText() ;

        address[1] = portField.getText();
        address[2] = databaseField.getText();
        address[3] = userField.getText();
        address[4] = passwordField.getText();
        return address;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginin) {

            //select table_name,column_name,data_type from information_schema.columns
            DBRecorder.updateParams(getAddress());
            if(!remember){
                DBRecorder.deleteFile();

            }else{
                DBRecorder.setRemember(true);
                DBRecorder.updateFile();

            }
            try(
            Statement st=DBRecorder.getStatement();

            ResultSet rs=st.executeQuery("use "+DBRecorder.getDatabase()+" select table_name,column_name,data_type from INFORMATION_SCHEMA.COLUMNS")


            ){
                JOptionPane.showMessageDialog(null,"Connected to "+DBRecorder.getDatabase(),"message",JOptionPane.INFORMATION_MESSAGE);
                //概念是不重複的table_name,內含column_name & data_type
                String tempName="null";//暫存表格
                Vector<Meta.Table> tableList=new Vector<>();//預先準備這個DB的所有表格資料
                Meta.Table tempTable=null;
                while(rs.next()){
                    //餵資料給DBRecorder,並準備selectTable

                    String table_name=rs.getString("table_name");//
                    String column_name=rs.getString("column_name");
                    String data_type=rs.getString("data_type");


                    if(!tempName.equals(table_name)){
                        //目前假設抓回來的資料依表格名稱排序,
                        // 那麼如果出現新的表格名稱,就新增一個table物件,

                        Meta.Table table=new Meta.Table(table_name);
                        tableList.add(table);//將這張表加進去
                        tempTable=table;
                        tempName=table_name;

                    }
                    //將資料寫入目前的table
                    tempTable.addColumn(new Meta.Column(column_name,data_type));

                }
                //執行後顯示dialog
//                String s[]={"C","D","e"};
                DB_tb_select select=new DB_tb_select(tableList);
//                select.setModal(true);
                Meta.Table pick= select.getKey();
                DBRecorder.executeQuery(pick);
//                System.out.println(select.getResult());
//                DB_TableSelectDialog dialog=new DB_TableSelectDialog(tableList);
//                Meta.Table table_selected=dialog.getResult();//取得選取的table
//                DBRecorder.executeQuery(table_selected);

               */
/* DB_TableSelectDialog dialog=new DB_TableSelectDialog(tableList);




                //將table資料另開視窗顯示*//*


            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }



            */
/*DBRecorder.updateParams(getAddress());
            if (!remember) {
                DBRecorder.deleteFile();
            }else{
                DBRecorder.setRemember(true);
                DBRecorder.writeData();

            }
            Connection conn = DBRecorder.getConnect();
            if (conn != null) {
                JOptionPane.showMessageDialog(null, "connected", "DB", JOptionPane.INFORMATION_MESSAGE);

                DBRecorder.executeQuery(conn);

//                DBRecorder.executeUpdate(conn);
                try {
                    conn.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

            } else {
                JOptionPane.showMessageDialog(null, "connect fail", "DB", JOptionPane.ERROR_MESSAGE);
            }*//*

        }
        if (e.getSource() == cancel) {
            this.dispose();
        }
        if (e.getSource() == radio) {

        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {

        if (e.getStateChange() == ItemEvent.SELECTED) {
            remember = true;//當remember為true,執行login時建立紀錄檔

        } else if (e.getStateChange() == ItemEvent.DESELECTED) {
            remember = false;//當remember為false,執行login時刪除紀錄檔

        }


    }
}

public class DBDialog extends JFrame {


    public DBDialog(String title) {//如果要設定標題的話...
        super(title);
        init();
    }

    public DBDialog() {
        super();
        init();

    }

    private void init() {
        this.getContentPane().setLayout(new FlowLayout());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        final AddressDialog dialog = new AddressDialog(this, false);
        JButton button = new JButton("Show Dialog");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                dialog.setSize(500, 250);
                dialog.setLocationRelativeTo(null);
                dialog.setVisible(true);

            }
        });
        this.getContentPane().add(button);
    }

    public static void main(String[] args) {

        DBRecorder.getInstance();
        JFrame.setDefaultLookAndFeelDecorated(true);
        JDialog.setDefaultLookAndFeelDecorated(true);
        DBDialog frame = new DBDialog();
        frame.pack();
        frame.setVisible(true);

    }
}*/
