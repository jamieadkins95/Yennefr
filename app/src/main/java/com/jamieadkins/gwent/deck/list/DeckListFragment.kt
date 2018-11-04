package com.jamieadkins.gwent.deck.list

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.jamieadkins.gwent.R
import com.jamieadkins.gwent.card.list.VerticalSpaceItemDecoration
import com.jamieadkins.gwent.deck.create.CreateDeckDialog
import com.jamieadkins.gwent.deck.detail.DeckDetailsActivity
import com.jamieadkins.gwent.domain.deck.model.GwentDeck
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.appbar_layout.*
import kotlinx.android.synthetic.main.fragment_deck_list.*
import timber.log.Timber
import javax.inject.Inject

class DeckListFragment : DaggerFragment(), DeckListContract.View {

    @Inject lateinit var presenter: DeckListContract.Presenter

    private val controller = DeckListController()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_deck_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as? AppCompatActivity)?.apply {
            setSupportActionBar(toolbar)
            title = getString(R.string.deck_builder)
        }

        toolbar.setTitleTextAppearance(requireContext(), R.style.GwentTextAppearance)

        loadingIndicator.setColorSchemeResources(R.color.gwentAccent)
        loadingIndicator.isEnabled = false

        val layoutManager = LinearLayoutManager(recyclerView.context)
        recyclerView.layoutManager = layoutManager
        val dividerItemDecoration = VerticalSpaceItemDecoration(resources.getDimensionPixelSize(R.dimen.divider_spacing))
        recyclerView.addItemDecoration(dividerItemDecoration)
        recyclerView.adapter = controller.adapter

        btnCreate.setOnClickListener {
            val dialog = CreateDeckDialog()
            dialog.show(activity?.supportFragmentManager, dialog.tag)
        }

        presenter.onAttach()
    }

    override fun onDestroyView() {
        presenter.onDetach()
        super.onDestroyView()
    }

    override fun showDecks(decks: List<GwentDeck>) {
       controller.setData(decks)
    }

    override fun showDeckDetails(deckId: String) {
        startActivity(DeckDetailsActivity.getIntent(requireContext(), deckId))
    }

    override fun showLoadingIndicator(loading: Boolean) { loadingIndicator.isRefreshing = loading }
}
