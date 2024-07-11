package com.example.foro_hub.service;

import com.example.foro_hub.model.Topico;
import com.example.foro_hub.model.Usuario;
import com.example.foro_hub.repository.TopicoRepository;
import com.example.foro_hub.repository.UsuarioRepository;
import com.example.foro_hub.request.TopicoRequest;
import com.example.foro_hub.request.TopicoResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TopicoService {

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Topico crearTopico(TopicoRequest topicoRequest) {
        if (topicoRepository.existsByTituloAndMensaje(topicoRequest.getTitulo(), topicoRequest.getMensaje())) {
            throw new RuntimeException("El tópico con el mismo título y mensaje ya existe.");
        }

        Usuario autor = usuarioRepository.findByNombre(topicoRequest.getAutor())
                .orElseThrow(() -> new RuntimeException("Autor no encontrado"));

        Topico topico = new Topico();
        topico.setTitulo(topicoRequest.getTitulo());
        topico.setMensaje(topicoRequest.getMensaje());
        topico.setAutor(autor);
        topico.setCurso(topicoRequest.getCurso());

        return topicoRepository.save(topico);
    }

    public List<TopicoResponse> obtenerTodosLosTopicos() {
        return topicoRepository.findAll().stream()
                .map(topico -> new TopicoResponse(
                        topico.getTitulo(),
                        topico.getMensaje(),
                        topico.getFechaCreacion(),
                        topico.getEstado(),
                        topico.getAutor().getNombre(),
                        topico.getCurso()
                ))
                .collect(Collectors.toList());
    }

    public TopicoResponse obtenerTopicoPorId(Long id) {
        Topico topico = topicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tópico no encontrado"));

        return new TopicoResponse(
                topico.getTitulo(),
                topico.getMensaje(),
                topico.getFechaCreacion(),
                topico.getEstado(),
                topico.getAutor().getNombre(),
                topico.getCurso()
        );
    }

    public Topico actualizarTopico(Long id, TopicoRequest topicoRequest) {
        Optional<Topico> topicoExistenteOpt = topicoRepository.findById(id);
        if (!topicoExistenteOpt.isPresent()) {
            throw new RuntimeException("Tópico no encontrado");
        }

        Topico topicoExistente = topicoExistenteOpt.get();

        // Verificar si el nuevo título y mensaje ya existen
        if (topicoRepository.existsByTituloAndMensaje(topicoRequest.getTitulo(), topicoRequest.getMensaje())) {
            throw new RuntimeException("El tópico con el mismo título y mensaje ya existe.");
        }

        topicoExistente.setTitulo(topicoRequest.getTitulo());
        topicoExistente.setMensaje(topicoRequest.getMensaje());
        topicoExistente.setCurso(topicoRequest.getCurso());

        return topicoRepository.save(topicoExistente);
    }

    public void eliminarTopico(Long id) {
        Optional<Topico> topicoExistenteOpt = topicoRepository.findById(id);
        if (!topicoExistenteOpt.isPresent()) {
            throw new RuntimeException("Tópico no encontrado");
        }

        topicoRepository.deleteById(id);
    }
}
