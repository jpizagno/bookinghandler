package de.booking.service;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import de.booking.model.Percentages;

@Repository("percentagesService")
public class PercentagesServiceImpl implements PercentagesService {

	@Autowired
	private SessionFactory sessionFactory;
	
	public Percentages readPercentages(long id) {
		return (Percentages) sessionFactory.getCurrentSession().get(Percentages.class, id);
	}

}
