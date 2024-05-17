package view;

import controller.Reader;
import model.Pair;
import model.Participant;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Main GUI class of the project. Opens a window with all important GUI elements.
 */
public class MainFrame extends JFrame {
    private final ParticipantPanel participantPanel;
    private final PairPanel pairPanel;
    private final ControlPanel controlPanel;
    private final InfoConsolePanel infoConsolePanel;

    private boolean isEnglish = true;
    private ResourceBundle messages;
    private String consoleTextToken;

    /**
     * MainFrame constructor to initialize the GUI of the program.
     */
    public MainFrame() {
        setTitle("Language Switcher");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        messages = ResourceBundle.getBundle("languages.messages", Locale.ENGLISH);
        consoleTextToken = "infoConsoleStartUp";

        List<Participant> placeHolderParticipantsDataModel = Reader.getParticipants();
        DefaultListModel<Participant> participantListModel = new DefaultListModel<>();
        for (Participant participant : placeHolderParticipantsDataModel) {
            participantListModel.addElement(participant);
        }
        participantPanel = new ParticipantPanel(participantListModel, messages);

        List<Pair> placeHolderPairDataModel = getPlaceholderPairModelList(placeHolderParticipantsDataModel);
        DefaultListModel<Pair> pairListModel = new DefaultListModel<>();
        for (Pair pair : placeHolderPairDataModel) {
            pairListModel.addElement(pair);
        }
        pairPanel = new PairPanel(pairListModel, messages);

        controlPanel = new ControlPanel(this, messages);
        infoConsolePanel = new InfoConsolePanel(messages, messages.getString(consoleTextToken));

        addComponents();
    }

    /**
     * !!!!!!!!!!!! THIS IS JUST A PLACEHOLDER TO SHOW SOME PAIRS IN THE GUI !!!!!!!!!!!!!!!!!!!
     * todo: replace with actual data model
     */
    private static List<Pair> getPlaceholderPairModelList(List<Participant> placeHolderParticipantsDataModel) {
        List<Pair> placeHolderPairDataModel = new ArrayList<>();
        Participant p1 = placeHolderParticipantsDataModel.get(0);
        Participant p2 = placeHolderParticipantsDataModel.get(1);
        Participant p3 = placeHolderParticipantsDataModel.get(2);
        Participant p4 = placeHolderParticipantsDataModel.get(3);
        Pair pair1 = new Pair(p1, p2, true);
        Pair pair2 = new Pair(p3, p4, true);
        placeHolderPairDataModel.add(pair1);
        placeHolderPairDataModel.add(pair2);
        return placeHolderPairDataModel;
    }

    /**
     * add listPanels to the center of the main frame
     */
    private void addComponents() {
        JPanel centerPanel = new JPanel(new GridLayout(1, 2));
        centerPanel.add(participantPanel);
        centerPanel.add(pairPanel);

        add(centerPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.EAST);
        add(infoConsolePanel, BorderLayout.SOUTH);
    }

    /**
     * Toggles the language between english and german for most GUI elements.
     */
    public void toggleLanguage() {
        if (isEnglish) {
            messages = ResourceBundle.getBundle("languages.messages", Locale.GERMAN);
        } else {
            messages = ResourceBundle.getBundle("languages.messages", Locale.ENGLISH);
        }

        participantPanel.setBorder(BorderFactory.createTitledBorder(messages.getString("participants")));
        pairPanel.setBorder(BorderFactory.createTitledBorder(messages.getString("pairs")));
        controlPanel.updateLanguage(messages);
        infoConsolePanel.updateLanguage(messages, messages.getString(consoleTextToken));

        isEnglish = !isEnglish;
    }

    /**
     * Test-main() method for GUI testing.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}
