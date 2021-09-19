package ru.gaket.themoviedb.data.genres.local

import androidx.room.Dao
import androidx.room.Query

@Dao
interface GenresDao {

	@Query("SELECT * FROM genres")
	suspend fun getAll(): List<GenreEntity>

	@Query("SELECT * FROM genres WHERE id IN (:ids)")
	suspend fun getAllByIds(ids: List<Int>): List<GenreEntity>

	@Query("SELECT * FROM genres WHERE id=:id LIMIT 1")
	suspend fun getById(id: Int): GenreEntity
}