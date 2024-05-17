package view;

import model.Pair;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.ResourceBundle;

/**
 * Class for managing the pair list in the GUI.
 */
public class PairPanel extends JPanel {

    /**
     * Constructor. Initializes the JScrollPane with its content
     * @param pairListModel list model
     * @param messages language bundle
     */
    public PairPanel(DefaultListModel<Pair> pairListModel, ResourceBundle messages) {
        setLayout(new BorderLayout());

        JList<Pair> pairJList = new JList<>(pairListModel);
        pairJList.setCellRenderer(new PairListCellRenderer());
        JScrollPane pairScrollPane = new JScrollPane(pairJList);
        add(pairScrollPane, BorderLayout.CENTER);

        TitledBorder titledBorder = BorderFactory.createTitledBorder(messages.getString("pairs"));
        setBorder(titledBorder);
    }
}
