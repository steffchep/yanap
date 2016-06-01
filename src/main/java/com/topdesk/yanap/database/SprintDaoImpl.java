package com.topdesk.yanap.database;

import java.util.List;

import javax.persistence.EntityManagerFactory;

/**
 * Created by stephaniep on 17.05.2016.
 */
public class SprintDaoImpl implements SprintDao {
	private final EntityManagerFactory factory;

	private static final String BY_ID = " where id = ?";
	private static final String ORDER_BY_DATE_DESC = " order by startDate desc";
	private static final String QUERY_ALL = "select * from sprints" + BY_ID;
	private static final String DELETE_BY_ID = "delete from sprints" + BY_ID;
	private static final String UPDATE_BY_ID = "update sprints set name = ?, startDate = ?, endDate = ?, status = ?" + BY_ID;

	public SprintDaoImpl(EntityManagerFactory factory) {
		this.factory = factory;
	}

	@Override
	public List<Sprint> getAll() {
		return null;
	}

	@Override
	public Sprint getById(long id) {
		return null;
	}

	@Override
	public Sprint delete(Sprint sprint) {
		return null;
	}

	@Override
	public Sprint create(Sprint newSprint) {
		return null;
	}
}
