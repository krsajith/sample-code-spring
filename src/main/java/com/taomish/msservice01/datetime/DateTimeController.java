package com.taomish.msservice01.datetime;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/datetime")
@Slf4j
@CrossOrigin("*")
public class DateTimeController {

    private final DateSampleRepository dateSampleRepository;

    public DateTimeController(DateSampleRepository dateSampleRepository) {
        this.dateSampleRepository = dateSampleRepository;
    }

    @PostMapping
    @RequestMapping("/create")
    DateSample post(@RequestBody DateSample data) {
        return dateSampleRepository.save(data);
    }


    @GetMapping
    @RequestMapping("/fetch")
    List<DateSample> fetch() {
        return dateSampleRepository.findAll();
    }
}
