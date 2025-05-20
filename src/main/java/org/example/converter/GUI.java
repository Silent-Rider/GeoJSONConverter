package org.example.converter;

import org.example.converter.process.Transformer;
import org.example.converter.system.CoordinateSystem;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;

public class GUI extends JFrame {

    private final JComboBox<String> coordinateSystemComboBox;
    private final Transformer transformer;
    private final JTextField selectedFileLine;
    private final JTextField selectedSystemLine;
    private File selectedFile;
    private String selectedSystem;

    public GUI () {
        transformer = new Transformer();
        coordinateSystemComboBox = new JComboBox<>();
        for(CoordinateSystem coordinateSystem: CoordinateSystem.values()) {
            coordinateSystemComboBox.addItem(coordinateSystem.getName());
        }
        selectedFileLine = new JTextField("Файл: не выбран");
        selectedFileLine.setEditable(false);
        selectedSystemLine = new JTextField("Координатная система: ");
        selectedSystemLine.setEditable(false);

        setTitle("GeoJSONConverter");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false); // можно отключить изменение размера окна

        initComponents();
    }

    public static void main (String[] args) {
        new GUI().setVisible(true);
    }

    private void initComponents() {
        setLayout(null);
        selectedFileLine.setBounds(20, 20, 350, 30);
        add(selectedFileLine);

        JButton selectFileButton = new JButton("Выбрать файл");
        selectFileButton.setBounds(130, 60, 140, 30); // маленькая кнопка по центру
        selectFileButton.addActionListener(this::selectFileAction);
        add(selectFileButton);

        selectedSystemLine.setBounds(20, 120, 150, 30);
        coordinateSystemComboBox.setBounds(175, 120, 195, 30);
        coordinateSystemComboBox.addActionListener(a ->
                selectedSystem = (String) coordinateSystemComboBox.getSelectedItem()
        );
        add(selectedSystemLine);
        add(coordinateSystemComboBox);

        JButton startButton = getStartButton();
        add(startButton);
    }

    private JButton getStartButton() {
        JButton startButton = new JButton("Запустить");
        startButton.setBounds(100, 160, 200, 40);
        startButton.setBackground(Color.GREEN);
        startButton.setOpaque(true);
        startButton.setBorderPainted(false);
        startButton.addActionListener(a -> {
            if (selectedFile != null && selectedSystem != null && !selectedSystem.isEmpty()) {
                transformer.start(selectedFile, selectedSystem);
            }
            else {
                JOptionPane.showMessageDialog(this,
                        "Выбранный файл и координатная система не могут быть пустыми",
                        "Ошибка", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        return startButton;
    }

    private void selectFileAction(ActionEvent event) {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("GeoJSON Files", "geojson");
        fileChooser.setFileFilter(filter);

        int returnValue = fileChooser.showOpenDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            selectedFile = fileChooser.getSelectedFile();
            selectedFileLine.setText(selectedFileLine.getText().replace("не выбран", selectedFile.getName()));
        }
    }
}
