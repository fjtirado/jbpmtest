package example.booking.controller;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import example.booking.model.BookingSelector;
import example.booking.operations.BookingOptionsService;
import org.jbpm.process.core.context.variable.VariableScope;
import org.jbpm.process.instance.context.variable.VariableScopeInstance;
import org.jbpm.services.api.ProcessService;
import org.kie.api.event.process.DefaultProcessEventListener;
import org.kie.api.event.process.ProcessCompletedEvent;
import org.kie.api.event.process.ProcessNodeTriggeredEvent;
import org.kie.api.event.process.ProcessStartedEvent;
import org.kie.api.event.process.ProcessVariableChangedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("booking")
public class BookingController extends DefaultProcessEventListener {

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
        params.put("rooms", new HashMap<>());
        processService.startProcess("business-application-kjar-1_0-SNAPSHOT", "booking", params);
        ModelAndView result = new ModelAndView("confirm");
        result.addObject("name", startBookInfo.getBookingInfo().getUserName());
        return result;
    }

    @Override
    public void afterProcessStarted(ProcessStartedEvent event) {
        VariableScopeInstance variableScope = (VariableScopeInstance) ((org.jbpm.process.instance.ProcessInstance) event.getProcessInstance()).getContextInstance(VariableScope.VARIABLE_SCOPE);
        logger.info("Process started with variables {}", variableScope.getVariables());
    }

    @Override
    public void beforeProcessCompleted(ProcessCompletedEvent event) {
        VariableScopeInstance variableScope = (VariableScopeInstance) ((org.jbpm.process.instance.ProcessInstance) event.getProcessInstance()).getContextInstance(VariableScope.VARIABLE_SCOPE);
        logger.info("Process finished with variables {}", variableScope.getVariables());
    }

    public void beforeNodeTriggered(ProcessNodeTriggeredEvent event) {
        logger.info("Before node triggered {}", event);
    }

    @Override
    public void afterVariableChanged(ProcessVariableChangedEvent event) {
        logger.info("Variable change event {}", event);
    }
}
