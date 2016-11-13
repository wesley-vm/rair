package com.realdolmen.domain.flight;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Discount {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	// Determines if it is a percentage or a real number. If it is a real number � will be pre-pendend with the toString. If a percentage, "%" will be appended with the toString.
	private boolean isPercentage;
	
	// This can be a percentage or a real number, depending on the boolean isPercentage
	private double discount;
	
	// Determines if it is only valid during a period
	private boolean isPeriodical;
	
	private Date beginDate = null;
	
	private Date endDate = null;
	
	private SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
	
	public Discount(){
		
	}

	public Discount(boolean isPercentage, double discount, boolean isPeriodical, Date beginDate, Date endDate) {
		this.isPercentage = isPercentage;
		this.discount = discount;
		this.isPeriodical = isPeriodical;
		if(this.isPeriodical){
			this.beginDate = beginDate;
			this.endDate = endDate;
		}
	}
	
	public double addDiscountToPrice(double price){
		if(isPeriodical){
			Date d = new Date();
			// Test if its in the right period
			if(d.before(endDate) && d.after(beginDate)){
				return addDiscountToPrice2(price);
			}
			// Not in the right period
			else{
				return price;
			}
		}
		//If it's not periodical
		else{
			return addDiscountToPrice2(price);
		}
	}
	
	private double addDiscountToPrice2(double price){
		if(isPercentage){
			return price - price * discount;
		}
		else{
			return price - discount;
		}
	}
	
	@Override
	public String toString(){
		String toString = ""+discount;
		
		if(isPercentage){
			toString += "%";
		}
		else{
			toString = "�" + toString();
		}
			
		if(isPeriodical){
			toString += " from " + formatter.format(beginDate);
			toString += " to " + formatter.format(endDate);
		}
		return toString;
	}
	
	/******************************************************************
	 * 
	 * GETTERS AND SETTERS
	 * 
	 ******************************************************************/

	public boolean isPercentage() {
		return isPercentage;
	}

	public void setPercentage(boolean isPercentage) {
		this.isPercentage = isPercentage;
	}

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	public boolean isPeriodical() {
		return isPeriodical;
	}

	public void setPeriodical(boolean isPeriodical) {
		this.isPeriodical = isPeriodical;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public long getId() {
		return id;
	}
}