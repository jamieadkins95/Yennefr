package com.jamieadkins.gwent.domain.filter.model

import com.jamieadkins.gwent.domain.GwentFaction
import com.jamieadkins.gwent.domain.card.model.GwentCardColour
import com.jamieadkins.gwent.domain.card.model.GwentCardRarity
import com.jamieadkins.gwent.domain.card.model.GwentCardSet
import com.jamieadkins.gwent.domain.card.model.SortedBy

data class CardFilter(
        val rarityFilter: Map<GwentCardRarity, Boolean> = GwentCardRarity.values().map { it to true }.toMap(),
        val colourFilter: Map<GwentCardColour, Boolean> = GwentCardColour.values().map { it to true }.toMap(),
        val factionFilter: Map<GwentFaction, Boolean> = GwentFaction.values().map { it to true }.toMap(),
        val cardSetFilter: Map<GwentCardSet, Boolean> = GwentCardSet.values().map { it to true }.toMap(),
        val minProvisions: Int = 0,
        val maxProvisions: Int = 20,
        val isCollectibleOnly: Boolean = false,
        val sortedBy: SortedBy = SortedBy.ALPHABETICALLY_ASC)
