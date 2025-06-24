package view;

import dao.UserDAO;
import model.User;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

public class AdminUserManagementView extends JFrame {
    private JTextField searchField;
    private JButton searchButton, addUserButton;
    private JTable userTable;
    private DefaultTableModel tableModel;
    private JLabel currentUserLabel, adminStatusLabel;
    private UserDAO userDAO;

    public AdminUserManagementView() {
        initializeDAO();
        initializeUI();
        loadUsersFromDatabase();
    }

    private void initializeDAO() {
        this.userDAO = new UserDAO();
    }

    private void initializeUI() {
        configureWindow();
        createStatusBar();
        createSearchPanel();
        createButtonPanel();
        createUserTable();
        assembleMainPanel();
    }

    private void configureWindow() {
        setTitle("Survey Management Application - Admin User Management");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
    }

    private void createStatusBar() {
        JPanel statusPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        currentUserLabel = new JLabel(" Current User: Not logged in");
        adminStatusLabel = new JLabel("Admin Status: None");

        Font statusFont = new Font("Segoe UI", Font.PLAIN, 12);
        currentUserLabel.setFont(statusFont);
        adminStatusLabel.setFont(statusFont);

        statusPanel.add(currentUserLabel);
        statusPanel.add(adminStatusLabel);
        add(statusPanel, BorderLayout.SOUTH);
    }

    private void createSearchPanel() {
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        searchField = new JTextField(30);
        searchButton = new JButton("Search");

        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        searchButton.setBackground(new Color(70, 130, 180));
        searchButton.setForeground(Color.WHITE);

        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        add(searchPanel, BorderLayout.NORTH);
    }

    private void createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        addUserButton = new JButton("+ Add New Admin User");

        addUserButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        addUserButton.setBackground(new Color(70, 130, 180));
        addUserButton.setForeground(Color.WHITE);

        buttonPanel.add(addUserButton);
        add(buttonPanel, BorderLayout.PAGE_END);
    }

    private void createUserTable() {
        String[] columnNames = {"Username", "Role", "Actions"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2;
            }

            @Override
            public Class<?> getColumnClass(int column) {
                return column == 2 ? JPanel.class : String.class;
            }
        };

        userTable = new JTable(tableModel);
        userTable.setRowHeight(40);
        userTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        userTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        userTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        TableColumn actionsColumn = userTable.getColumnModel().getColumn(2);
        actionsColumn.setCellRenderer(new ButtonRenderer());
        actionsColumn.setCellEditor(new ButtonEditor());
        actionsColumn.setPreferredWidth(200);
    }

    private void assembleMainPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.add(new JScrollPane(userTable), BorderLayout.CENTER);
        add(mainPanel, BorderLayout.CENTER);
    }

    private void loadUsersFromDatabase() {
        try {
            List<User> users = userDAO.getAllUsers();
            tableModel.setRowCount(0);
            for (User user : users) {
                addUserToTable(user.getUsername(), user.getRole());
            }
        } catch (SQLException ex) {
            showErrorDialog("Error loading users: " + ex.getMessage());
        }
    }

    public JPanel addUserToTable(String username, String role) {
        JButton editButton = new JButton("Edit");
        JButton deleteButton = new JButton("Delete");

        // Style buttons
        editButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        deleteButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        editButton.setBackground(new Color(70, 130, 180));
        deleteButton.setBackground(new Color(220, 53, 69));
        editButton.setForeground(Color.WHITE);
        deleteButton.setForeground(Color.WHITE);
        editButton.setFocusPainted(false);
        deleteButton.setFocusPainted(false);

        // Set action commands
        editButton.setActionCommand("edit:" + username);
        deleteButton.setActionCommand("delete:" + username);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        actionPanel.setOpaque(true);
        actionPanel.add(editButton);
        actionPanel.add(deleteButton);

        tableModel.addRow(new Object[]{username, role, actionPanel});
        return actionPanel;
    }

    public String getSearchText() {
        return searchField.getText().trim();
    }

    public void clearTable() {
        tableModel.setRowCount(0);
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }

    public JTable getUserTable() {
        return userTable;
    }

    public void addAddUserListener(ActionListener listener) {
        addUserButton.addActionListener(listener);
    }

    public void addSearchListener(ActionListener listener) {
        searchButton.addActionListener(listener);
    }

    public void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void showSuccessDialog(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    public void setLoggedInUser(String username, boolean isAdmin) {
        currentUserLabel.setText(" Current User: " + username);
        adminStatusLabel.setText(" Admin Status: " + (isAdmin ? "Admin" : "User"));
    }
}