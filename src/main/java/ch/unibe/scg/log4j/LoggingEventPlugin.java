package ch.unibe.scg.log4j;

import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

import org.apache.log4j.spi.LoggingEvent;

import ch.unibe.scg.doodle.Doodler;
import ch.unibe.scg.doodle.htmlgen.Tag;
import ch.unibe.scg.doodle.plugins.AbstractPlugin;
import ch.unibe.scg.doodle.rendering.DoodleRenderException;

public class LoggingEventPlugin extends AbstractPlugin {
	
//	@Inject
//	Doodler doodler;

	@Override
	public Set<Class<?>> getDrawableClasses() {
		Set<Class<?>> hs = new HashSet<Class<?>>();
		hs.add(LoggingEvent.class);
		return hs;
	}

	@Override
	public void render(Object object, Tag tag) throws DoodleRenderException {
		LoggingEvent event = (LoggingEvent) object;
//		doodler.renderInlineInto(event.getMessage(), tag);
	}

	@Override
	public void renderSimplified(Object object, Tag tag)
			throws DoodleRenderException {
		LoggingEvent event = (LoggingEvent) object;
//		doodler.renderSmallInlineInto(event.getMessage(), tag);
	}

}
