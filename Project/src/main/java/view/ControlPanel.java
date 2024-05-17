package view;

import javax.swing.*;
import java.awt.*;
import java.util.ResourceBundle;

public class ControlPanel extends JPanel {
    private final JButton switchButton;
    private final JButton readCSVButton;
    private final JButton autoAssignButton;
    private final JButton outputCSVButton;

    public ControlPanel(ResourceBundle messages) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        switchButton = new JButton(messages.getString("switchLangButton"));
        readCSVButton = new JButton(messages.getString("readCSVButton"));
        autoAssignButton = new JButton(messages.getString("autoAssignButton"));
        outputCSVButton = new JButton(messages.getString("outputCSVButton"));

        add(switchButton);
        add(readCSVButton);
        add(autoAssignButton);
        add(outputCSVButton);
    }

    public void updateLanguage(ResourceBundle messages) {
        switchButton.setText(messages.getString("switchLangButton"));
        readCSVButton.setText(messages.getString("readCSVButton"));
        autoAssignButton.setText(messages.getString("autoAssignButton"));
        outputCSVButton.setText(messages.getString("outputCSVButton"));
    }
}
