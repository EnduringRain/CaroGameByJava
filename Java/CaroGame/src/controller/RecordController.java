/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import model.Record;
import service.RecordService;
import utils.Constants;

/**
 * FXML Controller class
 *
 * @author Vu
 */
public class RecordController implements Initializable {

    @FXML
    private RadioButton rdoCaNhan;
    @FXML
    private RadioButton rdoAll;
    @FXML
    private TableView<Record> tblKyLuc;
    @FXML
    private TextField txtTimKiem;
    @FXML
    private TextArea taShow;
    @FXML
    private MenuItem menuItemTroChoi;
    @FXML
    private MenuItem menuItemKyLuc;
    @FXML
    private MenuItem menuItemThoat;
    @FXML
    private MenuItem menuLuatChoi;
    @FXML
    private MenuItem menuCachChoi;
    @FXML
    private MenuItem menuDangNhap;

    private RecordService recordService;
    private ObservableList<Record> recordList;
    private FilteredList<Record> filteredRecords;
    private String currentUser;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            // Khởi tạo RecordService
            recordService = new RecordService();

            // Tạo ToggleGroup cho RadioButton
            ToggleGroup toggleGroup = new ToggleGroup();
            rdoCaNhan.setToggleGroup(toggleGroup);
            rdoAll.setToggleGroup(toggleGroup);

            // Khởi tạo danh sách records
            recordList = FXCollections.observableArrayList();

            // Khởi tạo FilteredList ngay sau khi có recordList
            filteredRecords = new FilteredList<>(recordList, p -> true);
            tblKyLuc.setItems(filteredRecords);

            // Thiết lập các cột cho TableView
            setupTableColumns();

            // Lấy thông tin người dùng hiện tại từ LoginController
            currentUser = LoginController.getCurrentUser();

            // Nếu là khách thì hiển thị tất cả, ngược lại hiển thị kỷ lục cá nhân
            if (currentUser == null || currentUser.equalsIgnoreCase("Khách")) {
                rdoAll.setSelected(true);
                loadAllRecords();
            } else {
                rdoCaNhan.setSelected(true);
                loadPersonalRecords();
            }

            // Thiết lập sự kiện khi chọn một dòng trong TableView
            tblKyLuc.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    displayRecordDetails(newSelection);
                }
            });

        } catch (ClassNotFoundException ex) {
            showAlert("Lỗi", "Không thể khởi tạo RecordService: " + ex.getMessage());
        }
    }

    private void setupTableColumns() {
        // Xóa tất cả các cột hiện có
        tblKyLuc.getColumns().clear();

        // Tạo cột STT
        TableColumn<Record, Integer> colSTT = new TableColumn<>("STT");
        colSTT.setCellValueFactory(cellData
                -> new SimpleIntegerProperty(tblKyLuc.getItems().indexOf(cellData.getValue()) + 1).asObject());
        colSTT.setPrefWidth(50);

        // Tạo cột Mã kỷ lục
        TableColumn<Record, String> colMaKyLuc = new TableColumn<>("Mã kỷ lục");
        colMaKyLuc.setCellValueFactory(cellData
                -> new SimpleStringProperty(cellData.getValue().getId()));
        colMaKyLuc.setPrefWidth(100);

        // Tạo cột Thời gian
        TableColumn<Record, Integer> colThoiGian = new TableColumn<>("Thời gian (giây)");
        colThoiGian.setCellValueFactory(cellData
                -> new SimpleIntegerProperty(cellData.getValue().getTime()).asObject());
        colThoiGian.setPrefWidth(120);

        // Tạo cột Ngày tạo
        TableColumn<Record, java.sql.Date> colNgayTao = new TableColumn<>("Ngày tạo");
        colNgayTao.setCellValueFactory(cellData
                -> new SimpleObjectProperty<>(cellData.getValue().getCreatedDate()));
        colNgayTao.setPrefWidth(150);

        // Tạo cột Người chơi
        TableColumn<Record, String> colNguoiChoi = new TableColumn<>("Người chơi");
        colNguoiChoi.setCellValueFactory(cellData
                -> new SimpleStringProperty(cellData.getValue().getAccountId()));
        colNguoiChoi.setPrefWidth(150);

        // Thêm các cột vào TableView
        tblKyLuc.getColumns().addAll(colSTT, colMaKyLuc, colThoiGian, colNgayTao, colNguoiChoi);
    }

    private void loadAllRecords() {
        try {
            List<Record> records = recordService.getAllRecord();
            recordList.clear();
            recordList.addAll(records);
            updateFilterPredicate();
        } catch (SQLException ex) {
            showAlert("Lỗi", "Không thể tải danh sách kỷ lục: " + ex.getMessage());
        }
    }

    private void loadPersonalRecords() {
        try {
            if (currentUser != null && !currentUser.equalsIgnoreCase("Khách")) {
                List<Record> records = recordService.getRecordByUser(currentUser);
                recordList.clear();
                recordList.addAll(records);
                updateFilterPredicate();
            } else {
                loadAllRecords();
            }
        } catch (SQLException ex) {
            showAlert("Lỗi", "Không thể tải danh sách kỷ lục cá nhân: " + ex.getMessage());
        }
    }

    private void displayRecordDetails(Record record) {
        if (record != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("CHI TIẾT KỶ LỤC\n");
            sb.append("Mã kỷ lục: ").append(record.getId()).append("\n");
            sb.append("Thời gian: ").append(record.getTime()).append(" giây\n");
            sb.append("Ngày tạo: ").append(record.getCreatedDate()).append("\n");
            sb.append("Người chơi: ").append(record.getAccountId()).append("\n");

            taShow.setText(sb.toString());
        }
    }

    private void updateFilterPredicate() {
        String searchText = txtTimKiem.getText().toLowerCase();
        filteredRecords.setPredicate(record -> {
            if (searchText == null || searchText.isEmpty()) {
                return true;
            }

            // Tìm kiếm theo tên người chơi
            return record.getAccountId().toLowerCase().contains(searchText);
        });
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void rdoCaNhanAction(ActionEvent event) {
        if (rdoCaNhan.isSelected()) {
            loadPersonalRecords();
        }
    }

    @FXML
    private void rdoAllAction(ActionEvent event) {
        if (rdoAll.isSelected()) {
            loadAllRecords();
        }
    }

    @FXML
    private void txtTimKiemTextchanged(InputMethodEvent event) {
        updateFilterPredicate();
    }

    @FXML
    private void txtTimKiemKeyReleased(KeyEvent event) {
        updateFilterPredicate();
    }

    // Phương thức để thiết lập người dùng hiện tại
    public void setCurrentUser(String username) {
        this.currentUser = username;

        // Nếu là khách thì hiển thị tất cả, ngược lại hiển thị kỷ lục cá nhân
        if (currentUser == null || currentUser.equalsIgnoreCase("Khách")) {
            rdoAll.setSelected(true);
            loadAllRecords();
        } else {
            rdoCaNhan.setSelected(true);
            loadPersonalRecords();
        }
    }

    @FXML
    private void menuItemTroChoiAction(ActionEvent event) {
        try {
            // Chuyển đến màn hình Caro
            Stage stage = (Stage) ((MenuItem) event.getSource()).getParentPopup().getOwnerWindow();
            URL resourceUrl = getClass().getResource(Constants.CARO_FXML);
            FXMLLoader loader = new FXMLLoader(resourceUrl);
            Parent root = loader.load();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.show();
        } catch (IOException ex) {
            showAlert("Lỗi", "Không thể mở màn hình trò chơi: " + ex.getMessage());
        }
    }

    @FXML
    private void menuItemKyLucAction(ActionEvent event) {
        if (rdoCaNhan.isSelected()) {
            loadPersonalRecords();
        } else {
            loadAllRecords();
        }
    }

    @FXML
    private void menuItemThoatAction(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    private void menuLuatChoiAction(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Luật chơi");
        alert.setHeaderText("Luật chơi Caro");
        alert.setContentText("- Người chơi và máy lần lượt đánh X và O vào các ô trên bàn cờ.\n"
                + "- Người chơi nào tạo được 5 ký hiệu liên tiếp theo hàng ngang, dọc hoặc chéo sẽ thắng.\n"
                + "- Trò chơi kết thúc khi một người chơi thắng hoặc bàn cờ đã đầy.");
        alert.showAndWait();
    }

    @FXML
    private void menuCachChoiAction(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Cách chơi");
        alert.setHeaderText("Cách chơi Caro");
        alert.setContentText("1. Chọn kích thước bàn cờ và số ký hiệu liên tiếp để thắng.\n"
                + "2. Chọn người chơi đi trước và ký hiệu (X hoặc O).\n"
                + "3. Chọn độ khó của AI.\n"
                + "4. Nhấn 'Trò chơi mới' để bắt đầu.\n"
                + "5. Nhấp vào các ô trên bàn cờ để đánh.\n"
                + "6. Khi thắng, bạn có thể lưu kỷ lục thời gian chơi.");
        alert.showAndWait();
    }

    @FXML
    private void menuDangNhapAction(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((MenuItem) event.getSource()).getParentPopup().getOwnerWindow();
        URL resourceUrl = getClass().getResource(Constants.LOGIN_FXML);
        FXMLLoader loader = new FXMLLoader(resourceUrl);
        Parent root = loader.load();

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }
}
