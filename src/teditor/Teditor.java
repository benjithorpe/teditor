package teditor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

public class Teditor extends Application {

    ComboBox fontNames = new ComboBox(FXCollections.observableArrayList(Font.getFontNames()));
    ComboBox fontStyles = new ComboBox(FXCollections.observableArrayList(FontWeight.values()));

    Menu fileMenu = new Menu("File");
    Menu editMenu = new Menu("Edit");
    Menu fontMenu = new Menu("", fontNames);
    Menu stylesMenu = new Menu("", fontStyles);
    Menu helpMenu = new Menu("Help");
    TextArea textArea = new TextArea();
    FileChooser fileChooser = new FileChooser();

    // file menu items
    MenuItem newItem = new MenuItem("New");
    MenuItem openItem = new MenuItem("Open");
    MenuItem saveItem = new MenuItem("Save");
    MenuItem saveAsItem = new MenuItem("Save as");
    MenuItem exitItem = new MenuItem("Exit");

    // edit menu items
    Menu colorMenu = new Menu("Color");
    MenuItem fontColor = new MenuItem("Font Color");
    MenuItem bgColor = new MenuItem("Background Color");
    MenuItem replace = new MenuItem("Replace");

    Menu lineWrap = new Menu("Line Wrap");
    MenuItem lineWrapOn = new MenuItem("On");
    MenuItem lineWrapOff = new MenuItem("Off");

    // help menu item
    MenuItem aboutItem = new MenuItem("About");

    // stores the name of the file that is currently open
    String currentOpenFile = null;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Teditor");
        design();
        fontNames.setMaxWidth(100);
        fontNames.setValue("Arial");
        fontStyles.setMaxWidth(100);
        fontStyles.setValue(FontWeight.NORMAL);

        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(fileMenu, editMenu, helpMenu, fontMenu, stylesMenu);

        BorderPane borderPane = new BorderPane();
        borderPane.setTop(menuBar);
        borderPane.setCenter(textArea);

        stage.setScene(new Scene(borderPane, 650, 400));
        stage.show();
        buttonFunctions();

        saveItem.setOnAction((ActionEvent ae) -> {

            if (currentOpenFile != null) {
                try (FileWriter fileWriter = new FileWriter(new File(currentOpenFile));
                        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);) {

                    bufferedWriter.write(textArea.getText());

                } catch (IOException e) {
                    // do nothing
                }
            } else {
                File selectedFile = fileChooser.showSaveDialog(stage);
                if (selectedFile != null) {
                    try (FileWriter fileWriter = new FileWriter(selectedFile.getAbsolutePath());
                            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);) {

                        bufferedWriter.write(textArea.getText());
                        currentOpenFile = selectedFile.getAbsolutePath();
                    } catch (IOException e) {
                        // do nothing
                    }
                }
            }
        });

        openItem.setOnAction((ActionEvent ae) -> {
            File selectedFile = fileChooser.showOpenDialog(stage);

            if (selectedFile != null) {
                textArea.clear();

                try (FileReader fileReader = new FileReader(selectedFile);
                        BufferedReader bufferedReader = new BufferedReader(fileReader);) {

                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        textArea.appendText(line + "\n");
                    }

                    currentOpenFile = selectedFile.getAbsolutePath();
                    // stage.setTitle(selectedFile.getName() + " - Teditor");
                } catch (IOException e) {
                    // do nothing
                }
            }
        });

        saveAsItem.setOnAction((ActionEvent ae) -> {
            File selectedFile = fileChooser.showSaveDialog(stage);

            if (selectedFile != null) {
                try (FileWriter fileWriter = new FileWriter(selectedFile);
                        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {

                    bufferedWriter.write(textArea.getText());
                } catch (IOException e) {
                    // do nothing
                }
            }
        });

    }

    void addFileExtensions() {
        fileChooser.getExtensionFilters().addAll(
                new ExtensionFilter("All Files", "*.*"),
                new ExtensionFilter("Text Files (*.txt)", "*.txt"),
                new ExtensionFilter("HTML Files (*.html, *.htm)", "*.html", "*.htm"),
                new ExtensionFilter("CSS Files (*.css)", "*.css"),
                new ExtensionFilter("Python Files (*.py)", "*.py"),
                new ExtensionFilter("Java Files (*.java)", "*.java"),
                new ExtensionFilter("JavaScript Files (*.js)", "*.js"),
                new ExtensionFilter("Markdown Files (*.md)", "*.md"),
                new ExtensionFilter("XML Files (*.xml)", "*.xml"));
    }

    void design() {
        // adding file menu items
        fileMenu.getItems().addAll(newItem, openItem, saveItem, saveAsItem, exitItem);

        // adding edit menu items
        editMenu.getItems().addAll(/*colorMenu,*/lineWrap, replace);
        colorMenu.getItems().addAll(fontColor, bgColor);
        lineWrap.getItems().addAll(lineWrapOn, lineWrapOff);

        // adding help menu items
        helpMenu.getItems().add(aboutItem);
        addFileExtensions();
    }

    void buttonFunctions() {

        newItem.setOnAction((ActionEvent ae) -> {
            textArea.clear();
            currentOpenFile = null;
        });

        exitItem.setOnAction((ActionEvent ae) -> {
            Platform.exit();
        });

        fontNames.setOnAction((ae) -> {
            textArea.setFont(Font.font(fontStyles.getValue().toString(), 15));
        });

        fontStyles.setOnAction((ae) -> {
            textArea.setFont(Font.font(fontStyles.getValue().toString(), (FontWeight) fontStyles.getValue(), 15));
        });

//        replace.setOnAction((ActionEvent ae) -> {
//
//            String[] words = JOptionPane
//                    .showInputDialog("Enter Word to find\n and what to replace it with\nSeparated by comma")
//                    .split(",");
//            String wordToFind = words[0].trim();
//            String replaceWith = words[1].trim();
//
//            // replacing the words
//            textArea.setText(textArea.getText().replaceAll(wordToFind, replaceWith));
//        });
        lineWrapOn.setOnAction((ActionEvent ae) -> {
            textArea.setWrapText(true);
        });

        lineWrapOff.setOnAction((ActionEvent ae) -> {
            textArea.setWrapText(false);
        });

        aboutItem.setOnAction((ActionEvent ae) -> {
            JOptionPane.showMessageDialog(null, "Teditor\nCopyright 2021\nVersion 0.1.0");
        });
    }
}
