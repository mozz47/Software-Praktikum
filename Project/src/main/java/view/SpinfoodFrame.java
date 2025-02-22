package view;

import controller.*;
import model.*;
import model.Pair;
import model.SpinfoodEvent;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;


import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;
import java.net.URL;
import java.util.*;

/**
 * Represents the main GUI window of the Spinfood application.
 */
public class SpinfoodFrame extends JFrame implements DisplayCallback {
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
    private JLabel pairKeyFiguresLabel;
    private JLabel groupKeyFiguresLabel;
    private JLabel successorsKeyFiguresLabel;
    private JScrollPane participantsPane;
    private JScrollPane pairsPane;
    private JScrollPane groupPane;
    private JScrollPane successorPane;
    private JButton pairBuildingButton;
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
    private JList<String> participantJList;
    private JList<String> pairJList;
    private JList<String> groupJList;
    private JList<String> successorsJList;
    private final DefaultListModel<String> participantListModel;
    private final DefaultListModel<String> pairListModel;
    private final DefaultListModel<String> groupListModel;
    private JComboBox<String> comboBoxLang;
    private JLabel logoLabel;
    private JButton groupBuildingButton;
    private JButton changeCriteriaOrderButton;
    private JButton showPairMapButton;
    private JButton openNumberSpinnerButton;
    private JButton cancellationButton;
    private JButton compareGroupsButton;
    private JButton comparePairsButton;
    private JLabel participantAmountLabel;
    private JButton chooseSuccessorButton;
    private JButton swapSelectedPairButton;
    private JButton redoSwapButton;
    private JButton undoSwapButton;
    private JPanel swapCandidatesPanel;
    private JButton clearFirstCandidateButton;
    private JButton clearSecondCandidateButton;
    private JTextArea candidate1;
    private JTextArea candidate2;
    private JLabel swapCandidatesLabel;
    private JButton swapSelectedParticipantButton;


    /**
     * Creates a SpinfoodFrame object, which represents the main GUI window of the Spinfood application.
     * The constructor initializes all the necessary components of the GUI and sets their initial properties.
     * It also sets up event listeners for user interactions with the GUI elements.
     * <p>
     * This method does not return any value and has no input parameters.
     *
     * @see JFrame
     * @see DisplayCallback
     */
    public SpinfoodFrame() {
        // Initialize components from the .form file
        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();

        // Set Logo
        URL logoURL = getClass().getResource("/images/logo_transparent.png");
        if (logoURL != null) {
            ImageIcon originalIcon = new ImageIcon(logoURL);
            Image originalImage = originalIcon.getImage();
            Image resizedImage = originalImage.getScaledInstance(200, 200, Image.SCALE_SMOOTH);
            ImageIcon resizedIcon = new ImageIcon(resizedImage);
            logoLabel.setIcon(resizedIcon);
        } else {
            System.err.println("errors, cannot find photo, please check it again.");
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

        //set enabled of buttons initially off
        pairBuildingButton.setEnabled(false);
        groupBuildingButton.setEnabled(false);
        outputCSVButton.setEnabled(false);
        loadPreviousButton.setEnabled(false);
        showPairMapButton.setEnabled(false);
        cancellationButton.setEnabled(false);
        comparePairsButton.setEnabled(false);
        compareGroupsButton.setEnabled(false);

        // Add Action Listener for the autoAssignButton, use Algorithm
        changeCriteriaOrderButton.addActionListener(e -> {
            SwingUtilities.invokeLater(() -> new CriteriaRankingFrame(resourceBundle, this));
            printToConsole(resourceBundle.getString("criteriaRankingFrameOrder"));
            groupBuildingButton.setEnabled(false);
            outputCSVButton.setEnabled(false);
            showPairMapButton.setEnabled(false);
        });

        showPairMapButton.addActionListener(e -> {
            SwingUtilities.invokeLater(() -> {
                SpinfoodEvent event = SpinfoodEvent.getInstance();
                List<Pair> pairs = event.getPairList();
                PairMap pairMap = new PairMap("Pair Map", pairs);

                pairMap.pack();
                pairMap.setLocationRelativeTo(null);
                pairMap.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                pairMap.setVisible(true);
            });
        });

        pairBuildingButton.addActionListener(e -> {
            SpinfoodEvent event = SpinfoodEvent.getInstance();

            // participant check
            if (event.participants == null) {
                printToConsole(resourceBundle.getString("noParticipantsError"));
                return;
            }

            // criteria check
            if (event.criteria == null) {
                printToConsole(resourceBundle.getString("noCriteriaError"));
                return;
            }

            // start pair building algorithm
            PairList pairList = PairListBuilder.getPairList(event.criteria);
            event.updatePairList(pairList.getPairList());
            event.updateSuccessors(pairList.getSuccessorList());
            printToConsole(resourceBundle.getString("createdPairsWithAlgorithm"));

            displayPairs(event.getPairList());
            displaySuccessors(event.getSuccessors());

            groupBuildingButton.setEnabled(true);
            pairBuildingButton.setEnabled(false);
            showPairMapButton.setEnabled(true);
            if (event.hasOldPairData()) {
                comparePairsButton.setEnabled(true);
            }
        });

        openNumberSpinnerButton.addActionListener(e -> {
            SwingUtilities.invokeLater(() -> new NumberSpinnerFrame(resourceBundle));
        });

        cancellationButton.addActionListener(e -> {
            SpinfoodEvent event = SpinfoodEvent.getInstance();
            CancellationHandler.handleAllCancellations(this);
        });

        groupBuildingButton.addActionListener(e -> {
            SpinfoodEvent event = SpinfoodEvent.getInstance();

            // pair check
            if (SpinfoodEvent.getInstance().getPairList() == null) {
                printToConsole(resourceBundle.getString("noPairsError"));
                return;
            }

            // start group building algorithm
            GroupListBuilder glb = new GroupListBuilder();
            glb.buildGroupList(event.criteria);
            printToConsole(resourceBundle.getString("createdGroupsWithAlgorithm"));

            displayGroups(event.getGroupList());
            displaySuccessors(event.getSuccessors());

            outputCSVButton.setEnabled(true);
            groupBuildingButton.setEnabled(false);
            pairBuildingButton.setEnabled(false);
            cancellationButton.setEnabled(true);

            if (event.hasOldData()) {
                loadPreviousButton.setEnabled(true);
                compareGroupsButton.setEnabled(true);
            }
        });

        // Add an action listener to the comboBoxLang to change the language
        comboBoxLang.addActionListener(e -> updateLanguage());

        readCSVButton.addActionListener(e -> {
            SpinfoodEvent event = SpinfoodEvent.getInstance();
            event.participants = Reader.getParticipants();
            if (event.participants == null) {
                JOptionPane.showMessageDialog(null, resourceBundle.getString("errorFileRead"), resourceBundle.getString("cancellation"), JOptionPane.ERROR_MESSAGE);
                printToConsole(resourceBundle.getString("errorFileRead"));
                return;

            }
            event.partyLocation = Reader.getPartyLocation();
            if (event.partyLocation == null) {
                JOptionPane.showMessageDialog(null, resourceBundle.getString("errorFileRead"), resourceBundle.getString("cancellation"), JOptionPane.ERROR_MESSAGE);
                return;
            }

            displayParticipants();
            participantAmountLabel.setText(String.valueOf(event.participants.size()));

            printToConsole(resourceBundle.getString("infoConsoleFileRead"));

        });

        outputCSVButton.addActionListener(e -> {
            SpinfoodEvent event = SpinfoodEvent.getInstance();
            // group check
            if (event.getGroupList() == null) {
                printToConsole(resourceBundle.getString("noGroupsError"));
                return;
            }

            boolean success = Saver.save();
            if (success) {
                printToConsole(resourceBundle.getString("savedConsoleText"));
            }
        });

        loadPreviousButton.addActionListener(e -> {
            SpinfoodEvent event = SpinfoodEvent.getInstance();

            // old groups, pairs and successors check
            if (!event.hasOldData()) {
                printToConsole(resourceBundle.getString("noOldDataError"));
                return;
            }

            event.restoreOldEvent();
            printToConsole(resourceBundle.getString("backedUpConsoleText"));
            displayGroups(event.getGroupList());
            displayPairs(event.getPairList());
            displayParticipants();
            displaySuccessors(event.getSuccessors());
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

        comparePairsButton.addActionListener(e -> {
            final SpinfoodFrame frame = this;
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    SpinfoodEvent event = SpinfoodEvent.getInstance();
                    if (event.hasOldData()) {
                        new ComparePairFrame(event.getPairList(), event.getOldPairList(), frame);
                    } else throw new RuntimeException("no old pairs to show in compareFrame");
                }
            });
        });

        compareGroupsButton.addActionListener(e -> {
            final SpinfoodFrame frame = this;
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    SpinfoodEvent event = SpinfoodEvent.getInstance();
                    if (event.hasOldData()) {
                        new CompareGroupFrame(event.getGroupList(), event.getGroupListOld(), frame);
                    } else throw new RuntimeException("no old groups to show in compareFrame");
                }
            });
        });

        chooseSuccessorButton.addActionListener(e -> {
            SpinfoodEvent event = SpinfoodEvent.getInstance();
            CandidateSwapper.chooseSuccessor(event.getSuccessors().get(successorsJList.getSelectedIndex()));

            // print to GUI
            if (event.getSwapCandidate1() != null) {
                candidate1.setText(event.getSwapCandidate1().getShortRepresentation());
            }
            if (event.getSwapCandidate2() != null) {
                candidate2.setText(event.getSwapCandidate2().getShortRepresentation());
            }
        });

        clearFirstCandidateButton.addActionListener(e -> {
            SpinfoodEvent event = SpinfoodEvent.getInstance();
            candidate1.setText("");
            event.setSwapCandidate1(null);
        });

        clearSecondCandidateButton.addActionListener(e -> {
            SpinfoodEvent event = SpinfoodEvent.getInstance();
            candidate2.setText("");
            event.setSwapCandidate2(null);
        });

        swapSelectedPairButton.addActionListener(e -> {
            //check if two swap candidates are selected //TODO
            //check if swap candidates have a kitchen? //TODO
            SpinfoodEvent event = SpinfoodEvent.getInstance();
            //find the selected pair in Pairlist to swap
            if (pairJList.getSelectedValue() == null) {
                JOptionPane.showMessageDialog(this, "You must select a pair to replace!"); //check if Pair is in groups?
            }

            String selectedPair = pairJList.getSelectedValue();
            String[] names = selectedPair.replaceAll("\\s", "").split(",");

            for (Pair pair : event.getPairList()) { //find right pair, and then swap
                if (pair.participant1.name.equals(names[0]) || pair.participant1.name.equals(names[1])) {
                    CandidateSwapper.swap(pair, false);
                    break;
                }
            }

            displayGroups(event.getGroupList());
            displayPairs(event.getPairList());
            displaySuccessors(event.getSuccessors());
        });

        undoSwapButton.addActionListener(e -> {
            SpinfoodEvent event = SpinfoodEvent.getInstance();

            CandidateSwapper.undo();

            candidate1.setText(event.getSwapCandidate1().getShortRepresentation());
            candidate2.setText(event.getSwapCandidate2().getShortRepresentation());

            displayGroups(event.getGroupList());
            displayPairs(event.getPairList());
            displaySuccessors(event.getSuccessors());
        });

        redoSwapButton.addActionListener(e -> {
            SpinfoodEvent event = SpinfoodEvent.getInstance();

            CandidateSwapper.redo();

            candidate1.setText(event.getSwapCandidate1().getShortRepresentation());
            candidate2.setText(event.getSwapCandidate2().getShortRepresentation());

            displayGroups(event.getGroupList());
            displayPairs(event.getPairList());
            displaySuccessors(event.getSuccessors());
        });

        swapSelectedParticipantButton.addActionListener(e -> {
            SpinfoodEvent event = SpinfoodEvent.getInstance();

            //check if swap candidate1 is selected
            if (event.getSwapCandidate1() == null) {
                JOptionPane.showMessageDialog(this, "You must select a successor!");
                return;
            }
            if (pairJList.getSelectedValue() == null) {
                JOptionPane.showMessageDialog(this, "You must select a pair !");
                return;
            }
            String selectedPair = pairJList.getSelectedValue();
            String[] names = selectedPair.replaceAll("\\s", "").split(",");

            Pair selectedPairForSwap = null;
            for (Pair pair : event.getPairList()) { //find right pair
                if (pair.participant1.name.equals(names[0]) || pair.participant1.name.equals(names[1])) {
                    selectedPairForSwap = pair;
                    break;
                }
            }
            if (selectedPairForSwap != null) {
                ReplacePersonPopup popup = new ReplacePersonPopup();
                popup.showReplacePersonPopup(selectedPairForSwap.participant1, selectedPairForSwap.participant2, this);

            }

            //update GUI
            displaySuccAndPairs();
        });

        // Initialize the UI with the default language texts
        updateLanguage();
        // Initial console message
        printToConsole(resourceBundle.getString("infoConsoleStartUp"));

        // Set JFrame properties
        setTitle(resourceBundle.getString("title"));
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setVisible(true);


    }

    /**
     * Updates the language of the GUI components based on the selected language from the combo box.
     * It retrieves the language locale based on the selected language and sets it as the default locale.
     * Then it retrieves the resource bundle for the selected language and updates the text of all the relevant labels and buttons.
     * This method does not return any value.
     */
    private void updateLanguage() {
        String selectedLanguage = (String) comboBoxLang.getSelectedItem();
        if (selectedLanguage != null) {
            Locale.setDefault(LANGUAGE_LOCALE_MAP.get(selectedLanguage));

            resourceBundle = ResourceBundle.getBundle("languages.messages", Locale.getDefault());

            // labels
            participantsLabel.setText(resourceBundle.getString("participants"));
            pairLabel.setText(resourceBundle.getString("pairs"));
            groupLabel.setText(resourceBundle.getString("groups"));
            successorLabel.setText(resourceBundle.getString("successorLabel"));

            consoleLabel.setText(resourceBundle.getString("console"));
            selectLanguageLabel.setText(resourceBundle.getString("selectLanguageLabel"));

            participantInfoLabel.setText(resourceBundle.getString("participantInfoLabel"));
            pairInfoLabel.setText(resourceBundle.getString("pairInfoLabel"));
            groupInfoLabel.setText(resourceBundle.getString("groupInfoLabel"));
            successorInfoLabel.setText(resourceBundle.getString("successorInfoLabel"));

            pairKeyFiguresLabel.setText(resourceBundle.getString("pairKeyFiguresLabel"));
            groupKeyFiguresLabel.setText(resourceBundle.getString("groupKeyFiguresLabel"));
            successorsKeyFiguresLabel.setText(resourceBundle.getString("successorsKeyFiguresLabel"));

            swapCandidatesLabel.setText(resourceBundle.getString("swapCandidatesLabel"));

            // buttons
            readCSVButton.setText(resourceBundle.getString("readCSVButton"));
            outputCSVButton.setText(resourceBundle.getString("outputCSVButton"));
            changeCriteriaOrderButton.setText(resourceBundle.getString("changeCriteriaOrderButton"));
            pairBuildingButton.setText(resourceBundle.getString("pairBuildingButton"));
            groupBuildingButton.setText(resourceBundle.getString("groupBuildingButton"));
            loadPreviousButton.setText(resourceBundle.getString("loadPreviousButton"));
            showPairMapButton.setText(resourceBundle.getString("showMapButton"));
            cancellationButton.setText(resourceBundle.getString("cancellationButton"));
            openNumberSpinnerButton.setText(resourceBundle.getString("openNumberSpinnerButton"));
            compareGroupsButton.setText(resourceBundle.getString("compareGroupsButton"));
            comparePairsButton.setText(resourceBundle.getString("comparePairsButton"));
            clearFirstCandidateButton.setText(resourceBundle.getString("clearFirstCandidateButton"));
            clearSecondCandidateButton.setText(resourceBundle.getString("clearSecondCandidateButton"));
            chooseSuccessorButton.setText(resourceBundle.getString("chooseSuccessorButton"));
            swapSelectedPairButton.setText(resourceBundle.getString("swapSelectedPairButton"));
            undoSwapButton.setText(resourceBundle.getString("undoSwapButton"));
            redoSwapButton.setText(resourceBundle.getString("redoSwapButton"));
            swapSelectedParticipantButton.setText(resourceBundle.getString("swapSelectedParticipantButton"));

            // other
            setTitle(resourceBundle.getString("title"));
        }
    }

    /**
     * Displays the participants in the Spinfood event.
     *
     * If the participants list is null or empty, no action is taken.
     * Otherwise, each participant's short representation is added to the participantListModel.
     */
    private void displayParticipants() {
        SpinfoodEvent event = SpinfoodEvent.getInstance();
        if (event.participants == null) {
            return;
        }
        participantListModel.clear();
        // Add all participant-Strings to the list model
        for (Participant participant : event.participants) {
            participantListModel.addElement(participant.getShortRepresentation());
        }
    }

    /**
     * Displays the given pairs in the GUI.
     *
     * This method takes a list of Pair objects as input and updates the user interface accordingly. If the list is null or empty, no action is taken. Otherwise, each pair's short
     *  representation is added to the pairListModel, which is responsible for displaying the pairs in a graphical list. Additionally, the pair key figures are retrieved from the
     * SpinfoodEvent instance and set as the text of the pairKeyFiguresTextPane.
     *
     * @param pairs the list of Pair objects to be displayed
     */
    private void displayPairs(List<Pair> pairs) {
        SpinfoodEvent event = SpinfoodEvent.getInstance();
        pairListModel.clear();
        // Add all pair-Strings to the list model
        for (Pair pair : pairs) {
            pairListModel.addElement(pair.shortString());
        }
        pairKeyFiguresTextPane.setText(event.getPairKeyFigures());
    }

    /**
     * Displays the given groups in the GUI.
     *
     * This method takes a list of Group objects as input and updates the user interface accordingly. If the list is null or empty, no action is taken. Otherwise,
     * each group's short representation is added to the groupListModel, which is responsible for displaying the groups in a graphical list.
     * Additionally, the pair key figures are retrieved from the SpinfoodEvent instance and set as the text of the pairKeyFiguresTextPane.
     *
     * @param groups the list of Group objects to be displayed
     */
    private void displayGroups(List<Group> groups) {
        SpinfoodEvent event = SpinfoodEvent.getInstance();
        groupListModel.clear();
        for (Group group : groups) {
            groupListModel.addElement(group.shortString());
        }
        pairKeyFiguresTextPane.setText(event.getPairKeyFigures());
        groupKeyFiguresTextPane.setText(event.getGroupKeyFigures());
    }

    /**
     * Displays the given successors in the GUI.
     *
     * This method takes a list of Participant objects as input and updates the user interface accordingly. If the list is null or empty, no action is taken.
     * Otherwise, each successor's name is added to the successorListModel, which is responsible for displaying the successors in a graphical list.
     * Additionally, the successors key figures are retrieved from the SpinfoodEvent instance and set as the text of the successorsKeyFiguresTextPane.
     *
     * @param successors the list of Participant objects to be displayed as successors
     */
    private void displaySuccessors(List<Participant> successors) {
        SpinfoodEvent event = SpinfoodEvent.getInstance();
        successorListModel.clear();
        for (Participant successor : successors) {
            successorListModel.addElement(successor.name);
        }
        successorsKeyFiguresTextPane.setText(event.getSuccessorsKeyFigures());
    }

    /**
     * Appends a message to the console pane in the GUI.
     *
     * @param message the message to be printed to the console pane
     */
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
            // Ignore
        }
    }

    @Override
    public void enableBuildPairsButton() {
        pairBuildingButton.setEnabled(true);
    }

    @Override
    public void displaySuccAndPairs() {
        // update the GUI
        displayPairs(SpinfoodEvent.getInstance().getPairList());
        displaySuccessors(SpinfoodEvent.getInstance().getSuccessors());
        displayParticipants();

        participantAmountLabel.setText(String.valueOf(SpinfoodEvent.getInstance().participants.size()));
    }
}
