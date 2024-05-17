package view;

import model.Participant;

import javax.swing.*;
import java.awt.*;

class ParticipantListCellRenderer extends DefaultListCellRenderer {
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (value instanceof Participant) {
            Participant participant = (Participant) value;
            setText(participant.name); // Set the text to the desired parameter of Participant
        }
        return this;
    }
}