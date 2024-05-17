package view;

import controller.Reader;
import model.Pair;
import model.Participant;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class MainFrame extends JFrame {
    private final JPanel listPanel;
    private final JScrollPane participantScrollPane;
    private final JList<Participant> participantJList;
    private DefaultListModel<Participant> participantListModel;
    private final JList<Pair> pairJList;
    private final JList<Participant> runnerUpsJList; // Nachrücker
    // todo missing cluster JList
    private final JLabel greetingLabel;

    private final JPanel rightButtonPanel;
    private final JButton readCSVButton;
    private final JButton autoAssignButton;
    private final JButton outputCSVButton;
    private final JButton switchButton;

    private final JPanel infoConsolePanel;
    private final JLabel infoConsoleLabel;

    private boolean isEnglish = true;
    private ResourceBundle messages;
    private String consoleTextToken;

    public MainFrame() {
        setTitle("Language Switcher");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Load default English messages
        messages = ResourceBundle.getBundle("languages.messages", Locale.ENGLISH);

        consoleTextToken = "infoConsoleStartUp";
        String infoConsoleText = messages.getString(consoleTextToken);

        listPanel = new JPanel(new FlowLayout());
        //todo replace placeholder with managed data
        List<Participant> placeHolderParticipantsDataModel = Reader.getParticipants();
        participantListModel = new DefaultListModel<>();
        assert placeHolderParticipantsDataModel != null;
        for (Participant participant : placeHolderParticipantsDataModel) {
            participantListModel.addElement(participant);
        }
        participantJList = new JList<>(participantListModel);
        participantJList.setCellRenderer(new ParticipantListCellRenderer());
        participantScrollPane = new JScrollPane(participantJList);
        listPanel.add(participantScrollPane);
        TitledBorder titledBorder = BorderFactory.createTitledBorder("Participants");
        listPanel.setBorder(titledBorder);
        pairJList = null; //todo pairJList
        runnerUpsJList = null; //todo runnerUpsJList
        greetingLabel = new JLabel(messages.getString("greeting"));
        listPanel.add(greetingLabel);
        add(listPanel, BorderLayout.CENTER);

        rightButtonPanel = new JPanel();
        rightButtonPanel.setLayout(new BoxLayout(rightButtonPanel, BoxLayout.Y_AXIS));
        switchButton = new JButton(messages.getString("switchLangButton"));
        switchButton.addActionListener(e -> toggleLanguage());
        rightButtonPanel.add(switchButton);
        readCSVButton = new JButton(messages.getString("readCSVButton"));
        // todo read csv button
        rightButtonPanel.add(readCSVButton);
        autoAssignButton = new JButton(messages.getString("autoAssignButton"));
        // todo auto assign button
        rightButtonPanel.add(autoAssignButton);
        outputCSVButton = new JButton(messages.getString("outputCSVButton"));
        // todo output csv button
        rightButtonPanel.add(outputCSVButton);
        add(rightButtonPanel, BorderLayout.EAST);

        infoConsolePanel = new JPanel();
        infoConsoleLabel = new JLabel(infoConsoleText);
        infoConsolePanel.add(infoConsoleLabel);
        add(infoConsolePanel, BorderLayout.SOUTH);
    }

    private void toggleLanguage() {
        if (isEnglish) {
            // Load German messages
            messages = ResourceBundle.getBundle("languages.messages", Locale.GERMAN);
        } else {
            // Load English messages
            messages = ResourceBundle.getBundle("languages.messages", Locale.ENGLISH);
        }

        greetingLabel.setText(messages.getString("greeting"));
        switchButton.setText(messages.getString("switchLangButton"));
        readCSVButton.setText(messages.getString("readCSVButton"));
        autoAssignButton.setText(messages.getString("autoAssignButton"));
        outputCSVButton.setText(messages.getString("outputCSVButton"));
        infoConsoleLabel.setText(messages.getString(consoleTextToken));

        isEnglish = !isEnglish;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}
