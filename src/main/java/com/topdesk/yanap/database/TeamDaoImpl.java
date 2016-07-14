package com.topdesk.yanap.database;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

import lombok.RequiredArgsConstructor;

/**
 * Created by stephaniep on 14.07.2016.
 */
@RequiredArgsConstructor
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
	public Team delete(Team team) {
		
		EntityManager entityManager = factory.createEntityManager();
		try {
			Team toRemove = entityManager.find(Team.class, team.getId());
			
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
