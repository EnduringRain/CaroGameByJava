/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controller;

import service.AccountService;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import utils.Constants;

/**
 * FXML Controller class
 *
 * @author Vu
 */
public class RegisterController implements Initializable {

    @FXML
    private TextField txtTK;
    @FXML
    private PasswordField txtMK;
    @FXML
    private PasswordField txtMKR;
    @FXML
    private Button btnDangKy;

    @FXML
    private MenuItem menuDangNhap;

    @FXML
    private MenuItem menuThoat;

    private AccountService tkS;

    @FXML
    private void btnDangKyAction(ActionEvent event) throws IOException, SQLException {
        String tk = txtTK.getText();
        String mk = txtMK.getText();
        String mkR = txtMKR.getText();

        if (tk.isEmpty() || mk.isEmpty() || mkR.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Vui lòng điền đầy đủ thông tin");
            return;
        }

        if (!mk.equalsIgnoreCase(mkR)) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Mật khẩu không khớp");
        } else {
            int n = tkS.taoTaiKhoan(tk, mk);
            if (n > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Thông báo", "Đăng ký thành công");

                FXMLLoader loader = new FXMLLoader(getClass().getResource(Constants.LOGIN_FXML));
                Parent root = loader.load();

                controller.LoginController control = loader.getController();
                control.setTaiKhoan(tk, mk);

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

                stage.setScene(new Scene(root));
                stage.centerOnScreen();
                stage.show();
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            this.tkS = new AccountService();
        } catch (ClassNotFoundException ex) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", ex.getMessage());
        }
    }

    private void showAlert(Alert.AlertType alrt, String title, String content) {
        Alert alert = new Alert(alrt, title, ButtonType.OK);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void menuDangNhapAction(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((MenuItem) event.getSource()).getParentPopup().getOwnerWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource(Constants.LOGIN_FXML));
        Parent root = loader.load();
        stage.setScene(new Scene(root));
        stage.centerOnScreen();
        stage.show();
    }

    @FXML
    private void menuThoatAction(ActionEvent event) throws IOException {
        if (showConfirmationDialog("Xác nhận thoát", "Bạn có chắc muốn thoát không?")) {
            Stage stage = (Stage) ((MenuItem) event.getSource()).getParentPopup().getOwnerWindow();
            stage.close();
        }
    }

    private boolean showConfirmationDialog(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        return alert.showAndWait().filter(response -> response == ButtonType.OK).isPresent();
    }
}
