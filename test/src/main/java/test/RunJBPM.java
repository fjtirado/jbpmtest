package test;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.sql.XADataSource;
import javax.transaction.TransactionManager;
import javax.transaction.TransactionSynchronizationRegistry;

import com.arjuna.ats.jta.common.jtaPropertyManager;
import com.mysql.cj.jdbc.MysqlXADataSource;
import org.apache.tomcat.dbcp.dbcp2.managed.BasicManagedDataSource;
import org.drools.persistence.jta.JtaTransactionManager;
import org.h2.jdbcx.JdbcDataSource;
import org.jbpm.process.core.context.variable.VariableScope;
import org.jbpm.process.core.impl.ProcessImpl;
import org.jbpm.process.instance.context.variable.VariableScopeInstance;
import org.jbpm.runtime.manager.impl.DefaultRegisterableItemsFactory;
import org.jbpm.services.task.events.DefaultTaskEventListener;
import org.kie.api.definition.process.Process;
import org.kie.api.event.process.DefaultProcessEventListener;
import org.kie.api.event.process.ProcessCompletedEvent;
import org.kie.api.event.process.ProcessStartedEvent;
import org.kie.api.io.Resource;
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
    private static final String PERSISTENCE_UNIT_NAME = "org.jbpm.persistence.jpa";

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

    //    private static KieServiceConfigurator getServiceConfiguration(CustomUser ugCallback) {
    //        KieServiceConfigurator services = new DefaultKieServiceConfigurator();
    //        services.configureServices(PERSISTENCE_UNIT_NAME, ugCallback, ugCallback);
    //        return services;
    //    }

    public static void main(String[] args) throws NamingException, SQLException {
        /*
          An alternative way of instantiating user group callback
           System.setProperty("org.jbpm.ht.callback", "custom");
          System.setProperty("org.jbpm.ht.custom.callback", "test.RunJBPM$CustomUser");
          */

        CustomUser ugCallback = new CustomUser();
        EntityManagerFactory emf = setupPersistence();
        //        KieServiceConfigurator services = getServiceConfiguration(ugCallback);
        //        DeploymentService deployService = services.getDeploymentService();
        //        logger.info("Deployed units {}", deployService.getDeployedUnits());

        DefaultRegisterableItemsFactory registerFactory = new DefaultRegisterableItemsFactory();
        registerFactory.addTaskListener(Sl4jTaskListener.class);
        registerFactory.addProcessListener(Sl4jProcessListener.class);
        Resource resource = ResourceFactory.newClassPathResource("Evaluation.bpmn");
        RuntimeEnvironment environment =
                RuntimeEnvironmentBuilder.Factory.get().newDefaultBuilder().entityManagerFactory(emf).userGroupCallback(ugCallback)
                                                 .addAsset(resource, ResourceType.BPMN2)
                                                 .registerableItemsFactory(registerFactory)
                                                 .get();

        RuntimeManager manager = RuntimeManagerFactory.Factory.get().newPerProcessInstanceRuntimeManager(environment);
        RuntimeEngine runtime = manager.getRuntimeEngine(ProcessInstanceIdContext.get());

        try {

            KieSession session = runtime.getKieSession();
            TaskService taskSvc = runtime.getTaskService();
            final String userId = "javi";

            Collection<Process> processes = environment.getKieBase().getProcesses();

            for (Process processDef : processes) {
                ProcessImpl context = (ProcessImpl) processDef;

                VariableScope variableScope = (VariableScope) context.getDefaultContext(VariableScope.VARIABLE_SCOPE);
                Map<String, Object> params = new HashMap<String, Object>();
                for (String variableName : variableScope.getVariableNames()) {
                    params.put(variableName, userId);
                }

                ProcessInstance process = session.startProcess(processDef.getId(), params);
                logger.info("Process: {}", process);
                while (session.getProcessInstance(process.getId()) != null) {
                    for (TaskSummary task : taskSvc.getTasksByStatusByProcessInstanceId(process.getId(), allowedStatus, "es-ES")) {
                        //if (task.getStatus() == Status.Reserved)
                        //  taskSvc.release(task.getId(), userId);
                        //  taskSvc.claim(task.getId(), userId);
                        String taskUserId = task.getActualOwnerId() != null ? task.getActualOwnerId() : userId;
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

    private static XADataSource getH2DS(String userName, String password) {
        JdbcDataSource sds = new JdbcDataSource();
        sds.setURL("jdbc:h2:mem:jbpm-db;MVCC=true");
        sds.setUser(userName);
        sds.setPassword(password);
        return sds;
    }

    private static XADataSource getMySQL(String userName, String password) throws SQLException {
        MysqlXADataSource sds = new MysqlXADataSource();
        sds.setUser(userName);
        sds.setPassword(password);
        sds.setDatabaseName("jbpmdb");
        sds.setServerTimezone("CET");
        return sds;
    }

    private static EntityManagerFactory setupPersistence() throws NamingException, SQLException {
        final String userName = "jbpm";
        final String password = "jbpm";
        Context ctx = new InitialContext();
        TransactionManager tm = com.arjuna.ats.jta.TransactionManager.transactionManager();
        TransactionSynchronizationRegistry tsr = jtaPropertyManager.getJTAEnvironmentBean().getTransactionSynchronizationRegistry();
        BasicManagedDataSource mds = new BasicManagedDataSource();
        mds.setUsername(userName);
        mds.setPassword(password);
        mds.setTransactionManager(tm);
        mds.setXaDataSourceInstance(getMySQL(userName, password));
        mds.setTransactionSynchronizationRegistry(tsr);
        ctx.bind("jdbc/jbpm-ds", mds);
        ctx.bind(JtaTransactionManager.DEFAULT_USER_TRANSACTION_NAME, com.arjuna.ats.jta.UserTransaction.userTransaction());
        ctx.bind("java:comp/TransactionManager", tm);
        ctx.bind("java:comp/TransactionSynchronizationRegistry", tsr);
        return Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    }

    private static class CustomUser implements UserGroupCallback/*, IdentityProvider*/ {

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

        //        @Override
        //        public String getName() {
        //            // TODO Auto-generated method stub
        //            return null;
        //        }
        //
        //        @Override
        //        public List<String> getRoles() {
        //            // TODO Auto-generated method stub
        //            return null;
        //        }
        //
        //        @Override
        //        public boolean hasRole(String role) {
        //            // TODO Auto-generated method stub
        //            return false;
        //        }
    }

}
