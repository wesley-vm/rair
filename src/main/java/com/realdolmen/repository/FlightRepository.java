package com.realdolmen.repository;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.realdolmen.domain.flight.Airport;
import com.realdolmen.domain.flight.Flight;
import com.realdolmen.domain.flight.GlobalRegion;
import com.realdolmen.domain.flight.Seat;
import com.realdolmen.domain.flight.SeatType;
import com.realdolmen.domain.user.Partner;
import com.realdolmen.domain.user.User;

@Stateless
public class FlightRepository {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@PersistenceContext
	EntityManager em;

	public Flight save(Flight flight) {
		em.persist(flight);
		return flight;
	}

	public Flight create(Flight flight) {
		em.persist(flight);
		return flight;
	}

	public Flight findById(Long id) {
		return em.find(Flight.class, id);
	}

	public List<Flight> findAll() {
		return em.createNamedQuery("Flight.findAll", Flight.class).getResultList();
	}

	public void remove(long flightId) {
		logger.info("Removing user with id " + flightId);
		em.remove(em.getReference(User.class, flightId));
	}

	public List<Flight> findByParams2(SeatType t, Partner p, Date d) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Flight> cq = cb.createQuery(Flight.class);
		Root<Flight> from = cq.from(Flight.class);
		From<Flight, Seat> join = from.join("seatList");
		Path<Seat> s = join.get("type");
		Predicate predicate = cb.equal(s, t);
		cq.where(predicate);
		cq.select(from);
		TypedQuery<Flight> typedQuery = em.createQuery(cq);

		return typedQuery.getResultList();
	}

	public List<Flight> findByParams3(SeatType t, Partner p, Date d) {
//works but has duplicates
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Flight> cq = cb.createQuery(Flight.class);
		Root<Flight> from = cq.from(Flight.class);
		From<Flight, Seat> join = from.join("seatList");
		Path<Seat> s = join.get("type");
		Predicate predicate = cb.equal(s, t);
		cq.where(predicate);
		cq.select(from).distinct(true);
		TypedQuery<Flight> typedQuery = em.createQuery(cq);

		return typedQuery.getResultList();
	}

	public List<Flight> findByParams(SeatType t, Partner partner, Date departureDate,Airport destination,Airport departure,GlobalRegion globalRegion) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Flight> cq = cb.createQuery(Flight.class);
		Root<Flight> from = cq.from(Flight.class);
		Date date2;
		Calendar c = Calendar.getInstance();
		c.setTime(departureDate);
		c.add(Calendar.DATE, 1); 
		date2 = c.getTime();
		//departure=null;
		//destination=null;
		System.out.println("SeatType: " + t.toString()+ " from findByParams");
		System.out.println("Partner: " + partner.toString()+ " from findByParams");
		System.out.println("DepartureDate: " + departureDate.toString()+ " from findByParams");
		System.out.println("Destination: " + destination.toString()+ " from findByParams");
		System.out.println("Departure: " + departure.toString()+ " from findByParams");
		System.out.println("GlobalRegion: " + globalRegion.toString()+ " from findByParams");
		
		List<Predicate> predicates = new ArrayList<Predicate>();

		// Adding predicates in case of parameter not being null
		if (t != null) {
			From<Flight, Seat> join = from.join("seatList");
			Path<Seat> s = join.get("type");
			predicates.add(cb.equal(s, t));
			System.out.println(s + " seattype from database");
		}
		if (partner != null) {
			Path<Partner> dbPartner=from.<Partner>get("partner");
			predicates.add(cb.equal(dbPartner, partner));
			System.out.println(dbPartner + " partner from database");
		}
		if (departureDate != null) {
			Path<Date> dbDepartureDate=from.<Date>get("dateOfDeparture");
			predicates.add(cb.between(dbDepartureDate, departureDate, date2));
			System.out.println(dbDepartureDate + " departuredate from database");
		}
		if (destination != null)
		{
			Path<Airport> dbAirport = from.<Airport>get("destinationAirport");
			predicates.add(cb.equal(dbAirport, destination));
			System.out.println(dbAirport + " destinationAirports from database");
		}
		if (departure != null)
		{
			Path<Airport> dbAirport = from.<Airport>get("departureAirport");
			System.out.println(dbAirport + " destination_airport");
			predicates.add(cb.equal(dbAirport, departure));
		}
		if (globalRegion != null)
		{
			Path<Airport> dbAirport = from.<Airport>get("destinationAirport");
			Path<String> dbGlobalRegion = dbAirport.<String>get("globalRegion");
			System.out.println(dbAirport + " dbAirport");
			System.out.print(dbGlobalRegion.toString() + " = dbGlobalRegion");
			System.out.println(globalRegion.toString()+ " = globalRegion");
			System.out.println(cq.getSelection());
			predicates.add(cb.equal(dbGlobalRegion, "ASIA (EX. NEAR EAST)"));
		}
		cq.select(from).where(predicates.toArray(new Predicate[] {})).distinct(true);
		return em.createQuery(cq).getResultList();
	}

	public ArrayList<Flight> getFlightsByPartner(Partner partner) {
		ArrayList<Flight> resultList = new ArrayList<Flight>();
		try{
			resultList = (ArrayList<Flight>) em.createQuery("select f from Flight f where f.partner = :arg", Flight.class).setParameter("arg", partner).getResultList();
		}
		catch (Exception e){
			logger.error("No flights found for partner: " + partner.getName());
			return resultList;
		}
		return (ArrayList<Flight>) resultList;
	}

}
