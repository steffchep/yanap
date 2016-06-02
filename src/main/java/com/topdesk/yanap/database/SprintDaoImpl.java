package com.topdesk.yanap.database;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

import lombok.RequiredArgsConstructor;

/**
 * Created by stephaniep on 17.05.2016.
 */
@RequiredArgsConstructor
public class SprintDaoImpl implements SprintDao {
	private final EntityManagerFactory factory;

	@Override
	public List<Sprint> getAll() {
		EntityManager entityManager = factory.createEntityManager();
		try {
			TypedQuery<Sprint> query = entityManager.createNamedQuery("Sprint.getAllForSprint", Sprint.class);
			return query.getResultList();
		}
		finally {
			entityManager.close();
		}
	}

	@Override
	public Sprint getById(long id) {
		EntityManager entityManager = factory.createEntityManager();
		try {
			Sprint sprint = entityManager.find(Sprint.class, id);
			entityManager.getTransaction().begin();
			entityManager.merge(sprint);
			entityManager.getTransaction().commit();
			return sprint;
		}
		finally {
			entityManager.close();
		}
	}

	@Override
	public Sprint update(Sprint sprint) {
		EntityManager entityManager = factory.createEntityManager();
		try {
			Sprint update = entityManager.find(Sprint.class, sprint.getId());
			update.setStatus(sprint.getStatus());
			update.setName(sprint.getName());
			update.setEndDate(sprint.getEndDate());
			update.setStartDate(sprint.getStartDate());

			entityManager.getTransaction().begin();
			entityManager.merge(update);
			entityManager.getTransaction().commit();
			return update;
		}
		finally {
			entityManager.close();
		}
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
