package com.realdolmen.domain.flight;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import com.realdolmen.domain.flight.Booking;
import com.realdolmen.domain.flight.PaymentStatus;
import com.realdolmen.domain.user.Address;
import com.realdolmen.domain.user.Customer;
import com.realdolmen.domain.user.Employee;

public class BookingTest {
	
	Booking b;
	Address a;
	Customer c;
	Date d;
	//Employee e;
	@Before
	public void init()
	{
		//b = new Booking(PaymentStatus.PENDING, c, Calendar.getInstance());
		a = new Address("teststreet",1,3670,"Hasselt","Belgium");
		c = new Customer(a,"email@address.com","firstName","lastName","password","userName");
		d = new Date();
		b  = new Booking(PaymentStatus.PENDING,c,d);
		//e = new Employee("firstName","lastName","password","username");
	}

	@Test
	public void bookingUnitTest() {
		assertEquals(PaymentStatus.PENDING,b.getPaymentStatus());
		System.out.println("PaymentStatus = " +PaymentStatus.PENDING.toString());
		assertEquals(a,b.getCustomer().getAddress());//Test address
		assertEquals(c,b.getCustomer());//Test Customer
		assertEquals(d,b.getDateOfReservation());//Test reservationDate
		assertNotEquals(b,new Booking(PaymentStatus.PENDING,c,d));//Test Id
		
		
		
	}

}