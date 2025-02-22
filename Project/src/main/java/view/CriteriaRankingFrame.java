package view;

import model.Criterion;
import model.SpinfoodEvent;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Window for ranking the 5 criteria that are not mandatory. The criteria will be displayed in a list and can be dragged
 * and dropped by the user to sort them by importance or preference before starting the automatic assignment algorithm.
 */
public class CriteriaRankingFrame extends JFrame {

    /**
     * Constructor for the CriteriaRankingFrame. Creates a list of the 5 criteria and displays them in a JList.
     *
     * @param resourceBundle
     */
    public CriteriaRankingFrame(ResourceBundle resourceBundle, DisplayCallback callback) {
        List<Criterion> criteria = new ArrayList<>();
        criteria.add(Criterion.Criterion_06_Food_Preference);
        criteria.add(Criterion.Criterion_07_Age_Difference);
        criteria.add(Criterion.Criterion_08_Sex_Diversity);
        criteria.add(Criterion.Criterion_09_Path_Length);
        criteria.add(Criterion.Criterion_10_Group_Amount);

        // Create list models and populate them
        DefaultListModel<Criterion> lm1 = new DefaultListModel<>();
        for (Criterion item : criteria) {
            lm1.addElement(item);
        }

        // Create JLists with the list models
        JList<Criterion> list1 = new JList<>(lm1);

        // Enable drag and drop
        list1.setDragEnabled(true);
        list1.setDropMode(DropMode.INSERT);

        // Set the TransferHandler for both lists
        list1.setTransferHandler(new ListTransferHandler<>(list1, criteria));

        // Create scroll panes for the JLists
        JScrollPane scrollPane1 = new JScrollPane(list1);

        // Create OK Button
        JButton okButton = new JButton(resourceBundle.getString("startAlgo")); //TODO Rename
        okButton.addActionListener(e -> {
            startAlgorithm(criteria, callback);
            callback.enableBuildPairsButton();

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
        setSize(400, 220);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void startAlgorithm(List<Criterion> criteria, DisplayCallback callback) {
        dispose();

        SpinfoodEvent event = SpinfoodEvent.getInstance();
        event.criteria = criteria;

        for (Criterion criterion : event.criteria) {
            callback.printToConsole(criterion.toString());
        }
    }

    /**
     * Test method for the CriteriaRankingFrame.
     *
     * @param args
     */
    public static void main(String[] args) {
        //SwingUtilities.invokeLater(() -> new CriteriaRankingFrame(ResourceBundle.getBundle("languages.messages", Locale.ENGLISH)));
    }

}

