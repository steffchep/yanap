package com.topdesk.examples;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class Init implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent event) {
	}

	@Override
	public void contextDestroyed(ServletContextEvent context) {
		// I didn't create the data source, I'm not destroying it either
	}
}
