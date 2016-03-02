package lexicon;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Vector;

public class GetConceptList {
    public Vector<Integer> getConceptListAll(String fileName) {
        BufferedReader br = null;
        Vector<Integer> v = new Vector<>();
        try {
            br = new BufferedReader(new FileReader(fileName));
            String line;
            while ((line = br.readLine()) != null) {
                if (line.equals(""))
                    continue;
                v.add(Integer.parseInt(line.split("\t")[0]));
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
