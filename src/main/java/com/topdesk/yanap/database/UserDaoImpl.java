package com.topdesk.yanap.database;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

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
		return null;
	}

	@Override
	public User delete(User user) {
		return null;
	}

	@Override
	public User create(String name, boolean isDeveloper) {
		return null;
	}
}
