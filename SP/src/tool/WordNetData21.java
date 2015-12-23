package tool;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;

public class WordNetData21 {

    /**
     * @param args
     */
    private String root = "WordNet/WordNet-2.1/dict/";

    public HashMap<Long, HashSet<String>> getIdToSynsetMap() {
        HashMap<Long, HashSet<String>> idToSynsetMap = new HashMap<>();
        BufferedReader br;

        try {
            br = new BufferedReader(new FileReader(root + "data.noun"));
            String line;
            while ((line = br.readLine()) != null) {
                if (line.equals(""))
                    continue;
                //System.out.println(line);
                HashSet<String> tmp = new HashSet<>();
                String[] strs = line.trim().split(" ");
                if (!strs[2].equals("n"))
                    continue;
                int word_count = Integer.valueOf(strs[3], 16);

                int count = 0;
                for (int i = 4; i < strs.length - 1; i++) {
                    if (count == word_count)
                        break;
                    tmp.add(strs[i].trim());
                    i++;
                    count++;
                }

                idToSynsetMap.put(100000000 + Long.parseLong(strs[0]), tmp);
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return idToSynsetMap;
    }

    public HashMap<Long, HashSet<Long>> getSynsetConceptEntityMap() {
        HashMap<Long, HashSet<Long>> synsetConceptEntityMap = new HashMap<>();
        BufferedReader br;

        try {
            br = new BufferedReader(new FileReader(root + "data.noun"));
            String line;
            while ((line = br.readLine()) != null) {
                if (line.equals(""))
                    continue;

                String[] strs = line.trim().split(" ");
                if (!strs[2].equals("n"))
                    continue;
                int word_count = Integer.valueOf(strs[3], 16);
                int ptr_count = Integer.parseInt(strs[3 + 2 * word_count + 1]);
                int ptr_index = 3 + 2 * word_count + 2;

                long source_synset = 100000000 + Long.parseLong(strs[0]);
                int count = 0;

                HashSet<Long> enitySet = new HashSet<>();
                for (int i = ptr_index; i < strs.length; i += 4) {
                    if (count == ptr_count)
                        break;
                    count++;
                    long target_synset = 100000000 + Long.parseLong(strs[i + 1]);
                    if (strs[i].equals("~") || strs[i].equals("~i"))
                        enitySet.add(target_synset);
                }
                if (enitySet.size() != 0)
                    synsetConceptEntityMap.put(source_synset, enitySet);
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return synsetConceptEntityMap;
    }

    public HashMap<Long, HashSet<Long>> getSynsetEntityConceptMap() {
        HashMap<Long, HashSet<Long>> synsetEntityConceptMap = new HashMap<>();
        BufferedReader br;

        try {
            br = new BufferedReader(new FileReader(root + "data.noun"));
            String line;
            while ((line = br.readLine()) != null) {
                if (line.equals(""))
                    continue;

                String[] strs = line.trim().split(" ");
                if (!strs[2].equals("n"))
                    continue;
                int word_count = Integer.valueOf(strs[3], 16);
                int ptr_count = Integer.parseInt(strs[3 + 2 * word_count + 1]);
                int ptr_index = 3 + 2 * word_count + 2;

                long source_synset = 100000000 + Long.parseLong(strs[0]);
                int count = 0;

                HashSet<Long> enitySet = new HashSet<>();
                for (int i = ptr_index; i < strs.length; i += 4) {
                    if (count == ptr_count)
                        break;
                    count++;
                    long target_synset = 100000000 + Long.parseLong(strs[i + 1]);
                    if (strs[i].equals("@") || strs[i].equals("@i"))
                        enitySet.add(target_synset);
                }
                if (enitySet.size() != 0)
                    synsetEntityConceptMap.put(source_synset, enitySet);
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return synsetEntityConceptMap;
    }

    public Vector<String[]> getSenseIndex() {
        Vector<String[]> senseIndex = new Vector<>();
        BufferedReader br;

        try {
            br = new BufferedReader(new FileReader(root + "index.sense"));
            String line;
            while ((line = br.readLine()) != null) {
                if (line.equals(""))
                    continue;
                String[] strs = line.trim().split(" ");
                senseIndex.add(strs);
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return senseIndex;
    }

}
