package view;

import model.Pair;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.List;
import java.util.ResourceBundle;

public class PairPanel extends JPanel {
    private final JList<Pair> pairJList;
    private final DefaultListModel<Pair> pairListModel;

    public PairPanel(DefaultListModel<Pair> pairListModel, ResourceBundle messages) {
        setLayout(new BorderLayout());

        this.pairListModel = pairListModel;
        pairJList = new JList<>(pairListModel);
        pairJList.setCellRenderer(new DefaultListCellRenderer());
        JScrollPane pairScrollPane = new JScrollPane(pairJList);
        add(pairScrollPane, BorderLayout.CENTER);

        TitledBorder titledBorder = BorderFactory.createTitledBorder(messages.getString("pairs"));
        setBorder(titledBorder);
    }
}
