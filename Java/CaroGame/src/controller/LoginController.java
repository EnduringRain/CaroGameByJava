package controller;

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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import service.AccountService;
import utils.Constants;

public class LoginController implements Initializable {

    @FXML
    private TextField txtTK;
    @FXML
    private PasswordField txtMK;
    @FXML
    private Button btnDangNhap;
    @FXML
    private Button btnDangNhapK;
    @FXML
    private Label lbDangKy;
    @FXML
    private MenuItem menuDangKy;
    @FXML
    private MenuItem menuThoat;

    private AccountService tkS;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            tkS = new AccountService();
        } catch (ClassNotFoundException ex) {
            showError("Lỗi khởi tạo", "Không thể khởi tạo TaiKhoanService: " + ex.getMessage());
        }
    }

    @FXML
    private void btnDangNhapClick(ActionEvent event) {
        try {
            String tk = txtTK.getText();
            String mk = txtMK.getText();

            if (tk.isEmpty() || mk.isEmpty()) {
                showError("Lỗi", "Vui lòng điền đầy đủ thông tin");
                return;
            }

            if (tkS.kiemTraTaiKhoan(tk, mk)) {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Thông báo");
                alert.setHeaderText(null);
                alert.setContentText("Đăng nhập thành công");
                alert.showAndWait();

                // Chuyển đến màn hình Caro
                loadCaroScreen(event, tk);
            } else {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Thông báo");
                alert.setHeaderText(null);
                alert.setContentText("Đăng nhập thất bại");
                alert.showAndWait();
            }
        } catch (SQLException | IOException ex) {
            showError("Lỗi", ex.getMessage());
        }
    }

    @FXML
    private void btnDangNhapKAction(ActionEvent event) {
        try {
            // Chuyển đến màn hình Caro với tài khoản "Khách"
            loadCaroScreen(event, "Khách");
        } catch (IOException ex) {
            showError("Lỗi", ex.getMessage());
        }
    }

    @FXML
    private void lbDangKyClick(MouseEvent event) {
        try {
            // Chuyển đến màn hình đăng ký
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            URL resourceUrl = getClass().getResource(Constants.REGISTER_FXML);

            Parent root = FXMLLoader.load(resourceUrl);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.show();
        } catch (IOException ex) {
            showError("Lỗi", ex.getMessage());
        }
    }

    @FXML
    private void menuDangKyAction(ActionEvent event) {
        try {
            // Chuyển đến màn hình đăng ký
            Stage stage = (Stage) ((MenuItem) event.getSource()).getParentPopup().getOwnerWindow();
            URL resourceUrl = getClass().getResource(Constants.REGISTER_FXML);

            Parent root = FXMLLoader.load(resourceUrl);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.show();
        } catch (IOException ex) {
            showError("Lỗi", ex.getMessage());
        }
    }

    @FXML
    private void menuThoatAction(ActionEvent event) {
        System.exit(0);
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void loadCaroScreen(ActionEvent event, String username) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        URL resourceUrl = getClass().getResource(Constants.CARO_FXML);
        FXMLLoader loader = new FXMLLoader(resourceUrl);
        Parent root = loader.load();

        controller.caro.CaroController caroController = loader.getController();
        caroController.setCurrentUser(username);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    void setTaiKhoan(String tk, String mk) {
        txtTK.setText(tk);
        txtMK.setText(mk);
    }
}
