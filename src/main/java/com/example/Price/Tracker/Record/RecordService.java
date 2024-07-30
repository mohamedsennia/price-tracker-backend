package com.example.Price.Tracker.Record;

import com.example.Price.Tracker.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
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
        getProductRecordLastTrimester(13);
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
    public  List<RecordDTO> getProductRecordLastWeek(int id){
        Calendar todayCalender = Calendar.getInstance();
        Calendar lastWeekCalendar = Calendar.getInstance();
        lastWeekCalendar.add(Calendar.WEEK_OF_YEAR,-1);
        lastWeekCalendar.set(Calendar.HOUR_OF_DAY, 0);
        lastWeekCalendar.set(Calendar.MINUTE, 0);
        lastWeekCalendar.set(Calendar.SECOND, 0);
        lastWeekCalendar.set(Calendar.MILLISECOND, 0);
        return  this.getProductRecordsByPeriod(id,new Date(todayCalender.getTimeInMillis()),new Date(lastWeekCalendar.getTimeInMillis()));
    }
    public  List<RecordDTO> getProductRecordLastMonth(int id){
        Calendar todayCalender = Calendar.getInstance();
        Calendar firstDayOfTheMonthCalendar = Calendar.getInstance();
        firstDayOfTheMonthCalendar.set(Calendar.DAY_OF_MONTH,1);
        firstDayOfTheMonthCalendar.set(Calendar.HOUR_OF_DAY, 0);
        firstDayOfTheMonthCalendar.set(Calendar.MINUTE, 0);
        firstDayOfTheMonthCalendar.set(Calendar.SECOND, 0);
        firstDayOfTheMonthCalendar.set(Calendar.MILLISECOND, 0);
        return  this.getProductRecordsByPeriod(id,new Date(todayCalender.getTimeInMillis()),new Date(firstDayOfTheMonthCalendar.getTimeInMillis()));
    }
    public  void getProductRecordLastTrimester(int id){
        List<RecordDTO> prices= new ArrayList<RecordDTO>();
        Calendar endOfTheMonth = Calendar.getInstance();

        List<RecordDTO> currentMonthPrices;
        for(int i=0;i<3;i++){
            Calendar firstDayOfTheMonthCalendar = (Calendar) endOfTheMonth.clone();
            firstDayOfTheMonthCalendar.set(Calendar.DAY_OF_MONTH,1);
            firstDayOfTheMonthCalendar.set(Calendar.HOUR_OF_DAY, 0);
            firstDayOfTheMonthCalendar.set(Calendar.MINUTE, 0);
            firstDayOfTheMonthCalendar.set(Calendar.SECOND, 0);
            firstDayOfTheMonthCalendar.set(Calendar.MILLISECOND, 0);
            currentMonthPrices=this.getProductRecordsByPeriod(id,new Date(firstDayOfTheMonthCalendar.getTimeInMillis()),new Date(endOfTheMonth.getTimeInMillis()));
            float currentMonthPrice=0;
           for(RecordDTO record:currentMonthPrices){
               currentMonthPrice+=record.getAveragePrice();
           }
            currentMonthPrice=currentMonthPrice/currentMonthPrices.size();
           prices.add(new RecordDTO(0,"",new Date(firstDayOfTheMonthCalendar.getTimeInMillis()),currentMonthPrice));
           endOfTheMonth.set(Calendar.DAY_OF_MONTH,1);
           endOfTheMonth.add(endOfTheMonth.DAY_OF_MONTH,-1);

        }
    }

}
