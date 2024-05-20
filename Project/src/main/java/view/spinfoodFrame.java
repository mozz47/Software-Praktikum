package view;

import controller.Reader;
import model.Group;
import model.Pair;
import model.Participant;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class spinfoodFrame extends JFrame {
    private static final int MAX_CONSOLE_LINES = 4;
    private static ResourceBundle resourceBundle;
    private JLabel participantsLabel;
    private JButton autoAssignButton;
    private JButton readCSVButton;
    private JButton outputCSVButton;
    private JTextPane consolePane;
    private JLabel consoleLabel;
    private JLabel groupLabel;
    private JComboBox<String> comboBoxLang;
    private JLabel pairLabel;
    private JPanel mainPanel;
    private JScrollPane groupPane;
    private JScrollPane participantsPane;
    private JScrollPane pairsPane;
    private JList<Pair> pairJList;
    private JList<Participant> participantJList;
    private JList<Group> groupJList;
    private DefaultListModel<Participant> participantListModel;


    public spinfoodFrame() {
        // Initialize components from the .form file
        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();

        // Initialize participants list model
        participantListModel = new DefaultListModel<>();
        // Initialize participants list and set the model
        participantJList.setModel(participantListModel);
        participantsPane.setViewportView(participantJList);

        // Add languages to comboBoxLang for language selection
        comboBoxLang.addItem("English");
        comboBoxLang.addItem("Deutsch");

        // Add an action listener to the comboBoxLang to change the language
        comboBoxLang.addActionListener(e -> updateLanguage());

        readCSVButton.addActionListener(e -> {
            // Get List from Reader-Controller
            List<Participant> participants = Reader.getParticipants();

            if (participants != null) {
                displayParticipants(participants);
                printToConsole(resourceBundle.getString("infoConsoleFileRead"));
            } else {
                printToConsole(resourceBundle.getString("errorFileRead"));
            }
        });

        // Initialize the UI with the default language texts
        updateLanguage();

        // Set JFrame properties
        setTitle(resourceBundle.getString("title"));
        setSize(800, 600);
        setLocationRelativeTo(null);
        setVisible(true);

        // Initial console message
        printToConsole(resourceBundle.getString("infoConsoleStartUp"));

        // Add Action Listener for the autoAssignButton, use Algorithm
        autoAssignButton.addActionListener(e -> {
            //TODO
        });

        // Add Action Listener for the outputCSVButton, choose DIR to save CSV
        outputCSVButton.addActionListener(e -> {
            //TODO
        });
    }

    private void updateLanguage() {
        String selectedLanguage = (String) comboBoxLang.getSelectedItem();
        if (selectedLanguage != null) {
            switch (selectedLanguage) {
                case "English":
                    Locale.setDefault(Locale.ENGLISH);
                    break;
                case "Deutsch":
                    Locale.setDefault(Locale.GERMAN);
                    break;
            }
            resourceBundle = ResourceBundle.getBundle("languages.messages", Locale.getDefault());

            // Now update texts with correct language
            participantsLabel.setText(resourceBundle.getString("participants"));
            pairLabel.setText(resourceBundle.getString("pairs"));
            groupLabel.setText(resourceBundle.getString("groups"));
            autoAssignButton.setText(resourceBundle.getString("autoAssignButton"));
            readCSVButton.setText(resourceBundle.getString("readCSVButton"));
            outputCSVButton.setText(resourceBundle.getString("outputCSVButton"));
            consoleLabel.setText(resourceBundle.getString("console"));
            setTitle(resourceBundle.getString("title"));
        }
    }

    private void displayParticipants(List<Participant> participants) {
        // Clear the existing model
        participantListModel.clear();
        // Add all participants to the list model
        for (Participant participant : participants) {
            participantListModel.addElement(participant);
        }
    }

    private void printToConsole(String message) {
        StyledDocument doc = consolePane.getStyledDocument();
        try {
            // Check the number of lines and remove the oldest if necessary
            if (doc.getDefaultRootElement().getElementCount() > MAX_CONSOLE_LINES) {
                int end = doc.getDefaultRootElement().getElement(0).getEndOffset();
                doc.remove(0, end);
            }

            // Append the new message with a newline
            doc.insertString(doc.getLength(), message + "\n", null);

            // Scroll to the end of the document
            consolePane.setCaretPosition(doc.getLength());

        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

}
