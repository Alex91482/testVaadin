package org.example.views;

import com.vaadin.navigator.View;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.components.grid.MultiSelectionModel;
import org.example.entity.MyEvent;
import org.example.util.jdbc.dao.MyEventDAO;

import java.util.List;
import java.util.Set;

public class GridView extends VerticalLayout implements View {
    public GridView(){
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Grid<MyEvent> grid1 = makeATable();

        MultiSelectionModel<MyEvent> model = (MultiSelectionModel<MyEvent>)
                grid1.setSelectionMode(Grid.SelectionMode.MULTI);
        grid1.addSelectionListener(event -> {
            Set<MyEvent> set = event.getAllSelectedItems();

        });

        Button buttonSelect = new Button("Add All Entries");
        buttonSelect.addClickListener(event -> {
            insertAllMyEvent(grid1);
        });
        Button buttonDelete = new Button("Delete");
        buttonDelete.addClickListener(event -> {
            //
        });
        horizontalLayout.addComponents(buttonSelect);

        this.addComponents(grid1, horizontalLayout);
    }

    private Grid<MyEvent> makeATable(){
        Grid<MyEvent> grid = new Grid<>();
        grid.addColumn(MyEvent::getId).setCaption("Id");
        grid.addColumn(MyEvent::getName).setCaption("Name");
        grid.addColumn(MyEvent::getDate).setCaption("Date");
        grid.addColumn(MyEvent::getCity).setCaption("City");
        grid.addColumn(MyEvent::getBuilding).setCaption("Building");
        return grid;
    }

    private void insertAllMyEvent(Grid<MyEvent> grid){
        List<MyEvent> list = new MyEventDAO().findAllMyEvent(); //получить все события

        grid.setItems(list);
    }


}
