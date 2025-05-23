package com.example.demo.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.management.ValueExp;
import java.util.List;
@RestController
public class ApiController {

    @GetMapping(value = "/")
    public String getpage(){
        return "Welcome";
    }
}
