package lexicon;


import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Vector;

public class GetCoverageVector {
    public Vector<Double> getCoverageVectorAll(String file) {
        Vector<Double> coverage = new Vector<>();
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                if (line.equals(""))
                    continue;
                String[] strs = line.split("\t");
                coverage.add(Double.parseDouble(strs[2]));
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return coverage;
    }
}
