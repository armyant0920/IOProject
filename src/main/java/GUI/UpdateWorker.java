package GUI;

import javax.swing.*;
import java.util.Random;

public class UpdateWorker extends SwingWorker {
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
