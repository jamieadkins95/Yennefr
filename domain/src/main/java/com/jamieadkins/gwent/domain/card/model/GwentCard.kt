package com.jamieadkins.gwent.domain.card.model

import com.jamieadkins.gwent.domain.GwentFaction

data class GwentCard(
        val id: String,
        val name: String = "",
        val tooltip: String = "",
        val flavor: String = "",
        val categories: List<String> = emptyList(),
        val keywords: List<GwentKeyword> = emptyList(),
        val faction: GwentFaction,
        val strength: Int = 0,
        val provisions: Int = 0,
        val mulligans: Int = 0,
        val colour: GwentCardColour,
        val rarity: GwentCardRarity,
        val collectible: Boolean = false,
        val craftCost: Int = 0,
        val millValue: Int = 0,
        val relatedCards: List<String> = emptyList(),
//        val cardSet: GwentCardSet,
        val cardArt: GwentCardArt) {

    override fun toString(): String {
        return name
    }
}