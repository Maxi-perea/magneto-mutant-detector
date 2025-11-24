package com.magneto.mutant_detector.service;

import org.springframework.stereotype.Service;

@Service
public class MutantService {

    // Constantes para mejor legibilidad y mantenimiento
    private static final int SEQUENCE_LENGTH = 4;
    private static final int MUTANT_SEQUENCE_LIMIT = 1; // Necesitamos MÁS de 1, es decir, si encontramos 2, es mutante.

    public boolean isMutant(String[] dna) {
        // 1. Validaciones básicas (Matriz NxN, caracteres válidos, no nulos)
        if (!isValidDna(dna)) {
            throw new IllegalArgumentException("ADN inválido: Debe ser NxN y contener solo A, T, C, G");
        }

        int n = dna.length;
        int sequencesFound = 0;

        // Recorremos la matriz
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {

                // Optimizaciones: Solo comprobamos si es posible formar una secuencia desde esta posición
                // Comprobación Horizontal
                if (j + SEQUENCE_LENGTH <= n) {
                    if (checkHorizontal(dna, i, j)) {
                        sequencesFound++;
                        if (sequencesFound > MUTANT_SEQUENCE_LIMIT) return true; // Salida temprana
                    }
                }

                // Comprobación Vertical
                if (i + SEQUENCE_LENGTH <= n) {
                    if (checkVertical(dna, i, j)) {
                        sequencesFound++;
                        if (sequencesFound > MUTANT_SEQUENCE_LIMIT) return true; // Salida temprana
                    }
                }

                // Comprobación Diagonal (Principal)
                if (i + SEQUENCE_LENGTH <= n && j + SEQUENCE_LENGTH <= n) {
                    if (checkDiagonal(dna, i, j)) {
                        sequencesFound++;
                        if (sequencesFound > MUTANT_SEQUENCE_LIMIT) return true; // Salida temprana
                    }
                }

                // Comprobación Diagonal (Inversa / Anti-diagonal)
                if (i + SEQUENCE_LENGTH <= n && j - SEQUENCE_LENGTH + 1 >= 0) {
                    if (checkAntiDiagonal(dna, i, j)) {
                        sequencesFound++;
                        if (sequencesFound > MUTANT_SEQUENCE_LIMIT) return true; // Salida temprana
                    }
                }
            }
        }

        return false; // No se encontraron suficientes secuencias
    }

    // --- Métodos auxiliares de verificación ---

    private boolean checkHorizontal(String[] dna, int row, int col) {
        char base = dna[row].charAt(col);
        for (int k = 1; k < SEQUENCE_LENGTH; k++) {
            if (dna[row].charAt(col + k) != base) return false;
        }
        return true;
    }

    private boolean checkVertical(String[] dna, int row, int col) {
        char base = dna[row].charAt(col);
        for (int k = 1; k < SEQUENCE_LENGTH; k++) {
            if (dna[row + k].charAt(col) != base) return false;
        }
        return true;
    }

    private boolean checkDiagonal(String[] dna, int row, int col) {
        char base = dna[row].charAt(col);
        for (int k = 1; k < SEQUENCE_LENGTH; k++) {
            if (dna[row + k].charAt(col + k) != base) return false;
        }
        return true;
    }

    private boolean checkAntiDiagonal(String[] dna, int row, int col) {
        char base = dna[row].charAt(col);
        for (int k = 1; k < SEQUENCE_LENGTH; k++) {
            if (dna[row + k].charAt(col - k) != base) return false;
        }
        return true;
    }

    // Validación de estructura y contenido
    private boolean isValidDna(String[] dna) {
        if (dna == null || dna.length == 0) return false;
        int n = dna.length;
        for (String row : dna) {
            if (row == null || row.length() != n) return false; // Debe ser NxN
            if (!row.matches("[ATCG]+")) return false; // Solo bases nitrogenadas válidas
        }
        return true;
    }
}