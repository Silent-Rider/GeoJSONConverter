package org.example.converter;

import org.example.converter.process.Transformer;
import org.example.converter.system.CoordinateSystem;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;

public class GUI extends JFrame {

    private static final String NOT_CHOSEN = "не выбран";

    private final JComboBox<String> coordinateSystemComboBox;
    private final Transformer transformer;
    private final JTextField selectedFileLine;
    private final JTextField selectedSystemLine;
    private File selectedFile;
    private CoordinateSystem selectedSystem;

    public GUI () {
        transformer = new Transformer();
        coordinateSystemComboBox = new JComboBox<>();
        for(CoordinateSystem coordinateSystem: CoordinateSystem.values()) {
            coordinateSystemComboBox.addItem(coordinateSystem.getName());
        }
        coordinateSystemComboBox.setSelectedItem(null);

        selectedFileLine = new JTextField("Файл: " + NOT_CHOSEN);
        selectedFileLine.setEditable(false);
        selectedSystemLine = new JTextField("Координатная система: ");
        selectedSystemLine.setEditable(false);

        setTitle("GeoJSONConverter");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

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
        selectFileButton.setBounds(130, 60, 140, 30);
        selectFileButton.addActionListener(this::selectFileAction);
        add(selectFileButton);

        selectedSystemLine.setBounds(20, 120, 150, 30);
        coordinateSystemComboBox.setBounds(175, 120, 195, 30);
        coordinateSystemComboBox.addActionListener(a -> {
                    selectedSystem = CoordinateSystem.values()[coordinateSystemComboBox.getSelectedIndex()];
            }
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
            if (selectedFile != null && selectedSystem != null) {
                try {
                    transformer.start(selectedFile, selectedSystem);
                    JOptionPane.showMessageDialog(this, "Файл успешно отконвертирован",
                            "Уведомление", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this,
                            "Ошибка: " + e.getMessage(),
                            "Ошибка", JOptionPane.ERROR_MESSAGE);
                }
            }
            else if (selectedFile == null) {
                JOptionPane.showMessageDialog(this,
                        "Вы не выбрали файл",
                        "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
            else {
                JOptionPane.showMessageDialog(this,
                        "Вы не выбрали координатную систему",
                        "Ошибка", JOptionPane.ERROR_MESSAGE);
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
            selectedFileLine.setText(selectedFileLine.getText().replace(NOT_CHOSEN, selectedFile.getName()));
        }
    }
}
