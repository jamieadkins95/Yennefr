package com.jamieadkins.gwent.domain.deck

import com.jamieadkins.gwent.domain.SchedulerProvider
import com.jamieadkins.gwent.domain.card.model.GwentCardColour
import com.jamieadkins.gwent.domain.card.repository.CardRepository
import com.jamieadkins.gwent.domain.deck.DeckConstants.BRONZE_MAX
import com.jamieadkins.gwent.domain.deck.DeckConstants.GOLD_MAX
import com.jamieadkins.gwent.domain.deck.repository.DeckRepository
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import javax.inject.Inject

class AddCardToDeckUseCase @Inject constructor(
    private val cardRepository: CardRepository,
    private val deckRepository: DeckRepository,
    private val schedulerProvider: SchedulerProvider
) {

    fun addCard(deckId: String, cardId: String): Single<AddCardToDeckResult> {
        val countInDeck = deckRepository.getDeckOnce(deckId).map { it.cardCounts[cardId] ?: 0 }
        val cardTier = cardRepository.getCard(cardId).firstOrError().map { it.colour }
        return Single.zip(countInDeck, cardTier, BiFunction { inDeck: Int, tier: GwentCardColour -> Pair(inDeck, tier) })
            .flatMap {
                val count = it.first
                val newCount = count + 1
                when (it.second) {
                    GwentCardColour.BRONZE -> {
                        if (count < BRONZE_MAX) updateCardCount(deckId, cardId, newCount) else Single.just(AddCardToDeckResult.MaximumReached)
                    }
                    GwentCardColour.GOLD -> {
                        if (count < GOLD_MAX) updateCardCount(deckId, cardId, newCount) else Single.just(AddCardToDeckResult.MaximumReached)
                    }
                    GwentCardColour.LEADER -> Single.just(AddCardToDeckResult.CantAddLeaders)
                }
            }
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
    }

    private fun updateCardCount(deckId: String, cardId: String, count: Int): Single<AddCardToDeckResult> {
        return deckRepository.updateCardCount(deckId, cardId, count).toSingleDefault(AddCardToDeckResult.Success)
    }
}