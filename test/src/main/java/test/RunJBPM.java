package test;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;
import javax.persistence.EntityManagerFactory;

import org.jbpm.process.core.context.variable.VariableScope;
import org.jbpm.process.core.impl.ProcessImpl;
import org.jbpm.process.instance.context.variable.VariableScopeInstance;
import org.jbpm.runtime.manager.impl.DefaultRegisterableItemsFactory;
import org.jbpm.services.task.events.DefaultTaskEventListener;
import org.kie.api.definition.process.Process;
import org.kie.api.event.process.DefaultProcessEventListener;
import org.kie.api.event.process.ProcessCompletedEvent;
import org.kie.api.event.process.ProcessStartedEvent;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.manager.RuntimeEnvironment;
import org.kie.api.runtime.manager.RuntimeEnvironmentBuilder;
import org.kie.api.runtime.manager.RuntimeManager;
import org.kie.api.runtime.manager.RuntimeManagerFactory;
import org.kie.api.runtime.process.ProcessInstance;
import org.kie.api.task.TaskEvent;
import org.kie.api.task.TaskService;
import org.kie.api.task.UserGroupCallback;
import org.kie.api.task.model.Status;
import org.kie.api.task.model.TaskSummary;
import org.kie.internal.io.ResourceFactory;
import org.kie.internal.runtime.manager.context.ProcessInstanceIdContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RunJBPM {

    private final static Logger logger = LoggerFactory.getLogger(RunJBPM.class);

    private static final List<Status> allowedStatus = Arrays.asList(Status.Reserved, Status.Ready, Status.InProgress);
    private static final String USER_ID = "javi";

    // needs to be public 
    public static class Sl4jTaskListener extends DefaultTaskEventListener {

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

    public static class Sl4jProcessListener extends DefaultProcessEventListener {

        @Override
        public void afterProcessStarted(ProcessStartedEvent event) {
            VariableScopeInstance variableScope = (VariableScopeInstance) ((org.jbpm.process.instance.ProcessInstance) event.getProcessInstance()).getContextInstance(VariableScope.VARIABLE_SCOPE);
            logger.info("Process started with variables {}", variableScope.getVariables());
        }

        @Override
        public void beforeProcessCompleted(ProcessCompletedEvent event) {
            logger.info("this is the end, my little friend");
        }
    }

    public static void main(String[] args) throws NamingException, SQLException {

        EntityManagerFactory emf = PersistenceHelper.setupPersistence();
        DefaultRegisterableItemsFactory registerFactory = new DefaultRegisterableItemsFactory();
        registerFactory.addTaskListener(Sl4jTaskListener.class);
        registerFactory.addProcessListener(Sl4jProcessListener.class);
        /*
        An alternative way of instantiating user group callback
         System.setProperty("org.jbpm.ht.callback", "custom");
        System.setProperty("org.jbpm.ht.custom.callback", "test.RunJBPM$CustomUser");
        */
        RuntimeEnvironment environment =
                RuntimeEnvironmentBuilder.Factory.get().newDefaultBuilder().entityManagerFactory(emf).userGroupCallback(new CustomUser())
                                                 .addAsset(ResourceFactory.newClassPathResource("Evaluation.bpmn"), ResourceType.BPMN2)
                                                 .registerableItemsFactory(registerFactory)
                                                 .get();

        RuntimeManager manager = RuntimeManagerFactory.Factory.get().newPerProcessInstanceRuntimeManager(environment);
        RuntimeEngine runtime = manager.getRuntimeEngine(ProcessInstanceIdContext.get());

        try {

            KieSession session = runtime.getKieSession();
            TaskService taskSvc = runtime.getTaskService();

            Collection<Process> processes = environment.getKieBase().getProcesses();

            for (Process processDef : processes) {
                ProcessImpl context = (ProcessImpl) processDef;

                VariableScope variableScope = (VariableScope) context.getDefaultContext(VariableScope.VARIABLE_SCOPE);
                Map<String, Object> params = new HashMap<String, Object>();
                for (String variableName : variableScope.getVariableNames()) {
                    params.put(variableName, USER_ID);
                }

                ProcessInstance process = session.startProcess(processDef.getId(), params);
                logger.info("Process: {}", process);
                while (session.getProcessInstance(process.getId()) != null) {
                    for (TaskSummary task : taskSvc.getTasksByStatusByProcessInstanceId(process.getId(), allowedStatus, "es-ES")) {
                        //if (task.getStatus() == Status.Reserved)
                        //  taskSvc.release(task.getId(), userId);
                        //  taskSvc.claim(task.getId(), userId);
                        String taskUserId = task.getActualOwnerId() != null ? task.getActualOwnerId() : USER_ID;
                        taskSvc.start(task.getId(), taskUserId);
                        taskSvc.complete(task.getId(), taskUserId, Collections.singletonMap("evaluation", "dissapointed"));
                    }
                }
            }
        } finally {
            manager.disposeRuntimeEngine(runtime);
            manager.close();
            emf.close();
        }
    }

    private static class CustomUser implements UserGroupCallback {

        @Override
        public boolean existsUser(String userId) {
            return true;
        }

        @Override
        public boolean existsGroup(String groupId) {
            return true;
        }

        @Override
        public List<String> getGroupsForUser(String userId) {
            return Arrays.asList("HR", "PM");
        }
    }
}
