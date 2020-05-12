package com.company.controller;

import javax.inject.Inject;

import com.company.model.StartBookInfo;
import com.company.services.BookingOptionsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("booking")
public class BookingController {

    @Inject
    BookingOptionsService bookOptionsSvc;

    @GetMapping
    public ModelAndView home() {
        ModelAndView result = new ModelAndView("booking");
        result.addObject("startbookinfo", new StartBookInfo());
        result.addObject("cities", bookOptionsSvc.getCities());
        return result;
    }

    @PostMapping
    public ModelAndView startBooking(StartBookInfo startBookInfo) {
        ModelAndView result = new ModelAndView("confirm");
        result.addObject("name", startBookInfo.getUserName());
        return result;
    }
}
