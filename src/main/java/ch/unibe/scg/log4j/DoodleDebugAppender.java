package ch.unibe.scg.log4j;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;

import ch.unibe.scg.doodle.Doo;

public class DoodleDebugAppender extends AppenderSkeleton {

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
	protected void append(LoggingEvent event) {
		// Doo.dle(event);
		Doo.dle(event.getMessage());

		// workaround to avoid circular dependency
		// try {
		// Method dle = Class.forName("ch.unibe.scg.doodle.Doo").getMethod(
		// "dle", Object.class);
		// dle.invoke(null, event.getMessage());
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
	}

}
