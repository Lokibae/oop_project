package view;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;

public class ButtonRenderer implements TableCellRenderer {
    private JPanel panel;

    public ButtonRenderer() {
        panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    }

    @Override
    public Component getTableCellRendererComponent(
            JTable table, Object value, boolean isSelected,
            boolean hasFocus, int row, int column) {

        panel.removeAll();
        if (value instanceof JPanel) {
            JPanel buttonPanel = (JPanel) value;
            for (Component comp : buttonPanel.getComponents()) {
                panel.add(comp);
            }
        }
        return panel;
    }
}