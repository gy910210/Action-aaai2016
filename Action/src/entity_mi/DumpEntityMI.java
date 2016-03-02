package entity_mi;

import tool.GetVerbs;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Vector;

public class DumpEntityMI {

    class EntityScore {
        String entity;
        double score;
    }

    class cmp implements Comparator<EntityScore> {

        @Override
        public int compare(EntityScore cs1, EntityScore cs2) {
            if (cs1.score > cs2.score)
                return -1;
            else if (cs1.score < cs2.score)
                return 1;
            else
                return 0;
        }
    }

    private static final String mi_rootPath = "entityMI/ngram_1770/";
    private HashMap<String, HashMap<String, Long>> verbEntityMap;
    private HashMap<String, Long> verbTotalMap;
    private HashMap<String, Long> entityMap;
    private Long entityTotal;
    private Vector<String> verbs;

    public DumpEntityMI() {
        super();
        init();
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        DumpEntityMI esp = new DumpEntityMI();
        esp.getEntityMI();
    }

    private void init() {
        verbEntityMap = new HashMap<>();
        verbTotalMap = new HashMap<>();
        entityMap = new HashMap<>();
        entityTotal = 0L;
        verbs = new GetVerbs().getVerbs("verbs_1770.txt");

        BufferedReader br;
        try {
            for (String verb : verbs) {
                br = new BufferedReader(new FileReader("ngram_1770/subj/" + verb + ".txt"));
                String line;
                br.readLine();
                HashMap<String, Long> temp = new HashMap<>();
                Long sum = 0L;
                while ((line = br.readLine()) != null) {
                    String[] strs = line.split("\t");
                    String entity = strs[0];
                    Long freq = Long.parseLong(strs[1]);
                    entityTotal += freq;
                    sum += freq;
                    temp.put(entity, freq);

                    if (entityMap.containsKey(entity)) {
                        Long l = entityMap.get(entity) + freq;
                        entityMap.put(entity, l);
                    } else {
                        entityMap.put(entity, freq);
                    }
                }
                verbEntityMap.put(verb, temp);
                verbTotalMap.put(verb, sum);
                br.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getEntityMI() {
        try {
            BufferedWriter bw;
            for (String verb : verbs) {
                System.out.println(verb);
                bw = new BufferedWriter(new FileWriter(mi_rootPath + verb + "_subj.txt"));
                Vector<EntityScore> _entityList = new Vector<>();
                for (String entity : verbEntityMap.get(verb).keySet()) {
                    double pxy = (double) verbEntityMap.get(verb).get(entity) / entityTotal;
                    double px = (double) verbTotalMap.get(verb) / entityTotal;
                    double py = (double) entityMap.get(entity) / entityTotal;
                    double score = pxy * Math.log(pxy / (px * py));

                    EntityScore es = new EntityScore();
                    es.entity = entity;
                    es.score = score;
                    _entityList.add(es);
                }

                Collections.sort(_entityList, new cmp());
                for (EntityScore es : _entityList) {
                    bw.write(es.entity + "\t" + String.valueOf(es.score));
                    bw.newLine();
                    bw.flush();
                }
                bw.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
