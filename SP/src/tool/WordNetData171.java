package tool;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.HashSet;

public class WordNetData171 {
    private String root = "WordNet\\wordnet1.7.1\\prolog\\";

    public HashMap<Long, HashSet<String>> getIdToSynsetMap() {
        HashMap<Long, HashSet<String>> idToSynsetsMap = new HashMap<Long, HashSet<String>>();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(root + "wn_s.pl"));
            String line = null;
            while ((line = br.readLine()) != null) {
                if (line.equals(""))
                    continue;
                if (!line.trim().startsWith("s(1"))
                    continue;

                String[] strs = line.trim().split(",");
                long synsetKey = Long.parseLong(strs[0].trim().substring(2));
                String word = strs[2].trim().substring(1, strs[2].trim().length() - 1);

                if (!idToSynsetsMap.containsKey(synsetKey)) {
                    HashSet<String> tmp = new HashSet<String>();
                    tmp.add(word);
                    idToSynsetsMap.put(synsetKey, tmp);
                } else {
                    HashSet<String> tmp = idToSynsetsMap.get(synsetKey);
                    tmp.add(word);
                    idToSynsetsMap.put(synsetKey, tmp);
                }
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return idToSynsetsMap;
    }


    public HashMap<Long, HashSet<Long>> getSynsetConceptEntityMap() {
        HashMap<Long, HashSet<Long>> synsetConceptEntityMap = new HashMap<Long, HashSet<Long>>();
        String nounHyper = root + "wn_hyp.pl";
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(nounHyper));
            String line = null;
            while ((line = br.readLine()) != null) {
                String[] strs = line.trim().substring(4, line.trim().length() - 2).split(",");
                if (!strs[0].startsWith("1"))
                    continue;
                if (!strs[1].startsWith("1"))
                    continue;
                long down = Long.parseLong(strs[0]);
                long up = Long.parseLong(strs[1]);

                if (!synsetConceptEntityMap.containsKey(up)) {
                    HashSet<Long> tmp = new HashSet<Long>();
                    tmp.add(down);
                    synsetConceptEntityMap.put(up, tmp);
                } else {
                    HashSet<Long> tmp = synsetConceptEntityMap.get(up);
                    tmp.add(down);
                    synsetConceptEntityMap.put(up, tmp);
                }
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return synsetConceptEntityMap;
    }

    public HashMap<Long, HashSet<Long>> getSynsetEntityConceptMap() {
        HashMap<Long, HashSet<Long>> synsetEntityConceptMap = new HashMap<Long, HashSet<Long>>();
        String nounHyper = root + "wn_hyp.pl";
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(nounHyper));
            String line = null;
            while ((line = br.readLine()) != null) {
                String[] strs = line.trim().substring(4, line.trim().length() - 2).split(",");
                if (!strs[0].startsWith("1"))
                    continue;
                if (!strs[1].startsWith("1"))
                    continue;
                long down = Long.parseLong(strs[0]);
                long up = Long.parseLong(strs[1]);

                if (!synsetEntityConceptMap.containsKey(down)) {
                    HashSet<Long> tmp = new HashSet<Long>();
                    tmp.add(up);
                    synsetEntityConceptMap.put(down, tmp);
                } else {
                    HashSet<Long> tmp = synsetEntityConceptMap.get(down);
                    tmp.add(up);
                    synsetEntityConceptMap.put(down, tmp);
                }
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return synsetEntityConceptMap;
    }


    public HashMap<String, Long> getSenseSynsetMap() {
        HashMap<String, Long> senseSynsetMap = new HashMap<String, Long>();
        String senseSynsetMapFile = "WordNet\\wordnet1.7.1\\WordNet-1.7.1\\dict\\index.sense";
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(senseSynsetMapFile));
            String line = null;
            while ((line = br.readLine()) != null) {
                String[] strs = line.trim().split(" ");
                String[] senseParts = strs[0].trim().split("%");
                if (!senseParts[1].startsWith("1"))
                    continue;

                senseSynsetMap.put(strs[0].trim(), 100000000 + Long.parseLong(strs[1].trim()));
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return senseSynsetMap;
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }

}
