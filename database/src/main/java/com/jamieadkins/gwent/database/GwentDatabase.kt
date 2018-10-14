package com.jamieadkins.gwent.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import com.jamieadkins.gwent.database.entity.*

@Database(entities = [CardEntity::class, ArtEntity::class,
    DeckEntity::class, DeckCardEntity::class, CollectionEntity::class, CategoryEntity::class,
    KeywordEntity::class], version = 1)
@TypeConverters(DatabaseConverters::class)
abstract class GwentDatabase : RoomDatabase() {

    companion object {
        const val DB_NAME = "GwentDatabase"
        const val CARD_TABLE = "cards"
        const val ART_TABLE = "art"
        const val DECK_TABLE = "decks"
        const val DECK_CARD_TABLE = "deck_cards"
        const val COLLECTION_TABLE = "collection"
        const val CATEGORY_TABLE = "category"
        const val KEYWORD_TABLE = "keyword"
    }

    abstract fun cardDao(): CardDao

    abstract fun artDao(): ArtDao

    abstract fun deckDao(): DeckDao

    abstract fun deckCardDao(): DeckCardDao

    abstract fun collectionDao(): CollectionDao

    abstract fun keywordDao(): KeywordDao

    abstract fun categoryDao(): CategoryDao
}
