package view;

import javax.swing.*;
import java.awt.*;

public class SurveyResultView extends JFrame {
    private JTextArea resultArea;
    private JButton exportButton;

    public SurveyResultView() {
        setTitle("Survey Result");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setText("Survey: Customer Satisfaction\n\nQuestion 1: How satisfied are you?\n- Very Satisfied: 30\n- Satisfied: 50\n- Neutral: 15\n- Unsatisfied: 5\n\nQuestion 2: Would you recommend us?\n- Yes: 80\n- No: 20\n");

        JScrollPane scrollPane = new JScrollPane(resultArea);
        exportButton = new JButton("Export to CSV");
        exportButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "Results exported (simulated)."));

        add(scrollPane, BorderLayout.CENTER);
        add(exportButton, BorderLayout.SOUTH);

        setVisible(true);
    }
}