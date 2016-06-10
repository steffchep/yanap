package com.topdesk.yanap.database;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

import lombok.RequiredArgsConstructor;

/**
 * Created by stephaniep on 02.06.2016.
 */
@RequiredArgsConstructor
public class UserDaoImpl implements UserDao {
	private final EntityManagerFactory factory;

	@Override
	public User getById(long id) {
		EntityManager entityManager = factory.createEntityManager();
		try {
			User user = entityManager.find(User.class, id);
			entityManager.getTransaction().begin();
			entityManager.merge(user);
			entityManager.getTransaction().commit();
			return user;
		}
		finally {
			entityManager.close();
		}
	}

	@Override
	public List<User> getByTeam(String team) {
		if ("all".equals(team)) {
			return getAll();
		}
		EntityManager entityManager = factory.createEntityManager();
		try {
			TypedQuery<User> query = entityManager.createNamedQuery("User.getByTeam", User.class);
			query.setParameter("team", team);
			return query.getResultList();
		}
		finally {
			entityManager.close();
		}
	}

	private List<User> getAll() {
		EntityManager entityManager = factory.createEntityManager();
		try {
			TypedQuery<User> query = entityManager.createNamedQuery("User.getAll", User.class);
			return query.getResultList();
		}
		finally {
			entityManager.close();
		}
	}

	@Override
	public List<User> getByIdList(List<Long> userIds) {
		EntityManager entityManager = factory.createEntityManager();
		try {
			TypedQuery<User> query = entityManager.createNamedQuery("User.getByIdList", User.class);
			query.setParameter("ids", userIds);
			return query.getResultList();
		}
		finally {
			entityManager.close();
		}
	}

	@Override
	public User create(User newUser) {
		EntityManager entityManager = factory.createEntityManager();
		try {
			entityManager.getTransaction().begin();
			entityManager.persist(newUser);
			entityManager.getTransaction().commit();
			return newUser;
		}
		finally {
			entityManager.close();
		}
	}
}
