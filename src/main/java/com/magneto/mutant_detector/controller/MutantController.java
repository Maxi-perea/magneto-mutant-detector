package com.magneto.mutant_detector.controller;

import com.magneto.mutant_detector.dto.DnaRequest;
import com.magneto.mutant_detector.service.MutantService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mutant")
public class MutantController {

    private final MutantService mutantService;

    public MutantController(MutantService mutantService) {
        this.mutantService = mutantService;
    }

    // Mapea a POST /mutant/
    @PostMapping("/")
    public ResponseEntity<Void> detectMutant(@Valid @RequestBody DnaRequest dnaRequest) {
        try {
            boolean isMutant = mutantService.isMutant(dnaRequest.getDna());

            if (isMutant) {
                // Requisito: HTTP 200 OK si es mutante [cite: 54]
                return ResponseEntity.ok().build();
            } else {
                // Requisito: HTTP 403 Forbidden si no es mutante [cite: 55]
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        } catch (IllegalArgumentException e) {
            // Manejo de error si el ADN tiene caracteres inválidos o estructura incorrecta
            return ResponseEntity.badRequest().build();
        }
    }
}