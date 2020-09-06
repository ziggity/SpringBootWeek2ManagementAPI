package com.promineotech.inventoryManagementApi.repository;

import org.springframework.data.repository.CrudRepository;

import com.promineotech.inventoryManagementApi.entity.Orders;

public interface OrderRepository extends CrudRepository<Orders, Long> {

}
