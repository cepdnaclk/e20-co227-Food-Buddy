package com.foodBudy_v2.demo.repository;

import com.foodBudy_v2.demo.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {

}
