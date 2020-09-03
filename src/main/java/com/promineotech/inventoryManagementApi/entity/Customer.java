package com.promineotech.inventoryManagementApi.entity;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javex.persistence.OneToOne;
import javex.persistence.OneToMany;
import javex.persistence.JoinColumn;

import com.promineotech.inventoryManagementApi.util.MembershipLevel;

@Entity
public class Customer {
	
	private Long id;
	private String firstName;
	private String lastName;
	private Address address;
	private MembershipLevel level;
	private Set<Order> orders;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastname;
	}
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "id")
	public Address getAddress() {
		return address;
	}
	
	public void setAddress(Address address) {
		this.address = address;
	}
	
	public MembershipLevel getLevel() {
		return level;
	}
	
	public setLevel(MembershipLevel level) {
		this.level = level;
	}
	@OneToMany(mappedBy = "customer")
	public Set<Order> getOrders(){
		return orders;
	}
	
	public void setOrders(Set<Order> orders) {
		this.orders = orders;
	}
	
}

