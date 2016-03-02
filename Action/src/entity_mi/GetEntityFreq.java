package entity_mi;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;

public class GetEntityFreq {

    public HashMap<String, Double> getEntityFreq(String file) {
        HashMap<String, Double> hashMap = new HashMap<>();
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(file));
            String line;
            double sum = 0.0;
            br.readLine();
            while ((line = br.readLine()) != null) {
                if (line.equals(""))
                    continue;
                String[] strs = line.split("\t");
                sum += Double.parseDouble(strs[1]);
                hashMap.put(strs[0], Double.parseDouble(strs[1]));
            }
            br.close();
            for (String key : hashMap.keySet()) {
                hashMap.put(key, hashMap.get(key) / sum);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hashMap;
    }
}
