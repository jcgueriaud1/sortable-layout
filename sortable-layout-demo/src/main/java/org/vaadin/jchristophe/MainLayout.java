package org.vaadin.jchristophe;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.theme.lumo.LumoUtility;

public class MainLayout extends AppLayout {

    private H1 viewTitle;

    public MainLayout() {
        final DrawerToggle drawerToggle = new DrawerToggle();
        drawerToggle.setAriaLabel("Menu toggle");

        viewTitle = new H1();
        viewTitle.addClassName(LumoUtility.FontSize.LARGE);

        final SideNavItem simple = new SideNavItem("Simple demos", SortableLayoutView.class);
        final SideNavItem twoLayout = new SideNavItem("TwoLayout", TwoLayoutsView.class);
        final SideNavItem chosenEventsLayout = new SideNavItem("Chosen Events", ChosenEventLayoutsView.class);
        final SideNavItem multiDragLayout = new SideNavItem("Multiple drag", MultiDragTwoLayoutsView.class);
        final SideNavItem trello = new SideNavItem("Trello-like", TrelloLayoutView.class);
        final SideNavItem layoutEditor = new SideNavItem("Layout Editor", LayoutEditorView.class);
        final SideNavItem accessibleView = new SideNavItem("Accessible Drag and drop", AccessibleSortableView.class);
        final SideNav menuLayout = new SideNav();
        menuLayout.addItem(twoLayout, multiDragLayout, simple, trello, layoutEditor, chosenEventsLayout, accessibleView);
        addToDrawer(new SortableLayout(menuLayout));
        addToNavbar(true,drawerToggle, new Header(viewTitle));
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        viewTitle.setText(getCurrentPageTitle());
    }

    private String getCurrentPageTitle() {
        PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
        return title == null ? "" : title.value();
    }
}