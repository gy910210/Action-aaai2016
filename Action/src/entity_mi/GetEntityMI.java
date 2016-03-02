package entity_mi;


import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;

public class GetEntityMI {
    public HashMap<String, Double> getEntityMI(String file) {
        HashMap<String, Double> hashMap = new HashMap<>();
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                String[] strs = line.split("\t");
                hashMap.put(strs[0], Double.parseDouble(strs[1]));
            }
            br.close();
        } catch (Exception e) {
            // e.printStackTrace();
            return null;
        }
        return hashMap;
    }

}
