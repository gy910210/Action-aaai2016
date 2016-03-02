package entity_mi;

import tool.GetVerbs;

import java.io.*;
import java.util.HashMap;
import java.util.Vector;

public class DumpNgramData {

    public static void main(String[] args) throws IOException {
        Vector<String> verbs = new GetVerbs().getVerbs("verbs_1770.txt");
        for (String verb : verbs) {
            BufferedReader br_tuple = new BufferedReader(new FileReader("all_tuples/" + verb + ".txt"));
            BufferedWriter bw_subj = new BufferedWriter(new FileWriter("ngram_1770/subj/" + verb + ".txt"));
            BufferedWriter bw_obj = new BufferedWriter(new FileWriter("ngram_1770/obj/" + verb + ".txt"));

            HashMap<String, Integer> subj_hash = new HashMap<>();
            HashMap<String, Integer> obj_hash = new HashMap<>();
            br_tuple.readLine();
            String line;
            while ((line = br_tuple.readLine()) != null) {
                if (line.equals(""))
                    continue;
                String[] strs = line.split("\t");
                System.out.println(strs.length);
                System.out.print(line);
                int freq = Integer.parseInt(strs[2]);

                if (!strs[0].equals("")) {
                    if (!subj_hash.containsKey(strs[0])) subj_hash.put(strs[0], freq);
                    else subj_hash.put(strs[0], subj_hash.get(strs[0]) + freq);
                }

                if (!strs[1].equals("")) {
                    if (!obj_hash.containsKey(strs[1])) obj_hash.put(strs[1], freq);
                    else obj_hash.put(strs[1], obj_hash.get(strs[1]) + freq);
                }
            }
            br_tuple.close();

            bw_subj.write("subj" + "\t" + "freq");
            bw_subj.newLine();
            for (String subj : subj_hash.keySet()) {
                bw_subj.write(subj + "\t" + String.valueOf(subj_hash.get(subj)));
                bw_subj.newLine();
            }
            bw_subj.close();

            bw_obj.write("obj" + "\t" + "freq");
            bw_obj.newLine();
            for (String obj : obj_hash.keySet()) {
                bw_obj.write(obj + "\t" + String.valueOf(obj_hash.get(obj)));
                bw_obj.newLine();
            }
            bw_obj.close();
        }
    }

}
