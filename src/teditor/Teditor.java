package teditor;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;

public class Teditor {

    JFrame frame = new JFrame("Teditor");
    JMenu fileMenu = new JMenu("File");
    JMenu editMenu = new JMenu("Edit");
    JMenu helpMenu = new JMenu("Help");
    JTextArea textArea = new JTextArea();
    JScrollPane scrollPane = new JScrollPane(textArea,
            ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

    // file menu items
    JMenuItem newItem = new JMenuItem("New");
    JMenuItem openItem = new JMenuItem("Open");
    JMenuItem saveItem = new JMenuItem("Save");
    JMenuItem saveAsItem = new JMenuItem("Save as");
    JMenuItem exitItem = new JMenuItem("Exit");

    // edit menu items
    JMenuItem fontItem = new JMenuItem("Font");
    JMenuItem replaceItem = new JMenuItem("Replace");
    JMenuItem wordWrapItem = new JMenuItem("Word Wrap");
    JMenuItem backgroundColor = new JMenuItem("Background Color");

    // help menu item
    JMenuItem aboutItem = new JMenuItem("About");

    public void design() {
        frame.setSize(500, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocation(200, 150);

        // adding file menu items
        fileMenu.add(newItem);
        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.add(saveAsItem);
        fileMenu.add(exitItem);

        // adding edit menu items
        editMenu.add(fontItem);
        editMenu.add(replaceItem);
        editMenu.add(wordWrapItem);
        editMenu.add(backgroundColor);

        // adding help menu items
        helpMenu.add(aboutItem);

        // main menu
        JMenuBar menuPanel = new JMenuBar();
        menuPanel.add(fileMenu);
        menuPanel.add(editMenu);
        menuPanel.add(helpMenu);

        // adding components
        frame.getContentPane().add(BorderLayout.NORTH, menuPanel);
        frame.getContentPane().add(BorderLayout.CENTER, scrollPane);
        frame.setVisible(true);
    }

    String currentOpenFile = null;

    void buttonFunctions() {

        newItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                // TODO: sent new file to tabbed menu on the text editor
                textArea.setText("");
            }
        });

        openItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                JFileChooser openNewFile = new JFileChooser();
                int response = openNewFile.showOpenDialog(frame);

                if (response == JFileChooser.APPROVE_OPTION) {
                    // gets the specified file
                    File file = new File(openNewFile.getSelectedFile().getAbsolutePath());
                    try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
                        String currentLine;
                        String content = "";
                        while ((currentLine = bufferedReader.readLine()) != null) {
                            content += currentLine + "\n";
                        }
                        textArea.setText(content);
                        currentOpenFile = file.getAbsolutePath();
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(frame, e.getMessage());
                    }
                }
            }
        });

        saveItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (currentOpenFile != null) {
                    File file = new File(currentOpenFile);

                    try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
                            PrintWriter printWriter = new PrintWriter(bufferedWriter, true);) {

                        printWriter.println(textArea.getText());  // writes content to the file
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(frame, e.getMessage());
                    }
                }
            }
        });

        saveAsItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                JFileChooser saveAsFile = new JFileChooser();
                int response = saveAsFile.showSaveDialog(frame);

                if (response == JFileChooser.APPROVE_OPTION) {
                    // gets the specified file
                    File file = new File(saveAsFile.getSelectedFile().getAbsolutePath());

                    try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
                            PrintWriter printWriter = new PrintWriter(bufferedWriter, true);) {

                        printWriter.println(textArea.getText());  // writes content to the file
                        currentOpenFile = file.getAbsolutePath();
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(frame, e.getMessage());
                    }
                }
            }
        });

        exitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                System.exit(0);
            }
        });

        aboutItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                JOptionPane.showMessageDialog(frame, "Teditor 2021\nVersion 0.0.1");
            }
        });
    }

    public static void main(String[] args) {
        nimbusLAF();
        Teditor teditor = new Teditor();
        teditor.design();
        teditor.buttonFunctions();
    }

    static void nimbusLAF() {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
        }
    }

}
