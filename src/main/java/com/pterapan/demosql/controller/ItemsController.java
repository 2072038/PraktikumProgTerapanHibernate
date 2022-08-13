package com.pterapan.demosql.controller;

import com.pterapan.demosql.dao.CategoryDao;
import com.pterapan.demosql.dao.ItemsDao;
import com.pterapan.demosql.HelloApplication;
import com.pterapan.demosql.model.CategoryEntity;
import com.pterapan.demosql.model.ItemsEntity;
import com.pterapan.demosql.util.MyConnection;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.*;

public class ItemsController implements Initializable {
    public MenuItem labelMenu;
    public Label labelId;
    public TextField txtIdItems;
    public Label labelName;
    public TextField txtNameItems;
    public Label labelPrice;
    public TextField txtPrice;
    public Label labelDescription;
    public TextArea txtDescription;
    public ComboBox<CategoryEntity> cmb1;
    public TableView<ItemsEntity> tableItems;
    public TableColumn<ItemsEntity, Integer> colId;
    public TableColumn<ItemsEntity, String> colName;
    public TableColumn<ItemsEntity, Double> colPrice;
    public TableColumn<ItemsEntity, CategoryEntity> colCategory;
    public Button btnUpdate;
    public Button bnSave;
    public Button btnReset;
    public Button btnDelete;
    public MenuItem labelSimple;
    public MenuItem labelGroup;

    private ObservableList<ItemsEntity> ilist;
    private ObservableList<CategoryEntity> clist;
    private ItemsDao itemsDao;
    private CategoryDao categoryDao;

    private ItemsEntity selectedItems;

    private Stage stage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        itemsDao = new ItemsDao();
        categoryDao = new CategoryDao();
        ilist = FXCollections.observableArrayList();
        clist = FXCollections.observableArrayList();

        ilist.addAll(itemsDao.getData());
        clist.addAll(categoryDao.getData());

        cmb1.setItems(clist);
        tableItems.setItems(ilist);
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colCategory.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getCategoryByCategoryId()));

        btnDelete.setDisable(true);
        btnUpdate.setDisable(true);

        labelMenu.setAccelerator(KeyCombination.keyCombination("Alt+F2"));
        labelSimple.setAccelerator(KeyCombination.keyCombination("Alt+S"));
        labelGroup.setAccelerator(KeyCombination.keyCombination("Alt+G"));
    }

    public void refreshData() {
        ItemsDao dao = new ItemsDao();
        ilist = FXCollections.observableArrayList(dao.getData());
        tableItems.setItems(ilist);
    }

    public void goToCategory(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader;
        loader = new FXMLLoader(HelloApplication.class.getResource("second_category.fxml"));
        Scene scene = new Scene(loader.load(), 600, 400);
        stage.setTitle("Category Management");
        stage.setScene(scene);
        stage.showAndWait();
    }

    public void addItems(ActionEvent actionEvent) {
        String id = txtIdItems.getText();
        String name = txtNameItems.getText();
        String price = txtPrice.getText();
        String description = txtDescription.getText();
        CategoryEntity category = cmb1.getValue();

        if (id != "" && name != "" && price != "" && description != "") {
            ItemsDao dao = new ItemsDao();
            ItemsEntity i = new ItemsEntity();
            i.setName(name);
            i.setPrice(Double.valueOf(price));
            i.setDescription(description);
            i.setCategoryByCategoryId(category);
            dao.addData(i);
            tableItems.setItems(ilist);
            refreshData();

            txtIdItems.clear();
            txtNameItems.clear();
            txtPrice.clear();
            txtDescription.clear();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Please fill in all the field", ButtonType.OK);
            alert.showAndWait();
        }
    }

    public void delItems(ActionEvent actionEvent) {
        ItemsEntity selectedItems;
        selectedItems = tableItems.getSelectionModel().getSelectedItem();
        ItemsDao dao = new ItemsDao();

        int hasil = dao.delData(selectedItems);
        if (hasil > 0) {
            Alert alertbox = new Alert(Alert.AlertType.INFORMATION, "Data berhasil dihapus");
            alertbox.showAndWait();
        }
        refreshData();
        txtIdItems.clear();
        txtNameItems.clear();
        txtPrice.clear();
        txtDescription.clear();
        bnSave.setDisable(false);
        btnDelete.setDisable(true);
        btnUpdate.setDisable(true);
    }

    public void updateItems(ActionEvent actionEvent) {
        if (txtNameItems.getText().trim().isEmpty() || txtPrice.getText().trim().isEmpty() || txtDescription.getText().trim().isEmpty() || cmb1 == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Please fill in all the field", ButtonType.OK);
            alert.showAndWait();
        } else {
            selectedItems.setName(txtNameItems.getText().trim());
            selectedItems.setPrice(Double.parseDouble(txtPrice.getText().trim()));
            selectedItems.setDescription(txtDescription.getText().trim());
            selectedItems.setCategoryByCategoryId(cmb1.getValue());

            if (itemsDao.updateData(selectedItems) == 1) {
                ilist.clear();
                ilist.addAll(itemsDao.getData());

                txtIdItems.clear();
                txtNameItems.clear();
                txtPrice.clear();
                txtDescription.clear();
                bnSave.setDisable(false);
                btnReset.setDisable(false);
                btnDelete.setDisable(true);
                btnUpdate.setDisable(true);
            }
        }
    }

    public void getSelectedItems(MouseEvent mouseEvent) {
        selectedItems = tableItems.getSelectionModel().getSelectedItem();
        if (selectedItems != null) {
            txtIdItems.setText(String.valueOf(selectedItems.getId()));
            txtNameItems.setText(selectedItems.getName());
            txtPrice.setText(String.valueOf(selectedItems.getPrice()));
            txtDescription.setText(selectedItems.getDescription());
            cmb1.setValue(selectedItems.getCategoryByCategoryId());
            bnSave.setDisable(true);
            btnReset.setDisable(false);
            btnUpdate.setDisable(false);
            btnDelete.setDisable(false);
        }
    }

    public void reset(ActionEvent actionEvent) {
        bnSave.setDisable(false);
        btnDelete.setDisable(true);
        btnUpdate.setDisable(true);
        txtIdItems.clear();
        txtNameItems.clear();
        txtPrice.clear();
        txtDescription.clear();
    }

    public void lihatSimpleReport(ActionEvent actionEvent) {
        JasperPrint jp;
        SessionFactory sf = MyConnection.getSessionFactory();
        Session s = sf.openSession();

        Map param = new HashMap();
        try {
            jp = JasperFillManager.fillReport("report/Items.jasper", param, (Connection) s);
            JasperViewer viewer = new JasperViewer(jp, false);
            viewer.setTitle("laporan saya");
            viewer.setVisible(true);
        } catch (JRException e) {
            throw new RuntimeException(e);
        }
    }

    public void lihatGroupReport(ActionEvent actionEvent) {
        JasperPrint jp;
        SessionFactory sf = MyConnection.getSessionFactory();
        Session s = sf.openSession();

        Map param = new HashMap();
        try {
            jp = JasperFillManager.fillReport("report/Group.jasper", param, (Connection) s);
            JasperViewer viewer = new JasperViewer(jp, false);
            viewer.setTitle("laporan saya");
            viewer.setVisible(true);
        } catch (JRException e) {
            throw new RuntimeException(e);
        }
    }
}
