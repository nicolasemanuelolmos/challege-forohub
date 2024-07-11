package com.example.foro_hub.controllers;

import com.example.foro_hub.model.Topico;
import com.example.foro_hub.request.TopicoRequest;
import com.example.foro_hub.request.TopicoResponse;
import com.example.foro_hub.service.TopicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/topicos")
public class TopicoController {

    @Autowired
    private TopicoService topicoService;

    @PostMapping
    public ResponseEntity<Topico> crearTopico(@RequestBody @Valid TopicoRequest topicoRequest) {
        Topico nuevoTopico = topicoService.crearTopico(topicoRequest);
        return new ResponseEntity<>(nuevoTopico, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<TopicoResponse>> obtenerTodosLosTopicos() {
        List<TopicoResponse> topicos = topicoService.obtenerTodosLosTopicos();
        return new ResponseEntity<>(topicos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TopicoResponse> obtenerTopicoPorId(@PathVariable Long id) {
        TopicoResponse topico = topicoService.obtenerTopicoPorId(id);
        return new ResponseEntity<>(topico, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Topico> actualizarTopico(@PathVariable Long id, @RequestBody @Valid TopicoRequest topicoRequest) {
        Topico actualizadoTopico = topicoService.actualizarTopico(id, topicoRequest);
        return new ResponseEntity<>(actualizadoTopico, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarTopico(@PathVariable Long id) {
        topicoService.eliminarTopico(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
