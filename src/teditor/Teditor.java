package teditor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Teditor {

    JFrame mainFrame = new JFrame("Teditor");
    JMenu fileMenu = new JMenu("File");
    JMenu editMenu = new JMenu("Edit");
    JMenu helpMenu = new JMenu("Help");
    JTextArea textArea = new JTextArea();
    JScrollPane scrollPane = new JScrollPane(textArea,
            ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    JFileChooser fileChooser = new JFileChooser();

    // file menu items
    JMenuItem newItem = new JMenuItem("New");
    JMenuItem openItem = new JMenuItem("Open");
    JMenuItem saveItem = new JMenuItem("Save");
    JMenuItem saveAsItem = new JMenuItem("Save as");
    JMenuItem exitItem = new JMenuItem("Exit");

    // edit menu items
    JMenu font = new JMenu("Font");
    JMenuItem arialFont = new JMenuItem("Arial");
    JMenuItem consoalsFont = new JMenuItem("Consolas");
    JMenuItem liberationMonoFont = new JMenuItem("Liberation Mono");
    JMenuItem timesNewRomanFont = new JMenuItem("Times New Roman");
    JMenuItem tahomaFont = new JMenuItem("Tahoma");

    JMenu fontStyle = new JMenu("Font Style");
    JMenuItem italic = new JMenuItem("Italics");
    JMenuItem bold = new JMenuItem("Bold");
    JMenuItem plain = new JMenuItem("Plain");

    JMenu color = new JMenu("Color");
    JMenuItem fontColor = new JMenuItem("Font Color");
    JMenuItem bgColor = new JMenuItem("Background Color");
    JMenuItem replace = new JMenuItem("Replace");

    JMenu lineWrap = new JMenu("Line Wrap");
    JMenuItem lineWrapOn = new JMenuItem("On");
    JMenuItem lineWrapOff = new JMenuItem("Off");

    // help menu item
    JMenuItem aboutItem = new JMenuItem("About");

    void addFileExtensions() {
        // adding the file extensions in the file extension chooser
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("HyperText Markup Language (*.html)", "html"));
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Cascading Style Sheets (*.css)", "css"));
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("JavaScript (*.js)", "js"));
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Java (*.java)", "java"));
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Python (*.py)", "py"));
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Text Files (*.txt)", "txt"));
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Extensive Markup Language (*.xml)", "xml"));
    }

    public void design() {
        mainFrame.setSize(600, 450);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLocationRelativeTo(null);

        // adding file menu items
        fileMenu.add(newItem);
        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.add(saveAsItem);
        fileMenu.add(exitItem);

        // adding edit menu items
        editMenu.add(font);
        font.add(arialFont);
        font.add(consoalsFont);
        font.add(timesNewRomanFont);
        font.add(liberationMonoFont);
        font.add(tahomaFont);

        editMenu.add(fontStyle);
        fontStyle.add(italic);
        fontStyle.add(plain);
        fontStyle.add(bold);

        editMenu.add(color);
        color.add(fontColor);
        color.add(bgColor);
        editMenu.add(replace);

        editMenu.add(lineWrap);
        lineWrap.add(lineWrapOn);
        lineWrap.add(lineWrapOff);

        // adding help menu items
        helpMenu.add(aboutItem);

        // main menu
        JMenuBar menuPanel = new JMenuBar();
        menuPanel.add(fileMenu);
        menuPanel.add(editMenu);
        menuPanel.add(helpMenu);

        // default font for text area
        textArea.setFont(new Font("Liberation Mono", defaultFontStyle, 14));

        // adding components
        mainFrame.getContentPane().add(BorderLayout.NORTH, menuPanel);
        mainFrame.getContentPane().add(BorderLayout.CENTER, scrollPane);
        mainFrame.setVisible(true);
    }

    int defaultFontStyle = Font.PLAIN;

    void changeFontTo(String fontName) {
        textArea.setFont(new Font(fontName, defaultFontStyle, 14));
    }

    // stores the name of the file that is currently open
    String currentOpenFile = null;

    void buttonFunctions() {
        addFileExtensions();  // adding the file extensions to choose from

        newItem.addActionListener((ae) -> {
            // TODO: send new file to tabbed menu on the text editor
            textArea.setText("");
            mainFrame.setTitle("Teditor");
        });

        openItem.addActionListener((ae) -> {

            int response = fileChooser.showOpenDialog(mainFrame);
            if (response == JFileChooser.APPROVE_OPTION) {

                File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
                try (FileReader fileReader = new FileReader(file);
                        BufferedReader bufferedReader = new BufferedReader(fileReader);) {

                    String currentLine;
                    while ((currentLine = bufferedReader.readLine()) != null) {
                        textArea.append(currentLine + "\n");
                    }
                    currentOpenFile = file.getName();
                    mainFrame.setTitle(currentOpenFile + " - Teditor");
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(mainFrame, e.getMessage());
                }
            }
        });

        saveItem.addActionListener((ae) -> {

            if (currentOpenFile != null) {
                File file = new File(currentOpenFile);

                try (FileWriter fileWriter = new FileWriter(file.getAbsolutePath());
                        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                        PrintWriter printWriter = new PrintWriter(bufferedWriter, true)) {

                    printWriter.println(textArea.getText());  // writes content to the file
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(mainFrame, e.getMessage());
                }
            } else {
                int response = fileChooser.showSaveDialog(mainFrame);
                if (response == JFileChooser.APPROVE_OPTION) {
                    File file = new File(fileChooser.getSelectedFile().getAbsolutePath());

                    try (FileWriter fileWriter = new FileWriter(file);
                            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                            PrintWriter printWriter = new PrintWriter(bufferedWriter, true);) {

                        printWriter.println(textArea.getText());  // writes content to the file
                        currentOpenFile = file.getName();
                        mainFrame.setTitle(currentOpenFile + " - Teditor");
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(mainFrame, e.getMessage());
                    }
                }
            }
        });

        saveAsItem.addActionListener((ae) -> {

            JFileChooser saveAsFile = new JFileChooser();
            int response = saveAsFile.showSaveDialog(mainFrame);

            if (response == JFileChooser.APPROVE_OPTION) {
                File file = new File(saveAsFile.getSelectedFile().getAbsolutePath());
                try (FileWriter fileWriter = new FileWriter(file);
                        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);) {

                    bufferedWriter.write(textArea.getText()); // writes content to the file
                    currentOpenFile = file.getName();
                    mainFrame.setTitle(currentOpenFile + " - Teditor");
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(mainFrame, e.getMessage());
                }
            }
        });

        exitItem.addActionListener((ae) -> {
            System.exit(0);
        });

        arialFont.addActionListener((ae) -> {
            changeFontTo("Arial");
        });

        consoalsFont.addActionListener((ae) -> {
            changeFontTo("Consolas");
        });

        timesNewRomanFont.addActionListener((ae) -> {
            changeFontTo("Times New Roman");
        });

        liberationMonoFont.addActionListener((ae) -> {
            changeFontTo("Liberation Mono");
        });

        tahomaFont.addActionListener((ae) -> {
            changeFontTo("Tahoma");
        });

        italic.addActionListener((ae) -> {
            textArea.setFont(new Font("Arial", Font.ITALIC, 15));
        });

        bold.addActionListener((ae) -> {
            textArea.setFont(new Font("Arial", Font.BOLD, 15));
        });

        plain.addActionListener((ae) -> {
            textArea.setFont(new Font("Arial", Font.PLAIN, 15));
        });

        fontColor.addActionListener((ae) -> {
            Color colo = JColorChooser.showDialog(mainFrame, "Font Color", Color.BLACK);
            textArea.setForeground(colo);
        });

        bgColor.addActionListener((ae) -> {
            Color colo = JColorChooser.showDialog(mainFrame, "Background Color", Color.WHITE);
            textArea.setBackground(colo);
        });

        replace.addActionListener((ae) -> {
            new FindAndReplace();
        });

        lineWrapOn.addActionListener((ae) -> {
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);
        });

        lineWrapOff.addActionListener((ae) -> {
            textArea.setLineWrap(false);
            textArea.setWrapStyleWord(false);
        });

        aboutItem.addActionListener((ae) -> {
            JOptionPane.showMessageDialog(mainFrame, "Teditor\nCopyright 2021\nVersion 0.0.4");
        });
    }

    public static void main(String[] args) {
        nimbusLAF();
        Teditor teditor = new Teditor();
        teditor.design();
        teditor.buttonFunctions();
    }

    class FindAndReplace {

        JFrame replaceFrame = new JFrame("Find and Replace");
        JLabel findWhatLabel = new JLabel("Find What");
        JLabel replaceWithLabel = new JLabel("Replace With");
        JTextField findWhatField = new JTextField();
        JTextField replaceWithField = new JTextField();
        JButton replaceBtn = new JButton("Replace");

        public FindAndReplace() {
            replaceFrame.setSize(300, 200);
            replaceFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            replaceFrame.setLayout(null);
            replaceFrame.setLocationRelativeTo(mainFrame);
            replaceFrame.setResizable(false);

            // setting dimensions
            findWhatLabel.setBounds(10, 10, 90, 30);
            findWhatField.setBounds(110, 10, 150, 30);
            replaceWithLabel.setBounds(10, 50, 90, 30);
            replaceWithField.setBounds(110, 50, 150, 30);
            replaceBtn.setBounds(110, 100, 90, 30);

            // adding components
            replaceFrame.getContentPane().add(replaceWithField);
            replaceFrame.getContentPane().add(findWhatField);
            replaceFrame.getContentPane().add(replaceWithLabel);
            replaceFrame.getContentPane().add(findWhatLabel);
            replaceFrame.getContentPane().add(replaceBtn);
            replaceFrame.setVisible(true);

            replaceBtn.addActionListener((ae) -> {
                String target = findWhatField.getText().trim();
                String replacement = replaceWithField.getText().trim();
                String content = textArea.getText().replace(target, replacement);
                textArea.setText(content);
                replaceFrame.dispose();
            });
        }
    }

    static void nimbusLAF() {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException e) {
        }
    }
}
