package tool;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;

public class GetVerbFreqMap {
    public HashMap<String, Double> getVerbFreqMap(String fileName, int buckets_num, int mult) {
        HashMap<String, Double> re = new HashMap<String, Double>();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(fileName));
            br.readLine();
            String line = null;
            while ((line = br.readLine()) != null) {
                if (line.equals(""))
                    continue;
                String[] strs = line.trim().split("\t");
                int freq = Integer.parseInt(strs[1]);
                for (int i = buckets_num; i >= 0; i--) {
                    if (freq >= i * mult) {
                        re.put(strs[0], (double) i * mult);
                        break;
                    }
                }
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return re;
    }

    public HashMap<String, Double> getVerbFreqMap2(String fileName, int jian, int mult) {
        HashMap<String, Double> re = new HashMap<String, Double>();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(fileName));
            br.readLine();
            String line = null;
            boolean flag = false;
            int buckets_num = 0;
            while ((line = br.readLine()) != null) {
                if (line.equals(""))
                    continue;
                String[] strs = line.trim().split("\t");
                int freq = Integer.parseInt(strs[1]);

                if (flag == false) {
                    flag = true;
                    int tmp = String.valueOf(freq).length();
                    buckets_num = 1;
                    for (int i = 1; i <= tmp - jian; i++)
                        buckets_num *= 10;
                    re.put("##", (double) (buckets_num * mult));
                }

                for (int i = buckets_num; i >= 0; i = i - mult) {
                    if (freq >= i * mult) {
                        re.put(strs[0], (double) i * mult);
                        break;
                    }
                }
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return re;
    }
}
