// === Main.java ===
package view;

// === Main.java ===
import view.LoginView;
import view.RegisterView;
import controller.AuthController;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginView loginView = new LoginView();
            RegisterView registerView = new RegisterView(); // ✅ Make sure this is created


            new AuthController(loginView, registerView);
            loginView.setVisible(true);
        });
    }
}
