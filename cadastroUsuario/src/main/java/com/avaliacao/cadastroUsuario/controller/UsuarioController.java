package com.avaliacao.cadastroUsuario.controller;

import com.avaliacao.cadastroUsuario.controller.dto.UsuarioDtoEntrada;
import com.avaliacao.cadastroUsuario.controller.dto.UsuarioDtoSaida;
import com.avaliacao.cadastroUsuario.exception.UsuarioNaoEncontradoException;
import com.avaliacao.cadastroUsuario.service.AlteraDadosUsuario;
import com.avaliacao.cadastroUsuario.service.ConsultaDadosUsuarios;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.io.IOException;
import java.net.URI;
import java.util.logging.Logger;

@RestController
@RequestMapping("/usuarios")
@Validated
public class UsuarioController {

    private static final Logger log = Logger.getLogger(UsuarioController.class.getName());

    @Autowired
    private ConsultaDadosUsuarios consultarService;

    @Autowired
    private AlteraDadosUsuario alteraDadosService;

    @GetMapping("/cadastro")
    public String teste() {
        return "Teste";
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarDadosUsuarioPorId(@PathVariable  @Min(value = 1, message = "Id precisa ser maior que 0") Long id) {
        try {
            UsuarioDtoSaida dto = consultarService.findById(id);
            log.info("Consulta realizada com sucesso!");
            return ResponseEntity.ok(dto);
        } catch (UsuarioNaoEncontradoException ex) {
            log.warning(ex.getMessage());
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.severe("Erro interno na consulta: " + e.getMessage());
            return ResponseEntity.internalServerError().body(e);
        }

    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?>cadastrar(@Valid @RequestParam("usuario") String dadosEntrada, @RequestParam MultipartFile foto) {
        try {
            ObjectMapper mapper = JsonMapper.builder().addModule(new JavaTimeModule()).build();
            UsuarioDtoEntrada usuarioDtoEntrada = mapper.readValue(dadosEntrada, UsuarioDtoEntrada.class);
            Long codigo = alteraDadosService.inserir(usuarioDtoEntrada, foto);
            log.info("Usuário cadastrado com sucesso!");
            return ResponseEntity.created(URI.create("/usuarios/" + codigo)).build();
        } catch (Exception e) {
            log.severe("Erro interno na inserção: " + e.getMessage());
            return ResponseEntity.internalServerError().body(e);
        }
    }


    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?>alterar(@Valid @RequestBody UsuarioDtoEntrada dtoEntrada, @PathVariable @Min(value = 1, message = "Id precisa ser maior que 0") Long id) {

        try {
            UsuarioDtoSaida dto = alteraDadosService.alterar( dtoEntrada, id);
            log.info("Alteração realizada com sucesso!");
            return ResponseEntity.ok(dto);
        } catch (UsuarioNaoEncontradoException ex) {
            log.warning(ex.getMessage());
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.severe("Erro interno na alteração: " + e.getMessage());
            return ResponseEntity.internalServerError().body(e);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable @Min(value = 1, message = "Id precisa ser maior que 0") Long id) {

        try {
            alteraDadosService.excluir(id);
            log.info("Exclusão realizada com sucesso!");
            return ResponseEntity.ok("Exclusão realizada com sucesso!");
        } catch (UsuarioNaoEncontradoException ex) {
            log.warning(ex.getMessage());
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.severe("Erro interno na exclusão: " + e.getMessage());
            return ResponseEntity.internalServerError().body(e);
        }

    }

    @PatchMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?>alterarFotoUsuario(@RequestParam MultipartFile foto, @PathVariable @Min(value = 1, message = "Id precisa ser maior que 0") Long id) throws  IOException {
        try {
            alteraDadosService.alteraFotoUsuario(foto, id);
            log.info("Foto alterada com sucesso!");
            return ResponseEntity.ok("Foto alterada com sucesso!");
        }catch (UsuarioNaoEncontradoException ex) {
            log.warning(ex.getMessage());
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.severe("Erro interno na exclusão: " + e.getMessage());
            return ResponseEntity.internalServerError().body(e);
        }

    }

}
