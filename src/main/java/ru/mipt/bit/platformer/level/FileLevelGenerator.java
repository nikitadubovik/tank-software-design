package ru.mipt.bit.platformer.level;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class FileLevelGenerator implements LevelGenerator {

    private final String resourceName;

    public FileLevelGenerator(String resourceName) {
        this.resourceName = resourceName;
    }

    @Override
    public LevelLayout generate() {
        return LevelParser.parse(readLines());
    }

    private List<String> readLines() {
        InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(resourceName);
        if (stream == null) {
            throw new IllegalArgumentException("Файл уровня не найден: " + resourceName);
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
            List<String> lines = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
            return lines;
        } catch (IOException e) {
            throw new UncheckedIOException("Не удалось прочитать файл уровня: " + resourceName, e);
        }
    }
}
