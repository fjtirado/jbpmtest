package example.booking.controller;

import java.util.Collections;

import javax.inject.Inject;

import example.booking.model.BookingSelector;
import example.booking.operations.BookingOptionsService;
import org.jbpm.services.api.ProcessService;
import org.jbpm.services.api.RuntimeDataService;
import org.jbpm.services.api.model.ProcessInstanceDesc;
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

    @Inject
    ProcessService processService;

    @Inject
    RuntimeDataService runtimeService;

    @GetMapping
    public ModelAndView home() {
        ModelAndView result = new ModelAndView("booking");
        result.addObject("startbookinfo", new BookingSelector());
        result.addObject("cities", bookOptionsSvc.getCities());
        return result;
    }

    @PostMapping
    public ModelAndView startBooking(BookingSelector startBookInfo) {
        ModelAndView result = new ModelAndView("confirm");
        long processInstanceId = processService.startProcess("business-application-kjar-1_0-SNAPSHOT", "booking", Collections.singletonMap("bookingSelector", startBookInfo));
        ProcessInstanceDesc processDesc = runtimeService.getProcessInstanceById(processInstanceId);
        result.addObject("name", startBookInfo.getBookingInfo().getUserName());
        return result;
    }
}
