package com.example.Price.Tracker.Record;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@CrossOrigin(
        origins = {
                "http://localhost:4200",
                "https://senniamohamed.netlify.app/",
                "https://trackprices.netlify.app/"
        },
        methods = {
                RequestMethod.OPTIONS,
                RequestMethod.GET,
                RequestMethod.PUT,
                RequestMethod.DELETE,
                RequestMethod.POST
        }
)
@RestController
@NoArgsConstructor
@RequestMapping("/api/record")
public class RecordController {
    private RecordService recordService;
    @Autowired
    public RecordController(RecordService recordService){
        this.recordService=recordService;
    }
    @GetMapping("getProductRecords/{id}")
    public List<RecordDTO> getProductRecords(@PathVariable int id){
        return this.recordService.getProductRecords(id);
    }
    @GetMapping("getProductRecordsByPeriod/{id}/{periodStart}/{periodEnd}")
    public List<RecordDTO> getProductRecordsByPeriod(@PathVariable int id, @PathVariable("periodStart") @DateTimeFormat(pattern = "yyyy-MM-dd") Date periodStart, @PathVariable("periodEnd") @DateTimeFormat(pattern = "yyyy-MM-dd") Date periodEnd){
return this.recordService.getProductRecordsByPeriod(id, periodStart, periodEnd);
    }
    @GetMapping("getProductRecordsTrimester/{id}")
    public  List<RecordDTO> getProductRecordsTrimester(@PathVariable int id){
        return  this.recordService.getProductRecordLastTrimester(id);
    }
    @GetMapping("getProductRecordsLastWeek/{id}")
    public  List<RecordDTO> getProductRecordLastWeek(@PathVariable int id){
        return  this.recordService.getProductRecordLastWeek(id);
    }
    @GetMapping("getProductRecordsLastMonth/{id}")
    public  List<RecordDTO> getProductRecordLastMonth(@PathVariable int id){
        return  this.recordService.getProductRecordLastMonth(id);
    }
    @GetMapping("getProductRecordLastYear/{id}")
    public  List<RecordDTO> getProductRecordLastYear(@PathVariable int id){
        return  this.recordService.getProductRecordLastYear(id);
    }

}
