package com.attilavarga.customer.relationship.manager.rest.client.service;

import java.util.List;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.attilavarga.customer.relationship.manager.rest.client.model.Customer;

@Service
public class CustomerServiceRestClientImpl implements CustomerService {

	private RestTemplate restTemplate;
	private String crmRestUrl;
	private Logger logger = Logger.getLogger(getClass().getName());
	
	@Autowired
	public CustomerServiceRestClientImpl(RestTemplate restTemplate, @Value("${crm.rest.url}") String url) {
		this.restTemplate = restTemplate;
		crmRestUrl = url;	
		logger.info("Loaded property:  crm.rest.url=" + crmRestUrl);
	}
	
	@Override
	public List<Customer> getCustomers() {	
		logger.info("in getCustomers(): Calling REST API " + crmRestUrl);

		ResponseEntity<List<Customer>> responseEntity = restTemplate.exchange(crmRestUrl, HttpMethod.GET, null, 
																  new ParameterizedTypeReference<List<Customer>>() {});
		List<Customer> customers = responseEntity.getBody();
		logger.info("in getCustomers(): customers" + customers);
		
		return customers;
	}

	@Override
	public Customer getCustomer(int id) {
		logger.info("in getCustomer(): Calling REST API " + crmRestUrl);
		Customer customer = restTemplate.getForObject(crmRestUrl + "/" + id, Customer.class);
		logger.info("in saveCustomer(): theCustomer=" + customer);
		
		return customer;
	}

	@Override
	public void saveCustomer(Customer customer) {
		logger.info("in saveCustomer(): Calling REST API " + crmRestUrl);	
		int employeeId = customer.getId();

		if (employeeId == 0) {
			restTemplate.postForEntity(crmRestUrl, customer, String.class);			
		
		} else {
			restTemplate.put(crmRestUrl, customer);
		}

		logger.info("in saveCustomer(): success");
	}

	@Override
	public void deleteCustomer(int id) {
		logger.info("in deleteCustomer(): Calling REST API " + crmRestUrl);
		restTemplate.delete(crmRestUrl + "/" + id);
		logger.info("in deleteCustomer(): deleted customer theId=" + id);
	}
}