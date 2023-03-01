package com.avaliacao.cadastroUsuario.service;

import com.avaliacao.cadastroUsuario.controller.dto.UsuarioDtoSaida;
import com.avaliacao.cadastroUsuario.entity.UsuarioEntity;
import com.avaliacao.cadastroUsuario.exception.UsuarioNaoEncontradoException;
import com.avaliacao.cadastroUsuario.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Service
public class ConsultaDadosUsuarioService implements  ConsultaDadosUsuarios {

    @Autowired
    private UsuarioRepository repository;

    @Override
    public UsuarioDtoSaida findById(Long id) throws UsuarioNaoEncontradoException, MalformedURLException {

        Optional<UsuarioEntity> optional = repository.findById(id);
        validaSeUsuarioFoiEncontrado(optional.isPresent());
        UsuarioEntity entity = optional.get();

        UsuarioDtoSaida dto = new UsuarioDtoSaida(entity.getCodigo(), entity.getNome(), entity.getDataNascimento());
        return dto;
    }

    @Override
    public byte[] buscarFotoUsuario(Long id) throws UsuarioNaoEncontradoException, IOException {

        Optional<UsuarioEntity> optional = repository.findById(id);
        validaSeUsuarioFoiEncontrado(optional.isPresent());
        UsuarioEntity entity = optional.get();

        return buscarFotoUsuario(entity.getFoto());
    }

    private void validaSeUsuarioFoiEncontrado(boolean encontradaNaBaseDeDados) throws UsuarioNaoEncontradoException {

        if (encontradaNaBaseDeDados) return;

        throw new UsuarioNaoEncontradoException("{usuario.nao.encontrado}");

    }

    private byte[] buscarFotoUsuario(String dirFoto) throws IOException {

        Path path = Paths.get(dirFoto);
        return Files.readAllBytes(path);

    }

}
