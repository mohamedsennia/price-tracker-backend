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
        return  this.getProductRecordsByPeriod(id,new Date(lastWeekCalendar.getTimeInMillis()),new Date(todayCalender.getTimeInMillis()));
    }
    public  List<RecordDTO> getProductRecordLastMonth(int id){
        Calendar todayCalender = Calendar.getInstance();
        Calendar oneMonthAgo = Calendar.getInstance();
        oneMonthAgo.add(Calendar.MONTH,-1);
        return  this.getProductRecordsByPeriod(id,new Date(oneMonthAgo.getTimeInMillis()),new Date(todayCalender.getTimeInMillis()));
    }
    public  List<RecordDTO> getProductRecordLastTrimester(int id){
        /* TO DO*/
        //* trimastalRecords (list of records)
        List<RecordDTO> trimastalRecords=new ArrayList<>();
        List<RecordDTO> tempRecords=new ArrayList<>();
        //* set start date as one week ago and date end date as today's
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        Calendar trimastStart=Calendar.getInstance();
        startDate.add(Calendar.WEEK_OF_YEAR,-1);
        //* get 3months ago date
        trimastStart.add(Calendar.MONTH,-3);
        //* loop until start date before 3 months ago
        while (trimastStart.compareTo(startDate)<=0){

        //* get records between start and end
            tempRecords=this.getProductRecordsByPeriod(id,new Date(startDate.getTimeInMillis()),new Date(endDate.getTimeInMillis()));
        //* sums them up and calculate the average
            float average=0;
            if(tempRecords.size()!=0){


            for(RecordDTO recordDTO:tempRecords){
                average+=recordDTO.getAveragePrice();
                System.out.println(average);
            }
            average/=tempRecords.size();
            }
        //* get the date of mid week (start + 3 Days)
            endDate.add(Calendar.DAY_OF_MONTH,-3);
        //* add new record with mid week date and average of that week to trimastalRecords
            trimastalRecords.add(new RecordDTO(id,"",new Date(endDate.getTimeInMillis()),average));
        //* set end date= start date and start date a week before that

        //* end loop
        endDate=(Calendar) startDate.clone();
            startDate.add(Calendar.WEEK_OF_YEAR,-1);
        }
         return trimastalRecords;
    }
    public  List<RecordDTO> getProductRecordLastYear(int id){
        Calendar startingDate = Calendar.getInstance();
        Calendar endingDate = Calendar.getInstance();
        List<RecordDTO> tempRecords,resultRecords;
        resultRecords=new ArrayList<>();

       int i=0;
       while (i<12){
           startingDate.add(Calendar.MONTH,-1);
           tempRecords=getProductRecordsByPeriod(id,new Date(startingDate.getTimeInMillis()),new Date(endingDate.getTimeInMillis()));
           float average_price=0.0F;
           for(RecordDTO recordDTO:tempRecords){
               average_price+=recordDTO.getAveragePrice();
           }
           average_price/=tempRecords.size();
           resultRecords.add(new RecordDTO(id,"",new Date(startingDate.getTimeInMillis()),average_price));
           endingDate=(Calendar) startingDate.clone();
            i++;
       }
       return  resultRecords;
    }

}
