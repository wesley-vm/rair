package com.realdolmen.repository;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.realdolmen.domain.flight.Airport;
import com.realdolmen.domain.flight.Flight;
import com.realdolmen.domain.user.Employee;
import com.realdolmen.domain.user.Partner;
import com.realdolmen.domain.user.User;

@Stateless
public class EmployeeRepository {
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private CriteriaBuilder cb;
	
	@EJB
	private FlightRepository flightRepository;
	
	@EJB
	private AirportRepository airportRepository;
	
	@PersistenceContext
	EntityManager em;
	
	protected void setEm(EntityManager em)
	{
		this.em=em;
	}

	public Employee save(Employee employee) {
		em.persist(employee);
		return employee;
	}

	public Employee create(Employee employee) {
		em.persist(employee);
		return employee;
	}

	public Employee findById(Long id) {
		return em.find(Employee.class, id);
	}

	public List<Employee> findAll() {
		cb = em.getCriteriaBuilder();
		CriteriaQuery<Employee> cq = cb.createQuery(Employee.class);
		Root<Employee> rootElement = cq.from(Employee.class);
		CriteriaQuery<Employee> all = cq.select(rootElement);
		TypedQuery<Employee> allQuery = em.createQuery(all);
        return allQuery.getResultList();
	}

	public void remove(Long employeeId) {
		logger.info("Removing employee with id " + employeeId);
		em.remove(em.getReference(Employee.class, employeeId));
	}	
}
