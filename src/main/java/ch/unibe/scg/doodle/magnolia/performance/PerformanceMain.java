package ch.unibe.scg.doodle.magnolia.performance;

import ch.unibe.scg.doodle.Doo;
import ch.unibe.scg.doodle.DoodleDebug;

public class PerformanceMain {

	private static final String QUOTE = "The most exciting phrase to hear in science, the one that heralds new discoveries, is not 'Eureka!' but 'That's funny...'";
	private static final Exception EXCEPTION = new NullPointerException();
	private static int CYCLES = 1; // XXX: Ugh (semi-final)
	private static long nanoTic;

	public static void overrideCycles(int cycles) {
		CYCLES = cycles;
	}

	public static void noHBase() {
		DoodleDebug.setDatabaseMap(SimFastHBaseMap.class);
	}

	public static void noDatabase() {
		DoodleDebug.setDatabaseMap(VolatileMap.class);
	}

	public static void exceptionMeasure() {
		tic();
		for (int i = 0; i < CYCLES; i++)
			Doo.dle(EXCEPTION);
		toc("Doo.dle");
	}

	public static void stringMeasure() {
		tic();
		for (int i = 0; i < CYCLES; i++)
			Doo.dle(QUOTE);
		toc("Doo.dle");
	}

	private static void tic() {
		nanoTic = System.nanoTime();
	}

	private static void toc(String label) {
		long elapsedNanos = System.nanoTime() - nanoTic;
		System.out.println(label + ": " + (elapsedNanos / 1000000.) + " ms");
	}
}
