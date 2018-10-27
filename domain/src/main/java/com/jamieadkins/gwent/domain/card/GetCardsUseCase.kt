package com.jamieadkins.gwent.domain.card

import com.jamieadkins.gwent.domain.SchedulerProvider
import com.jamieadkins.gwent.domain.card.model.GwentCard
import com.jamieadkins.gwent.domain.card.model.SortedBy
import com.jamieadkins.gwent.domain.card.repository.CardRepository
import com.jamieadkins.gwent.domain.filter.model.CardFilter
import com.jamieadkins.gwent.domain.filter.repository.FilterRepository
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import javax.inject.Inject

class GetCardsUseCase @Inject constructor(
    private val cardRepository: CardRepository,
    private val filterRepository: FilterRepository,
    private val schedulerProvider: SchedulerProvider
) {

    fun getCards(): Observable<List<GwentCard>> {
        return internalGetCards(cardRepository.getCards())
    }

    fun searchCards(query: String): Observable<List<GwentCard>> {
        return internalGetCards(cardRepository.searchCards(query))
    }

    private fun internalGetCards(cardObservable: Observable<List<GwentCard>>): Observable<List<GwentCard>> {
        return Observable.combineLatest(
            cardObservable,
            filterRepository.getFilter(),
            BiFunction { cards: List<GwentCard>, filter: CardFilter ->
                val filtered = cards.filter { doesCardMeetFilter(filter, it) }

                when (filter.sortedBy) {
                    SortedBy.SEARCH_RELEVANCE -> filtered
                    SortedBy.ALPHABETICALLY_ASC -> filtered.sortedBy { it.name }
                    SortedBy.ALPHABETICALLY_DESC -> filtered.sortedByDescending { it.name }
                    SortedBy.STRENGTH_ASC -> filtered.sortedBy { it.strength }
                    SortedBy.STRENGTH_DESC -> filtered.sortedByDescending { it.strength }
                }
            })
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
    }

    private fun doesCardMeetFilter(filter: CardFilter, card: GwentCard): Boolean {
        val include = !filter.isCollectibleOnly || card.collectible
        val faction = filter.factionFilter[card.faction] ?: false
        val rarity = filter.rarityFilter[card.rarity] ?: false
        val colour = filter.colourFilter[card.colour] ?: false
        val provisions = card.provisions >= filter.minProvisions && card.provisions <= filter.maxProvisions
//        val cardSet = filter.cardSetFilter[card.cardSet] ?: false
        return (faction && rarity && colour && include && provisions)
    }

}