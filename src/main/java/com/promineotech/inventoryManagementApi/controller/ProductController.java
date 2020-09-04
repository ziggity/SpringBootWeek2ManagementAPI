package com.promineotech.inventoryManagementApi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;


import com.promineotech.inventoryManagementApi.entity.Product;
import com.promineotech.inventoryManagementApi.service.ProductService;

@RestController
@RequestMapping("/products")
public class ProductController {

	@Autowired
	private ProductService service;
	
	@RequestMapping(method=RequestMethod.GET)
	public ResponseEntity<Object> getProducts(){
		return new ResponseEntity<Object>(service.getProducts(), HttpStatus.OK);
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public ResponseEntity<Object> createProduct(@RequestBody Product product){
		return new ResponseEntity<Object>(service.createProduct(product), HttpStatus.CREATED);
	}
	
	@RequestMapping(value="/{id}", method=RequestMethod.PUT)
	public ResponseEntity<Object> updateProduct(@RequestBody Product product, @PathVariable Long id) {
		try {
			return new ResponseEntity<Object>(service.updateProduct(product, id), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Object>("Unable to update product: ", HttpStatus.BAD_REQUEST);
		}
	}
}
