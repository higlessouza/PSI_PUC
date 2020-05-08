package com.pucminas.psi.repository;

import com.pucminas.psi.domain.Partida;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Partida entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PartidaRepository extends JpaRepository<Partida, Long> {
}
