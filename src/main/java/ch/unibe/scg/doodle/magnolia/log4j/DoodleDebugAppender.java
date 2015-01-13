package ch.unibe.scg.doodle.magnolia.log4j;

import java.util.ArrayList;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;

import ch.unibe.scg.doodle.Doo;
import ch.unibe.scg.doodle.DoodleDebug;
import ch.unibe.scg.doodle.plugins.RenderingPlugin;

public class DoodleDebugAppender extends AppenderSkeleton {

	public DoodleDebugAppender() {
		super();
		
		ArrayList<RenderingPlugin> plugins = new ArrayList<RenderingPlugin>();
		plugins.add(new LoggingEventPlugin());
		DoodleDebug.addRenderingPlugins(plugins);
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
		Doo.dle(event);
	}

}
