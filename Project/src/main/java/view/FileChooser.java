package view;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FileChooser {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame("Open");
                JButton button = new JButton("Browse");

                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JFileChooser fileChooser = new JFileChooser();
                        fileChooser.setCurrentDirectory(new java.io.File(System.getProperty("user.home")));
                        fileChooser.setDialogTitle("Choose a file");
                        FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILES", "txt", "text");
                        fileChooser.setFileFilter(filter);

                        int returnValue = fileChooser.showOpenDialog(null);
                        if (returnValue == JFileChooser.APPROVE_OPTION) {
                            java.io.File selectedFile = fileChooser.getSelectedFile();
                            System.out.println(selectedFile.getName());
                        }
                    }
                });

                frame.setLayout(new BorderLayout());
                Box box = Box.createHorizontalBox();
                box.add(Box.createHorizontalGlue());
                box.add(button);
                box.add(Box.createHorizontalGlue());

                //frame.add(box, BorderLayout.SOUTH);
                frame.add(box);
                frame.setSize(400, 200);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
            }
        });
    }
}