package com.kiroule.vaadin.demo.ui.view.orderedit;

import java.time.LocalTime;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.data.BeanValidationBinder;
import com.vaadin.data.BindingValidationStatus;
import com.vaadin.data.HasValue;
import com.vaadin.data.ValueContext;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewBeforeLeaveEvent;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.kiroule.vaadin.demo.backend.data.OrderState;
import com.kiroule.vaadin.demo.backend.data.entity.Order;
import com.kiroule.vaadin.demo.backend.data.entity.OrderItem;
import com.kiroule.vaadin.demo.ui.components.ConfirmPopup;
import com.kiroule.vaadin.demo.ui.util.DollarPriceConverter;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;

@SpringView(name = "order")
public class OrderEditView extends OrderEditViewDesign implements View {

	public enum Mode {
		EDIT, REPORT, CONFIRMATION;
	}

	private final OrderEditPresenter presenter;

	private final DollarPriceConverter priceConverter;

	private BeanValidationBinder<Order> binder;

	private Mode mode;

	private boolean hasChanges;

	private final BeanFactory beanFactory;

	@Autowired
	public OrderEditView(OrderEditPresenter presenter, BeanFactory beanFactory, DollarPriceConverter priceConverter) {
		this.presenter = presenter;
		this.beanFactory = beanFactory;
		this.priceConverter = priceConverter;
	}

	@PostConstruct
	public void init() {
		presenter.init(this);

		// We're limiting dueTime to even hours between 07:00 and 17:00
		dueTime.setItems(IntStream.range(7, 17).mapToObj(h -> LocalTime.of(h, 0)));

		// Binder takes care of binding Vaadin fields defined as Java member
		// fields in this class to properties in the Order bean
		binder = new BeanValidationBinder<>(Order.class);

		// Almost all fields are required, so we don't want to display
		// indicators
		binder.setRequiredConfigurator(null);

		// Bindings are done in the order the fields appear on the screen as we
		// report validation errors for the first invalid field and it is most
		// intuitive for the user that we start from the top if there are
		// multiple errors.
		binder.bindInstanceFields(this);

		// Must bind sub properties manually until
		// https://github.com/vaadin/framework/issues/9210 is fixed
		binder.bind(fullName, "customer.fullName");
		binder.bind(phone, "customer.phoneNumber");
		binder.bind(details, "customer.details");

		// Track changes manually as we use setBean and nested binders
		binder.addValueChangeListener(e -> hasChanges = true);

		addItems.addClickListener(e -> addEmptyOrderItem());
		cancel.addClickListener(e -> presenter.editBackCancelPressed());
		ok.addClickListener(e -> presenter.okPressed());
	}

	@Override
	public void enter(ViewChangeEvent event) {
		String orderId = event.getParameters();
		if ("".equals(orderId)) {
			presenter.enterView(null);
		} else {
			presenter.enterView(Long.valueOf(orderId));
		}
	}

	public void setOrder(Order order) {
		stateLabel.setValue(order.getState().getDisplayName());
		binder.setBean(order);
		productInfoContainer.removeAllComponents();

		reportHeader.setVisible(order.getId() != null);
		if (order.getId() == null) {
			addEmptyOrderItem();
			dueDate.focus();
		} else {
			orderId.setValue("#" + order.getId());
			for (OrderItem item : order.getItems()) {
				ProductInfo productInfo = createProductInfo(item);
				productInfo.setReportMode(mode != Mode.EDIT);
				productInfoContainer.addComponent(productInfo);
			}
			history.setOrder(order);
		}
		hasChanges = false;
	}

	private void addEmptyOrderItem() {
		OrderItem orderItem = new OrderItem();
		ProductInfo productInfo = createProductInfo(orderItem);
		productInfoContainer.addComponent(productInfo);
		productInfo.focus();
		getOrder().getItems().add(orderItem);
	}

	protected void removeOrderItem(OrderItem orderItem) {
		getOrder().getItems().remove(orderItem);

		for (Component c : productInfoContainer) {
			if (c instanceof ProductInfo && ((ProductInfo) c).getItem() == orderItem) {
				productInfoContainer.removeComponent(c);
				break;
			}
		}
	}

	/**
	 * Create a ProductInfo instance using Spring so that it is injected and can
	 * in turn inject a ProductComboBox and its data provider.
	 *
	 * @param orderItem
	 *            the item to edit
	 *
	 * @return a new product info instance
	 */
	private ProductInfo createProductInfo(OrderItem orderItem) {
		ProductInfo productInfo = beanFactory.getBean(ProductInfo.class);
		productInfo.setItem(orderItem);
		return productInfo;
	}

	protected Order getOrder() {
		return binder.getBean();
	}

	protected void setSum(int sum) {
		total.setValue(priceConverter.convertToPresentation(sum, new ValueContext(Locale.US)));
	}

	public void showNotFound() {
		removeAllComponents();
		addComponent(new Label("Order not found"));
	}

	public void setMode(Mode mode) {
		// Allow to style different modes separately
		if (this.mode != null) {
			removeStyleName(this.mode.name().toLowerCase());
		}
		addStyleName(mode.name().toLowerCase());

		this.mode = mode;
		binder.setReadOnly(mode != Mode.EDIT);
		for (Component c : productInfoContainer) {
			if (c instanceof ProductInfo) {
				((ProductInfo) c).setReportMode(mode != Mode.EDIT);
			}
		}
		addItems.setVisible(mode == Mode.EDIT);
		history.setVisible(mode == Mode.REPORT);
		state.setVisible(mode == Mode.EDIT);

		if (mode == Mode.REPORT) {
			cancel.setCaption("Edit");
			cancel.setIcon(VaadinIcons.EDIT);
			Optional<OrderState> nextState = presenter.getNextHappyPathState(getOrder().getState());
			ok.setCaption("Mark as " + nextState.map(OrderState::getDisplayName).orElse("?"));
			ok.setVisible(nextState.isPresent());
		} else if (mode == Mode.CONFIRMATION) {
			cancel.setCaption("Back");
			cancel.setIcon(VaadinIcons.ANGLE_LEFT);
			ok.setCaption("Place order");
			ok.setVisible(true);
		} else if (mode == Mode.EDIT) {
			cancel.setCaption("Cancel");
			cancel.setIcon(VaadinIcons.CLOSE);
			if (getOrder() != null && !getOrder().isNew()) {
				ok.setCaption("Save");
			} else {
				ok.setCaption("Review order");
			}
			ok.setVisible(true);
		} else {
			throw new IllegalArgumentException("Unknown mode " + mode);
		}
	}

	public Mode getMode() {
		return mode;
	}

	public Stream<HasValue<?>> validate() {
		Stream<HasValue<?>> errorFields = binder.validate().getFieldValidationErrors().stream()
				.map(BindingValidationStatus::getField);

		for (Component c : productInfoContainer) {
			if (c instanceof ProductInfo) {
				ProductInfo productInfo = (ProductInfo) c;
				if (!productInfo.isEmpty()) {
					errorFields = Stream.concat(errorFields, productInfo.validate());
				}
			}
		}
		return errorFields;
	}

	@Override
	public void beforeLeave(ViewBeforeLeaveEvent event) {
		if (!containsUnsavedChanges()) {
			event.navigate();
		} else {
			ConfirmPopup confirmPopup = beanFactory.getBean(ConfirmPopup.class);
			confirmPopup.showLeaveViewConfirmDialog(this, event::navigate);
		}
	}

	public void onProductInfoChanged() {
		hasChanges = true;
	}

	public boolean containsUnsavedChanges() {
		return hasChanges;
	}
}
