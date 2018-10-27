package com.jamieadkins.gwent.database.entity

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.jamieadkins.gwent.database.GwentDatabase

@Entity(tableName = GwentDatabase.CARD_TABLE)
data class CardEntity(
        @PrimaryKey val id: String,
        val strength: Int = 0,
        val collectible: Boolean,
        val rarity: String,
        val color: String,
        val faction: String,
        val provisions: Int = 0,
        val mulligans: Int = 0,
        val name: Map<String, String>,
        val tooltip: Map<String, String>,
        val flavor: Map<String, String>,
        val craft: Int,
        val mill: Int,
        val categoryIds: List<String>,
        val keywordIds: List<String>,
        val loyalties: List<String>,
        val related: List<String>,
        val availability: String
) {

    @Ignore var art: List<ArtEntity>? = null
}