package com.promineotech.inventoryManagementApi.service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.apache.loggign.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.promineotech.inventoryManagementApi.entity.Product;
import com.promineotech.inventoryManagementApi.entity.Customer;
import com.promineotech.inventoryManagementApi.entity.Order;
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
	
	public Order submitNewOrder(Set<Long> productIds, Long customerId) throws Exception{
		try {
			Customer customer = customerRepo.findOne(customerId);
			Order order = initializeNewOrder(productIds, customer);
			return repo.save(order);
		} catch (Exception e) {
			Logger.error("Exception occurred whiletrying to create new order for customer " + customerId, e);
			throw e;
		}
	}
	
	public Order cancelOrder(Long orderId) throws Exception {
		try {
			Order order = repo.findOne(orderId);
			order.setStatus(OrderStatus.CANCELED);
			return repo.save(order);
		} catch(Exception e) {
			Logger.error("exception occurred while trying ot cancel order" + orderId, e);
			throw new Exception("unable to update order.");
		}
	}
	
	public Order completeOrder(Long orderId) throws Exception{
		try {
			Order order = repo.findOne(orderId);
			order.setStatus(OrderStatus.DELIVERED);
			return repo.save(order);
		}catch (Exception e) {
			Logger.error("exception occurred while trying to complete order" + orderId, e);
			throw new Exception("unable to update order");
		}
	}
	
	private void addOrdertoProducts(Order order) {
		Set<Product> products = order.getProducts();
		for(Product product : products) {
			product.getOrders().add(order);
		}
	}
	
	private Set<Product> convertToProductSet(Iterable<Product> iterable){
		Set<Product> set = new HashSet<Product>();
		for(Product product : iterable) {
			set.add(product);
		}
		return set;
	}
	
	private double calculateOrderTotal(Set<Product> products, MembershipLevel level) {
		double total = 0;
		for(Product product : products) {
			total += product.getPrice();
		}
		return total - total * level.getDiscount();
	}
	
}
