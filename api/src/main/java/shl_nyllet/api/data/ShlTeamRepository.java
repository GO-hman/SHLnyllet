package shl_nyllet.api.data;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import shl_nyllet.api.models.ShlTeam;

public interface ShlTeamRepository extends JpaRepository<ShlTeam, String> {

    @Query("select t.uuid from ShlTeam t")
    Set<String> findAllUuids();

}
