package view;

import javax.swing.*;
import java.util.Locale;
import java.util.ResourceBundle;

public class CriteriaRegulator extends JFrame{


    CriteriaRegulator(ResourceBundle resourceBundle) {
        setContentPane(criteriaPane);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(resourceBundle.getString("criteriaRegulatorTitle"));
        setSize(400,300);
        setLocationRelativeTo(null);
        criteriaLabel.setText(resourceBundle.getString("criteriaListLabel"));


        setVisible(true);

    }
    private JPanel criteriaPane;
    private JButton criteriaAcceptButton;
    private JList criteriaList;
    private JLabel criteriaLabel;


}
