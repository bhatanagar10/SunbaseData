package com.project.sunbasedata.service;

import com.project.sunbasedata.model.Details;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ServiceClass {
    String authenticationService(String username , String password);
    List<Details> getCustomerListService(String access_token);
    String addCustomerService(Details details, String access_token);
    String deleteCustomerService(String s, String uuid);
    String updateCustomerService(String uuid, Details details);

}
