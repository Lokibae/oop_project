package view;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.*;
import java.util.EventObject;

public class ButtonEditor extends AbstractCellEditor implements TableCellEditor {
    private JPanel panel;

    public ButtonEditor() {
        panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int column) {
        panel.removeAll();
        if (value instanceof JPanel) {
            JPanel buttonPanel = (JPanel) value;
            for (Component comp : buttonPanel.getComponents()) {
                panel.add(comp);
            }
        }
        return panel;
    }

    @Override
    public Object getCellEditorValue() {
        return panel;
    }

    @Override
    public boolean isCellEditable(EventObject e) {
        return true;
    }

    @Override
    public boolean shouldSelectCell(EventObject anEvent) {
        return false;
    }
}