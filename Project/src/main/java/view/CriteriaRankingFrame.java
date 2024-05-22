package view;

import model.Criterion;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class CriteriaRankingFrame extends JFrame {

    public CriteriaRankingFrame(ResourceBundle resourceBundle) {
        // todo Initialize the underlying data models
        // temporary placeholder data
        List<Criterion> data1 = new ArrayList<>();
        data1.add(Criterion.Criterion_06_Food_Preference);
        data1.add(Criterion.Criterion_07_Age_Difference);
        data1.add(Criterion.Criterion_08_Sex_Diversity);
        data1.add(Criterion.Criterion_09_Path_Length);
        data1.add(Criterion.Criterion_10_Group_Amount);

        // Create list models and populate them
        DefaultListModel<Criterion> lm1 = new DefaultListModel<>();
        for (Criterion item : data1) {
            lm1.addElement(item);
        }

        // Create JLists with the list models
        JList<Criterion> list1 = new JList<>(lm1);

        // Enable drag and drop
        list1.setDragEnabled(true);
        list1.setDropMode(DropMode.INSERT);

        // Set the TransferHandler for both lists
        list1.setTransferHandler(new ListTransferHandler<>(list1, data1));

        // Create scroll panes for the JLists
        JScrollPane scrollPane1 = new JScrollPane(list1);

        // Create OK Button
        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> {
            System.out.println("data1");
            for (Criterion s : data1) {
                System.out.println(s.toString());
            }
            dispose();
        });
        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.add(okButton, BorderLayout.CENTER);

        // Create Description Label
        JLabel descriptionLabel = new JLabel(resourceBundle.getString("criteriaRankingFrame_description"));

        // Create scrollPanePanel
        JPanel scrollPanePanel = new JPanel();
        scrollPanePanel.setLayout(new GridLayout(1, 2));
        scrollPanePanel.add(scrollPane1);
        scrollPanePanel.setPreferredSize(new Dimension(300, 300));

        // Set up the frame layout
        setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
        add(descriptionLabel);
        add(scrollPanePanel);
        add(buttonPanel);

        // Set up frame properties
        setTitle(resourceBundle.getString("criteriaRankingFrame_title"));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(500, 300);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CriteriaRankingFrame(ResourceBundle.getBundle("languages.messages", Locale.ENGLISH)));
    }

}

