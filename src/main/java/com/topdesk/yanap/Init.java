package com.topdesk.yanap;

import javax.annotation.Resource;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.sql.DataSource;

public class Init implements ServletContextListener {

	@Resource(name="jdbc/yanap")
	private DataSource dataSource;

	@Override
	public void contextInitialized(ServletContextEvent event) {

//		event.getServletContext().setAttribute(PersonaDao.CONTEXT_NAME, new MyPersonaDao(dataSource));
//		event.getServletContext().setAttribute(PersonaConverter.CONTEXT_NAME, new MyPersonaConverter());
	}

	@Override
	public void contextDestroyed(ServletContextEvent context) {
		// I didn't create the data source, I'm not destroying it either
	}
}
