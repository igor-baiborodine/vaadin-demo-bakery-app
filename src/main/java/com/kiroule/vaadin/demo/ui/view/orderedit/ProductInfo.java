package com.kiroule.vaadin.demo.ui.view.orderedit;

import java.util.Locale;
import java.util.Optional;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.annotation.PrototypeScope;
import org.vaadin.spring.events.EventBus.ViewEventBus;

import com.vaadin.data.BeanValidationBinder;
import com.vaadin.data.BindingValidationStatus;
import com.vaadin.data.HasValue;
import com.vaadin.data.ValueContext;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.spring.annotation.SpringComponent;
import com.kiroule.vaadin.demo.backend.data.entity.OrderItem;
import com.kiroule.vaadin.demo.backend.data.entity.Product;
import com.kiroule.vaadin.demo.ui.util.DollarPriceConverter;
import com.vaadin.ui.Label;

@SpringComponent
@PrototypeScope
public class ProductInfo extends ProductInfoDesign {

	private final DollarPriceConverter priceFormatter;

	private final ViewEventBus viewEventBus;

	private BeanValidationBinder<OrderItem> binder;

	// Use Label instead of TextArea in "report mode" for a better presentation
	private Label readOnlyComment = new Label();

	private boolean reportMode = false;

	@Autowired
	public ProductInfo(DollarPriceConverter priceFormatter, ViewEventBus viewEventBus) {
		this.priceFormatter = priceFormatter;
		this.viewEventBus = viewEventBus;

	}

	@PostConstruct
	public void init() {
		binder = new BeanValidationBinder<>(OrderItem.class);
		binder.setRequiredConfigurator(null);
		binder.forField(quantity).withConverter(new StringToIntegerConverter(-1, "Please enter a number"))
				.bind("quantity");
		binder.bindInstanceFields(this);
		binder.addValueChangeListener(e -> fireProductInfoChanged());

		product.addSelectionListener(e -> {
			Optional<Product> selectedProduct = e.getFirstSelectedItem();
			int productPrice = selectedProduct.map(Product::getPrice).orElse(0);
			updatePrice(productPrice);
		});

		readOnlyComment.setWidth("100%");
		readOnlyComment.setId(comment.getId());
		readOnlyComment.setStyleName(comment.getStyleName());

		delete.addClickListener(e -> fireOrderItemDeleted());
	}

	private void updatePrice(int productPrice) {
		price.setValue(priceFormatter.convertToPresentation(productPrice, new ValueContext(Locale.US)));
	}

	private void fireProductInfoChanged() {
		viewEventBus.publish(this, new ProductInfoChangeEvent());
	}

	private void fireOrderItemDeleted() {
		viewEventBus.publish(this, new OrderItemDeletedEvent(getItem()));
	}

	public int getSum() {
		OrderItem item = getItem();
		return item.getQuantity() * item.getProduct().getPrice();
	}

	public void setItem(OrderItem item) {
		binder.setBean(item);
	}

	public OrderItem getItem() {
		return binder.getBean();
	}

	public void setReportMode(boolean reportMode) {
		if (reportMode == this.reportMode) {
			return;
		}
		this.reportMode = reportMode;
		binder.setReadOnly(reportMode);
		delete.setVisible(!reportMode);

		// Swap the TextArea for a Label in report mode
		if (reportMode) {
			readOnlyComment.setVisible(!comment.isEmpty());
			readOnlyComment.setValue(comment.getValue());
			replaceComponent(comment, readOnlyComment);
		} else {
			replaceComponent(readOnlyComment, comment);
		}
	}

	/**
	 * Checks if no product has been selected. If no product is selected, the
	 * whole product info section is ignored when saving changes.
	 *
	 * @return <code>true</code> if no product is selected, <code>false</code>
	 *         otherwise
	 */
	public boolean isEmpty() {
		return product.isEmpty();
	}

	public Stream<HasValue<?>> validate() {
		return binder.validate().getFieldValidationErrors().stream().map(BindingValidationStatus::getField);
	}

	@Override
	public void focus() {
		product.focus();
	}
}
