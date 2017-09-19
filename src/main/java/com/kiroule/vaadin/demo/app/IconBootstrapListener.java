package com.kiroule.vaadin.demo.app;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.vaadin.server.BootstrapFragmentResponse;
import com.vaadin.server.BootstrapListener;
import com.vaadin.server.BootstrapPageResponse;

/**
 * Adds link-tags for "add to homescreen" icons to the head-section of the
 * bootstrap page.
 * <p>
 * Generates links of the type
 *
 * <pre>
 * {@code
 * <link rel="icon" sizes="96x96" href="VAADIN/themes/apptheme/icon-96.png">
 * <link rel="apple-touch-icon" sizes="192x192" href=
"VAADIN/themes/apptheme/icon-192.png">
 * }
 * </pre>
 * </p>
 */
public class IconBootstrapListener implements BootstrapListener {

	protected String baseUri = "theme://icon-";
	protected String extension = ".png";
	protected String[] rels = { "icon", "apple-touch-icon" };
	protected int[] sizes = { 192, 96 };

	@Override
	public void modifyBootstrapFragment(BootstrapFragmentResponse response) {
		// NOP
	}

	@Override
	public void modifyBootstrapPage(BootstrapPageResponse response) {
		// Generate link-tags for "add to homescreen" icons
		final Document document = response.getDocument();
		final Element head = document.getElementsByTag("head").get(0);
		for (String rel : rels) {
			for (int size : sizes) {
				String iconUri = baseUri + size + extension;
				String href = response.getUriResolver().resolveVaadinUri(iconUri);
				String s = size + "x" + size;
				Element element = document.createElement("link");
				element.attr("rel", rel);
				element.attr("sizes", s);
				element.attr("href", href);
				head.appendChild(element);
			}
		}

		/*- Enable these to hide browser controls when app is started from homescreen:
		Element element = document.createElement("meta");
		element.attr("name", "mobile-web-app-capable");
		element.attr("content", "yes");
		head.appendChild(element);

		element = document.createElement("meta");
		element.attr("name", "apple-mobile-web-app-capable");
		element.attr("content", "yes");
		head.appendChild(element);
		*/
	}

}