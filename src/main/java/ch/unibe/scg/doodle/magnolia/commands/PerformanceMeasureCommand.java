package ch.unibe.scg.doodle.magnolia.commands;

import ch.unibe.scg.doodle.magnolia.performance.PerformanceMain;
import info.magnolia.commands.impl.BaseRepositoryCommand;
import info.magnolia.context.Context;

public class PerformanceMeasureCommand extends BaseRepositoryCommand {

	private int cycles = 1;
	private boolean exception = false;
	private boolean noDatabase = false;

	@Override
	public boolean execute(Context ctx) throws Exception {
		PerformanceMain.overrideCycles(cycles);

		if (noDatabase)
			PerformanceMain.noDatabase();

		if (exception)
			PerformanceMain.exceptionMeasure();
		else
			PerformanceMain.stringMeasure();

		return true;
	}

	public int getCycles() {
		return cycles;
	}

	public void setCycles(int cycles) {
		this.cycles = cycles;
	}

	public boolean isException() {
		return exception;
	}

	public void setException(boolean exception) {
		this.exception = exception;
	}

	public boolean isNoDatabase() {
		return noDatabase;
	}

	public void setNoDatabase(boolean noDatabase) {
		this.noDatabase = noDatabase;
	}

}
