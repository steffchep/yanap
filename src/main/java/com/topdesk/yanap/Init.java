package com.topdesk.yanap;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.topdesk.yanap.database.SprintDao;
import com.topdesk.yanap.database.SprintDaoImpl;

public class Init implements ServletContextListener {

	private static final String PERSISTENCE_UNIT_NAME = "yanapdb";

	@Override
	public void contextInitialized(ServletContextEvent event) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);

		event.getServletContext().setAttribute(SprintDao.CONTEXT_NAME, new SprintDaoImpl(emf));
	}

	@Override
	public void contextDestroyed(ServletContextEvent context) {
		// I didn't create the data source, I'm not destroying it either
	}
}
