package com.example.Price.Tracker.Record;

import com.example.Price.Tracker.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecordService {
    private RecordRepository  recordRepository;
    private Mapper mapper;
    @Autowired
    private RecordService(RecordRepository recordRepository,Mapper mapper){
        this.recordRepository=recordRepository;
        this.mapper=mapper;
    }
    public List<RecordDTO> getProductRecords(int id){
    return this.recordRepository.getProductRecords(id).stream().map(mapper::toRecordDTO).collect(Collectors.toList());
    }
    public  void  addRecord(Record record){
        this.recordRepository.save(record);
    }
    public List<RecordDTO> getProductRecordsByPeriod(int id, Date periodStart, Date periodEnd){
    return this.recordRepository.getProductRecordsByPeriod(id, periodStart, periodEnd).stream().map(mapper::toRecordDTO).collect(Collectors.toList());
    }
}
