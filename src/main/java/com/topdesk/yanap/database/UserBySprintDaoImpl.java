package com.topdesk.yanap.database;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

import lombok.RequiredArgsConstructor;

/**
 * Created by stephaniep on 01.06.2016.
 */
@RequiredArgsConstructor
public class UserBySprintDaoImpl implements UserBySprintDao {
	private final EntityManagerFactory factory;
	@Override
	public List<UserBySprint> getAllForSprint(Sprint sprint) {
		EntityManager entityManager = factory.createEntityManager();
		try {
			TypedQuery<UserBySprint> query = entityManager.createNamedQuery("UserBySprint.getBySprint", UserBySprint.class);
			query.setParameter("sprint", sprint);
			return query.getResultList();
		}
		finally {
			entityManager.close();
		}
	}

	@Override
	public UserBySprint saveAvailability(long sprintId, long userId, int index, float value) {
		EntityManager entityManager = factory.createEntityManager();
		try {
			System.err.println("getting user....");
			TypedQuery<UserBySprint> query = entityManager.createNamedQuery("UserBySprint.getSingleBySprint", UserBySprint.class);
			query.setParameter("sprintId", sprintId);
			query.setParameter("userId", userId);
			UserBySprint save = query.getSingleResult();
			save.setDayByIndex(index, value);
			entityManager.getTransaction().begin();
			entityManager.merge(save);
			entityManager.getTransaction().commit();
			return save;
		}
		finally {
			entityManager.close();
		}
	}

	@Override
	public void deleteBySprint(Sprint sprint) {

	}

	@Override
	public List<UserBySprint> createForSprint(long sprintId, List<User> users) {
		return null;
	}
}
