package view;

import javax.swing.*;
import java.awt.*;

public class SurveyManagementView extends JFrame {
    private DefaultListModel<String> surveyListModel;
    private JList<String> surveyList;
    private JButton viewButton, editButton, deleteButton;

    public SurveyManagementView() {
        setTitle("Survey Management");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        surveyListModel = new DefaultListModel<>();
        surveyList = new JList<>(surveyListModel);
        JScrollPane scrollPane = new JScrollPane(surveyList);

        JPanel buttonPanel = new JPanel();
        viewButton = new JButton("View");
        editButton = new JButton("Edit");
        deleteButton = new JButton("Delete");
        buttonPanel.add(viewButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        // Dummy data
        surveyListModel.addElement("Survey 1: Customer Feedback");
        surveyListModel.addElement("Survey 2: Product Review");

        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }
}