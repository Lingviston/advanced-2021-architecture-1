package ru.gaket.themoviedb.data.genres.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface GenresDao {

    @Query("SELECT COUNT(id) FROM genres")
    suspend fun hasData(): Int

    @Query("SELECT * FROM genres")
    suspend fun getAll(): List<GenreEntity>

    @Query("SELECT * FROM genres WHERE id IN (:ids)")
    suspend fun getAllByIds(ids: List<Int>): List<GenreEntity>

    @Query("SELECT * FROM genres WHERE id=:id LIMIT 1")
    suspend fun getById(id: Int): GenreEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun replaceAll(genres: List<GenreEntity>)

    @Query("DELETE FROM genres")
    suspend fun deleteAll()

    @Query("DELETE FROM genres WHERE id=:id")
    suspend fun deleteById(id: Int)
}