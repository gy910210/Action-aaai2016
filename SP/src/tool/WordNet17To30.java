package tool;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.HashSet;

public class WordNet17To30 {

    private HashMap<String, HashSet<String>> getSynsetSenseMap(String file) {
        BufferedReader br;
        HashMap<String, HashSet<String>> synsetSenseMap = new HashMap<String, HashSet<String>>();
        try {
            br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                String[] strs = line.trim().split("\t");
                if (!(strs[0].split("%")[1]).startsWith("1"))
                    continue;
                String id = "1".concat(strs[1]);
                if (!synsetSenseMap.containsKey(id)) {
                    HashSet<String> tmp = new HashSet<>();
                    tmp.add(strs[0]);
                    synsetSenseMap.put(id, tmp);
                } else {
                    HashSet<String> tmp = synsetSenseMap.get(id);
                    tmp.add(strs[0]);
                    synsetSenseMap.put(id, tmp);
                }
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return synsetSenseMap;
    }

    private HashMap<String, String> getSynsetMap(String file) {
        BufferedReader br;
        HashMap<String, String> synsetMap = new HashMap<>();
        try {
            br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                if (line.equals(""))
                    continue;
                String[] strs = line.trim().split("\t");
                if (!strs[0].equals("n"))
                    continue;
                synsetMap.put("1".concat(strs[1]), "1".concat(strs[2]));
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return synsetMap;
    }
}
