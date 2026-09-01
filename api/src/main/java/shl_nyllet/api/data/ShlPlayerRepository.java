package shl_nyllet.api.data;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import shl_nyllet.api.models.ShlPlayer;

public interface ShlPlayerRepository extends JpaRepository<ShlPlayer, String> {

    @Query(value = "SELECT * FROM shl_player WHERE url IS NOT NULL ORDER BY RANDOM() LIMIT 1", nativeQuery = true)
    ShlPlayer findRandomPlayer();

    @Query(value = "SELECT * FROM shl_player WHERE team_uuid = :teamId AND url IS NOT NULL ORDER BY RANDOM() LIMIT 1", nativeQuery = true)
    ShlPlayer findRandomPlayerByTeamUuid(@Param("teamId") String teamId);

    List<ShlPlayer> findAllByTeamUuid(String teamId);

    // List<ShlPlayer> findByTeamUuid(String teamId);
}
