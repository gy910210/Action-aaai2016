package tool;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Vector;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class WordNetData30 {
    public static HashMap<String, HashSet<String>> entityConceptSetMap;
    public static HashMap<String, HashSet<String>> conceptEntitySetMap;

    private String root = "WordNet/";

    public HashMap<Long, HashSet<String>> getIdToSynsetMap() {
        HashMap<Long, HashSet<String>> idToSynsetsMap = new HashMap<>();
        SAXReader reader = new SAXReader();
        String nounXml = root + "wordnet3.0/WN30/noun_removedtd.xml";
        try {
            Document doc = reader.read(new File(nounXml));
            Element root = doc.getRootElement();
            List<Element> synsetList = root.elements("synset");
            for (Element synset : synsetList) {
                long id = Long.parseLong(synset.attributeValue("id").trim().substring(1));
                id += 100000000;
                Element temp = synset.element("terms");
                List<Element> termList = temp.elements("term");
                HashSet<String> v = new HashSet<>();
                for (Element term : termList) {
                    v.add(term.getText().trim());
                }
                idToSynsetsMap.put(id, v);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return idToSynsetsMap;
    }

    public HashMap<Long, HashSet<Long>> getSynsetConceptEntityMap() {
        HashMap<Long, HashSet<Long>> synsetConceptEntityMap = new HashMap<>();
        String nounHyper = root + "wordnet3.0/WNprolog-3.0/prolog/wn_hyp.pl";
        String nounIns = root + "wordnet3.0/WNprolog-3.0/prolog/wn_ins.pl";
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(nounHyper));
            String line = null;
            while ((line = br.readLine()) != null) {
                String[] strs = line.trim().substring(4, line.trim().length() - 2).split(",");
                if (!strs[0].startsWith("1"))
                    continue;
                if (!strs[1].startsWith("1"))
                    continue;
                long down = Long.parseLong(strs[0]);
                long up = Long.parseLong(strs[1]);

                if (!synsetConceptEntityMap.containsKey(up)) {
                    HashSet<Long> tmp = new HashSet<>();
                    tmp.add(down);
                    synsetConceptEntityMap.put(up, tmp);
                } else {
                    HashSet<Long> tmp = synsetConceptEntityMap.get(up);
                    tmp.add(down);
                    synsetConceptEntityMap.put(up, tmp);
                }
            }
            br.close();

            br = new BufferedReader(new FileReader(nounIns));
            while ((line = br.readLine()) != null) {
                String[] strs = line.trim().substring(4, line.trim().length() - 2).split(",");
                if (!strs[0].startsWith("1"))
                    continue;
                if (!strs[1].startsWith("1"))
                    continue;
                long down = Long.parseLong(strs[0]);
                long up = Long.parseLong(strs[1]);

                if (!synsetConceptEntityMap.containsKey(up)) {
                    HashSet<Long> tmp = new HashSet<>();
                    tmp.add(down);
                    synsetConceptEntityMap.put(up, tmp);
                } else {
                    HashSet<Long> tmp = synsetConceptEntityMap.get(up);
                    tmp.add(down);
                    synsetConceptEntityMap.put(up, tmp);
                }
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return synsetConceptEntityMap;
    }

    public HashMap<Long, HashSet<Long>> getSynsetEntityConceptMap() {
        HashMap<Long, HashSet<Long>> synsetEntityConceptMap = new HashMap<>();
        String nounHyper = root + "wordnet3.0/WNprolog-3.0/prolog/wn_hyp.pl";
        String nounIns = root + "wordnet3.0/WNprolog-3.0/prolog/wn_ins.pl";
        BufferedReader br;

        try {
            br = new BufferedReader(new FileReader(nounHyper));
            String line;
            while ((line = br.readLine()) != null) {
                String[] strs = line.trim().substring(4, line.trim().length() - 2).split(",");
                if (!strs[0].startsWith("1"))
                    continue;
                if (!strs[1].startsWith("1"))
                    continue;
                long down = Long.parseLong(strs[0]);
                long up = Long.parseLong(strs[1]);

                if (!synsetEntityConceptMap.containsKey(down)) {
                    HashSet<Long> tmp = new HashSet<>();
                    tmp.add(up);
                    synsetEntityConceptMap.put(down, tmp);
                } else {
                    HashSet<Long> tmp = synsetEntityConceptMap.get(down);
                    tmp.add(up);
                    synsetEntityConceptMap.put(down, tmp);
                }
            }
            br.close();

            //////////////////
            br = new BufferedReader(new FileReader(nounIns));
            while ((line = br.readLine()) != null) {
                String[] strs = line.trim().substring(4, line.trim().length() - 2).split(",");
                if (!strs[0].startsWith("1"))
                    continue;
                if (!strs[1].startsWith("1"))
                    continue;
                long down = Long.parseLong(strs[0]);
                long up = Long.parseLong(strs[1]);

                if (!synsetEntityConceptMap.containsKey(down)) {
                    HashSet<Long> tmp = new HashSet<Long>();
                    tmp.add(up);
                    synsetEntityConceptMap.put(down, tmp);
                } else {
                    HashSet<Long> tmp = synsetEntityConceptMap.get(down);
                    tmp.add(up);
                    synsetEntityConceptMap.put(down, tmp);
                }
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return synsetEntityConceptMap;
    }

    public void writeWordNetFormat() {
        HashMap<Long, Vector<String>> idToSynsetsMap = new HashMap<Long, Vector<String>>();
        SAXReader reader = new SAXReader();
        String nounXml = root + "wordnet/WN30/noun_removedtd.xml";
        String nounHyper = root + "wordnet/WNprolog-3.0/prolog/wn_hyp.pl";
        String nounIns = root + "wordnet/WNprolog-3.0/prolog/wn_ins.pl";
        String outFormat = root + "WordNet_format_1.txt";

        BufferedReader br;
        BufferedWriter bw;
        try {
            Document doc = reader.read(new File(nounXml));
            Element root = doc.getRootElement();
            List<Element> synsetList = root.elements("synset");
            for (Element synset : synsetList) {
                long id = Long.parseLong(synset.attributeValue("id").trim().substring(1));
                id += 100000000;
                Element temp = synset.element("terms");
                List<Element> termList = temp.elements("term");
                Vector<String> v = new Vector<>();
                for (Element term : termList) {
                    v.add(term.getText().trim());
                }
                idToSynsetsMap.put(id, v);
            }
            System.out.println("...init idToSynsetsMap done!");

            br = new BufferedReader(new FileReader(nounHyper));
            bw = new BufferedWriter(new FileWriter(outFormat));
            String line = null;
            HashSet<Long> synsets = new HashSet<Long>();
            HashSet<Long> notHave = new HashSet<Long>();
            while ((line = br.readLine()) != null) {
                String[] strs = line.trim().substring(4, line.trim().length() - 2).split(",");
                if (!strs[0].startsWith("1"))
                    continue;
                if (!strs[1].startsWith("1"))
                    continue;
                long down = Long.parseLong(strs[0]);
                long up = Long.parseLong(strs[1]);

                synsets.add(down);
                synsets.add(up);

                Vector<String> upList = idToSynsetsMap.get(up);
                Vector<String> downList = idToSynsetsMap.get(down);
                if (upList == null) {
                    notHave.add(up);
                    System.out.println("hyp null" + "\t" + up);
                    continue;
                }
                if (downList == null) {
                    notHave.add(down);
                    System.out.println("hyp null" + "\t" + down);
                    continue;
                }
                for (String upStr : upList) {
                    for (String downStr : downList) {
                        bw.write(upStr + "\t" + downStr);
                        bw.newLine();
                    }
                }
                bw.flush();
            }
            br.close();

            br = new BufferedReader(new FileReader(nounIns));
            while ((line = br.readLine()) != null) {
                String[] strs = line.trim().substring(4, line.trim().length() - 2).split(",");
                if (!strs[0].startsWith("1"))
                    continue;
                if (!strs[1].startsWith("1"))
                    continue;
                long down = Long.parseLong(strs[0]);
                long up = Long.parseLong(strs[1]);

                synsets.add(down);
                synsets.add(up);

                Vector<String> upList = idToSynsetsMap.get(up);
                Vector<String> downList = idToSynsetsMap.get(down);
                if (upList == null) {
                    notHave.add(up);
                    System.out.println("ins null" + "\t" + up);
                    continue;
                }
                if (downList == null) {
                    notHave.add(down);
                    System.out.println("ins null" + "\t" + down);
                    continue;
                }
                for (String upStr : upList) {
                    for (String downStr : downList) {
                        bw.write(upStr + "\t" + downStr);
                        bw.newLine();
                    }
                }
                bw.flush();
            }
            br.close();

            System.out.println("...writeWordNetFormat done!");
            System.out.println("noun synsetsNum: " + idToSynsetsMap.keySet().size());
            System.out.println("hyper synsetsNum: " + synsets.size());
            System.out.println(Arrays.toString(notHave.toArray()));
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    ////////////////////////////////////////////////////////////////
    public void loadConceptAndEntityMap(String file) {
        System.out.println("Start loadConceptAndEntityMap...");
        long startTime = System.currentTimeMillis();
        String wordnetFile;
        if (file.equals(""))
            wordnetFile = root + "WordNet_format.txt";
        else
            wordnetFile = file;
        entityConceptSetMap = new HashMap<>();
        conceptEntitySetMap = new HashMap<>();

        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(wordnetFile));
            String line = null;
            while ((line = br.readLine()) != null) {
                if (line.trim().equals(""))
                    continue;

                String[] strs = line.trim().split("\t");
                if (strs.length < 2)
                    continue;
                String concept = strs[0].trim().toLowerCase();
                String entity = strs[1].trim().toLowerCase();

                HashSet<String> v = null;
                if (conceptEntitySetMap.containsKey(concept)) {
                    v = conceptEntitySetMap.get(concept);
                    if (v == null) {
                        v = new HashSet<String>();
                        v.add(entity);
                    } else {
                        v.add(entity);
                    }
                } else {
                    v = new HashSet<String>();
                    v.add(entity);
                }
                conceptEntitySetMap.put(concept, v);

                v = null;
                if (entityConceptSetMap.containsKey(entity)) {
                    v = entityConceptSetMap.get(entity);
                    if (v == null) {
                        v = new HashSet<String>();
                        v.add(concept);
                    } else {
                        v.add(concept);
                    }
                } else {
                    v = new HashSet<String>();
                    v.add(concept);
                }
                entityConceptSetMap.put(entity, v);
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        long endTime = System.currentTimeMillis(); //��ȡ����ʱ��

        System.out.println("loadConceptAndEntityMap running time�� " + (endTime - startTime) / 1000 + "s");
        System.out.println("loadConceptAndEntityMap done!");
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        WordNetData30 wnd = new WordNetData30();
        //wnd.writeWordNetFormat();

        //HashMap<Long, HashSet<String>> s=wnd.getIdToSynsetMap();
        HashMap<Long, HashSet<Long>> s = wnd.getSynsetConceptEntityMap();
        System.out.println(s.keySet().size());
    }

}
