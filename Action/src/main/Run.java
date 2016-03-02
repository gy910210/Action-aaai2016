package main;

import lexicon.BacktrackingThread;
import parameter.DumpCoveragreVectorThread;
import tool.ProbaseData;

import java.util.Vector;

public class Run {
    public static void main(String[] args) {
        ProbaseData.loadConceptAndEntityMap("");

        String[] files = new String[]{"verb_1", "verb_2", "verb_3", "verb_4", "verb_5",
                "verb_6", "verb_7", "verb_8"};

        // =============obj=============
        Vector<DumpCoveragreVectorThread> dtVec = new Vector<>();
        for (String file : files) {
            DumpCoveragreVectorThread tmp = new DumpCoveragreVectorThread(file
                    + "_thread", "res/" + file + ".txt", "obj");
            dtVec.add(tmp);
        }
        for (int i = 0; i < dtVec.size(); i++)
            dtVec.get(i).start();
        for (int i = 0; i < dtVec.size(); i++)
            dtVec.get(i).join();

        Vector<BacktrackingThread> btVec = new Vector<>();
        for (String file : files) {
            BacktrackingThread tmp = new BacktrackingThread(file + "_thread",
                    "res/" + file + ".txt", "obj");
            btVec.add(tmp);
        }
        for (int i = 0; i < btVec.size(); i++)
            btVec.get(i).start();
        for (int i = 0; i < btVec.size(); i++)
            btVec.get(i).join();

        // =============subj=============
        dtVec = new Vector<>();
        for (String file : files) {
            DumpCoveragreVectorThread tmp = new DumpCoveragreVectorThread(file
                    + "_thread", "res/" + file + ".txt", "subj");
            dtVec.add(tmp);
        }
        for (int i = 0; i < dtVec.size(); i++)
            dtVec.get(i).start();
        for (int i = 0; i < dtVec.size(); i++)
            dtVec.get(i).join();

        btVec = new Vector<>();
        for (String file : files) {
            BacktrackingThread tmp = new BacktrackingThread(file + "_thread",
                    "res/" + file + ".txt", "subj");
            btVec.add(tmp);
        }
        for (int i = 0; i < btVec.size(); i++)
            btVec.get(i).start();
        for (int i = 0; i < btVec.size(); i++)
            btVec.get(i).join();
    }
}
