package ru.gaket.themoviedb.data.genres.local

import javax.inject.Inject

// todo: [pash] discuss this need
interface GenresLocalDataSource {

    suspend fun hasData(): Boolean
    suspend fun getGenres(): List<GenreEntity>
    suspend fun getGenresByIds(ids: List<Int>): List<GenreEntity>
    suspend fun getGenreById(id: Int): GenreEntity?
    suspend fun insertAll(genres: List<GenreEntity>)
    suspend fun deleteAll()
    suspend fun deleteById(id: Int)
}

class GenresLocalDataSourceImpl @Inject constructor(
    private val genresDao: GenresDao,
) : GenresLocalDataSource {

    override suspend fun hasData(): Boolean = genresDao.genresCount() > 0

    override suspend fun getGenres(): List<GenreEntity> = genresDao.getAll()

    override suspend fun getGenresByIds(ids: List<Int>): List<GenreEntity> =
        genresDao.getAllByIds(ids)

    override suspend fun getGenreById(id: Int): GenreEntity? = genresDao.getById(id)

    override suspend fun insertAll(genres: List<GenreEntity>) {
        genresDao.replaceAll(genres)
    }

    override suspend fun deleteAll() {
        genresDao.deleteAll()
    }

    override suspend fun deleteById(id: Int) {
        genresDao.deleteById(id)
    }
}
