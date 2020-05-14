package example.booking.controller;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import example.booking.model.BookingSelector;
import example.booking.model.HotelDescription;
import example.booking.model.RoomDescription;
import example.booking.operations.BookingOptionsService;
import org.jbpm.services.api.ProcessService;
import org.jbpm.services.task.events.DefaultTaskEventListener;
import org.kie.api.task.TaskEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("booking")
public class BookingController extends DefaultTaskEventListener {

    private final static Logger logger = LoggerFactory.getLogger(BookingController.class);

    @Inject
    private BookingOptionsService bookOptionsSvc;

    @Inject
    private ProcessService processService;

    @GetMapping
    public ModelAndView home() {
        ModelAndView result = new ModelAndView("booking");
        result.addObject("startbookinfo", new BookingSelector());
        result.addObject("cities", bookOptionsSvc.getCities());
        return result;
    }

    @PostMapping
    public ModelAndView startBooking(BookingSelector startBookInfo) {
        Map<String, Object> params = new HashMap<>();
        params.put("bookingSelector", startBookInfo);
        Map<HotelDescription, Collection<RoomDescription>> rooms = new HashMap<>();
        ModelAndView result = new ModelAndView("confirm");
        result.addObject("name", startBookInfo.getBookingInfo().getUserName());
        result.addObject("rooms", rooms);
        params.put("rooms", rooms);
        processService.startProcess("business-application-kjar-1_0-SNAPSHOT", "booking", params);
        return result;
    }

    @Override
    public void beforeTaskAddedEvent(TaskEvent event) {
        logger.info("Task {} added by user {}", event.getTask(), event.getTaskContext().getUserId());
    }

    public void afterTaskReleasedEvent(TaskEvent event) {
        logger.info("Task {} released by user {}", event.getTask(), event.getTaskContext().getUserId());
    }

    @Override
    public void afterTaskStartedEvent(TaskEvent event) {
        logger.info("Task {} started by user {}", event.getTask(), event.getTaskContext().getUserId());
    }

    @Override
    public void afterTaskCompletedEvent(TaskEvent event) {
        logger.info("Task {} completed by user {}", event.getTask(), event.getTaskContext().getUserId());
    }

}
