package com.example.moviedb.repositories;

import com.example.moviedb.models.entity.Actor;
import com.example.moviedb.models.entity.Category;
import com.example.moviedb.models.entity.Movie;
import com.example.moviedb.models.entity.TVSeries;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TVSeriesRepository extends JpaRepository<TVSeries, Long> {
    Optional<TVSeries> findByTitle(String title);

    List<TVSeries> findAll();

    List<TVSeries> findByCategoryId(Long categoryId);
    @Query(value = "SELECT a.* FROM Actor a INNER JOIN TVSeries_Actor sa ON a.id = sa.actor_id WHERE sa.tvSeries_id = :tvSeriesId", nativeQuery = true)
    List<Actor> findActorsByTVSeriesId(@Param("tvSeriesId") Long tvSeriesId);

    List<TVSeries> findByTitleContainingIgnoreCase(String query);

    List<TVSeries> findByCategoryAndIdNot(Category category, Long id);

    @Query("SELECT s FROM TVSeries s WHERE s.director.id = :directorId")
    List<TVSeries> findSeriesByDirectorId(Long directorId);

    @Query("SELECT s FROM TVSeries s WHERE YEAR(s.releaseDate) = :year")
    List<TVSeries> findSeriesByReleaseYear(@Param("year") int year);

    List<TVSeries> findByTitleContainingIgnoreCaseOrActors_ActorNameContainingIgnoreCaseOrCategory_CategoryNameContainingIgnoreCaseOrDirector_DirectorNameContainingIgnoreCase(
            String title, String actorName, String categoryName, String directorName
    );
}
