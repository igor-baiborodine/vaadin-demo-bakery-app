/**
 *
 */
package com.kiroule.vaadin.bakeryapp.ui.crud;

import com.kiroule.vaadin.bakeryapp.app.HasLogger;
import com.kiroule.vaadin.bakeryapp.backend.data.entity.AbstractEntity;
import com.kiroule.vaadin.bakeryapp.ui.components.FormButtonsBar;
import com.kiroule.vaadin.bakeryapp.ui.components.SearchBar;
import com.kiroule.vaadin.bakeryapp.ui.utils.TemplateUtil;
import com.kiroule.vaadin.bakeryapp.ui.views.EntityView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasText;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.templatemodel.TemplateModel;

public abstract class CrudView<E extends AbstractEntity, T extends TemplateModel> extends PolymerTemplate<T>
		implements HasLogger, EntityView<E>, HasUrlParameter<Long> {

	public interface CrudForm<E> {
		FormButtonsBar getButtons();

		HasText getTitle();

		void setBinder(BeanValidationBinder<E> binder);
	}

	private final Dialog dialog = new Dialog();

	private ConfirmDialog confirmation;

	private final CrudForm<E> form;

	private final String entityName;

	protected abstract CrudEntityPresenter<E> getPresenter();

	protected abstract String getBasePage();

	protected abstract BeanValidationBinder<E> getBinder();

	protected abstract SearchBar getSearchBar();

	protected abstract Grid<E> getGrid();

	public CrudView(String entityName, CrudForm<E> form) {
		this.entityName = entityName;
		this.form = form;

		dialog.add((Component) getForm());

		dialog.setHeight("100%");
		// Workaround for https://github.com/vaadin/vaadin-dialog-flow/issues/28
		dialog.getElement().addAttachListener(event -> UI.getCurrent().getPage().executeJavaScript(
				"$0.$.overlay.setAttribute('theme', 'right');", dialog.getElement()));
	}

    public CrudForm<E> getForm() {
		return form;
    }

	public void setupEventListeners() {
		getGrid().addSelectionListener(e -> {
			e.getFirstSelectedItem().ifPresent(entity -> {
				navigateToEntity(entity.getId().toString());
				getGrid().deselectAll();
			});
		});

		getForm().getButtons().addSaveListener(e -> getPresenter().save());
		getForm().getButtons().addCancelListener(e -> getPresenter().cancel());

		getDialog().getElement().addEventListener("opened-changed", e -> {
			if (!getDialog().isOpened()) {
				getPresenter().cancel();
			}
		});

		getForm().getButtons().addDeleteListener(e -> getPresenter().delete());

		getSearchBar().addActionClickListener(e -> getPresenter().createNew());
		getSearchBar()
				.addFilterChangeListener(e -> getPresenter().filter(getSearchBar().getFilter()));

		getSearchBar().setActionText("New " + entityName);
		getBinder().addValueChangeListener(e -> getPresenter().onValueChange(isDirty()));
	}

	protected void navigateToEntity(String id) {
		getUI().ifPresent(ui -> ui.navigate(TemplateUtil.generateLocation(getBasePage(), id)));
	}

	@Override
	public ConfirmDialog getConfirmDialog() {
		return confirmation;
	}

	@Override
	public void setConfirmDialog(ConfirmDialog confirmDialog) {
		this.confirmation = confirmDialog;
	}

	@Override
	public void setParameter(BeforeEvent event, @OptionalParameter Long id) {
		if (id != null) {
			getPresenter().loadEntity(id);
		} else if (getDialog().isOpened()) {
		    getPresenter().closeSilently();
        }
	}

	public Dialog getDialog() {
		return dialog;
	}

	public void closeDialog() {
		getDialog().setOpened(false);
		navigateToEntity(null);
	}

	public void openDialog() {
		getDialog().setOpened(true);
	}

	public void updateTitle(boolean newEntity) {
		getForm().getTitle().setText((newEntity ? "New" : "Edit") + " " + entityName);
	}

	@Override
	public void write(E entity) throws ValidationException {
		getBinder().writeBean(entity);
	}

	@Override
	public boolean isDirty() {
		return getBinder().hasChanges();
	}

	@Override
	public void clear() {
		getBinder().readBean(null);
	}

	@Override
	public String getEntityName() {
		return entityName;
	}
}
