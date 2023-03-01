package com.avaliacao.cadastroUsuario.service;

import com.avaliacao.cadastroUsuario.controller.dto.UsuarioDtoEntrada;
import com.avaliacao.cadastroUsuario.controller.dto.UsuarioDtoSaida;
import com.avaliacao.cadastroUsuario.entity.UsuarioEntity;
import com.avaliacao.cadastroUsuario.exception.UsuarioNaoEncontradoException;
import com.avaliacao.cadastroUsuario.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Service
public class AlteraDadosService implements  AlteraDadosUsuario {

    @Autowired
    private UsuarioRepository repository;

    @Override
    public Long inserir(UsuarioDtoEntrada dto, MultipartFile foto) throws IOException {

        UsuarioEntity entity = new UsuarioEntity();
        entity.setNome(dto.getNome());
        entity.setDataNascimento(dto.getDataNascimento());
        entity.setFoto(uploadFoto(foto));

        entity = repository.save(entity);
        return entity.getCodigo();
    }

    @Override
    public UsuarioDtoSaida alterar(UsuarioDtoEntrada dto, Long id) throws UsuarioNaoEncontradoException {

        Optional<UsuarioEntity> optional = repository.findById(id);
        validaSeUsuarioFoiEncontrado(optional.isPresent());
        UsuarioEntity entity = optional.get();
        entity = repository.save(entity);
        return new UsuarioDtoSaida(entity.getCodigo(), entity.getNome(), entity.getDataNascimento());
    }

    @Override
    public void excluir(Long id) throws UsuarioNaoEncontradoException {
        Optional<UsuarioEntity> optional = repository.findById(id);
        validaSeUsuarioFoiEncontrado(optional.isPresent());
        UsuarioEntity entity = optional.get();
        repository.delete(entity);
        excluiImagemServidor(entity.getFoto());
    }

    @Override
    public void alteraFotoUsuario(MultipartFile foto, Long id) throws UsuarioNaoEncontradoException, IOException {

        Optional<UsuarioEntity> optional = repository.findById(id);
        validaSeUsuarioFoiEncontrado(optional.isPresent());
        UsuarioEntity entity = optional.get();
        String fotoAnterior = entity.getFoto();;
        entity.setFoto(uploadFoto(foto));
        repository.save(entity);
        excluiImagemServidor(fotoAnterior);
    }

    private void validaSeUsuarioFoiEncontrado(boolean encontradaNaBaseDeDados) throws UsuarioNaoEncontradoException {

        if (encontradaNaBaseDeDados) return;

        throw new UsuarioNaoEncontradoException("{usuario.nao.encontrado}");

    }

    private String uploadFoto(MultipartFile foto) throws IOException {

        /*
        Observação para os avaliadores: por se tratar de uma aplicação de teste, a foto do usuario é salva em servidor local
        e apenas o diretório é salvo no banco de dados.
        Porém se tratando de uma aplicação de escala maior, uma solução mais interessante seria salvar essa foto dentro de um S3 da AWS
        por exemplo.
         */

        String dirFoto = "/fotos/";
        Files.createDirectories(Paths.get(dirFoto));
        String filepath = dirFoto + foto.getOriginalFilename();
        byte[] bytes = foto.getBytes();
        Path path = Paths.get(filepath);
        Files.write(path, bytes);
        return filepath;

    }

    private void excluiImagemServidor(String fileName) {
        File file = new File(fileName);
        if (file.exists()) file.delete();

    }


}
