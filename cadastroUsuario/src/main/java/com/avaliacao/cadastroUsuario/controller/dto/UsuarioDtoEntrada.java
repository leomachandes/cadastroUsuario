package com.avaliacao.cadastroUsuario.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
public class UsuarioDtoEntrada {

    @NotBlank(message = "Favor informar o nome do usuário")
    @JsonProperty(value = "nome")
    private String nome;

    @NotNull(message = "Favor informar a data de Nascimento" )
    @JsonProperty(value = "data_nascimento")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dataNascimento;


}
