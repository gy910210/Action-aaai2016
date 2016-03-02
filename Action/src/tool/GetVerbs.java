package tool;


import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Vector;

public class GetVerbs {
    public Vector<String> getVerbs(String verbFile) {
        BufferedReader br = null;
        Vector<String> v = new Vector<>();
        try {
            br = new BufferedReader(new FileReader(verbFile));
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().equals("")) continue;
                v.add(line.trim());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                assert br != null;
                br.close();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        return v;
    }
}
