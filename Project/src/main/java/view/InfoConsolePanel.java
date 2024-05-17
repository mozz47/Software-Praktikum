package view;

import javax.swing.*;
import java.util.ResourceBundle;

public class InfoConsolePanel extends JPanel {
    private final JLabel infoConsoleLabel;

    public InfoConsolePanel(ResourceBundle messages, String consoleText) {
        infoConsoleLabel = new JLabel(consoleText);
        add(infoConsoleLabel);
    }

    public void updateLanguage(ResourceBundle messages, String consoleText) {
        infoConsoleLabel.setText(consoleText);
    }
}
