package com.javadeveloperzone;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {
    @RequestMapping(value = "/")
    public String landing(){
        return "Spring boot Gradle Example!!";
    }
    
    @RequestMapping(value = "hello")
    public String hello(){
        return "Spring boot Gradle Example Hello!!";
    }
}
