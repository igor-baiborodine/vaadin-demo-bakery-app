package com.kiroule.vaadin.demo.app;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.vaadin.server.ConnectorIdGenerator;
import com.vaadin.server.ServiceInitEvent;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinServiceInitListener;
import com.vaadin.server.VaadinSession;
import org.vaadin.connectoridgenerator.ComponentIdBasedConnectorIdGenerator;

/**
 * Configures the VaadinService instance that serves the app through a servlet.
 * <p>
 * In case of load test mode being enabled, uses a separate connector id
 * generator to make scalability tests more stable.
 * <p>
 * Uses a bootstrap listener to modify the bootstrap HTML page and include icons
 * for home screen for mobile devices.
 */
@Component
public class ApplicationInitListener implements VaadinServiceInitListener {

    @Value("${loadtestmode.enabled}")
    private boolean loadTestModeEnabled;

    @Override
    public void serviceInit(ServiceInitEvent serviceInitEvent) {
        VaadinService service = serviceInitEvent.getSource();

        service.addSessionInitListener(event -> event.getSession()
                .addBootstrapListener(new IconBootstrapListener()));

        if (loadTestModeEnabled) {
            Logger.getLogger(ApplicationInitListener.class.getName()).log(
                    Level.WARNING,
                    "Running in load test mode, do not use this in production");

            serviceInitEvent.addConnectorIdGenerator(
                    event -> getGenerator(event.getSession())
                            .generateConnectorId(event));
        }
    }

    private static ConnectorIdGenerator getGenerator(VaadinSession session) {
        ConnectorIdGenerator generator = session
                .getAttribute(ConnectorIdGenerator.class);
        if (generator == null) {
            generator = new ComponentIdBasedConnectorIdGenerator();
            session.setAttribute(ConnectorIdGenerator.class, generator);
        }
        return generator;
    }
}
