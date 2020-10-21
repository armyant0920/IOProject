package DBTool;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.event.*;
import java.util.Vector;
import java.util.logging.Logger;

public class DB_TableSelectDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JComboBox comboBox1;
    private Meta.Table pickTable;
    private static Vector<Meta.Table> tables=new Vector<>();
    private Logger logger;

    /**
     * @param input 連線DB後,將取得的table集合傳給對話框創建下拉選單
     */
    public DB_TableSelectDialog(Vector<Meta.Table> input) {
        tables.clear();
        for(Meta.Table t:input){
            tables.add(t);
        }
        ;
        logger = Logger.getLogger("dialog");
       /* for (Object o : tables) {
            logger.info(((Meta.Table) o).getTable_name());
        }*/
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                try {
                    onOK();
                } catch (FieldException fieldException) {
                    fieldException.printStackTrace();
                }
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        pack();

        this.setSize(500,500);
        setLocationRelativeTo(null);

    }

    private void onOK() throws FieldException {//按下OK後應該取得兩邊語言

        pickTable = (Meta.Table) comboBox1.getSelectedItem();


        setVisible(false);
        dispose();

    }

    /**
     * @return 返回選到的表格
     */
    public Meta.Table getResult() {
        setVisible(true);
        return pickTable;

    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }


    private void createUIComponents() {
        // TODO: place custom component creation code hereVector


        Vector<String> table=new Vector<>();
        for(Meta.Table t:tables){
            table.add(t.getTable_name());
            System.out.println("TEST:"+t.getTable_name());

        }
        String s[]={"23","dasd","weqwr"};
        comboBox1 = new JComboBox();
        comboBox1.addItem("AA");
        comboBox1.addItem("BB");

       // sportColumn.setCellEditor(new DefaultCellEditor(comboBox1));



        ItemListener itemListener = new
                ItemListener() {
                    @Override
                    public void itemStateChanged(ItemEvent e) {
                        if (ItemEvent.SELECTED == e.getStateChange()) {
                            String selectedItem = e.getItem().toString();
                            System.out.printf("new selected item : %s%n", selectedItem);
                            System.out.println(e.getSource());
                        }
                        if (ItemEvent.DESELECTED == e.getStateChange()) {
                            String selectedItem = e.getItem().toString();
                            System.out.printf("deselected item : %s%n", selectedItem);
                        }

                    }
                };
        comboBox1.addItemListener(itemListener);

//        contentPane.add(comboBox1);

    }


    class FieldException extends Exception {
        public FieldException(String msg) {
            super(msg);

        }
    }
}




