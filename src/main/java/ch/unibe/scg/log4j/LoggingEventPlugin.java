package ch.unibe.scg.log4j;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.spi.LoggingEvent;

import ch.unibe.scg.doodle.htmlgen.Tag;
import ch.unibe.scg.doodle.plugins.AbstractPlugin;
import ch.unibe.scg.doodle.rendering.DoodleRenderException;

public class LoggingEventPlugin extends AbstractPlugin {

	@Override
	public Set<Class<?>> getDrawableClasses() {
		Set<Class<?>> hs = new HashSet<Class<?>>();
		hs.add(LoggingEvent.class);
		return hs;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void render(Object object, Tag tag) throws DoodleRenderException {
		LoggingEvent event = (LoggingEvent) object;
		tag.add(createLevelDiv(event));
		Tag content = new Tag("p");
		content.add(event.getMessage());
		tag.add(content);
		tag.add(createDateDiv(event));
	}

	@SuppressWarnings("unchecked")
	@Override
	public void renderSimplified(Object object, Tag tag)
			throws DoodleRenderException {
		LoggingEvent event = (LoggingEvent) object;
		tag.add(createLevelDiv(event));
	}

	@SuppressWarnings("unchecked")
	private Tag createLevelDiv(LoggingEvent event) {
		Tag level = new Tag("div", "class=log4j-level");
		String levelString = event.getLevel().toString();
		level.addCSSClass("log4j-" + levelString.toLowerCase());
		level.add(levelString);
		return level;
	}

	@SuppressWarnings("unchecked")
	private Tag createDateDiv(LoggingEvent event) {
		Tag dateDiv = new Tag("div", "class=log-date");
		dateDiv.addCSSClass("log4j-date");
		Date date = new Date(event.getTimeStamp());
		String dateString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SS")
				.format(date);
		dateDiv.add(dateString);
		return dateDiv;
	}

	@Override
	public String getCSS() {
		String general = subRule("{line-height: 1.5em;}");
		String p = subRule("p {float:left; margin: 0;}");
		String level = subRule(".log4j-level {float: left; padding: 0 0.5em; margin-right: 0.5em; background-color: gray; color: white;}");
		String trace = subRule(".log4j-trace {background-color: #d8f;}");
		String debug = subRule(".log4j-debug {background-color: #0bf;}");
		String info = subRule(".log4j-info {background-color: #0a0;}");
		String warn = subRule(".log4j-warn {background-color: #eec200;}");
		String error = subRule(".log4j-error {background-color: #f60;}");
		String fatal = subRule(".log4j-fatal {background-color: #e00;}");
		String date = subRule(".log4j-date {font-size: 80%; opacity: 0.5;}");
		return general + p + level + trace + debug + info + warn + error
				+ fatal + date;
	}

	private String subRule(String rule) {
		return "." + this.getClassAttribute() + " " + rule;
	}

}
