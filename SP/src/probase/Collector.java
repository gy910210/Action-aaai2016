package probase;


import tool.ProbaseData;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;

public class Collector {

    /**
     * @param args
     * @author Gongyu
     */
    private String verb;
    private HashMap<String, Integer> entityFrequency;

    private String type;

    public Collector(String verb, String type) {
        this.verb = verb;
        this.type = type;
        init();
    }


    private void init() {
        // TODO Auto-generated method stub
        entityFrequency = new HashMap<>();
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader("ngram_1770/" + type + "/" + verb + ".txt"));
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

    public void getVerbConceptFrequency() {
        BufferedWriter bw;
        try {
            bw = new BufferedWriter(new FileWriter("result_" + type + "/freq_0226/" + verb + "_concept_freq.txt"));

            for (int conceptId : ProbaseData.conceptEntitySetMap.keySet()) {

                String concept = ProbaseData.idTermMap.get(conceptId);
                double freq = 0;

                for (Integer entityId : ProbaseData.conceptEntitySetMap.get(conceptId)) {
                    String entity = ProbaseData.idTermMap.get(entityId);
                    if (!entityFrequency.containsKey(entity))
                        continue;
                    if (!ProbaseData.entityConceptSetMap.containsKey(entityId))
                        continue;
                    freq += (double) entityFrequency.get(entity) / ProbaseData.entityConceptSetMap.get(entityId).size();
                }

                if (freq != 0) {
                    bw.write(concept + "\t" + freq);
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
