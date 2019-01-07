package com.jamieadkins.gwent.domain.deck.model

import com.jamieadkins.gwent.domain.GwentFaction
import com.jamieadkins.gwent.domain.card.model.GwentCard
import java.util.*

data class GwentDeck(
    val id: String,
    val name: String = "",
    val faction: GwentFaction,
    val leader: GwentCard,
    val createdAt: Date,
    val cards: Map<String, GwentCard> = emptyMap(),
    val cardCounts: Map<String, Int> = emptyMap(),
    val maxProvisionCost: Int = 150) {

    override fun toString(): String {
        return "$name $faction ${leader.name}"
    }

    val totalCardCount: Int = cardCounts.values.sum()
    val provisionCost: Int = cards.values.map { it.provisions * cardCounts.getValue(it.id) }.sum()
}