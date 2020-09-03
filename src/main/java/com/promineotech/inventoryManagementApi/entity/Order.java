package com.promineotech.inventoryManagementApi.entity;

import java.util.Set;
import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javex.persistence.ManyToMany;
import javex.persistence.ManyToOne;
import javex.persistence.JoinColumn;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.promineotech.inventoryManagementApi.util.OrderStatus;

@Entity
public class Order {
	
	private Long id;
	private LocalDate ordered;
	private LocalDate estimatedDelivery;
	private LocalDate delivered;
	private double invoiceAmount;
	private OrderStatus status;
	private Set<Product> products;

	
	
	
}
