package com.example.moviedb.repositories;

import com.example.moviedb.models.entity.RatingFromUser;
import com.example.moviedb.models.entity.SeriesRatingFromUser;
import com.example.moviedb.models.entity.TVSeries;
import com.example.moviedb.models.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RatingSeriesFromUserRepository extends JpaRepository<SeriesRatingFromUser, Long> {
    SeriesRatingFromUser findByTvSeriesAndUser(TVSeries tvSeries, User user);
    SeriesRatingFromUser findByTvSeriesIdAndUserId(Long tvSeriesId, Long userId);
    List<SeriesRatingFromUser> findByTvSeriesId(Long tvSeriesId);
    List<SeriesRatingFromUser> findByUser(User user);
}
