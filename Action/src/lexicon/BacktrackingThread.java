package lexicon;

import tool.GetVerbs;

import java.util.HashMap;
import java.util.Vector;

public class BacktrackingThread implements Runnable {
    private Thread t;
    private String threadName;
    private String verbFile;
    private String type;

    public BacktrackingThread(String threadName, String verbFile, String type) {
        super();
        this.threadName = threadName;
        this.verbFile = verbFile;
        this.type = type;
    }

    @Override
    public void run() {
        Vector<String> verbs = new GetVerbs().getVerbs(verbFile);
        Backtracking backtracking = new Backtracking();

        for (String verb : verbs) {
            for (int k = 1; k <= 4; k += 1) {
                backtracking.doBackTracking(verb, k, 0.2, type);
                backtracking.print("k" + String.valueOf(k));
            }
        }
    }

    public void start() {
        System.out.println("Starting " + threadName);
        if (t == null) {
            t = new Thread(this, threadName);
            t.start();
        }
    }

    public void join() {
        System.out.println("End " + threadName);
        if (t != null) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
