package com.kiroule.vaadin.bakeryapp.app;

import org.springframework.stereotype.Component;

import com.vaadin.server.ServiceInitEvent;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinServiceInitListener;

/**
 * Configures the VaadinService instance that serves the app through a servlet.
 * <p>
 * Uses a bootstrap listener to modify the bootstrap HTML page and include icons
 * for home screen for mobile devices.
 */
@Component
public class ApplicationInitListener implements VaadinServiceInitListener {

	@Override
	public void serviceInit(ServiceInitEvent serviceInitEvent) {
		VaadinService service = serviceInitEvent.getSource();

		service.addSessionInitListener(event -> event.getSession().addBootstrapListener(new IconBootstrapListener()));
	}
}
