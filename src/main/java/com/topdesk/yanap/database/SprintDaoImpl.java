package com.topdesk.yanap.database;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
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
			TypedQuery<Sprint> query = entityManager.createNamedQuery("Sprint.getAll", Sprint.class);
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
	public List<Sprint> getByTeam(String team) {
		EntityManager entityManager = factory.createEntityManager();
		try {
			TypedQuery<Sprint> query = entityManager.createNamedQuery("Sprint.getByTeam", Sprint.class);
			query.setParameter("team", team);
			return query.getResultList();
		}
		finally {
			entityManager.close();
		}
	}

	@Override
	public Sprint update(Sprint sprint) {
		EntityManager entityManager = factory.createEntityManager();
		try {
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
	public void delete(long id) {
		EntityManager em = null;
		EntityTransaction tr = null;
		try {
			em = factory.createEntityManager();
			tr = em.getTransaction();
			tr.begin();
			Query query = em.createNamedQuery("Sprint.delete");
			query.setParameter("id", id);
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

	public Sprint create(Sprint newSprint) {
		EntityManager entityManager = factory.createEntityManager();
		try {
			entityManager.getTransaction().begin();
			entityManager.persist(newSprint);
			entityManager.getTransaction().commit();
			return newSprint;
		}
		finally {
			entityManager.close();
		}
	}
}
