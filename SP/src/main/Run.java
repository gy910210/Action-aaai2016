package main;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;

import probase.Collector;
import probase.Score;
import tool.GetVerbs;
import tool.ProbaseData;
import tool.WordNetData30;
import wordnet.CollectorWN;
import wordnet.ScoreWN;

public class Run {
	public void runPB() {
		Vector<String> verbs = new GetVerbs().getVerbs("verbs_1770.txt");
		ProbaseData pd = new ProbaseData();
		ProbaseData.loadConceptAndEntityMap("");

		// =======obj=======
		for (String verb : verbs) {
			System.out.println("===" + verb + "===");
			Collector c = new Collector(verb, "obj");
			c.getVerbConceptFrequency();
		}
		Score.build("obj");
		for (String verb : verbs) {
			System.out.println("===" + verb + "===");
			Score score = new Score(verb, "obj");
			score.getScore();
		}

		// =======obj=======
		for (String verb : verbs) {
			System.out.println("===" + verb + "===");
			Collector c = new Collector(verb, "subj");
			c.getVerbConceptFrequency();
		}
		Score.build("subj");
		for (String verb : verbs) {
			System.out.println("===" + verb + "===");
			Score score = new Score(verb, "subj");
			score.getScore();
		}
	}

	public void runWN() {
		// =====wn30=====
		WordNetData30 wnd = new WordNetData30();
		HashMap<Long, HashSet<String>> idToSynsetMap = wnd.getIdToSynsetMap();
		HashMap<Long, HashSet<Long>> synsetConceptEntityMap = wnd
				.getSynsetConceptEntityMap();
		HashMap<Long, HashSet<Long>> synsetEntityConceptMap = wnd
				.getSynsetEntityConceptMap();

		Vector<String> verbs = new GetVerbs().getVerbs("wn30\\wn30_verbs_10116.txt");
		for (String verb : verbs) {
			System.out.println("===" + verb + "===");
			CollectorWN c = new CollectorWN(verb, "obj", "wn30");
			c.getVerbConceptSynsetFrequency(idToSynsetMap,
					synsetConceptEntityMap, synsetEntityConceptMap);
		}

		ScoreWN.build("wn30", "obj");
		for (String verb : verbs) {
			System.out.println("===" + verb + "===");
			ScoreWN score = new ScoreWN(verb, "wn30", "obj");
			score.getScore(idToSynsetMap);
		}

		for (String verb : verbs) {
			System.out.println("===" + verb + "===");
			CollectorWN c = new CollectorWN(verb, "subj", "wn30");
			c.getVerbConceptSynsetFrequency(idToSynsetMap,
					synsetConceptEntityMap, synsetEntityConceptMap);
		}

		ScoreWN.build("wn30", "subj");
		for (String verb : verbs) {
			System.out.println("===" + verb + "===");
			ScoreWN score = new ScoreWN(verb, "wn30", "subj");
			score.getScore(idToSynsetMap);
		}
	}

	public static void main(String[] args) {

	}
}
