package com.pucminas.psi.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.pucminas.psi.domain.Time} entity.
 */
public class TimeDTO implements Serializable {
    
    private Long id;

    @NotNull
    private String nome;

    @NotNull
    private String escudo;

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEscudo() {
        return escudo;
    }

    public void setEscudo(String escudo) {
        this.escudo = escudo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TimeDTO timeDTO = (TimeDTO) o;
        if (timeDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), timeDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TimeDTO{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", escudo='" + getEscudo() + "'" +
            "}";
    }
}
