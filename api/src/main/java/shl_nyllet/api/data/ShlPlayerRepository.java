package shl_nyllet.api.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import shl_nyllet.api.models.ShlPlayer;

public interface ShlPlayerRepository extends JpaRepository<ShlPlayer, String> {

    @Query(value = "SELECT * FROM shl_player WHERE url IS NOT NULL ORDER BY RANDOM() LIMIT 1", nativeQuery = true)
    ShlPlayer findRandomPlayer();
}
