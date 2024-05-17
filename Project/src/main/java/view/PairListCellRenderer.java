package view;

import model.Pair;

import javax.swing.*;
import java.awt.*;

class PairListCellRenderer extends DefaultListCellRenderer {
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (value instanceof Pair pair) {
            setText(pair.shortName()); // Set the text to the desired parameter of Participant to show up in list
        }
        return this;
    }
}