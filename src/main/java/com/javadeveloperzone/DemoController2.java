package com.javadeveloperzone;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class DemoController2 {
	String logPrefix = this.getClass().toString();
    @RequestMapping(value = "/test")
    public ModelAndView test(){
    	System.out.println( logPrefix + ".test()");
    	
    	ModelAndView mav = new ModelAndView();
		mav.setViewName("test"); 
		
        return mav;
    }
    @RequestMapping(value = "/insertData")
    public ModelAndView insertData(@RequestParam("id") String id, @RequestParam("pw") String pw){
    	System.out.println( logPrefix + ".insertData()");
    	
    	System.out.println( logPrefix + ".insertData() id: " + id);
    	System.out.println( logPrefix + ".insertData() pw: " + pw);
    	
    	ModelAndView mav = new ModelAndView();
		mav.setViewName("test"); 
		
		return mav;
    }
}
