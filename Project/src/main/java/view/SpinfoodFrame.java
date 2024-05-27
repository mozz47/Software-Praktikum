package view;

import controller.Reader;
import controller.Saver;
import model.Group;
import model.Pair;
import model.Participant;
import model.SpinfoodEvent;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;
import java.util.*;
import java.util.List;

public class SpinfoodFrame extends JFrame implements PairDisplayCallback {
    private static final Map<String, Locale> LANGUAGE_LOCALE_MAP = new HashMap<>();

    static {
        LANGUAGE_LOCALE_MAP.put("English", Locale.ENGLISH);
        LANGUAGE_LOCALE_MAP.put("German", Locale.GERMAN);
        LANGUAGE_LOCALE_MAP.put("Vietnamese", new Locale.Builder().setLanguage("vi").build());
        LANGUAGE_LOCALE_MAP.put("French", Locale.FRENCH);
        LANGUAGE_LOCALE_MAP.put("Spanish", new Locale.Builder().setLanguage("es").build());
        LANGUAGE_LOCALE_MAP.put("Danish", new Locale.Builder().setLanguage("da").build());
        LANGUAGE_LOCALE_MAP.put("Polish", new Locale.Builder().setLanguage("pl").build());
        LANGUAGE_LOCALE_MAP.put("Mandarin", new Locale.Builder().setLanguage("zh").setRegion("CN").build());
        LANGUAGE_LOCALE_MAP.put("Japanese", Locale.JAPAN);
        LANGUAGE_LOCALE_MAP.put("Czech", new Locale.Builder().setLanguage("cs").build());
        LANGUAGE_LOCALE_MAP.put("Hungarian", new Locale.Builder().setLanguage("hu").build());
        LANGUAGE_LOCALE_MAP.put("Italian", Locale.ITALIAN);
    }

    private static final int MAX_CONSOLE_LINES = 8;
    private static ResourceBundle resourceBundle = ResourceBundle.getBundle("languages.messages", Locale.GERMAN);
    private final DefaultListModel<String> successorListModel;
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
    private JList<String> groupJList;
    private JLabel selectLanguageLabel;
    private JLabel successorLabel;
    private JScrollPane successorPane;
    private JList<String> successorsJList;
    private JButton loadPreviousButton;
    private final DefaultListModel<String> participantListModel;
    private final DefaultListModel<String> pairListModel;
    private final DefaultListModel<String> groupListModel;


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

        // Initialize group list model
        groupListModel = new DefaultListModel<>();
        // Initializ group list and set model
        groupJList.setModel(groupListModel);
        groupPane.setViewportView(groupJList);

        // Initialize successors list model
        successorListModel = new DefaultListModel<>();
        successorsJList.setModel(successorListModel);
        successorPane.setViewportView(successorsJList);

        // Add languages to comboBoxLang for language selection
        for (String language : LANGUAGE_LOCALE_MAP.keySet()) {
            comboBoxLang.addItem(language);
        }

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

        outputCSVButton.addActionListener(e -> {
            Saver.save();
            printToConsole(resourceBundle.getString("savedConsoleText"));
        });

        loadPreviousButton.addActionListener(e -> {
            SpinfoodEvent event = SpinfoodEvent.getInstance();
            event.restoreOldEvent();
            printToConsole(resourceBundle.getString("backedUpConsoleText"));
            displayGroups(event.getGroupList());
            displayPairs(event.getPairList());
            displayParticipants();
            printToConsole(resourceBundle.getString("restoredConsoleText"));
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
        autoAssignButton.addActionListener(e -> new CriteriaRankingFrame(resourceBundle, SpinfoodFrame.this));

        // Add Action Listener for the outputCSVButton, choose DIR to save CSV
        outputCSVButton.addActionListener(e -> {
            //TODO
        });
    }

    private void updateLanguage() {
        String selectedLanguage = (String) comboBoxLang.getSelectedItem();
        if (selectedLanguage != null) {
            Locale.setDefault(LANGUAGE_LOCALE_MAP.get(selectedLanguage));

            resourceBundle = ResourceBundle.getBundle("languages.messages", Locale.getDefault());
            participantsLabel.setText(resourceBundle.getString("participants"));
            pairLabel.setText(resourceBundle.getString("pairs"));
            groupLabel.setText(resourceBundle.getString("groups"));
            autoAssignButton.setText(resourceBundle.getString("autoAssignButton"));
            readCSVButton.setText(resourceBundle.getString("readCSVButton"));
            outputCSVButton.setText(resourceBundle.getString("outputCSVButton"));
            consoleLabel.setText(resourceBundle.getString("console"));
            selectLanguageLabel.setText(resourceBundle.getString("selectLanguageLabel"));
            successorLabel.setText(resourceBundle.getString("successorLabel"));
            loadPreviousButton.setText(resourceBundle.getString("loadPreviousButton"));
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

    @Override
    public void displayGroups(List<Group> groups) {
        groupListModel.clear();
        for (Group group : groups) {
            groupListModel.addElement(group.shortString());
        }
    }

    @Override
    public void displaySuccessors(List<Participant> successors) {
        successorListModel.clear();
        for (Participant successor : successors) {
            successorListModel.addElement(successor.toString());
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
