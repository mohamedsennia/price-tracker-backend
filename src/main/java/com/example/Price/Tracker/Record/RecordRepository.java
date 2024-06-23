package com.example.Price.Tracker.Record;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface RecordRepository extends JpaRepository<Record,Integer> {
    @Query("SELECT r FROM Record r where r.product.id=:id")
    public List<Record> getProductRecords(@Param("id") int id);
    @Query("SELECT r FROM Record r where r.product.id=:id and date between :periodStart and :periodEnd ")
    public List<Record>  getProductRecordsByPeriod(@Param("id") int id,@Param("periodStart") Date periodStart,@Param("periodEnd") Date periodEnd);
}
