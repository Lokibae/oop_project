package view;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.*;
import java.util.EventObject;

public class ButtonEditor extends AbstractCellEditor implements TableCellEditor {
    private JPanel currentPanel;
    private transient MouseListener[] buttonListeners;

    @Override
    public Component getTableCellEditorComponent(
            JTable table, Object value,
            boolean isSelected, int row, int column) {

        if (value instanceof JPanel) {
            currentPanel = (JPanel) value;

            // Remove old listeners if they exist
            if (buttonListeners != null) {
                for (Component comp : currentPanel.getComponents()) {
                    if (comp instanceof JButton) {
                        for (MouseListener listener : buttonListeners) {
                            comp.removeMouseListener(listener);
                        }
                    }
                }
            }

            // Add new listeners
            buttonListeners = new MouseListener[currentPanel.getComponentCount()];
            int i = 0;
            for (Component comp : currentPanel.getComponents()) {
                if (comp instanceof JButton) {
                    MouseListener listener = new MouseAdapter() {
                        public void mousePressed(MouseEvent e) {
                            stopCellEditing();
                            ((JButton)comp).doClick();
                        }
                    };
                    comp.addMouseListener(listener);
                    buttonListeners[i++] = listener;
                }
            }
        } else {
            currentPanel = new JPanel();
        }
        return currentPanel;
    }

    @Override
    public Object getCellEditorValue() {
        return currentPanel;
    }

    @Override
    public boolean isCellEditable(EventObject e) {
        return true;
    }

    @Override
    public boolean shouldSelectCell(EventObject anEvent) {
        return false;
    }

    @Override
    public boolean stopCellEditing() {
        fireEditingStopped();
        return super.stopCellEditing();
    }
}