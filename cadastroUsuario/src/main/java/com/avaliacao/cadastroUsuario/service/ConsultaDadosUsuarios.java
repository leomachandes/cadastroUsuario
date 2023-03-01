package com.avaliacao.cadastroUsuario.service;


import com.avaliacao.cadastroUsuario.controller.dto.UsuarioDtoSaida;
import com.avaliacao.cadastroUsuario.exception.UsuarioNaoEncontradoException;

import java.io.IOException;

public interface ConsultaDadosUsuarios {

    UsuarioDtoSaida findById(Long id) throws Exception;


    byte[] buscarFotoUsuario(Long id) throws UsuarioNaoEncontradoException, IOException;
}
