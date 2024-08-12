package com.example.Price.Tracker.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Integer> {
    @Query("SELECT p from Product p where p.owner.id=:id")
    public List<Product> findByOwner(@Param("id") int id);
    @Query("SELECT p.activated from Product p where p.id=:id")
    public boolean isActivated(@Param("id") int id);
}
