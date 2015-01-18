package ch.unibe.scg.log4j;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.MDC;
import org.apache.log4j.spi.LoggingEvent;

import ch.unibe.scg.doodle.Doo;

public class DoodleDebugAppender extends AppenderSkeleton {

	private static final String IN_APPEND_KEY = DoodleDebugAppender.class
			.getName() + ".inAppend";

	public DoodleDebugAppender() {
		super();

		// ArrayList<RenderingPlugin> plugins = new
		// ArrayList<RenderingPlugin>();
		// plugins.add(new LoggingEventPlugin());
		// DoodleDebug.addRenderingPlugins(plugins);
	}

	@Override
	public void close() {
	}

	@Override
	public boolean requiresLayout() {
		return false;
	}

	@Override
	protected void append(final LoggingEvent event) {
		// prevent cycle (because DD indirectly uses log4j, too)
		if (event.getMDC(IN_APPEND_KEY) != null)
			return;
		MDC.put(IN_APPEND_KEY, this);

		try {
			Runnable doodlingRunnable = new Runnable() {
				public void run() {
					// XXX: DD is not thread safe
					synchronized (Doo.class) {
						// Doo.dle(event);
						Doo.dle(event.getMessage());
					}
				}
			};
			new Thread(doodlingRunnable).start();
		} finally {
			MDC.remove(IN_APPEND_KEY);
		}
	}

}
