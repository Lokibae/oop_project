package view;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;

public class ButtonRenderer implements TableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(
            JTable table, Object value, boolean isSelected,
            boolean hasFocus, int row, int column) {

        // Return the panel that was stored in the table model
        return (value instanceof JPanel) ? (JPanel) value : new JPanel();
    }
}