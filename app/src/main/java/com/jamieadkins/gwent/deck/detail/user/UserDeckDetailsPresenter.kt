package com.jamieadkins.gwent.deck.detail.user

import com.jamieadkins.commonutils.mvp2.BaseSchedulerProvider
import com.jamieadkins.commonutils.mvp2.addToComposite
import com.jamieadkins.commonutils.mvp2.applySchedulers
import com.jamieadkins.gwent.base.BaseDisposableObserver
import com.jamieadkins.gwent.base.BaseDisposableSingle
import com.jamieadkins.gwent.base.BaseDisposableSubscriber
import com.jamieadkins.gwent.base.BaseFilterPresenter
import com.jamieadkins.gwent.bus.DeckEvent
import com.jamieadkins.gwent.bus.RxBus
import com.jamieadkins.gwent.data.card.CardsInteractor
import com.jamieadkins.gwent.data.deck.DecksInteractor
import com.jamieadkins.gwent.card.CardFilter
import com.jamieadkins.gwent.data.deck.Deck
import com.jamieadkins.gwent.data.interactor.RxDatabaseEvent
import com.jamieadkins.gwent.data.repository.card.CardRepository
import com.jamieadkins.gwent.model.CardColour
import com.jamieadkins.gwent.model.GwentFaction
import com.jamieadkins.gwent.model.GwentCard

/**
 * Listens to user actions from the UI, retrieves the data and updates the
 * UI as required.
 */
class UserDeckDetailsPresenter(private val deckId: String,
                               private val faction: com.jamieadkins.gwent.model.GwentFaction,
                               private val decksInteractor: DecksInteractor,
                               private val cardRepository: CardRepository,
                               schedulerProvider: BaseSchedulerProvider) :
        BaseFilterPresenter<UserDeckDetailsContract.View>(schedulerProvider), UserDeckDetailsContract.Presenter {

    override fun onAttach(newView: UserDeckDetailsContract.View) {
        super.onAttach(newView)
        onRefresh()
        getPotentialLeaders()

        RxBus.register(DeckEvent::class.java)
                .subscribeWith(object : BaseDisposableObserver<DeckEvent>() {
                    override fun onNext(event: DeckEvent) {
                        when (event.data.event) {
                            DeckEvent.Event.ADD_CARD -> {
                                decksInteractor.addCardToDeck(deckId, event.data.card)
                            }
                            DeckEvent.Event.REMOVE_CARD -> {
                                decksInteractor.removeCardFromDeck(deckId, event.data.card)
                            }
                        }
                    }
                })
                .addToComposite(disposable)

        decksInteractor.getDeck(deckId, false)
                .applySchedulers()
                .subscribeWith(object : BaseDisposableObserver<RxDatabaseEvent<Deck>>() {
                    override fun onNext(event: RxDatabaseEvent<Deck>) {
                        val deck = event.value
                        view?.onDeckUpdated(deck)

                        cardRepository.getCard(deck.leaderId)
                                .applySchedulers()
                                .subscribe { leader ->
                                    view?.onLeaderChanged(leader)

                                }
                                .addToComposite(disposable)
                    }

                })
                .addToComposite(disposable)

        decksInteractor.subscribeToCardCountUpdates(deckId)
                .applySchedulers()
                .subscribeWith(object : BaseDisposableObserver<RxDatabaseEvent<Int>>() {
                    override fun onNext(event: RxDatabaseEvent<Int>) {
                        when (event.eventType) {
                            RxDatabaseEvent.EventType.ADDED -> {
                                cardRepository.getCard(event.key)
                                        .applySchedulers()
                                        .subscribe { card ->
                                            view?.onCardAdded(card)
                                        }
                                        .addToComposite(disposable)
                                view?.updateCardCount(event.key, event.value)
                            }
                            RxDatabaseEvent.EventType.REMOVED -> {
                                view?.updateCardCount(event.key, 0)
                            }
                            RxDatabaseEvent.EventType.CHANGED -> {
                                view?.updateCardCount(event.key, event.value)
                            }
                        }
                    }

                })
                .addToComposite(disposable)
    }

    private fun getPotentialLeaders() {
        val cardFilter = CardFilter()

        // Set filter to leaders of this faction only.
        GwentFaction.values()
                .filter { it != faction }
                .forEach { cardFilter.factionFilter.put(it, false) }
        cardFilter.colourFilter.put(CardColour.BRONZE, false)
        cardFilter.colourFilter.put(CardColour.SILVER, false)
        cardFilter.colourFilter.put(CardColour.GOLD, false)

        cardRepository.getCards(cardFilter)
                .applySchedulers()
                .subscribeWith(object : BaseDisposableSingle<Collection<GwentCard>>() {
                    override fun onSuccess(result: Collection<GwentCard>) {
                        view?.showPotentialLeaders(result.toList())
                    }
                })
                .addToComposite(disposable)
    }

    override fun onCardFilterUpdated() {
        // Do nothing.
    }

    override fun publishDeck() {
        decksInteractor.publishDeck(deckId)
    }

    override fun changeLeader(leaderId: String) {
        decksInteractor.setLeader(deckId, leaderId)
    }

    override fun renameDeck(name: String) {
        decksInteractor.renameDeck(deckId, name)
    }

    override fun deleteDeck() {
        decksInteractor.deleteDeck(deckId)
    }

    override fun onRefresh() {
        // Do nothing.
    }
}