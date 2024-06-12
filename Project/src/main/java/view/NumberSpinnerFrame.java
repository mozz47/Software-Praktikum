package view;

import model.SpinfoodEvent;

import javax.swing.*;
import java.awt.*;
import java.util.Locale;
import java.util.ResourceBundle;

public class NumberSpinnerFrame extends JFrame {
    private final JSpinner spinner;

    /**
     * Constructor for the NumberSpinnerFrame. Creates a spinner and a button to approve the value in the spinner.
     *
     * @param resourceBundle the language resource bundle
     */
    public NumberSpinnerFrame(ResourceBundle resourceBundle) {
        // load singleton
        SpinfoodEvent event = SpinfoodEvent.getInstance();

        // frame setup
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 100);
        setLayout(new FlowLayout());
        setLocationRelativeTo(null);

        // label setup
        JLabel numberSpinnerLabel = new JLabel(resourceBundle.getString("numberSpinnerLabel"));
        add(numberSpinnerLabel);

        // spinner setup
        spinner = new JSpinner(new SpinnerNumberModel(event.maxParticipants, 0, 9999, 9));
        add(spinner);

        // button setup
        JButton approveNumberButton = new JButton(resourceBundle.getString("approveNumberButton"));
        add(approveNumberButton);

        // when clicking the button: set maxParticipants to the value in the spinner and close the spinner window
        approveNumberButton.addActionListener(e -> {
            event.maxParticipants = (int) spinner.getValue();
            dispose();
        });

        setVisible(true);
    }
}
