package parameter;

import entity_mi.GetEntityFreq;
import entity_mi.GetEntityMI;
import tool.ProbaseData;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class DumpCoverageVector {
    HashMap<Integer, Double> conceptCoverageMap;

    @SuppressWarnings("unchecked")
    public void dumpCoverageAndConceptVectorAll(
            String fileEntityMI, String fileEntityEntropy, String fileEntityFreq, String fileCoverage) {
        BufferedWriter bwFileCoverage;

        HashMap<String, Double> entityMIMap = new GetEntityMI()
                .getEntityMI(fileEntityMI);

        HashMap<String, Double> entityEntropyMap = new GetEntityMI()
                .getEntityMI(fileEntityEntropy);

        HashMap<String, Double> entityFreqMap = new GetEntityFreq()
                .getEntityFreq(fileEntityFreq);
        if (entityMIMap == null) {
            try {
                bwFileCoverage = new BufferedWriter(
                        new FileWriter(fileCoverage));
                bwFileCoverage.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }

        conceptCoverageMap = new HashMap<>();
        Vector<Integer> conceptList = new Vector<>();

        try {
            for (Integer id : ProbaseData.conceptEntitySetMap.keySet()) {
                double coverage = getConceptCoverage(ProbaseData.idTermMap.get(id),
                        entityMIMap, entityEntropyMap, entityFreqMap);
                if (coverage != 0) {
                    conceptCoverageMap.put(id, coverage);
                    conceptList.add(id);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
        Collections.sort(conceptList, new cmp());
        try {
            bwFileCoverage = new BufferedWriter(new FileWriter(fileCoverage));

            for (Integer id : conceptList) {
                bwFileCoverage.write(String.valueOf(id) + "\t"
                        + ProbaseData.idTermMap.get(id) + "\t"
                        + String.valueOf(conceptCoverageMap.get(id)));
                bwFileCoverage.newLine();
                bwFileCoverage.flush();
            }
            bwFileCoverage.flush();
            bwFileCoverage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private double getConceptCoverage(String concept, HashMap<String, Double> entityMIMap,
                                      HashMap<String, Double> entityEntropyMap,
                                      HashMap<String, Double> entityFreqMap) throws IOException {

        double score = 0.0;
        int conceptID = ProbaseData.termIdMap.get(concept);

        for (String entity : entityFreqMap.keySet()) {
            if (!ProbaseData.termIdMap.containsKey(entity))
                continue;
            int enitityID = ProbaseData.termIdMap.get(entity);

            if (!concept.equals(entity))
                if (!ProbaseData.conceptEntitySetMap.get(conceptID).contains(enitityID))
                    continue;


            if (entityEntropyMap.containsKey(entity) && entityMIMap.containsKey(entity)) {
                if (entityMIMap.get(entity) > 0)
                    score += entityEntropyMap.get(entity);
                else
                    score -= entityEntropyMap.get(entity);
            }
        }
        return score;
    }

    class cmp implements Comparator {
        @Override
        public int compare(Object arg0, Object arg1) {
            int c1 = (int) arg0;
            int c2 = (int) arg1;

            if (conceptCoverageMap.get(c1) > conceptCoverageMap.get(c2))
                return -1;
            else if (Objects.equals(conceptCoverageMap.get(c1), conceptCoverageMap.get(c2)))
                return 0;
            else
                return 1;
        }
    }
}