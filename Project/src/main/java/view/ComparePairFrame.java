package view;

import model.Pair;
import model.SpinfoodEvent;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;


public class ComparePairFrame extends JDialog {
    private JPanel mainPanel;
    private JList<String> pairJList;
    private JList<String> pairs2JList;
    private JScrollPane pairsPane;
    private JScrollPane pairsPane2;
    private JTextArea pair1KeyFigures;
    private JTextArea pair2KeyFigures;
    private JLabel labelCurrentPairs;
    private JLabel labelOldPairs;
    private JTextArea textAreaDifference;
    private JLabel differencesLabel;
    DefaultListModel<String> pairListModel, pairListModel2;

    public ComparePairFrame(List<Pair> pairList, List<Pair> pairList2, Frame parent) {
        super(parent, true); //Set modality to true

        ResourceBundle resourceBundle = ResourceBundle.getBundle("languages.messages", Locale.getDefault());
        setTitle(resourceBundle.getString("compare.pairs.title"));
        setContentPane(mainPanel);
        setSize(810,400);
        setResizable(false);

        SpinfoodEvent event = SpinfoodEvent.getInstance();

        labelCurrentPairs.setText(resourceBundle.getString("currentPairs"));
        labelOldPairs.setText(resourceBundle.getString("oldPairs"));

        //Fill scroll panes with data
        pairListModel = new DefaultListModel<>();
        pairListModel2 = new DefaultListModel<>();
        pairJList.setModel(pairListModel);
        pairs2JList.setModel(pairListModel2);
        for (Pair pair : pairList) {
            pairListModel.addElement(pair.shortString());
        }
        for (Pair pair : pairList2) {
            pairListModel2.addElement(pair.shortString());
        }
        pairJList.setModel(pairListModel);

        pairsPane.setViewportView(pairJList);
        pairsPane2.setViewportView(pairs2JList);

        //Fill key figures panes with data
        pair1KeyFigures.setEditable(false);
        pair2KeyFigures.setEditable(false);

        pair1KeyFigures.setText(event.getPairKeyFigures());
        pair2KeyFigures.setText(event.getOldPairKeyFigures());

        textAreaDifference.setText(event.getPairDifferenceKeyFigures());
        textAreaDifference.setEditable(false);
        differencesLabel.setText(resourceBundle.getString("differences"));

        setVisible(true);
    }
}
