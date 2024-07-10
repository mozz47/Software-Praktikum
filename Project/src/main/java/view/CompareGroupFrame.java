package view;

import model.Group;
import model.SpinfoodEvent;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class CompareGroupFrame extends JDialog{
    private JPanel mainPanel;
    private JScrollPane groupsPane;
    private JScrollPane groupsPane2;
    private JTextArea group1KeyFigures;
    private JTextArea group2KeyFigures;
    private JTextArea textAreaDifferences;
    private JList<String> groupJList;
    private JList<String> group2JList;
    private JLabel labelCurrentGroups;
    private JLabel labelOldGroups;
    private JLabel differencesLabel;
    DefaultListModel<String> groupListModel, groupListModel2;


    public CompareGroupFrame(List<Group> groupList, List<Group> groupList2, Frame parent) {
        super (parent, true);

        ResourceBundle resourceBundle = ResourceBundle.getBundle("languages.messages", Locale.getDefault());
        setTitle(resourceBundle.getString("compare_group_title"));
        setContentPane(mainPanel);
        setSize(810, 400);
        setResizable(false);

        SpinfoodEvent event = SpinfoodEvent.getInstance();

        labelCurrentGroups.setText(resourceBundle.getString("labelCurrentGroups"));
        labelOldGroups.setText(resourceBundle.getString("labelOldGroups"));

        //Fill scroll panes with data
        groupListModel = new DefaultListModel<>();
        groupListModel2 = new DefaultListModel<>();

        groupJList.setModel(groupListModel);
        group2JList.setModel(groupListModel2);

        for (Group group : groupList) {
            groupListModel.addElement(group.shortString());
        }
        for (Group group : groupList2) {
            groupListModel2.addElement(group.shortString());
        }
        groupsPane.setViewportView(groupJList);
        groupsPane2.setViewportView(group2JList);

        group1KeyFigures.setText(event.getGroupKeyFigures());
        group2KeyFigures.setText(event.getOldGroupKeyFigures());

        textAreaDifferences.setText(event.getGroupDifferenceKeyFigures());
        textAreaDifferences.setEditable(false);

        differencesLabel.setText(resourceBundle.getString("differences"));

        setVisible(true);


    }

}
