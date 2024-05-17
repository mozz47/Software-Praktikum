package view;

import javax.swing.*;
import java.util.ResourceBundle;

/**
 * Class for managing the info console on the bottom of the GUI.
 */
public class InfoConsolePanel extends JPanel {
    private final JLabel infoConsoleLabel;

    /**
     * InfoConsolePanel constructor
     * @param messages language bundle
     */
    public InfoConsolePanel(ResourceBundle messages, String consoleText) {
        infoConsoleLabel = new JLabel(consoleText);
        add(infoConsoleLabel);
    }

    /**
     * Updates the language of the info console panel.
     * @param messages language bundle.
     */
    public void updateLanguage(ResourceBundle messages, String consoleText) {
        infoConsoleLabel.setText(consoleText);
    }
}
