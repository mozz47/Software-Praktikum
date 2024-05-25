package view;

import controller.Reader;
import model.Group;
import model.Pair;
import model.Participant;
import model.SpinfoodEvent;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class SpinfoodFrame extends JFrame implements PairDisplayCallback {
    private static final int MAX_CONSOLE_LINES = 8;
    private static ResourceBundle resourceBundle = ResourceBundle.getBundle("languages.messages", Locale.GERMAN);
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
    private JList<String> pairJList;
    private JList<String> participantJList;
    private JList<Group> groupJList;
    private DefaultListModel<String> participantListModel;
    private DefaultListModel<String> pairListModel;


    public SpinfoodFrame() {
        // Initialize components from the .form file
        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();

        // Initialize participants list model
        participantListModel = new DefaultListModel<>();
        // Initialize participants list and set the model
        participantJList.setModel(participantListModel);
        participantsPane.setViewportView(participantJList);

        // Initialize pair list model
        pairListModel = new DefaultListModel<>();
        // Initialize pair list and set model
        pairJList.setModel(pairListModel);
        pairsPane.setViewportView(pairJList);


        // Add languages to comboBoxLang for language selection
        comboBoxLang.addItem("Deutsch");
        comboBoxLang.addItem("English");

        // Add an action listener to the comboBoxLang to change the language
        comboBoxLang.addActionListener(e -> updateLanguage());

        readCSVButton.addActionListener(e -> {
            SpinfoodEvent event = SpinfoodEvent.getInstance();
            event.participants = Reader.getParticipants();
            if (event.participants != null) {
                displayParticipants();

                printToConsole(resourceBundle.getString("infoConsoleFileRead"));

            } else {
                printToConsole(resourceBundle.getString("errorFileRead"));
            }
        });

        // Initialize the UI with the default language texts
        updateLanguage();

        // Initial display of participants
        displayParticipants();

        // Set JFrame properties
        setTitle(resourceBundle.getString("title"));
        setSize(800, 600);
        setLocationRelativeTo(null);
        setVisible(true);

        // Initial console message
        printToConsole(resourceBundle.getString("infoConsoleStartUp"));

        // Add Action Listener for the autoAssignButton, use Algorithm
        autoAssignButton.addActionListener(e -> SwingUtilities.invokeLater(() -> new CriteriaRankingFrame(resourceBundle, SpinfoodFrame.this)));

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

    private void displayParticipants() {
        SpinfoodEvent event = SpinfoodEvent.getInstance();
        if (participantListModel.isEmpty()) {
            // Add all participant-Strings to the list model
            for (Participant participant : event.participants) {
                participantListModel.addElement(getShortRepresentation(participant));
            }
        }
    }

    @Override
    public void displayPairs(List<Pair> pairs) {
        pairListModel.clear();
        // Add all pair-Strings to the list model
        for (Pair pair : pairs) {
            pairListModel.addElement(pair.shortString());
        }
    }

    //TODO move to Participant.java
    private String getShortRepresentation(Participant participant) {
        if (participant.partner == null) {
            return participant.name + "(" +
                    "foodPreference=" + participant.foodPreference +
                    ", age=" + participant.age +
                    ", sex=" + participant.sex +
                    ", hasKitchen=" + participant.hasKitchen +
                    ", mightHaveKitchen=" + participant.mightHaveKitchen +
                    ", kitchen=" + participant.kitchen +
                    ", partner=null" +
                    ", story=" + participant.story +
                    ')';
        } else {
            return participant.name + "(" +
                    "foodPreference=" + participant.foodPreference +
                    ", age=" + participant.age +
                    ", sex=" + participant.sex +
                    ", hasKitchen=" + participant.hasKitchen +
                    ", mightHaveKitchen=" + participant.mightHaveKitchen +
                    ", kitchen=" + participant.kitchen +
                    ", partner=" + participant.name + //or else stackoverflow-error because of recursive call to each other
                    ", story=" + participant.story +
                    ')';
        }
    }

    @Override
    public void printToConsole(String message) {
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
