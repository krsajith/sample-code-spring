package com.taomish.msservice01;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final RestTemplate restTemplate = new RestTemplate();
    private static final Logger log = LoggerFactory.getLogger(CustomerService.class);

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void update(Customer data) {
        var customer = customerRepository.findById(data.getUuid()).get();
        customer.setPlans(Map.of("time", LocalDateTime.now()));
        var body = restTemplate.getForEntity("https://dev.ctrm-xceler.com/ctrm-api/public/ping",String.class).getBody();
        log.info("Result {}",body);
        if(data.isSkip()){
            return;
        }
        customer.setLastName(data.getLastName());
        customer.setPlans(Map.of("time", LocalDateTime.now(),"status","updated"));
        customerRepository.save(customer);
        body = restTemplate.getForEntity("https://dev.ctrm-xceler.com/ctrm-api/public/ping",String.class).getBody();
        log.info("Result {}",body);
    }

    public void delete(Customer data) {
        customerRepository.deleteById(data.getUuid());
    }
}
