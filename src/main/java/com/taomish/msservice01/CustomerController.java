package com.taomish.msservice01;

import com.taomish.msservice01.repo.ActualizedQuantityObligations;
import com.taomish.msservice01.repo.ActualizedQuantityObligationsRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    private static final Logger log = LoggerFactory.getLogger(CustomerController.class);
    private final CustomerRepository customerRepository;
    private final CustomerService customerService;
    private final ActualizedQuantityObligationsRepository actualizedQuantityObligationsRepository;


    public CustomerController(CustomerRepository customerRepository, CustomerService customerService, ActualizedQuantityObligationsRepository actualizedQuantityObligationsRepository) {
        this.customerRepository = customerRepository;
        this.customerService = customerService;
        this.actualizedQuantityObligationsRepository = actualizedQuantityObligationsRepository;
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

    @PostMapping
    @RequestMapping("/delete")
    void delete(@RequestBody Customer data) {
        customerService.delete(data);
    }


    @GetMapping
    @RequestMapping("/mock")
    Customer mock() {
        var customer = new Customer();
        customer.setLastName("Last Name");
        customer.setFirstName("First Name");
        return customer;
    }

    @GetMapping
    @RequestMapping("/tes-audit")
    @Transactional
    List<ActualizedQuantityObligations> testAudit(@RequestParam("obId") String obId) {
        var data = actualizedQuantityObligationsRepository.findAllByTenantIdAndPlannedObligationId("d7408d31-d720-4173-b76e-0595ce2679b4", obId);

        actualizedQuantityObligationsRepository.deleteAll(data);
        return Collections.emptyList();
    }
}
