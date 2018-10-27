package com.jamieadkins.gwent.filter

import com.jamieadkins.gwent.domain.card.model.GwentCardSet

class CardSetFilter(val set: GwentCardSet, checked: Boolean) : FilterableItem(checked) {
    override fun hashCode(): Int {
        return set.hashCode()
    }
}