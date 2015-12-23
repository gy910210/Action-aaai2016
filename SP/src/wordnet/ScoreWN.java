package wordnet;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class ScoreWN {

    class ConceptScore {
        Long conceptId;
        double score;
    }

    class cmp implements Comparator<ConceptScore> {

        @Override
        public int compare(ConceptScore cs1, ConceptScore cs2) {
            // TODO Auto-generated method stub
            if (cs1.score > cs2.score)
                return -1;
            else if (cs1.score < cs2.score)
                return 1;
            else
                return 0;
        }
    }

    String verb;
    private HashMap<Long, Double> conceptSynsetFrequency;
    private HashMap<String, Double> verbTotalVerbConceptFrequency;
    private double totalConceptFrequency;

    private String version;
    private String type;

    public ScoreWN(String verb, String version, String type) {
        this.verb = verb;
        this.version = version;
        this.type = type;
        init();
    }

    public static void build(String version, String type) {
        HashMap<Long, Double> _conceptFrequency = new HashMap<>();
        File dir = new File(version + "/result_" + type + "/freq_0226");
        BufferedReader br;
        BufferedWriter bw;
        try {
            for (File file : dir.listFiles()) {
                br = new BufferedReader(new FileReader(file));
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split("\t");
                    double freq = Double.parseDouble(parts[1]);
                    if (_conceptFrequency.containsKey(Long.parseLong(parts[0]))) {
                        double temp = _conceptFrequency.get(Long.parseLong(parts[0])) + freq;
                        _conceptFrequency.put(Long.parseLong(parts[0]), temp);
                    } else
                        _conceptFrequency.put(Long.parseLong(parts[0]), freq);
                }
                br.close();
            }

            bw = new BufferedWriter(new FileWriter(version + "/result_" + type + "/score_0226/total_.txt"));
            for (Long key : _conceptFrequency.keySet()) {
                bw.write(String.valueOf(key) + "\t" + String.valueOf(_conceptFrequency.get(key)));
                bw.newLine();
            }
            bw.flush();
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init() {
        // TODO Auto-generated method stub
        conceptSynsetFrequency = new HashMap<>();
        verbTotalVerbConceptFrequency = new HashMap<>();
        totalConceptFrequency = 0;

        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(version + "\\result_" + type + "\\score_0226\\total_.txt"));
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\t");
                double freq = Double.parseDouble(parts[1]);
                conceptSynsetFrequency.put(Long.parseLong(parts[0]), freq);
                totalConceptFrequency += freq;
            }
            br.close();

            double totalVerbConceptFrequency = 0;
            br = new BufferedReader(new FileReader(version + "\\result_" + type + "\\freq_0226\\" + verb + "_concept_freq.txt"));
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\t");
                double freq = Double.parseDouble(parts[1]);
                totalVerbConceptFrequency += freq;
            }
            br.close();
            verbTotalVerbConceptFrequency.put(verb, totalVerbConceptFrequency);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getScore(HashMap<Long, HashSet<String>> idToSynsetMap) {
        BufferedReader br;
        BufferedWriter bw;
        try {
            br = new BufferedReader(new FileReader(version + "\\result_" + type + "\\freq_0226\\" + verb + "_concept_freq.txt"));
            String line;
            List<ConceptScore> scoreList = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\t");
                double freq = Double.parseDouble(parts[1]);
                double proVerbConcept = freq / verbTotalVerbConceptFrequency.get(verb);
                double proConcept = conceptSynsetFrequency.get(Long.parseLong(parts[0])) / totalConceptFrequency;
                double score = proVerbConcept * Math.log(proVerbConcept / proConcept);
                ConceptScore cs = new ConceptScore();
                cs.conceptId = Long.parseLong(parts[0]);
                cs.score = score;
                scoreList.add(cs);
            }
            br.close();
            Collections.sort(scoreList, new cmp());

            bw = new BufferedWriter(new FileWriter(version + "\\result_" + type + "\\score_0226\\" + verb + ".txt"));
            for (ConceptScore cs : scoreList) {
                bw.write(String.valueOf(cs.conceptId) + "\t" + getSynsetString(idToSynsetMap.get(cs.conceptId)) + "\t" + String.valueOf(cs.score));
                bw.newLine();
            }
            bw.flush();
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getSynsetString(HashSet<String> set) {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        for (String str : set) {
            sb.append(str);
            sb.append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(")");
        return sb.toString();
    }
}
