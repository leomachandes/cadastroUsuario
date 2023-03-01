package com.avaliacao.cadastroUsuario.service;

import com.avaliacao.cadastroUsuario.controller.dto.UsuarioDtoEntrada;
import com.avaliacao.cadastroUsuario.controller.dto.UsuarioDtoSaida;
import com.avaliacao.cadastroUsuario.exception.UsuarioNaoEncontradoException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface AlteraDadosUsuario {

    Long inserir(UsuarioDtoEntrada dto, MultipartFile foto) throws IOException;

    UsuarioDtoSaida alterar(UsuarioDtoEntrada dto, Long id) throws UsuarioNaoEncontradoException;

    void excluir(Long id) throws UsuarioNaoEncontradoException;

    void alteraFotoUsuario(MultipartFile foto, Long id) throws UsuarioNaoEncontradoException, IOException;
}
