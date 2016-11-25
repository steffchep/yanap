package com.topdesk.yanap.database;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by stephaniep on 14.07.2016.
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TeamDaoImpl implements TeamDao {
	private final EntityManagerFactory factory;

	@Override
	public List<Team> getAll() {
		EntityManager entityManager = factory.createEntityManager();
		try {
			TypedQuery<Team> query = entityManager.createNamedQuery("Team.getAll", Team.class);
			return query.getResultList();
		}
		finally {
			entityManager.close();
		}
	}
	
	@Override
	public Team delete(long id) {
		
		EntityManager entityManager = factory.createEntityManager();
		try {
			Team toRemove = entityManager.find(Team.class, id);
			
			entityManager.getTransaction().begin();
			entityManager.remove(toRemove);
			entityManager.getTransaction().commit();
			return toRemove;
		}
		finally {
			entityManager.close();
		}
		
	}
	
	@Override
	public Team create(Team newTeam) {
		EntityManager entityManager = factory.createEntityManager();
		try {
			entityManager.getTransaction().begin();
			entityManager.persist(newTeam);
			entityManager.getTransaction().commit();
			return newTeam;
		}
		finally {
			entityManager.close();
		}
	}
}
