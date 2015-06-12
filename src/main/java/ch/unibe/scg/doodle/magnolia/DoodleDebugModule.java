package ch.unibe.scg.doodle.magnolia;

import info.magnolia.module.ModuleLifecycle;
import info.magnolia.module.ModuleLifecycleContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.unibe.scg.doodle.DoodleDebug;
import ch.unibe.scg.doodle.DoodleDebugWebapp;
import ch.unibe.scg.doodle.magnolia.jcr.JcrDatabase;

/**
 * This class is optional and represents the configuration for the doodledebug
 * module. By exposing simple getter/setter/adder methods, this bean can be
 * configured via content2bean using the properties and node from
 * <tt>config:/modules/doodledebug</tt>. If you don't need this, simply remove
 * the reference to this class in the module descriptor xml.
 */
public class DoodleDebugModule implements ModuleLifecycle {

	private static final int PORT = 3180;

	private static final Logger log = LoggerFactory
			.getLogger(DoodleDebugModule.class);

	@Override
	public void start(ModuleLifecycleContext ctx) {
		DoodleDebug.setDatabaseMap(JcrDatabase.class);

		log.info("Starting DoodleDebugWebapp server...");
		try {
			DoodleDebugWebapp.startServer(PORT);
		} catch (Exception e) {
			log.error("Failed to start DoodleDebugWebapp server", e);
		}
	}

	@Override
	public void stop(ModuleLifecycleContext ctx) {
		try {
			DoodleDebugWebapp.stopServer();
		} catch (Exception e) {
			log.error("Failed to stop DoodleDebug webapp server", e);
		}
	}

}
