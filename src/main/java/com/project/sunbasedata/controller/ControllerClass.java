package com.project.sunbasedata.controller;

import com.project.sunbasedata.model.Credentials;
import com.project.sunbasedata.model.Details;
import com.project.sunbasedata.service.ServiceClass;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import reactor.core.publisher.Flux;

import javax.print.attribute.standard.Media;
import java.util.List;

@Controller
@RequestMapping("/api")
public class ControllerClass {

    @Autowired
    private ServiceClass serviceClass;
    private String access_token = null;

    @GetMapping("/login")
    public String showLogin(){
        return "login";
    }

    /*@PostMapping("/login")
    public ModelAndView authorization(@RequestParam String username , @RequestParam String password){
        access_token = serviceClass.authenticationService(username,password);
        return new ModelAndView("redirect:/api/customers");
    }*/

    @PostMapping(value = "/login" , consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    @ResponseBody
    public String authorization( Credentials credentials){
        System.out.println(credentials);
        return "jdsfcjkds";
    }

    @GetMapping("/customers")
    public ModelAndView getCustomers(){

        if(access_token == null){
            return new ModelAndView("login");
        }
        ModelAndView modelAndView = new ModelAndView("home");
        List<Details> listOfCustomers = serviceClass.getCustomerListService(access_token);
        modelAndView.addObject("listOfCustomers" , listOfCustomers);
        return modelAndView;
    }

    @GetMapping("/delete")
    public ModelAndView deleteCustomer(@RequestParam String uuid ){
        serviceClass.deleteCustomerService(access_token , uuid);
        return new ModelAndView("redirect:/api/customers");
    }

    @GetMapping("/add")
    public ModelAndView addCustomerGet(HttpSession session){
        if(access_token == null){
            return new ModelAndView("login");
        }
        Details details = new Details();
        ModelAndView modelAndView = new ModelAndView("customer");
        modelAndView.addObject("details" , details);
        session.setAttribute("type" , "create");
        return modelAndView;

    }

    @PostMapping("/addAndupdate")
    public ModelAndView addCustomerPost(@ModelAttribute Details details , HttpSession session){
        System.out.println(details);

        if(session.getAttribute("type").equals("create")){
            serviceClass.addCustomerService(details , access_token);
        }
        if(session.getAttribute("type").equals("update")){
            serviceClass.updateCustomerService(access_token , details);
        }
        System.out.println((String) session.getAttribute("type"));
        return new ModelAndView("redirect:/api/customers");
    }

    @GetMapping("update")
    public ModelAndView updateCustomer(@RequestParam String uuid , HttpSession session){
        if(access_token == null){
            return new ModelAndView("login");
        }
        ModelAndView modelAndView = new ModelAndView("customer");
        List<Details> listOfCustomers = serviceClass.getCustomerListService(access_token);
        Details current = new Details();
        for(Details d : listOfCustomers){
            if(d.getUuid().equals(uuid)) {
                current = d;
                break;
            }
        }
        modelAndView.addObject("details" , current);
        session.setAttribute("type" , "update");
        return modelAndView;
    }

    @PostMapping("/update")
    public ModelAndView addCustomerPost(@ModelAttribute Details details){
        serviceClass.addCustomerService(details , access_token);
        return new ModelAndView("redirect:/api/customers");
    }
}
