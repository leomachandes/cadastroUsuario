package com.avaliacao.cadastroUsuario.repository;

import com.avaliacao.cadastroUsuario.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {
}
