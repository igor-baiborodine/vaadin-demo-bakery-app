package com.kiroule.vaadin.bakeryapp.ui.views.admin.users;

import static com.kiroule.vaadin.bakeryapp.ui.utils.BakeryConst.PAGE_USERS;

import com.kiroule.vaadin.bakeryapp.backend.data.Role;
import com.kiroule.vaadin.bakeryapp.backend.data.entity.User;
import com.kiroule.vaadin.bakeryapp.backend.data.entity.util.EntityUtil;
import com.kiroule.vaadin.bakeryapp.ui.MainView;
import com.kiroule.vaadin.bakeryapp.ui.components.SearchBar;
import com.kiroule.vaadin.bakeryapp.ui.crud.CrudEntityPresenter;
import com.kiroule.vaadin.bakeryapp.ui.crud.CrudView;
import com.kiroule.vaadin.bakeryapp.ui.utils.BakeryConst;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.templatemodel.TemplateModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;

@Tag("users-view")
@HtmlImport("src/views/admin/users/users-view.html")
@Route(value = PAGE_USERS, layout = MainView.class)
@PageTitle(BakeryConst.TITLE_USERS)
@Secured(Role.ADMIN)
public class UsersView extends CrudView<User, TemplateModel> {

	@Id("search")
	private SearchBar search;

	@Id("grid")
	private Grid<User> grid;

	private final CrudEntityPresenter<User> presenter;

	private final BeanValidationBinder<User> binder = new BeanValidationBinder<>(User.class);

	@Autowired
	public UsersView(CrudEntityPresenter<User> presenter, UserForm form) {
		super(EntityUtil.getName(User.class), form);
		this.presenter = presenter;
		form.setBinder(binder);

		setupEventListeners();
		setupGrid();
		presenter.setView(this);
	}

	private void setupGrid() {
		grid.addColumn(User::getEmail).setWidth("270px").setHeader("Email").setFlexGrow(5);
		grid.addColumn(u -> u.getFirstName() + " " + u.getLastName()).setHeader("Name").setWidth("200px").setFlexGrow(5);
		grid.addColumn(User::getRole).setHeader("Role").setWidth("150px");
	}

	@Override
	public Grid<User> getGrid() {
		return grid;
	}

	@Override
	protected CrudEntityPresenter<User> getPresenter() {
		return presenter;
	}

	@Override
	protected String getBasePage() {
		return PAGE_USERS;
	}

	@Override
	public SearchBar getSearchBar() {
		return search;
	}

	@Override
	protected BeanValidationBinder<User> getBinder() {
		return binder;
	}
}
