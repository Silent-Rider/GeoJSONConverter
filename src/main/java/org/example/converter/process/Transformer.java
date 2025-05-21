package org.example.converter.process;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.converter.process.json.Feature;
import org.example.converter.process.json.FeatureCollection;
import org.example.converter.process.json.Geometry;
import org.example.converter.system.AsdCoordinatesConverter;
import org.example.converter.system.Converter;
import org.example.converter.system.CoordinateSystem;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Transformer {

    private final ObjectMapper objectMapper;
    private Converter converter;

    public Transformer () {
        objectMapper = new ObjectMapper();
    }

    public void start(File file, CoordinateSystem system) {
        switch (system) {
            case BGOK -> converter = new AsdCoordinatesConverter();
        }
        FeatureCollection featureCollection = null;
        try {
            String fileContent = readFile(file);
            if(!fileContent.isEmpty()) {
                featureCollection = objectMapper.readValue(fileContent,
                        FeatureCollection.class);
            }
            if(featureCollection == null)
                throw new RuntimeException("Возникла ошибка при десериализации");
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Возникла ошибка при десериализации. " + e.getMessage(), e);
        }
        for (Feature feature : featureCollection.getFeatures()) {
            Geometry geometry = feature.getGeometry();
            List<List<Double>> updatedCoordinates = geometry.getCoordinates().stream()
                    .map(coordinates -> {
                        List<Double> transformed = new ArrayList<>();
                        if (coordinates.size() > 1){
                            Converter.Point point = converter.convertToBL(coordinates.get(0), coordinates.get(1));
                            transformed.add(point.getLongitude());
                            transformed.add(point.getLatitude());
                            transformed.add(0.0);
                        }
                        else throw new RuntimeException("Слишком мало координат (< 2)");
                        return transformed;
                    })
                    .collect(Collectors.toList());

            geometry.setCoordinates(updatedCoordinates);
        }
        try {
            objectMapper.writeValue(new File(file.getName()), featureCollection);
        } catch (IOException e) {
            throw new RuntimeException("Возникла ошибка при записи в файл", e);
        }
    }

    public String readFile(File file) {
        Path path = file.toPath();
        String fileContent = "";
        try {
            fileContent = Files.readString(path);
        } catch (IOException e) {
            throw new RuntimeException("Возникла ошибка при чтении файла", e);
        }
        return fileContent;
    }
}
