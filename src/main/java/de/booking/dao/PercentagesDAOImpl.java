package de.booking.dao;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import de.booking.model.Percentages;

@Repository("percentagesDAO")
public class PercentagesDAOImpl implements PercentagesDAO {

	@Autowired
	private SessionFactory sessionFactory;
	
	public Percentages readPercentages(long id) {
		return (Percentages) sessionFactory.getCurrentSession().get(Percentages.class, id);
	}

}
