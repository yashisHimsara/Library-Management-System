package lk.ijse.Library_management_system.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.Library_management_system.bo.BOFactory;
import lk.ijse.Library_management_system.bo.custom.BranchBO;
import lk.ijse.Library_management_system.bo.custom.impl.BranchBOImpl;
import lk.ijse.Library_management_system.controller.util.ValidationController;
import lk.ijse.Library_management_system.dto.BranchDTO;
import lk.ijse.Library_management_system.entity.Branch;
import lk.ijse.Library_management_system.tdm.BranchTM;

import java.time.LocalDate;
import java.util.List;

public class AdminBranchFromController {

    public TextField txtSet;
    public TextField textId1;
    @FXML
    private TextField textID;

    @FXML
    private TextField textId;

    @FXML
    private TextField textAddress;

    @FXML
    private DatePicker OpenedDayPiker;

    @FXML
    private TableView<BranchTM> tblView;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnAddNew;

    @FXML
    private TableColumn<?, ?> columnId;

    @FXML
    private TableColumn<?, ?> columnAddress;
    @FXML
    private Button btnPluse;

    @FXML
    private TableColumn<BranchTM, LocalDate> columnOpenDate;
    BranchBO branchBO = (BranchBO) BOFactory.getBoFactory().getBO(BOFactory.BOType.BRANCH);
    public void initialize() {

        setvaluesFactory();
        loadAllBranch();
        resetAll();
        tblView.getSelectionModel().selectedItemProperty().addListener((observable,oldValue,newValue) ->{
            if (newValue != null){
                btnAddNew.setOpacity(0);
                btnAddNew.setDisable(true);
                // textID.setOpacity(100);
                btnDelete.setDisable(false);
                btnSave.setDisable(false);
                btnSave.setText("Update");
                textAddress.setDisable(false);
                OpenedDayPiker.setDisable(false);
                btnPluse.setDisable(false);
                btnPluse.setOpacity(100);

                textID.setText(String.valueOf(newValue.getId()));
                textAddress.setText(newValue.getAddress());
                OpenedDayPiker.setValue(newValue.getOpenedDate());

            }else {
                btnAddNew.setOpacity(100);
                btnAddNew.setDisable(false);
                btnSave.setText("Save");
                resetAll();
            }
        } );
    }
    private void setvaluesFactory() {
        columnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        columnOpenDate.setCellValueFactory(new PropertyValueFactory<>("openedDate"));
    }
    public void resetAll(){
        //textID.setOpacity(0);3
        textAddress.setDisable(true);
        OpenedDayPiker.setDisable(true);
        btnSave.setDisable(true);
        btnDelete.setDisable(true);
        textAddress.clear();
        OpenedDayPiker.setValue(null);
        textID.clear();
        btnPluse.setDisable(true);
        btnPluse.setOpacity(0);
        tblView.getSelectionModel().clearSelection();

    }
    @FXML
    void btnPlusOnAction(ActionEvent event) {
        btnAddNew.setOpacity(100);
        btnAddNew.setDisable(false);
        resetAll();
    }
    @FXML
    void btnAddNewOnAction(ActionEvent event) {
        textAddress.setDisable(false);
        OpenedDayPiker.setDisable(false);
        btnSave.setDisable(false);
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        boolean isDelete = branchBO.deleteBranch(Long.parseLong(textID.getText()));
        if (isDelete){
            loadAllBranch();
        }
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        LocalDate opDate = OpenedDayPiker.getValue();
        String address = textAddress.getText();
        if (textAddress.getText().isEmpty() || textID.getText().isEmpty()) {
            boolean validate = ValidationController.address(address);
            boolean validate1 = ValidationController.id(textID.getText());

            if (validate || validate1) {
//        save branch
                if ("Save".equals(btnSave.getText())) {
                    boolean isSaved = branchBO.saveBranch(new BranchDTO(address, opDate));
                    if (isSaved) {
                        new Alert(Alert.AlertType.INFORMATION, "SAVE SUCCESS !!!").show();
                        textAddress.clear();
                        OpenedDayPiker.setValue(null);
                        loadAllBranch();
                    }
                }
//        update branch
                else {
                    long id = Long.parseLong(textID.getText());
                    boolean isUpdate = branchBO.updateBranch(new BranchDTO(id, address, opDate));
                    if (isUpdate) {
                        new Alert(Alert.AlertType.INFORMATION, "UPDATE SUCCESS !!!").show();
                        textAddress.clear();
                        OpenedDayPiker.setValue(null);
                        loadAllBranch();
                    }
                }
            }
        }
    }
    public void loadAllBranch(){
        tblView.getItems().clear();
        List<BranchDTO> branches =branchBO.getAllBranch();
        for (BranchDTO branch : branches) {
            tblView.getItems().add(new BranchTM(branch.getId(),branch.getAddress(),branch.getOpenedDate()));
        }
    }

}
