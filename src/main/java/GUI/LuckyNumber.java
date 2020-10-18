package GUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.WindowConstants;


public class LuckyNumber extends JFrame implements ActionListener{
    private static final long serialVersionUID = 4276832367310733568L;
    private JLabel label;
    private JButton cancel;
    private JButton start;
    private SwingWorker worker;
    private JTextField textField;

    public LuckyNumber() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 1));

        JPanel buttons = new JPanel(new FlowLayout());
        start = new JButton("Start");
        start.addActionListener(this);
        start.setDefaultCapable(true);
        buttons.add(start);
        cancel = new JButton("Cancel");
        cancel.setEnabled(false);
        cancel.addActionListener(this);
        buttons.add(cancel);
        panel.add(buttons);

        ImageIcon image = new ImageIcon("D:\\專案資料夾\\JAVA\\IOProject\\src\\main\\java\\GUI\\spinner-loader-animation.gif");

        label = new JLabel(image);
        JPanel answer = new JPanel(new GridLayout(1, 2));
        textField = new JTextField("");
        answer.add(textField);
        answer.add(label);
        label.setVisible(false);
        panel.add(answer);

        setTitle("Lucky number generator");
        setPreferredSize(new Dimension(300, 100));
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(panel);

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                LuckyNumber luckyNumber = new LuckyNumber();
                luckyNumber.pack();
                luckyNumber.setVisible(true);
                luckyNumber.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            }
        });
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == start) {
            start.setEnabled(false);
            textField.setText("");
            worker = new NumberWorker();
            worker.execute();
            label.setVisible(true);
            cancel.setEnabled(true);
        } else if (e.getSource() == cancel) {
            cancel();
        }

    }

    @Override
    public void dispose() {
        cancel();
        super.dispose();
    }

    private void cancel() {
        start.setEnabled(true);
        label.setVisible(false);
        cancel.setEnabled(false);
        if (worker != null) {
            worker.cancel(true);
            worker = null;
        }

    }

    private class NumberWorker extends SwingWorker {

        private Random r = new Random();

        @Override
        protected String doInBackground() throws Exception {
            long s = System.currentTimeMillis();
            while (System.currentTimeMillis() - s < 5000 && !Thread.currentThread().isInterrupted()) {
                r.nextInt();
            }
            return String.valueOf(r.nextInt(10) + 1);
        }

        @Override
        protected void done() {
            try {
                textField.setText(String.valueOf(r.nextInt()));
                LuckyNumber.this.worker = null;
                LuckyNumber.this.cancel();
            } catch (Exception ignore) {
            }
        }
    }
}
