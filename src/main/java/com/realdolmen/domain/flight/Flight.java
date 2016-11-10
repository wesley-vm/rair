package com.realdolmen.domain.flight;

import java.io.Serializable;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.realdolmen.domain.user.Partner;

@NamedQueries(@NamedQuery(name = Flight.findAll, query = "SELECT f FROM Flight f"))

@Entity
public class Flight implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6851290785176539664L;
	public static final String findAll = "Flight.findAll";

	/**
	 * TODO: params
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "partner_id", nullable = false)
	private Partner partner;

	// TODO cascadetype hier oppassen! bookingOfFlight hoort bij zowel een
	// Booking als een Flight
	@OneToMany()
	@JoinTable(joinColumns = @JoinColumn(table = "flight", name = "flight_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(table = "bookingOfFlight", name = "bookingOfFlight_id", referencedColumnName = "id"))
	private List<BookingOfFlight> bookingOfFlightList = new ArrayList<>();

	@OneToOne(cascade = CascadeType.ALL)
	private Airport departureAirport;

	@OneToOne(cascade = CascadeType.ALL)
	private Airport destinationAirport;


	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(inverseJoinColumns = @JoinColumn(table = "seat", name = "seat_id", referencedColumnName = "id"))
	private List<Seat> seatList = new ArrayList<>();

	private Date dateOfDeparture;

	private Duration flightDuration;

	@ElementCollection
	private List<Double> discountsListEmployee = new ArrayList<Double>();

	@ElementCollection
	private List<Double> discountsListPartner = new ArrayList<Double>();

	public Flight() {

	}

	public int getNumberOfSeatForType(SeatType st) {
		int count = 0;
		System.out.println(seatList.size());
		for (int i = 0; i < seatList.size(); i++) {
			if (seatList.get(i).getType().equals(st)) {
				count++;
			}
		}
		return count;
	}

	public Flight(Partner partner, List<BookingOfFlight> bookingOfFlightList, Airport departureAirport,
			Airport destinationAirport, Date dateOfDeparture, Duration flightDuration) {
		this.partner = partner;
		this.bookingOfFlightList = bookingOfFlightList;
		this.departureAirport = departureAirport;
		this.destinationAirport = destinationAirport;
		this.dateOfDeparture = dateOfDeparture;
		this.flightDuration = flightDuration;
	}

	public Long getId() {
		return id;
	}

	public Partner getPartner() {
		return partner;
	}

	public void setPartner(Partner partner) {
		this.partner = partner;
	}

	public List<BookingOfFlight> getBookingOfFlightList() {
		return bookingOfFlightList;
	}

	public void setBookingOfFlightList(List<BookingOfFlight> bookingOfFlightList) {
		this.bookingOfFlightList = bookingOfFlightList;
	}

	public Airport getDepartureAirport() {
		return departureAirport;
	}

	public void setDepartureAirport(Airport departureAirport) {
		this.departureAirport = departureAirport;
	}

	public Airport getDestinationAirport() {
		return destinationAirport;
	}

	public void setDestinationAirport(Airport destinationAirport) {
		this.destinationAirport = destinationAirport;
	}

	public List<Seat> getSeatList() {
		return seatList;
	}

	public void setSeatList(List<Seat> seatList) {
		this.seatList = seatList;
	}

	public Date getDateOfDeparture() {
		return dateOfDeparture;
	}

	public void setDateOfDeparture(Date dateOfDeparture) {
		this.dateOfDeparture = dateOfDeparture;
	}

	public Duration getFlightDuration() {
		return flightDuration;
	}

	public void setFlightDuration(Duration flightDuration) {
		this.flightDuration = flightDuration;
	}

	public List<Double> getDiscountsListEmployee() {
		return discountsListEmployee;
	}

	public List<Double> getDiscountsListPartner() {
		return discountsListPartner;
	}

	public void addBookingOfFlight(BookingOfFlight bof) {
		this.bookingOfFlightList.add(bof);
	}

	public void addSeat(Seat s) {
		this.seatList.add(s);
	}

	public void addSeats(ArrayList<Seat> seats) {
		for(Seat s : seats){
			addSeat(s);
		}
	}

	public boolean removeSeat(Seat s) {
		return this.seatList.remove(s);
	}

	public void addDiscountEmployee(Double d) {
		this.discountsListEmployee.add(d);
	}

	public boolean removeDiscountEmployee(Double d) {
		return this.discountsListEmployee.remove(d);
	}

	public void addDiscountPartner(Double d) {
		this.discountsListPartner.add(d);
	}

	public boolean removeDiscountPartner(Double d) {
		return this.discountsListPartner.remove(d);
	}

	public Date getLandingTime() {
		return new Date(getDateOfDeparture().getTime() + getFlightDuration().toMillis());
	}

}
