package view;

import javax.swing.*;
import java.awt.*;

public class AdminUserManagementView extends JFrame {
    private DefaultListModel<String> userListModel;
    private JList<String> userList;
    private JButton addUserButton, deleteUserButton, assignRoleButton;

    public AdminUserManagementView() {
        setTitle("Admin User Management");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        userListModel = new DefaultListModel<>();
        userList = new JList<>(userListModel);
        JScrollPane scrollPane = new JScrollPane(userList);

        JPanel buttonPanel = new JPanel();
        addUserButton = new JButton("Add User");
        deleteUserButton = new JButton("Delete User");
        assignRoleButton = new JButton("Assign Role");
        buttonPanel.add(addUserButton);
        buttonPanel.add(deleteUserButton);
        buttonPanel.add(assignRoleButton);

        // Dummy data
        userListModel.addElement("admin1 - Super Admin");
        userListModel.addElement("creator2 - Survey Creator");

        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }
}