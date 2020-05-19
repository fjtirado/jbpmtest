package example.booking.controller;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import example.booking.model.BookChoice;
import example.booking.model.BookConfirmation;
import example.booking.model.BookingInfo;
import example.booking.model.BookingSelector;
import example.booking.model.HotelDescription;
import example.booking.model.RoomDescription;
import example.booking.operations.BookingOptionsService;
import example.booking.operations.HotelService;
import org.jbpm.services.api.ProcessService;
import org.jbpm.services.api.RuntimeDataService;
import org.jbpm.services.api.UserTaskService;
import org.jbpm.services.api.model.UserTaskInstanceDesc;
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

    @Inject
    private HotelService hotelService;

    @Inject
    private UserTaskService taskService;

    @Inject
    private RuntimeDataService runtimeService;

    @GetMapping
    public ModelAndView home() {
        ModelAndView result = new ModelAndView("booking");
        result.addObject("startbookinfo", new BookingSelector());
        result.addObject("cities", bookOptionsSvc.getCities());
        return result;
    }

    @PostMapping
    public ModelAndView startBooking(BookingSelector startBookInfo, HttpSession session) {
        Map<String, Object> params = new HashMap<>();
        session.setAttribute("bookingInfo", startBookInfo.getBookingInfo());
        params.put("bookingSelector", startBookInfo);
        Map<HotelDescription, Collection<RoomDescription>> rooms = new HashMap<>();
        ModelAndView result = new ModelAndView("selectHotel");
        result.addObject("rooms", rooms);
        result.addObject("bookchoice", new BookChoice());
        params.put("rooms", rooms);
        session.setAttribute("instanceId", processService.startProcess("business-application-kjar-1_0-SNAPSHOT", "booking", params));
        return result;
    }

    @PostMapping("confirm")
    public ModelAndView confirmBooking(BookChoice bookChoice, HttpSession session) {
        ModelAndView result = new ModelAndView("confirmation");
        BookConfirmation confirmation = hotelService.bookRoom((BookingInfo) session.getAttribute("bookingInfo"), bookChoice.getHotelId(), bookChoice.getRoomType());
        result.addObject("confirm", confirmation);
        long instanceId = (Long) session.getAttribute("instanceId");
        List<Long> tasks = runtimeService.getTasksByProcessInstanceId(instanceId);
        for (Long taskId : tasks) {
            UserTaskInstanceDesc task = runtimeService.getTaskById(taskId);
            if (task.getName().equals("Confirmation")) {
                String taskUser = task.getActualOwner() != null && !task.getActualOwner().isEmpty() ? task.getActualOwner() : "Administrator";
                taskService.activate(taskId, taskUser);
                taskService.start(taskId, taskUser);
                taskService.complete(taskId, taskUser, Collections.singletonMap("bookConfirmation", confirmation));
            }
        }
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
