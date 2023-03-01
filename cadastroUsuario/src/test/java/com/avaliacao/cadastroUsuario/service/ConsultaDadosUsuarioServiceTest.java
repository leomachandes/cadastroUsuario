package com.avaliacao.cadastroUsuario.service;


import com.avaliacao.cadastroUsuario.controller.dto.UsuarioDtoSaida;
import com.avaliacao.cadastroUsuario.entity.UsuarioEntity;
import com.avaliacao.cadastroUsuario.exception.UsuarioNaoEncontradoException;
import com.avaliacao.cadastroUsuario.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.net.MalformedURLException;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class ConsultaDadosUsuarioServiceTest {

    @Mock
    private UsuarioRepository repository;

    @InjectMocks
    private ConsultaDadosUsuarioService service;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindByIdSucess() throws UsuarioNaoEncontradoException, MalformedURLException {
        Long id = 1L;
        UsuarioEntity entity = new UsuarioEntity(1L, "Leonardo", LocalDate.of(1987, 12, 31), null);
        when(repository.findById(id)).thenReturn(Optional.of(entity));

        UsuarioDtoSaida dto = service.findById(id);

        assertEquals(entity.getCodigo(), dto.getCodigo());
        assertEquals(entity.getNome(), dto.getNome());
        assertEquals(entity.getDataNascimento(), dto.getDataNascimento());
    }

    @Test
    public void testFindByIdUsuarioNaoEncontradoException() {
        Long id = 1L;
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(UsuarioNaoEncontradoException.class, () -> service.findById(id));

    }
}








