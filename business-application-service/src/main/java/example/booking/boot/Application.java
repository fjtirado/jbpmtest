package example.booking.boot;

import java.util.stream.Collectors;

import javax.inject.Inject;

import org.jbpm.services.api.DeploymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"example.booking.controller", "example.booking.services", "example.booking.boot"})
public class Application implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    @Inject
    private DeploymentService service;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        //service.getDeployedUnits().stream().forEach(d -> service.undeploy(d.getDeploymentUnit()));
        logger.info("Deployed services are {}", service.getDeployedUnits().stream().map(d -> d.getDeploymentUnit().getIdentifier()).collect(Collectors.toList()));
    }
}
