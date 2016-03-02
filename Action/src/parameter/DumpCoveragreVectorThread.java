package parameter;

import tool.GetVerbs;
import tool.ProbaseData;

import java.util.Vector;

public class DumpCoveragreVectorThread implements Runnable {
    private static final String rootPath = "input_parameter_1770/";
    private static final String rootEntityMI = "entityMI/ngram_1770/";
    private static final String rootEntityFreq = "ngram_1770/";
    private static final String rootEntityEntropy = "entropy/";

    private Thread t;
    private String threadName;
    private String verbFile;
    private String type;

    public DumpCoveragreVectorThread(String threadName, String verbFile,
                                     String type) {
        super();
        this.threadName = threadName;
        this.verbFile = verbFile;
        this.type = type;
    }

    @Override
    public void run() {
        Vector<String> verbs = new GetVerbs().getVerbs(verbFile);
        DumpCoverageVector dcv = new DumpCoverageVector();

        for (String verb : verbs) {

            dcv.dumpCoverageAndConceptVectorAll(
                    rootEntityMI + verb + "_" + type + ".txt",
                    rootEntityEntropy + type + "/" + verb,
                    rootEntityFreq + type + "/" + verb + ".txt",
                    rootPath + verb + "_coverage_" + type + ".txt");
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
