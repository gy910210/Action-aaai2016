package wordnet;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.HashSet;

public class CollectorWN {
    private String verb;
    private HashMap<String, Integer> entityFrequency;

    private String type;
    private String version;

    public CollectorWN(String verb, String type, String version) {
        this.verb = verb;
        this.type = type;
        this.version = version;
        init();
    }

    private void init() {
        // TODO Auto-generated method stub
        entityFrequency = new HashMap<>();
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(version + "/ngram_wn/" + type + "/" + verb + ".txt"));
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                if (line.equals(""))
                    continue;
                String[] parts = line.split("\t");
                entityFrequency.put(parts[0].trim(), Integer.parseInt(parts[1].trim()));
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getVerbConceptSynsetFrequency(HashMap<Long, HashSet<String>> idToSynsetMap,
                                              HashMap<Long, HashSet<Long>> synsetConceptEntityMap,
                                              HashMap<Long, HashSet<Long>> synsetEntityConceptMap) {
        BufferedWriter bw;
        try {
            bw = new BufferedWriter(new FileWriter(version + "/result_" + type + "/freq_0226/" + verb + "_concept_freq.txt"));
            for (Long conceptId : idToSynsetMap.keySet()) {
                double freq = 0;

                if (!synsetConceptEntityMap.containsKey(conceptId))
                    continue;

                for (Long entityId : synsetConceptEntityMap.get(conceptId)) {
                    if (!synsetEntityConceptMap.containsKey(entityId))
                        continue;
                    for (String str : idToSynsetMap.get(entityId)) {
                        if (!entityFrequency.containsKey(str))
                            continue;
                        freq += (double) entityFrequency.get(str) / (double) synsetEntityConceptMap.get(entityId).size();
                    }
                }
                if (freq != 0) {
                    bw.write(String.valueOf(conceptId) + "\t" + freq);
                    bw.newLine();
                }
            }
            bw.flush();
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
