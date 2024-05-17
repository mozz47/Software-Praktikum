package view;

import model.Participant;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.ResourceBundle;

/**
 * Class for managing the participant list in the GUI.
 */
public class ParticipantPanel extends JPanel {
    private final JList<Participant> participantJList;
    private final DefaultListModel<Participant> participantListModel;

    /**
     * Constructor. Initializes the JScrollPane with its contents.
     * @param participantListModel list model
     * @param messages language bundle
     */
    public ParticipantPanel(DefaultListModel<Participant> participantListModel, ResourceBundle messages) {
        setLayout(new BorderLayout());

        this.participantListModel = participantListModel;
        participantJList = new JList<>(participantListModel);
        participantJList.setCellRenderer(new ParticipantListCellRenderer());
        JScrollPane participantScrollPane = new JScrollPane(participantJList);
        add(participantScrollPane, BorderLayout.CENTER);

        TitledBorder titledBorder = BorderFactory.createTitledBorder(messages.getString("participants"));
        setBorder(titledBorder);
    }
}

