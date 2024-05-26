package view;

import controller.PairListBuilder;
import model.Criterion;
import model.PairList;

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
    private final PairDisplayCallback callback;

    /**
     * Constructor for the CriteriaRankingFrame. Creates a list of the 5 criteria and displays them in a JList.
     *
     * @param resourceBundle
     */
    public CriteriaRankingFrame(ResourceBundle resourceBundle, PairDisplayCallback callback) {
        this.callback = callback;
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
            System.out.println("Ranked criteria:");
            for (Criterion s : data1) {
                System.out.println(resourceBundle.getString(s.getToken()));
            }
            dispose();
            PairList pairList = PairListBuilder.getPairList(data1);
            callback.displayPairs(pairList.getPairList());
            callback.printToConsole(resourceBundle.getString("createdPairsWithAlgorithm"));

            //starting of algorithm for groups
            //GroupController.getAllGroups(data1);
            //todo

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

    /**
     * Test method for the CriteriaRankingFrame.
     *
     * @param args
     */
    public static void main(String[] args) {
        //SwingUtilities.invokeLater(() -> new CriteriaRankingFrame(ResourceBundle.getBundle("languages.messages", Locale.ENGLISH)));
    }

}

