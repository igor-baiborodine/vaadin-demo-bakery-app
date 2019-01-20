package com.kiroule.vaadin.bakeryapp.ui.views.admin.products;

import com.kiroule.vaadin.bakeryapp.backend.data.entity.Product;
import com.kiroule.vaadin.bakeryapp.ui.components.FormButtonsBar;
import com.kiroule.vaadin.bakeryapp.ui.crud.CrudView.CrudForm;
import com.kiroule.vaadin.bakeryapp.ui.utils.BakeryConst;
import com.vaadin.flow.component.HasText;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.templatemodel.TemplateModel;
import java.util.Currency;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

@Tag("product-form")
@HtmlImport("src/views/admin/products/product-form.html")
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ProductForm extends PolymerTemplate<TemplateModel> implements CrudForm<Product> {

	@Id("title")
	private H3 title;

	@Id("buttons")
	private FormButtonsBar buttons;

	@Id("name")
	private TextField name;

	@Id("price")
	private TextField price;

	@Override
	public void setBinder(BeanValidationBinder<Product> binder) {
		binder.bind(name, "name");

		binder.forField(price).withConverter(new PriceConverter()).bind("price");
		price.setPattern("\\d+(\\.\\d?\\d?)?$");
		price.setPreventInvalidInput(true);

		String currencySymbol = Currency.getInstance(BakeryConst.APP_LOCALE).getSymbol();
		price.setPrefixComponent(new Span(currencySymbol));
	}

	@Override
	public FormButtonsBar getButtons() {
		return buttons;
	}

	@Override
	public HasText getTitle() {
		return title;
	}
}
