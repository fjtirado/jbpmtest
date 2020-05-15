package example.booking.boot;

import org.jbpm.process.core.context.variable.VariableScope;
import org.jbpm.process.instance.context.variable.VariableScopeInstance;
import org.kie.api.event.process.DefaultProcessEventListener;
import org.kie.api.event.process.ProcessCompletedEvent;
import org.kie.api.event.process.ProcessStartedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SpringProcessEventListener extends DefaultProcessEventListener {

    private static final Logger logger = LoggerFactory.getLogger(SpringProcessEventListener.class);

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

}
