package DBLab;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

class IOOutputGroup {


    public static BufferedWriter exportFile(String filePath, String encoding) {


        BufferedWriter bw = null;
        try {
            FileOutputStream fos = new FileOutputStream(filePath);
            OutputStreamWriter osw = new OutputStreamWriter(fos, encoding);
            bw = new BufferedWriter(osw);

        } catch (FileNotFoundException e) {

            e.printStackTrace();

        } catch (UnsupportedEncodingException e) {

            e.printStackTrace();
        }

        return bw;

    }
}
