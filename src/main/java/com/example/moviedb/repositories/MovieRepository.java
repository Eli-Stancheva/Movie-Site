package com.example.moviedb.repositories;

import com.example.moviedb.models.entity.Actor;
import com.example.moviedb.models.entity.Category;
import com.example.moviedb.models.entity.Movie;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    Optional<Movie> findByTitle(String title);

    Optional<Movie> findById(Long id);

    @EntityGraph(attributePaths = {"actors"})
    List<Movie> findAll();

    List<Movie> findByCategoryId(Long categoryId);
    Movie findTopByOrderByReleaseDateDesc();
    List<Movie> findByTitleContainingIgnoreCase(String query);

    List<Movie> findByRatingGreaterThan(Double rating);

    @Query(value = "SELECT a.* FROM Actor a INNER JOIN Movie_Actor ma ON a.id = ma.actor_id WHERE ma.movie_id = :movieId", nativeQuery = true)
    List<Actor> findActorsByMovieId(@Param("movieId") Long movieId);

    List<Movie> findByCategoryAndIdNot(Category category, Long id);
    @Query("SELECT m FROM Movie m WHERE m.director.id = :directorId")
    List<Movie> findMoviesByDirectorId(Long directorId);

    @Query("SELECT m FROM Movie m WHERE YEAR(m.releaseDate) = :year")
    List<Movie> findByReleaseYear(@Param("year") int year);

//    List<Movie> findByTitleContainingIgnoreCaseOrActorsNameContainingIgnoreCaseOrCategoryNameContainingIgnoreCaseOrDirectorName(String title, String authorName, String categoryName, String directorName);

    List<Movie> findByTitleContainingIgnoreCaseOrActors_ActorNameContainingIgnoreCaseOrCategory_CategoryNameContainingIgnoreCaseOrDirector_DirectorNameContainingIgnoreCase(
            String title, String actorName, String categoryName, String directorName
    );
}