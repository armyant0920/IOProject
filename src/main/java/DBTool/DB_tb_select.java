package DBTool;

import javax.swing.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class DB_tb_select extends JDialog {
    private JButton button1;
    private JButton button2;
    private JComboBox comboBox1;
    private JPanel contentPane;
    private String key;
    private static Map<String, Meta.Table>map=new HashMap<>();
    private static  Vector list=new Vector<>();//=new Vector<>(){{add("A");add("B");add("C");}};

    public DB_tb_select(Vector<Meta.Table> input) {
        list.clear();
        map.clear();
        for(Meta.Table t:input){
            map.put(t.getTable_name(),t);

        }

        for(Meta.Table t:input){
            list.add(t.getTable_name());
        }

//        list.add("A");
//        list.add("B");
//        list.add("C");

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(button1);
        button1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                    onOK();

                }
            }
        );

        button2.addActionListener(new ActionListener() {
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
//        createUIComponents();
        pack();
        setLocationRelativeTo(null);


    }


    private void onCancel() {
        // add your code here if necessary
        dispose();
    }
    private void onOK()  {//
        key =(String)comboBox1.getSelectedItem();
        setVisible(false);
        dispose();
    }
    public Meta.Table getKey(){
        setVisible(true);
        return  map.get(key);

    }




    private void createUIComponents() {
        // TODO: place custom component creation code here

        comboBox1=new JComboBox(list);
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




    }
}
