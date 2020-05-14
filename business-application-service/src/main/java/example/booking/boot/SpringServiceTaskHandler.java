package example.booking.boot;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import javax.inject.Inject;

import org.jbpm.bpmn2.handler.WorkItemHandlerRuntimeException;
import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemHandler;
import org.kie.api.runtime.process.WorkItemManager;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component("Service Task")
public class SpringServiceTaskHandler implements WorkItemHandler {

    @Inject
    ApplicationContext ctx;

    private static final Collection<String> ignoreMethods = new HashSet<>();

    static {
        ignoreMethods.add("toString");
        ignoreMethods.add("hashCode");
        ignoreMethods.add("equals");
    }

    @Override
    public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {

        Class<?> interfaceClass;
        try {
            interfaceClass = Class.forName((String) workItem.getParameter("Interface"));
        } catch (ClassNotFoundException e) {
            try {
                interfaceClass = Class.forName((String) workItem.getParameter("interfaceImplementationRef"));
            } catch (ClassNotFoundException e1) {
                throw new WorkItemHandlerRuntimeException(e1);
            }
        }

        Object parameter = workItem.getParameter("Parameter");
        Class<?> parameterClass = parameter != null ? parameter.getClass() : null;
        String methodName = (String) workItem.getParameter("Operation");
        String altMethodName = (String) workItem.getParameter("operationImplementationRef");
        Method[] methods = interfaceClass.getMethods();
        Object[] chosenOneParams = null;
        Method chosenOne = null;
        for (Method method : methods) {
            if (method.getName().equals(methodName) || method.getName().equals(altMethodName)) {
                Class[] methodParams = method.getParameterTypes();
                if (methodParams.length == 0) {
                    if (parameter == null) {
                        chosenOne = method;
                        chosenOneParams = new Object[0];
                    }
                }
                if (methodParams.length == 1) {
                    if (methodParams[0].isAssignableFrom(parameterClass)) {
                        chosenOne = method;
                        chosenOneParams = new Object[]{parameter};
                        break;
                    }
                } else if (parameterClass != null) {
                    Method[] candidateMethods = new Method[methodParams.length];
                    int counter = methodParams.length;
                    for (Method childMethod : parameterClass.getMethods()) {
                        if (!ignoreMethods.contains(childMethod.getName())) {
                            for (int i = 0; i < methodParams.length; i++) {
                                if (methodParams[i].isAssignableFrom(childMethod.getReturnType())) {
                                    if (candidateMethods[i] == null) {
                                        counter--;
                                    }
                                    candidateMethods[i] = childMethod;
                                    break;
                                }
                            }
                            if (counter == 0)
                                break;
                        }
                    }
                    if (counter == 0) {
                        chosenOne = method;
                        chosenOneParams = new Object[candidateMethods.length];
                        for (int i = 0; i < chosenOneParams.length; i++) {
                            try {
                                chosenOneParams[i] = candidateMethods[i].invoke(parameter);
                            } catch (IllegalAccessException e) {
                                throw new WorkItemHandlerRuntimeException(e);
                            } catch (InvocationTargetException e) {
                                throw new WorkItemHandlerRuntimeException(e.getTargetException());
                            }
                        }
                        break;
                    }
                }
            }
        }
        if (chosenOne == null)
            throw new WorkItemHandlerRuntimeException(null, "cannot find suitable method {" + methodName + "," + altMethodName + " for class " + interfaceClass);
        try {
            manager.completeWorkItem(workItem.getId(), Collections.singletonMap("result", chosenOne.invoke(ctx.getBean(interfaceClass), chosenOneParams)));
        } catch (BeansException | IllegalAccessException e) {
            throw new WorkItemHandlerRuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new WorkItemHandlerRuntimeException(e.getTargetException());
        }
    }

    @Override
    public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {
        // do nothing, cannot be aborted
    }

}
