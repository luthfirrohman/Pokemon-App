package com.luthfirrohman.pokemonapps.core.data.source.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.luthfirrohman.pokemonapps.core.data.source.local.entity.MoviesDataEntity

@Database(
    entities = [MoviesDataEntity::class],
    version = 1,
    exportSchema = false
)
abstract class MovieFavoriteDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieFavoriteDao
}