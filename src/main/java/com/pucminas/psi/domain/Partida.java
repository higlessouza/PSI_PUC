package com.pucminas.psi.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Partida.
 */
@Entity
@Table(name = "partida")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "partida")
public class Partida implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "mandante_id")
    private Integer mandanteID;

    @Column(name = "visitante_id")
    private Integer visitanteID;

    @Column(name = "gols_visitante")
    private Integer golsVisitante;

    @Column(name = "gols_mandante")
    private Integer golsMandante;

    @Column(name = "local")
    private String local;

    @Column(name = "campeonato")
    private Integer campeonato;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getMandanteID() {
        return mandanteID;
    }

    public Partida mandanteID(Integer mandanteID) {
        this.mandanteID = mandanteID;
        return this;
    }

    public void setMandanteID(Integer mandanteID) {
        this.mandanteID = mandanteID;
    }

    public Integer getVisitanteID() {
        return visitanteID;
    }

    public Partida visitanteID(Integer visitanteID) {
        this.visitanteID = visitanteID;
        return this;
    }

    public void setVisitanteID(Integer visitanteID) {
        this.visitanteID = visitanteID;
    }

    public Integer getGolsVisitante() {
        return golsVisitante;
    }

    public Partida golsVisitante(Integer golsVisitante) {
        this.golsVisitante = golsVisitante;
        return this;
    }

    public void setGolsVisitante(Integer golsVisitante) {
        this.golsVisitante = golsVisitante;
    }

    public Integer getGolsMandante() {
        return golsMandante;
    }

    public Partida golsMandante(Integer golsMandante) {
        this.golsMandante = golsMandante;
        return this;
    }

    public void setGolsMandante(Integer golsMandante) {
        this.golsMandante = golsMandante;
    }

    public String getLocal() {
        return local;
    }

    public Partida local(String local) {
        this.local = local;
        return this;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public Integer getCampeonato() {
        return campeonato;
    }

    public Partida campeonato(Integer campeonato) {
        this.campeonato = campeonato;
        return this;
    }

    public void setCampeonato(Integer campeonato) {
        this.campeonato = campeonato;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Partida)) {
            return false;
        }
        return id != null && id.equals(((Partida) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Partida{" +
            "id=" + getId() +
            ", mandanteID=" + getMandanteID() +
            ", visitanteID=" + getVisitanteID() +
            ", golsVisitante=" + getGolsVisitante() +
            ", golsMandante=" + getGolsMandante() +
            ", local='" + getLocal() + "'" +
            ", campeonato=" + getCampeonato() +
            "}";
    }
}
