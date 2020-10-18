package GUI;

import java.awt.BorderLayout;
import java.awt.Container;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;
import javax.swing.border.Border;

public class ProgressSample {
    public static void main(String args[]) {
        JFrame f = new JFrame("JProgressBar Sample");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container content = f.getContentPane();
        JProgressBar progressBar = new JProgressBar();
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        Border border = BorderFactory.createTitledBorder("Reading...");
        progressBar.setBorder(border);
        content.add(progressBar, BorderLayout.NORTH);
        f.setSize(300, 100);
        f.setVisible(true);
        UpdateWorker updateWorker = new UpdateWorker(f, progressBar);
        updateWorker.execute();
    }

    public static class UpdateWorker extends SwingWorker
    {
        JProgressBar bar = null;
        JFrame f=null;
        public UpdateWorker(JFrame f, JProgressBar bar)
        {
            this.bar = bar;
            this.f = f;
        }

        @Override
        protected String doInBackground() throws Exception {
            Random rdm = new Random();
            int pv = 0;
            while(pv<100)
            {
                Thread.sleep(rdm.nextInt(500)+500);
                pv+=rdm.nextInt(5);
                bar.setValue(pv);
            }
            return null;
        }

        @Override
        protected void done()
        {
            f.setVisible(false);
            f.dispose();
        }
    }
}