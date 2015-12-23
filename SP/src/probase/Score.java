package probase;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class Score {

    class ConceptScore {
        String concept;
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
    private HashMap<String, Double> conceptFrequency;
    private HashMap<String, Double> verbTotalVerbConceptFrequency;
    private double totalConceptFrequency;

    private String type;

    public Score(String verb, String type) {
        this.verb = verb;
        this.type = type;
        init();
    }

    public static void build(String type) {
        HashMap<String, Double> _conceptFrequency = new HashMap<>();
        File dir = new File("result_" + type + "/freq_0226");
        BufferedReader br;
        BufferedWriter bw;
        try {
            for (File file : dir.listFiles()) {
                br = new BufferedReader(new FileReader(file));
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split("\t");
                    double freq = Double.parseDouble(parts[1]);
                    if (_conceptFrequency.containsKey(parts[0])) {
                        double temp = _conceptFrequency.get(parts[0]) + freq;
                        _conceptFrequency.put(parts[0], temp);
                    } else
                        _conceptFrequency.put(parts[0], freq);
                }
                br.close();
            }

            bw = new BufferedWriter(new FileWriter("result_" + type + "/score_0226/_total_.txt"));
            for (String key : _conceptFrequency.keySet()) {
                bw.write(key + "\t" + String.valueOf(_conceptFrequency.get(key)));
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
        conceptFrequency = new HashMap<>();
        verbTotalVerbConceptFrequency = new HashMap<>();
        totalConceptFrequency = 0;

        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader("result_" + type + "/score_0226/_total_.txt"));
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\t");
                double freq = Double.parseDouble(parts[1]);
                conceptFrequency.put(parts[0], freq);
                totalConceptFrequency += freq;
            }
            br.close();

            double totalVerbConceptFrequency = 0;
            br = new BufferedReader(new FileReader("result_" + type + "/freq_0226/" + verb + "_concept_freq.txt"));
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

    public void getScore() {
        BufferedReader br;
        BufferedWriter bw;
        try {
            br = new BufferedReader(new FileReader("result_" + type + "/freq_0226/" + verb + "_concept_freq.txt"));
            String line;
            List<ConceptScore> scoreList = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\t");
                double freq = Double.parseDouble(parts[1]);
                double proVerbConcept = freq / verbTotalVerbConceptFrequency.get(verb);
                double proConcept = conceptFrequency.get(parts[0]) / totalConceptFrequency;
                double score = proVerbConcept * Math.log(proVerbConcept / proConcept);
                ConceptScore cs = new ConceptScore();
                cs.concept = parts[0];
                cs.score = score;
                scoreList.add(cs);
            }
            br.close();
            Collections.sort(scoreList, new cmp());

            bw = new BufferedWriter(new FileWriter("result_" + type + "/score_0226/" + verb + ".txt"));
            for (ConceptScore cs : scoreList) {
                bw.write(cs.concept + "\t" + String.valueOf(cs.score));
                bw.newLine();
            }
            bw.flush();
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
