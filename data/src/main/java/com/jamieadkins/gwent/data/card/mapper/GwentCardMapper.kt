package com.jamieadkins.gwent.data.card.mapper

import com.jamieadkins.gwent.data.FactionMapper
import com.jamieadkins.gwent.data.card.model.Rarity
import com.jamieadkins.gwent.data.card.model.Type
import com.jamieadkins.gwent.database.entity.CardWithArtEntity
import com.jamieadkins.gwent.database.entity.CategoryEntity
import com.jamieadkins.gwent.database.entity.KeywordEntity
import com.jamieadkins.gwent.domain.card.model.GwentCard
import com.jamieadkins.gwent.domain.card.model.GwentCardColour
import com.jamieadkins.gwent.domain.card.model.GwentCardRarity
import com.jamieadkins.gwent.domain.card.model.GwentKeyword
import javax.inject.Inject

class GwentCardMapper @Inject constructor(
    private val artMapper: GwentCardArtMapper,
    private val factionMapper: FactionMapper) {

    fun map(from: CardWithArtEntity, locale: String, allKeywords: List<KeywordEntity>, allCategories: List<CategoryEntity>): GwentCard {
        val cardEntity = from.card

        val categories = allCategories.asSequence()
            .filter { category ->
                cardEntity.categoryIds.contains(category.categoryId) && category.locale == locale }
            .distinct()
            .map { it.name }
            .toList()

        val keywords = allKeywords
            .asSequence()
            .filter { cardEntity.keywordIds.contains(it.keywordId) && it.locale == locale }
            .distinct()
            .map { GwentKeyword(it.keywordId, it.name, it.description) }
            .toList()

        return GwentCard(cardEntity.id,
                         cardEntity.name[locale] ?: "",
                         cardEntity.tooltip[locale] ?: "",
                         cardEntity.flavor[locale] ?: "",
                         categories,
                         keywords,
                         factionMapper.map(cardEntity.faction),
                         cardEntity.strength,
                         cardEntity.provisions,
                         cardEntity.mulligans,
                         typeToColour(cardEntity.color),
                         rarityIdToRarity(cardEntity.rarity),
                         cardEntity.collectible,
                         cardEntity.craft,
                         cardEntity.mill,
                         cardEntity.related,
                         artMapper.map(from.art.first()))
    }

    private fun typeToColour(type: String): GwentCardColour {
        return when (type) {
            Type.BRONZE_ID -> GwentCardColour.BRONZE
            Type.SILVER_ID -> GwentCardColour.SILVER
            Type.GOLD_ID -> GwentCardColour.GOLD
            Type.LEADER_ID -> GwentCardColour.LEADER
            else -> throw Exception("Colour not found")
        }
    }

    private fun rarityIdToRarity(rarity: String): GwentCardRarity {
        return when (rarity) {
            Rarity.COMMON_ID -> GwentCardRarity.COMMON
            Rarity.RARE_ID -> GwentCardRarity.RARE
            Rarity.EPIC_ID -> GwentCardRarity.EPIC
            Rarity.LEGENDARY_ID -> GwentCardRarity.LEGENDARY
            else -> throw Exception("Rarity not found")
        }
    }

    fun mapList(from: List<CardWithArtEntity>,
                locale: String,
                allKeywords: List<KeywordEntity>,
                allCategories: List<CategoryEntity>): List<GwentCard> {
        return from.mapNotNull {
            try {
                map(it, locale, allKeywords, allCategories)
            } catch (e: Exception) {
                null
            }
        }
    }
}