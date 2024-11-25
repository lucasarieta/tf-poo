package io.arieta.util;

import io.arieta.dados.drone.Drone;
import io.arieta.dados.transporte.Transporte;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CsvHandler {
    public List<Drone> readDronesFromCsv(String filePath) throws IOException {
        List<Drone> drones = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                // Adapte para seu formato de drone
                Drone drone = parseDrone(fields);
                drones.add(drone);
            }
        }
        return drones;
    }

    public List<Transporte> readTransportesFromCsv(String filePath) throws IOException {
        List<Transporte> transportes = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                // Adapte para seu formato de transporte
                Transporte transporte = parseTransporte(fields);
                transportes.add(transporte);
            }
        }
        return transportes;
    }

    private Drone parseDrone(String[] fields) {
        // Implemente o parsing de campos para criar objetos Drone
        return null; // Retorne um objeto Drone preenchido
    }

    private Transporte parseTransporte(String[] fields) {
        // Implemente o parsing de campos para criar objetos Transporte
        return null; // Retorne um objeto Transporte preenchido
    }
}
