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
import java.awt.*;
import java.io.File;
import java.net.URL;
import java.util.*;
import java.util.List;

public class SpinfoodFrame extends JFrame implements PairDisplayCallback {
    private static final Map<String, Locale> LANGUAGE_LOCALE_MAP = new HashMap<>();

    static {
        LANGUAGE_LOCALE_MAP.put("English", Locale.ENGLISH);
        LANGUAGE_LOCALE_MAP.put("German", Locale.GERMAN);
        /*
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
        
         */
    }

    private static final int MAX_CONSOLE_LINES = 8;
    private static ResourceBundle resourceBundle = ResourceBundle.getBundle("languages.messages", Locale.GERMAN);
    private final DefaultListModel<String> successorListModel;
    private JLabel participantsLabel;
    private JLabel pairLabel;
    private JLabel groupLabel;
    private JLabel successorLabel;
    private JLabel consoleLabel;
    private JLabel selectLanguageLabel;
    private JLabel participantInfoLabel;
    private JLabel pairInfoLabel;
    private JLabel groupInfoLabel;
    private JLabel successorInfoLabel;
    private JPanel mainPanel;
    private JLabel participantKeyFiguresLabel;
    private JLabel pairKeyFiguresLabel;
    private JLabel groupKeyFiguresLabel;
    private JLabel successorsKeyFiguresLabel;
    private JScrollPane participantsPane;
    private JScrollPane pairsPane;
    private JScrollPane groupPane;
    private JScrollPane successorPane;
    private JButton autoAssignButton;
    private JButton readCSVButton;
    private JButton outputCSVButton;
    private JButton loadPreviousButton;
    private JTextPane consolePane;
    private JTextPane participantTextPane;
    private JTextPane pairTextPane;
    private JTextPane groupTextPane;
    private JTextPane successorTextPane;
    private JTextPane pairKeyFiguresTextPane;
    private JTextPane groupKeyFiguresTextPane;
    private JTextPane successorsKeyFiguresTextPane;
    private JTextPane participantKeyFiguresTextPane;
    private JList<String> participantJList;
    private JList<String> pairJList;
    private JList<String> groupJList;
    private JList<String> successorsJList;
    private final DefaultListModel<String> participantListModel;
    private final DefaultListModel<String> pairListModel;
    private final DefaultListModel<String> groupListModel;
    private JComboBox<String> comboBoxLang;
    private JLabel logoLabel;


    public SpinfoodFrame() {
        // Initialize components from the .form file
        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();

        // Set Logo
        URL logoURL = getClass().getResource("/images/logo.png");
        if (logoURL != null) {
            ImageIcon originalIcon = new ImageIcon(logoURL);
            Image originalImage = originalIcon.getImage();
            Image resizedImage = originalImage.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            ImageIcon resizedIcon = new ImageIcon(resizedImage);
            logoLabel.setIcon(resizedIcon);
        } else {
            System.err.println("errors, cannot find photo, please check it again");
        }
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
        // Initialize group list and set model
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

        participantJList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                SpinfoodEvent event = SpinfoodEvent.getInstance();
                int selectedIndex = participantJList.getSelectedIndex();
                if (selectedIndex != -1) {
                    participantTextPane.setText(event.participants.get(selectedIndex).toString());
                }
            }
        });

        pairJList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                SpinfoodEvent event = SpinfoodEvent.getInstance();
                int selectedIndex = pairJList.getSelectedIndex();
                if (selectedIndex != -1) {
                    pairTextPane.setText(event.getPairList().get(selectedIndex).toString());
                }
            }
        });

        groupJList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                SpinfoodEvent event = SpinfoodEvent.getInstance();
                int selectedIndex = groupJList.getSelectedIndex();
                if (selectedIndex != -1) {
                    groupTextPane.setText(event.getGroupList().get(selectedIndex).toString());
                }
            }
        });

        successorsJList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                SpinfoodEvent event = SpinfoodEvent.getInstance();
                int selectedIndex = successorsJList.getSelectedIndex();
                if (selectedIndex != -1) {
                    successorTextPane.setText(event.getSuccessors().get(selectedIndex).toString());
                }
            }
        });

        // Initialize the UI with the default language texts
        updateLanguage();

        // Initial display of participants
        displayParticipants();

        // Set JFrame properties
        setTitle(resourceBundle.getString("title"));
        setSize(1200, 800);
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
            participantInfoLabel.setText(resourceBundle.getString("participantInfoLabel"));
            pairInfoLabel.setText(resourceBundle.getString("pairInfoLabel"));
            groupInfoLabel.setText(resourceBundle.getString("groupInfoLabel"));
            successorInfoLabel.setText(resourceBundle.getString("successorInfoLabel"));
            participantKeyFiguresLabel.setText(resourceBundle.getString("participantKeyFiguresLabel"));
            pairKeyFiguresLabel.setText(resourceBundle.getString("pairKeyFiguresLabel"));
            groupKeyFiguresLabel.setText(resourceBundle.getString("groupKeyFiguresLabel"));
            successorsKeyFiguresLabel.setText(resourceBundle.getString("successorsKeyFiguresLabel"));
            setTitle(resourceBundle.getString("title"));
        }
    }

    private void displayParticipants() {
        SpinfoodEvent event = SpinfoodEvent.getInstance();
        if (participantListModel.isEmpty()) {
            // Add all participant-Strings to the list model
            for (Participant participant : event.participants) {
                participantListModel.addElement(participant.getShortRepresentation());
                participantKeyFiguresTextPane.setText(event.getParticipantKeyFigures());
            }
        }
    }

    @Override
    public void displayPairs(List<Pair> pairs) {
        SpinfoodEvent event = SpinfoodEvent.getInstance();
        pairListModel.clear();
        // Add all pair-Strings to the list model
        for (Pair pair : pairs) {
            pairListModel.addElement(pair.shortString());
            pairKeyFiguresTextPane.setText(event.getPairKeyFigures());
        }
    }

    @Override
    public void displayGroups(List<Group> groups) {
        SpinfoodEvent event = SpinfoodEvent.getInstance();
        groupListModel.clear();
        for (Group group : groups) {
            groupListModel.addElement(group.shortString());
            groupKeyFiguresTextPane.setText(event.getGroupKeyFigures());
        }
    }

    @Override
    public void displaySuccessors(List<Participant> successors) {
        SpinfoodEvent event = SpinfoodEvent.getInstance();
        successorListModel.clear();
        for (Participant successor : successors) {
            successorListModel.addElement(successor.name);
            successorsKeyFiguresTextPane.setText(event.getSuccessorsKeyFigures());
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
