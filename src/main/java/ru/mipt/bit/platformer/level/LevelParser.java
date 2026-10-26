package ru.mipt.bit.platformer.level;

import ru.mipt.bit.platformer.model.GridPosition;

import java.util.ArrayList;
import java.util.List;

public final class LevelParser {

    private static final char EMPTY = '_';
    private static final char TREE = 'T';
    private static final char PLAYER = 'X';
    private static final char BYTE_ORDER_MARK = '﻿';

    private LevelParser() {
    }

    public static LevelLayout parse(List<String> rawLines) {
        List<String> lines = withoutTrailingBlankLines(rawLines);
        if (lines.isEmpty()) {
            throw new IllegalArgumentException("Уровень пуст");
        }
        int height = lines.size();
        int width = lines.get(0).length();

        GridPosition playerStart = null;
        List<GridPosition> trees = new ArrayList<>();

        for (int row = 0; row < height; row++) {
            String line = lines.get(row);
            if (line.length() != width) {
                throw new IllegalArgumentException(
                        "Строка " + (row + 1) + " длиной " + line.length() + ", ожидалось " + width);
            }
            int y = height - 1 - row;
            for (int x = 0; x < width; x++) {
                char symbol = line.charAt(x);
                GridPosition cell = new GridPosition(x, y);
                if (symbol == TREE) {
                    trees.add(cell);
                } else if (symbol == PLAYER) {
                    if (playerStart != null) {
                        throw new IllegalArgumentException("Начальная позиция игрока указана больше одного раза");
                    }
                    playerStart = cell;
                } else if (symbol != EMPTY) {
                    throw new IllegalArgumentException(
                            "Неизвестный символ '" + symbol + "' в строке " + (row + 1) + ", столбце " + (x + 1));
                }
            }
        }

        if (playerStart == null) {
            throw new IllegalArgumentException("Не указана начальная позиция игрока '" + PLAYER + "'");
        }
        return new LevelLayout(width, height, playerStart, trees);
    }

    private static List<String> withoutTrailingBlankLines(List<String> rawLines) {
        List<String> lines = new ArrayList<>(rawLines);
        if (!lines.isEmpty() && !lines.get(0).isEmpty() && lines.get(0).charAt(0) == BYTE_ORDER_MARK) {
            lines.set(0, lines.get(0).substring(1));
        }
        while (!lines.isEmpty() && lines.get(lines.size() - 1).trim().isEmpty()) {
            lines.remove(lines.size() - 1);
        }
        return lines;
    }
}
