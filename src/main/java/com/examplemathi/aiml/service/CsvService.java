package com.examplemathi.aiml.service;

import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class CsvService {

    private static final String CSV_FILE = "crop_data.csv"; // src/main/resources/

    public List<List<String>> search(String cropInput, String soilInput, String seasonInput) {

        // Normalize inputs
        cropInput = (cropInput != null && !cropInput.isEmpty()) ? cropInput.trim().toLowerCase() : null;
        soilInput = (soilInput != null && !soilInput.isEmpty()) ? soilInput.trim().toLowerCase() : null;
        seasonInput = (seasonInput != null && !seasonInput.isEmpty()) ? seasonInput.trim().toLowerCase() : null;

        List<List<String>> result = new ArrayList<>();

        // Load CSV from resources
        InputStream is = getClass().getClassLoader().getResourceAsStream(CSV_FILE);
        if (is == null) {
            throw new RuntimeException("CSV file not found in resources: " + CSV_FILE);
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            String line;
            br.readLine(); // Skip header

            while ((line = br.readLine()) != null) {
                String[] cols = line.split(",", -1); // preserve empty columns
                if (cols.length < 5) continue; // safety check

                String crop = cols[1].trim().toLowerCase();
                String season = cols[3].trim().toLowerCase();
                String soil = cols[4].trim().toLowerCase();

                boolean match = true;

                // Crop exact match
                if (cropInput != null) {
                    match = match && crop.contains(cropInput);
                }

                // Season exact match
                if (seasonInput != null) {
                    match = match && season.contains(seasonInput);
                }

                // Soil type flexible match
                if (soilInput != null) {
                    // Split soil CSV field by "/" to allow multiple matches
                    String[] soilParts = soil.split("/");
                    boolean soilMatch = false;
                    for (String s : soilParts) {
                        if (s.trim().equals(soilInput)) {
                            soilMatch = true;
                            break;
                        }
                    }
                    match = match && soilMatch;
                }

                if (match) {
                    List<String> rowList = new ArrayList<>();
                    for (String col : cols) rowList.add(col.trim());
                    result.add(rowList);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
