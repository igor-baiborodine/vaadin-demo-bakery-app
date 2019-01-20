package com.kiroule.vaadin.bakeryapp.app;

import com.vaadin.flow.server.BootstrapListener;
import com.vaadin.flow.server.DependencyFilter;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.VaadinServiceInitListener;
import com.vaadin.flow.spring.annotation.SpringComponent;

/**
 * Configures the {@link VaadinService}:
 * <ul>
 *   <li>adds a {@link BootstrapListener} to add favicons, viewport,
 *   etc to the initial HTML sent to the browser (see {@link CustomBootstrapListener})</li>
 *   <li>adds a {@link DependencyFilter} to allow dependency bundling
 *   in the production mode (when all individual .html dependencies are combined a single
 *   file to improve the page load performance)</li>
 * </ul>
 */
@SpringComponent
public class CustomVaadinServiceInitListener implements VaadinServiceInitListener {

	@Override
	public void serviceInit(ServiceInitEvent event) {
		event.addBootstrapListener(new CustomBootstrapListener());
	}
}
