package com.avaliacao.cadastroUsuario.service;

import com.avaliacao.cadastroUsuario.controller.dto.UsuarioDtoEntrada;
import com.avaliacao.cadastroUsuario.controller.dto.UsuarioDtoSaida;
import com.avaliacao.cadastroUsuario.entity.UsuarioEntity;
import com.avaliacao.cadastroUsuario.exception.UsuarioNaoEncontradoException;
import com.avaliacao.cadastroUsuario.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class AlteraDadosServiceTest {

    @Mock
    private UsuarioRepository repository;

    @InjectMocks
    private AlteraDadosService service;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testInserir() throws IOException {

        String dirFoto = "/foto1.jpg";

        UsuarioDtoEntrada dto = new UsuarioDtoEntrada();
        dto.setNome("Leonardo");
        dto.setDataNascimento(LocalDate.of(1987, 12, 31));
        MockMultipartFile foto = new MockMultipartFile("foto1.jpg", "foto1.jpg", null, new byte[]{});

        UsuarioEntity entity = new UsuarioEntity();
        entity.setCodigo(1L);

        when(repository.save(any(UsuarioEntity.class))).thenReturn(entity);

        Long codigo = service.inserir(dto, foto);

        assertEquals(entity.getCodigo(), codigo);

        File file = new File("/fotos" + dirFoto);
        if (file.exists()) file.delete();

    }

    @Test
    public void  testAlterarSucess() throws UsuarioNaoEncontradoException {

        Long codigo = 1L;

        UsuarioEntity entity = new UsuarioEntity();
        entity.setCodigo(codigo);
        entity.setNome("Leonardo");
        Optional<UsuarioEntity> optional = Optional.of(entity);

        UsuarioDtoEntrada dtoEntrada = new UsuarioDtoEntrada();
        dtoEntrada.setNome("Maria");

        UsuarioEntity entityAlterado = new UsuarioEntity();
        entityAlterado.setCodigo(codigo);
        entityAlterado.setNome(dtoEntrada.getNome());

        when(repository.findById(codigo)).thenReturn(optional);
        when(repository.save(entity)).thenReturn(entityAlterado);

        UsuarioDtoSaida dtoSaida = service.alterar(dtoEntrada, codigo);

        assertEquals(dtoEntrada.getNome(), dtoSaida.getNome());

    }

    @Test
    public void testAlterarUsuarioNaoEncontradoException() {

        Long codigo = 1L;
        when(repository.findById(codigo)).thenReturn(Optional.empty());
        assertThrows(UsuarioNaoEncontradoException.class, () -> service.alterar(new UsuarioDtoEntrada(), codigo));

    }

    @Test
    public void testExcluirSucess() throws UsuarioNaoEncontradoException {

        Long codigo = 1L;

        UsuarioEntity entity = new UsuarioEntity();
        entity.setCodigo(codigo);
        entity.setNome("Leonardo");
        entity.setFoto("/foto1.jpg");
        Optional<UsuarioEntity> optional = Optional.of(entity);

        when(repository.findById(codigo)).thenReturn(optional);
        service.excluir(codigo);
        verify(repository).delete(entity);

    }

    @Test
    public void testExcluirUsuarioNaoEncontradoException() {

        Long codigo = 1L;
        when(repository.findById(codigo)).thenReturn(Optional.empty());
        assertThrows(UsuarioNaoEncontradoException.class, () -> service.excluir(codigo));

    }

    @Test
    public void testAlteraFotoUsuarioSucess() throws UsuarioNaoEncontradoException, IOException {

        Long codigo = 1L;

        UsuarioEntity entity = new UsuarioEntity();
        entity.setCodigo(codigo);
        entity.setNome("Leonardo");
        entity.setFoto("/fotos/foto01.jpg");
        Optional<UsuarioEntity> optional = Optional.of(entity);

        MockMultipartFile novaFoto = new MockMultipartFile("foto2.jpg", "foto2.jpg", null, new byte[]{});

        when(repository.findById(codigo)).thenReturn(optional);
        when(repository.save(entity)).thenReturn(entity);

        service.alteraFotoUsuario(novaFoto, codigo);

        assertEquals(entity.getFoto(), "/fotos/" + novaFoto.getOriginalFilename());

    }

    @Test
    public void testAlteraFotoUsuarioNaoEncontradoException() {

        Long codigo = 1L;

        when(repository.findById(codigo)).thenReturn(Optional.empty());
        assertThrows(UsuarioNaoEncontradoException.class, () -> service.alteraFotoUsuario(new MockMultipartFile("foto2.jpg", "foto2.jpg", null, new byte[]{}), codigo));

    }


}
