package com.topdesk.yanap.database;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by stephaniep on 01.06.2016.
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserBySprintDaoImpl implements UserBySprintDao {
	private final EntityManagerFactory factory;
	@Override
	public List<UserBySprint> getAllForSprint(long sprintId) {
		EntityManager entityManager = factory.createEntityManager();
		try {
			TypedQuery<UserBySprint> query = entityManager.createNamedQuery("UserBySprint.getBySprint", UserBySprint.class);
			query.setParameter("sprintId", sprintId);
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
	public void deleteBySprint(long sprint) {
		EntityManager em = null;
		EntityTransaction tr = null;
		try {
			em = factory.createEntityManager();
			tr = em.getTransaction();
			tr.begin();
			Query query = em.createNamedQuery("UserBySprint.deleteBySprint");
			query.setParameter("sprintId", sprint);
			query.executeUpdate();
			tr.commit();
		} catch (RuntimeException e) {
			if (tr != null) {
				tr.rollback();
			}
			throw e;
		} finally {
			if (em != null) {
				em.close();
			}
		}

	}

	@Override
	public List<UserBySprint> createForSprint(Sprint sprint, List<User> users) {
		List<UserBySprint> result = new ArrayList<>();
		EntityManager entityManager = factory.createEntityManager();

		try {
			entityManager.getTransaction().begin();
			for (User user : users) {
				UserBySprint userBySprint = new UserBySprint(sprint, user);
				entityManager.persist(userBySprint);
				result.add(userBySprint);
			}
			entityManager.getTransaction().commit();
		}
		finally {
			entityManager.close();
		}

		return result;
	}
}
