package com.jamieadkins.gwent.card.detail

import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.jamieadkins.gwent.R
import com.jamieadkins.gwent.base.convertDpToPixel
import com.jamieadkins.gwent.card.list.GwentCardItem
import com.jamieadkins.gwent.base.items.SubHeaderItem
import com.jamieadkins.gwent.base.VerticalSpaceItemDecoration
import com.jamieadkins.gwent.domain.GwentFaction
import com.jamieadkins.gwent.domain.card.model.GwentCard
import com.jamieadkins.gwent.domain.card.model.GwentCardColour
import com.jamieadkins.gwent.base.CardResourceHelper
import com.jamieadkins.gwent.main.GwentStringHelper
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.appbar_layout.*
import kotlinx.android.synthetic.main.fragment_card_details.*
import javax.inject.Inject

class CardDetailsFragment : DaggerFragment(), DetailContract.View {

    lateinit var cardId: String
    @Inject lateinit var presenter: DetailContract.Presenter

    private val adapter = GroupAdapter<ViewHolder>()

    companion object {
        private const val KEY_ID = "cardId"

        fun withCardId(cardId: String): CardDetailsFragment {
            return CardDetailsFragment().apply {
                arguments = Bundle().apply {
                    putString(KEY_ID, cardId)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        cardId = savedInstanceState?.getString(KEY_ID) ?: arguments?.getString(KEY_ID) ?: throw Exception("Card id not found.")
        super.onCreate(savedInstanceState)

        presenter.setCardId(cardId)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_card_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? AppCompatActivity)?.apply {
            setSupportActionBar(toolbar)
        }
        toolbar.setTitleTextAppearance(requireContext(), R.style.GwentTextAppearance)

        val layoutManager = LinearLayoutManager(recyclerView.context)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
        val dividerItemDecoration = VerticalSpaceItemDecoration(requireContext().convertDpToPixel(8f).toInt())
        recyclerView.addItemDecoration(dividerItemDecoration)
        refreshLayout.setColorSchemeResources(R.color.gwentAccent)
        refreshLayout.isEnabled = false

        presenter.onAttach()
    }

    override fun onDestroyView() {
        presenter.onDetach()
        super.onDestroyView()
    }

    override fun showScreen(cardDetailsScreenData: CardDetailsScreenData) {
        val card = cardDetailsScreenData.card
        showCard(card)
        val items = mutableListOf<Item>()
        if (card.tooltip.isNotEmpty()) {
            items.add(SubHeaderItem(R.string.tooltip))
            items.add(ElevatedTextItem(card.tooltip))
        }
        if (card.flavor.isNotEmpty()) {
            items.add(SubHeaderItem(R.string.flavor))
            items.add(ElevatedTextItem(card.flavor, Typeface.defaultFromStyle(Typeface.ITALIC)))
        }
        if (card.categories.isNotEmpty()) {
            items.add(SubHeaderItem(R.string.categories))
            items.add(ElevatedTextItem(card.categories.joinToString()))
        }
        items.add(SubHeaderItem(R.string.type))
        items.add(ElevatedTextItem(GwentStringHelper.getTypeString(resources, card.type)))

        if (card.colour != GwentCardColour.LEADER && card.provisions > 0) {
            items.add(SubHeaderItem(R.string.provisions))
            items.add(ElevatedTextItem(card.provisions.toString()))
        }

        if (card.colour == GwentCardColour.LEADER) {
            items.add(SubHeaderItem(R.string.extra_provisions))
            items.add(ElevatedTextItem(card.extraProvisions.toString()))
        }

        if (card.collectible) {
            items.add(SubHeaderItem(R.string.craft))
            items.add(CraftCostItem(card.craftCost))
            items.add(SubHeaderItem(R.string.mill))
            items.add(CraftCostItem(card.millValue))
        }

        if (card.relatedCards.isNotEmpty()) {
            items.add(SubHeaderItem(R.string.related))
            items.addAll(cardDetailsScreenData.relatedCards.map { GwentCardItem(it) })
        }
        adapter.update(items)
    }

    private fun showCard(card: GwentCard) {
        (activity as? AppCompatActivity)?.title = card.name

        loadCardImage(card.cardArt.medium, card.faction)

        toolbar.setBackgroundColor(CardResourceHelper.getColorForFaction(resources, card.faction))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity?.window?.statusBarColor = CardResourceHelper.getDarkColorForFaction(resources, card.faction)
        }
    }

    private fun loadCardImage(imageUrl: String?, faction: GwentFaction) {
        val cardBack = when (faction) {
            GwentFaction.NORTHERN_REALMS -> R.drawable.cardback_northern_realms
            GwentFaction.NILFGAARD -> R.drawable.cardback_nilfgaard
            GwentFaction.NEUTRAL -> R.drawable.cardback_neutral
            GwentFaction.SCOIATAEL -> R.drawable.cardback_scoiatel
            GwentFaction.MONSTER -> R.drawable.cardback_monster
            GwentFaction.SKELLIGE -> R.drawable.cardback_skellige
            else -> R.drawable.cardback_neutral
        }

        if (imageUrl != null) {
            Glide.with(context)
                .load(imageUrl)
                .centerCrop()
                .error(cardBack)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imgCard)
        } else {
            Glide.with(context)
                .load(cardBack)
                .into(imgCard)
        }
    }

    override fun showLoadingIndicator() {
        refreshLayout.isRefreshing = true
    }

    override fun hideLoadingIndicator() {
        refreshLayout.isRefreshing = false
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(KEY_ID, cardId)
        super.onSaveInstanceState(outState)
    }
}