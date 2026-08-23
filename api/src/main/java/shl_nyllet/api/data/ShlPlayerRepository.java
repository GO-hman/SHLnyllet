package shl_nyllet.api.data;

import org.springframework.data.jpa.repository.JpaRepository;

import shl_nyllet.api.models.ShlPlayer;

public interface ShlPlayerRepository extends JpaRepository<ShlPlayer, String> {

}
