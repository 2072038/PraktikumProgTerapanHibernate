package com.pterapan.demosql.controller;

import com.pterapan.demosql.dao.CategoryDao;
import com.pterapan.demosql.model.CategoryEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class CategoryController implements Initializable {

    public Label labelIdCategory;
    public TextField txtIdCategory;
    public Label labelNameCategory;
    public TextField txtNameCategory;
    public Button btnSave;
    public TableView<CategoryEntity> tableCategory;
    public TableColumn<CategoryEntity, Integer> colId;
    public TableColumn<CategoryEntity, String> colCatName;

    private ObservableList<CategoryEntity> clist;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        CategoryDao dao = new CategoryDao();
        clist = FXCollections.observableArrayList(dao.getData());

        tableCategory.setItems(clist);
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCatName.setCellValueFactory(new PropertyValueFactory<>("name"));
    }

    public void refresh() {
        CategoryDao dao = new CategoryDao();
        clist = FXCollections.observableArrayList(dao.getData());
        tableCategory.setItems(clist);
    }

    public void addCategory(ActionEvent actionEvent) {
        String id = txtIdCategory.getText();
        String name = txtNameCategory.getText();

        if (id != "" && name != "") {
            CategoryDao dao = new CategoryDao();
            CategoryEntity c = new CategoryEntity();
            c.setName(name);
            dao.addData(c);
            tableCategory.setItems(clist);
            refresh();

            txtIdCategory.clear();
            txtNameCategory.clear();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Please fill in all the field", ButtonType.OK);
            alert.showAndWait();
        }
    }
}
