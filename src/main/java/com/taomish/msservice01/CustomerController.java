package com.taomish.msservice01;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    private static final Logger log = LoggerFactory.getLogger(CustomerController.class);
    private final CustomerRepository customerRepository;
    private final CustomerService customerService;

    public CustomerController(CustomerRepository customerRepository, CustomerService customerService) {
        this.customerRepository = customerRepository;
        this.customerService = customerService;
    }

    @PostMapping
    @RequestMapping("/create")
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    Customer post(@RequestBody Customer data) {
        var result = customerRepository.save(data);
        log.info("Save customer data: {}", data);
        return result;
    }

    @PostMapping
    @RequestMapping("/update")
    void update(@RequestBody Customer data) {
        customerService.update(data);
    }
}
