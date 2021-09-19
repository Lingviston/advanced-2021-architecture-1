package ru.gaket.themoviedb.data.movies.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface MoviesDao {

    @Query("SELECT * FROM movies WHERE id=:id LIMIT 1")
    suspend fun getById(id: Int): MovieEntity?

    @Insert(entity = MovieEntity::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(searchedMovies: List<SearchMovieEntity>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(movie: MovieEntity)

    @Query("DELETE FROM movies")
    suspend fun deleteAll()

    @Query("DELETE FROM movies WHERE id=:id")
    suspend fun deleteById(id: Int)
}