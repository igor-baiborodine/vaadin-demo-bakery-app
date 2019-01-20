package com.kiroule.vaadin.bakeryapp.app;

import com.vaadin.flow.server.BootstrapListener;
import com.vaadin.flow.server.BootstrapPageResponse;
import org.jsoup.nodes.Element;

/**
 * Modifies the Vaadin bootstrap page (the HTTP response) in order to
 * <ul>
 * <li>add service worker</li>
 * <li>add a link to the web app manifest</li>
 * <li>add links to favicons</li>
 * </ul>
 */
public class CustomBootstrapListener implements BootstrapListener {
	@Override
	public void modifyBootstrapPage(BootstrapPageResponse response) {

		final Element head = response.getDocument().head();

		// manifest needs to be prepended before scripts or it won't be loaded
		head.prepend("<meta name='theme-color' content='#227aef'>");
		head.prepend("<link rel='manifest' href='manifest.json'>");

		// Add service worker
		head.append("<script>if ('serviceWorker' in navigator) navigator.serviceWorker.register('sw.js')</script>");

		// add icons tags
		head.append("<link rel='shortcut icon' href='icons/favicon.ico'>");
		head.append("<link rel='icon' sizes='192x192' href='icons/icon-192.png'>");
		head.append("<link rel='icon' sizes='96x96' href='icons/icon-96.png'>");
		head.append("<link rel='apple-touch-icon' sizes='192x192' href='icons/icon-192.png'>");
		head.append("<link rel='apple-touch-icon' sizes='96x96' href='icons/icon-96.png'>");
	}

}
