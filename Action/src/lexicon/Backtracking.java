package lexicon;

import tool.ProbaseData;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Set;
import java.util.Vector;

public class Backtracking {

    private static final String rootPath = "result_0.2/";
    private static final String inputPath = "input_parameter_1770/";

    public Vector<Integer> conceptList;
    public Vector<Double> coverageVector;

    private String verb;
    private int K;
    private int SUM;
    private double w;
    private int ck;
    private double cp;
    private double bestp;
    private int[] bestx;
    private int[] x;
    private String type;

    private void init(String verb, int K, double w, String type) {
        this.verb = verb;
        this.type = type;
        coverageVector = new GetCoverageVector().getCoverageVectorAll(inputPath + verb + "_coverage_" + type + ".txt");
        conceptList = new GetConceptList().getConceptListAll(inputPath + verb + "_coverage_" + type + ".txt");

        this.K = K;
        this.w = w;
        this.SUM = conceptList.size();

        this.ck = 0;
        this.cp = 0.0;
        this.bestp = 0.0;
        this.x = new int[SUM];
        this.bestx = new int[SUM];
    }

    private void backtrack(int i) {
        //new condition
        if (SUM - i < K - ck)
            return;
        ////////////////////

        if (i >= SUM)
            return;

        if (ck == K) {
            if (cp > bestp) {
                bestp = cp;
                for (int j = 0; j < x.length; j++)
                    bestx[j] = x[j];
            }
            return;
        }

        if ((satisfy(i)) && (bound(i - 1) > bestp)) {
            ck += 1;
            cp += coverageVector.get(i);
            x[i] = 1;
            backtrack(i + 1);
            x[i] = 0;
            ck -= 1;
            cp -= coverageVector.get(i);
        }

        if (bound(i) > bestp) {
            x[i] = 0;
            backtrack(i + 1);
        }
    }

    private double bound(int i) {
        if (coverageVector.size() - (i + 1) < K - ck)
            return bestp;

        double b = cp;

        int tmp = i;
        for (int index = 1; index <= K - ck; index++) {
            b += coverageVector.get(tmp + 1);
            tmp++;
        }
        return b;
    }

    private boolean satisfy(int i) {
        if (ck >= K)
            return false;

        for (int j = 0; j < i; j++) {
            if (x[j] == 1) {
                double coverage = getOverlapFromProbase(conceptList.get(i), conceptList.get(j));
                if (coverage > w)
                    return false;
            }
        }
        return true;
    }

    private double getOverlapFromProbase(Integer c1, Integer c2) {
        Set<Integer> v1 = ProbaseData.conceptEntitySetMap.get(c1);
        Set<Integer> v2 = ProbaseData.conceptEntitySetMap.get(c2);

        double sum = 0.0;

        int v1_size = v1.size();
        int v2_size = v2.size();
        if (v1_size <= v2_size) {
            for (Integer item : v1) {
                if (v2.contains(item))
                    sum += 1;
            }
        } else {
            for (Integer item : v2) {
                if (v1.contains(item))
                    sum += 1;
            }
        }

        int min;
        if (v1_size <= v2_size) {
            min = v1_size;
        } else {
            min = v2_size;
        }

        return sum / min;
    }

    public void print(String thr) {
        BufferedWriter bw;
        try {
            bw = new BufferedWriter(new FileWriter(rootPath + verb + "_" + thr + "_" + type + ".txt"));
            for (int j = 0; j < bestx.length; j++) {
                if (bestx[j] == 1) {
                    bw.write(ProbaseData.idTermMap.get(conceptList.get(j)) + "\t" + String.valueOf(coverageVector.get(j)));
                    bw.newLine();
                }
            }

            bw.flush();
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void doBackTracking(String verb, int K, double w, String type) {
        init(verb, K, w, type);
        backtrack(0);
    }
}
