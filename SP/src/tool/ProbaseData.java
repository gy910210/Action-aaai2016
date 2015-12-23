package tool;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.NumberFormat;
import java.util.BitSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;

public class ProbaseData {

    /**
     * @param args
     * @author Gongyu
     */
    public static HashMap<String, Integer> termIdMap;
    public static Vector<String> idTermMap;
    public static HashMap<Integer, HashSet<Integer>> entityConceptSetMap;
    public static HashMap<Integer, HashSet<Integer>> conceptEntitySetMap;

    public static HashMap<Integer, HashMap<Integer, Double>> entityConceptSetMapWithTypicality;
    public static HashMap<Integer, HashMap<Integer, Double>> conceptEntitySetMapWithTypicality;

    public static String probaseFile = "probase/Isa_Iteration_Filter_10.txt";

    public static void loadConceptAndEntityMap(String file) {
        System.out.println("Start loadConceptAndEntityMap...");
        long startTime = System.currentTimeMillis();

        if (!file.equals(""))
            ProbaseData.probaseFile = file;

        entityConceptSetMap = new HashMap<>(5000, 1f);
        conceptEntitySetMap = new HashMap<>(5000, 1f);
        termIdMap = new HashMap<>();
        idTermMap = new Vector<>();

        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(probaseFile));
            String line;
            if (file.equals(""))
                br.readLine();
            int index = 0;
            while ((line = br.readLine()) != null) {
                if (line.equals(""))
                    continue;

                String[] strs = line.split("\t");
                if (strs.length < 2)
                    continue;

                int conceptId;
                int entityId;
                if (termIdMap.containsKey(strs[0].toLowerCase()))
                    conceptId = termIdMap.get(strs[0].toLowerCase());
                else {
                    conceptId = index;
                    termIdMap.put(strs[0].toLowerCase(), index);
                    idTermMap.add(strs[0].toLowerCase());
                    index++;
                }
                if (termIdMap.containsKey(strs[1].toLowerCase()))
                    entityId = termIdMap.get(strs[1].toLowerCase());
                else {
                    entityId = index;
                    termIdMap.put(strs[1].toLowerCase(), index);
                    idTermMap.add(strs[1].toLowerCase());
                    index++;
                }

                HashSet<Integer> v;
                if (conceptEntitySetMap.containsKey(conceptId)) {
                    v = conceptEntitySetMap.get(conceptId);
                    if (v == null) {
                        v = new HashSet<>();
                        v.add(entityId);
                    } else {
                        v.add(entityId);
                    }
                } else {
                    v = new HashSet<>();
                    v.add(entityId);
                }
                conceptEntitySetMap.put(conceptId, v);

                v = null;
                if (entityConceptSetMap.containsKey(entityId)) {
                    v = entityConceptSetMap.get(entityId);
                    if (v == null) {
                        v = new HashSet<>();
                        v.add(conceptId);
                    } else {
                        v.add(conceptId);
                    }
                } else {
                    v = new HashSet<>();
                    v.add(conceptId);
                }
                entityConceptSetMap.put(entityId, v);
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        long endTime = System.currentTimeMillis();

        System.out.println("loadConceptAndEntityMap running time�� " + (endTime - startTime) / 1000 + "s");
        System.out.println("loadConceptAndEntityMap done!");
    }

    public void loadConceptAndEntityMapWithTypicality() {
        System.out.println("Start loadConceptAndEntityMapWithTypicality...");
        long startTime = System.currentTimeMillis();

        termIdMap = new HashMap<>(10000, 1f);
        idTermMap = new Vector<>();
        entityConceptSetMapWithTypicality = new HashMap<>(5000, 1f);
        conceptEntitySetMapWithTypicality = new HashMap<>(5000, 1f);

        BufferedReader br;

        try {
            br = new BufferedReader(new FileReader(probaseFile));
            String line;
            br.readLine();
            int index = 0;
            while ((line = br.readLine()) != null) {
                if (line.equals(""))
                    continue;
                String[] strs = line.split("\t");
                if (strs.length < 10)
                    continue;

                double entityConceptTypicality;
                double conceptEntityTypicality;
                try {
                    entityConceptTypicality = Double.parseDouble(strs[2]) / Double.parseDouble(strs[9]);
                    conceptEntityTypicality = Double.parseDouble(strs[2]) / Double.parseDouble(strs[4]);
                } catch (Exception e) {
                    continue;
                }

                int conceptId;
                int entityId;
                if (termIdMap.containsKey(strs[0].toLowerCase()))
                    conceptId = termIdMap.get(strs[0].toLowerCase());
                else {
                    conceptId = index;
                    termIdMap.put(strs[0].toLowerCase(), index);
                    idTermMap.add(strs[0].toLowerCase());
                    index++;
                }
                if (termIdMap.containsKey(strs[1].toLowerCase()))
                    entityId = termIdMap.get(strs[1].toLowerCase());
                else {
                    entityId = index;
                    termIdMap.put(strs[1].toLowerCase(), index);
                    idTermMap.add(strs[1].toLowerCase());
                    index++;
                }
                //=====entityConceptTypicality=====
                HashMap<Integer, Double> v;
                if (entityConceptSetMapWithTypicality.containsKey(entityId)) {
                    v = entityConceptSetMapWithTypicality.get(entityId);
                    if (v == null) {
                        v = new HashMap<>();
                        v.put(conceptId, entityConceptTypicality);
                    } else {
                        v.put(conceptId, entityConceptTypicality);
                    }
                } else {
                    v = new HashMap<>();
                    v.put(conceptId, entityConceptTypicality);
                }
                entityConceptSetMapWithTypicality.put(entityId, v);

                //=====conceptEntitySetMapWithTypicality=====
                v = null;
                if (conceptEntitySetMapWithTypicality.containsKey(conceptId)) {
                    v = conceptEntitySetMapWithTypicality.get(conceptId);
                    if (v == null) {
                        v = new HashMap<>();
                        v.put(entityId, conceptEntityTypicality);
                    } else {
                        v.put(entityId, conceptEntityTypicality);
                    }
                } else {
                    v = new HashMap<>();
                    v.put(entityId, conceptEntityTypicality);
                }
                conceptEntitySetMapWithTypicality.put(conceptId, v);
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        long endTime = System.currentTimeMillis();

        System.out.println("loadConceptAndEntityMapWithTypicality running time�� " + (endTime - startTime) / 1000 + "s");
        System.out.println("loadConceptAndEntityMapWithTypicality done!");
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        ProbaseData.loadConceptAndEntityMap("");
        for (int i = 10; i <= 20; i++) {
            String term = ProbaseData.idTermMap.get(i);
            int id = ProbaseData.termIdMap.get(term);
            System.out.println(term + "\t" + id);
        }
    }

}
