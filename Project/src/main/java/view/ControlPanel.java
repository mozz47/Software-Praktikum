package view;

import javax.swing.*;
import java.util.ResourceBundle;

/**
 * Class for managing the buttons of the GUI.
 */
public class ControlPanel extends JPanel {
    private final JButton switchButton;
    private final JButton readCSVButton;
    private final JButton autoAssignButton;
    private final JButton outputCSVButton;

    /**
     * ControlPanel constructor initializing the buttons on the right hand side of the GUI.
     * @param mainFrame needed for toggling language of other GUI elements on mainFrame
     * @param messages language bundle
     */
    public ControlPanel(MainFrame mainFrame, ResourceBundle messages) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        switchButton = new JButton(messages.getString("switchLangButton"));
        readCSVButton = new JButton(messages.getString("readCSVButton"));
        autoAssignButton = new JButton(messages.getString("autoAssignButton"));
        outputCSVButton = new JButton(messages.getString("outputCSVButton"));

        switchButton.addActionListener(e -> {
            mainFrame.toggleLanguage();
        });

        add(switchButton);
        add(readCSVButton);
        add(autoAssignButton);
        add(outputCSVButton);
    }

    /**
     * updates the language of all buttons.
     * @param messages the inherited language resource bundle to update the texts.
     */
    public void updateLanguage(ResourceBundle messages) {
        switchButton.setText(messages.getString("switchLangButton"));
        readCSVButton.setText(messages.getString("readCSVButton"));
        autoAssignButton.setText(messages.getString("autoAssignButton"));
        outputCSVButton.setText(messages.getString("outputCSVButton"));
    }
}
