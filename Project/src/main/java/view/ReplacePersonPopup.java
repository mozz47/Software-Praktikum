package view;

import model.Pair;
import model.Participant;
import model.SpinfoodEvent;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class ReplacePersonPopup {
    JFrame frame;
    DisplayCallback callback;

    public void showReplacePersonPopup(Participant p1, Participant p2, DisplayCallback call) {
        callback = call;

        // Erstelle das Hauptfenster (JFrame)
        frame = new JFrame("Replace Person");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(300, 150);
        frame.setLocationRelativeTo(null);

        // Erstelle ein JPanel, um die Komponenten zu halten
        JPanel panel = new JPanel();
        frame.add(panel);
        placeComponents(panel, p1, p2);

        // Zeige das Fenster an
        frame.setVisible(true);
    }

    private void placeComponents(JPanel panel, Participant p1, Participant p2) {
        panel.setLayout(null);

        JLabel userLabel = new JLabel("Choose Person to replace:");
        userLabel.setBounds(10, 10, 250, 25);
        panel.add(userLabel);

        JRadioButton person1Button = new JRadioButton(p1.name);
        person1Button.setBounds(10, 40, 100, 25);
        panel.add(person1Button);

        JRadioButton person2Button = new JRadioButton(p2.name);
        person2Button.setBounds(150, 40, 100, 25);
        panel.add(person2Button);

        ButtonGroup group = new ButtonGroup();
        group.add(person1Button);
        group.add(person2Button);

        // Check which person can be replaced -> if successor has kitchen then both can be replaced, if not then only person with no kitchen can be replaced
        SpinfoodEvent event = SpinfoodEvent.getInstance();
        Participant successorToSwap = event.getSwapCandidate1();
        if (successorToSwap != null && successorToSwap.hasKitchen) { //succ has kitchen
            person1Button.setEnabled(true);
            person2Button.setEnabled(true);
        } else { //succ has no kitchen -> only enable button for person which has kitchen
            person1Button.setEnabled(!p1.hasKitchen);
            person2Button.setEnabled(!p2.hasKitchen);
        }

        JButton replaceButton = new JButton("Replace");
        replaceButton.setBounds(10, 80, 120, 25);
        panel.add(replaceButton);

        // Action Listener for Replace Button
        replaceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Participant toReplace = null;

                if (person1Button.isSelected()) {
                    JOptionPane.showMessageDialog(panel, "Person 1 getting replaced.");
                    toReplace = p1;
                } else if (person2Button.isSelected()) {
                    JOptionPane.showMessageDialog(panel, "Person 2 getting replaced");
                    toReplace = p2;
                } else {
                    JOptionPane.showMessageDialog(panel, "Please choose Person.");
                    return;
                }

                for (Pair p : SpinfoodEvent.getInstance().getPairList()) {
                    if (p.participant1.equals(toReplace)) {
                        p.participant1 = successorToSwap;
                        SpinfoodEvent.getInstance().getSuccessors().add(toReplace);
                        SpinfoodEvent.getInstance().getSuccessors().remove(successorToSwap);
                        break;
                    } else if (p.participant2.equals(toReplace)) {
                        p.participant2 = successorToSwap;
                        SpinfoodEvent.getInstance().getSuccessors().add(toReplace);
                        SpinfoodEvent.getInstance().getSuccessors().remove(successorToSwap);
                        break;
                    }
                }
                System.out.println(Arrays.toString(SpinfoodEvent.getInstance().getSuccessors().toArray()));
                frame.dispose();
                callback.displaySuccAndPairs();

            }
        });
    }
}

