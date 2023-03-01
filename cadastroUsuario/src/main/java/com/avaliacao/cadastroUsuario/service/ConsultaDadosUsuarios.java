package com.avaliacao.cadastroUsuario.service;


import com.avaliacao.cadastroUsuario.controller.dto.UsuarioDtoSaida;

public interface ConsultaDadosUsuarios {

    UsuarioDtoSaida findById(Long id) throws Exception;


}
