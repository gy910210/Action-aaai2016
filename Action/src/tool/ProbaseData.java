package tool;


import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;

public class ProbaseData {
    public static HashMap<String, Integer> termIdMap;
    public static Vector<String> idTermMap;
    public static HashMap<Integer, HashSet<Integer>> entityConceptSetMap;
    public static HashMap<Integer, HashSet<Integer>> conceptEntitySetMap;

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
                if (line.equals("")) {
                    continue;
                }

                String[] strs = line.split("\t");
                if (strs.length < 2) {
                    continue;
                }

                int conceptId;
                int entityId;

                if (termIdMap.containsKey(strs[0].toLowerCase())) {
                    conceptId = termIdMap.get(strs[0].toLowerCase());
                } else {
                    conceptId = index;
                    termIdMap.put(strs[0].toLowerCase(), index);
                    idTermMap.add(strs[0].toLowerCase());
                    index++;
                }

                if (termIdMap.containsKey(strs[1].toLowerCase())) {
                    entityId = termIdMap.get(strs[1].toLowerCase());
                }  else {
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

        System.out.println("loadConceptAndEntityMap running time:" + (endTime - startTime) / 1000 + "s");
        System.out.println("loadConceptAndEntityMap done!");
    }
}
