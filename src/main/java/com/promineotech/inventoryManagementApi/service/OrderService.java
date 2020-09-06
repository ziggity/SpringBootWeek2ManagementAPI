package com.promineotech.inventoryManagementApi.service;


import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.promineotech.inventoryManagementApi.entity.Customer;
import com.promineotech.inventoryManagementApi.entity.Orders;
import com.promineotech.inventoryManagementApi.entity.Product;
import com.promineotech.inventoryManagementApi.repository.CustomerRepository;
import com.promineotech.inventoryManagementApi.repository.OrderRepository; 
import com.promineotech.inventoryManagementApi.repository.ProductRepository;
import com.promineotech.inventoryManagementApi.util.MembershipLevel;
import com.promineotech.inventoryManagementApi.util.OrderStatus;

@Service
public class OrderService {

	private static final Logger Logger = LogManager.getLogger(OrderService.class);
	private final int DELIVERY_DAYS = 7;
	
	@Autowired
	private OrderRepository repo;
	
	@Autowired
	private CustomerRepository customerRepo;
	
	@Autowired
	private ProductRepository productRepo;
	
	public Orders submitNewOrder(Set<Long> productIds, Long customerId) throws Exception {
		try {
			Customer customer = customerRepo.findOne(customerId);
			Orders order = initializeNewOrder(productIds, customer);
			return repo.save(order);
		} catch (Exception e) {
			Logger.error("Exception occurred while trying to create new order for customer: " + customerId, e);
			throw e;
		}
	}
	
	public Orders cancelOrder(Long orderId) throws Exception {
		try {
			Orders order = repo.findOne(orderId);
			order.setStatus(OrderStatus.CANCELED);
			return repo.save(order);
		} catch (Exception e) {
			Logger.error("Exception occurred while trying to cancel order: " + orderId, e);
			throw new Exception("Unable to update order.");
		}
	}
	
	public Orders completeOrder(Long orderId) throws Exception {
		try {
			Orders order = repo.findOne(orderId);
			order.setStatus(OrderStatus.DELIVERED);
			return repo.save(order);
		} catch (Exception e) {
			Logger.error("Exception occurred while trying to complete order: " + orderId, e);
			throw new Exception("Unable to update order.");
		}
	}
	
	private Orders initializeNewOrder(Set<Long> productIds, Customer customer) {
		Orders order = new Orders(); 
		order.setProducts(convertToProductSet(productRepo.findAll(productIds)));
		order.setOrdered(LocalDate.now());
		order.setEstimatedDelivery(LocalDate.now().plusDays(DELIVERY_DAYS));
		order.setCustomer(customer);
		order.setInvoiceAmount(calculateOrderTotal(order.getProducts(), customer.getLevel()));
		order.setStatus(OrderStatus.ORDERED);
		addOrderToProducts(order);
		return order;
	}


	private void addOrderToProducts(Orders order) {
		Set<Product> products = order.getProducts();
		for (Product product : products) {
			product.getOrders().add(order);
		}
	}

	private Set<Product> convertToProductSet(Iterable<Product> iterable) {
		Set<Product> set = new HashSet<Product>();
		for (Product product : iterable) {
			set.add(product);
		}
		return set;
	}
	
	private double calculateOrderTotal(Set<Product> products, MembershipLevel level) {
		double total = 0;
		for (Product product : products) {
			total += product.getPrice();
		}
		return total- total * level.getDiscount();
	}
}
