package com.jamieadkins.gwent.filter

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.jamieadkins.gwent.R
import com.jamieadkins.gwent.base.DaggerSupportDialogFragment
import com.jamieadkins.gwent.domain.GwentFaction
import com.jamieadkins.gwent.domain.card.model.SortedBy
import kotlinx.android.synthetic.main.fragment_filter.*
import javax.inject.Inject

class FilterBottomSheetDialogFragment : DaggerSupportDialogFragment(), FilterContract.View {

    @Inject lateinit var presenter: FilterContract.Presenter

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext(), this.theme)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_filter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.setDeck(arguments?.getString(KEY_DECK_ID) ?: "")
        presenter.onAttach()
        btnApply.setOnClickListener { presenter.applyFilters() }
        btnReset.setOnClickListener { presenter.resetFilters() }

        filter_faction_ng.setOnCheckedChangeListener { _, checked -> presenter.onNilfgaardFilterChanged(checked) }
        filter_faction_mon.setOnCheckedChangeListener { _, checked -> presenter.onMonsterFilterChanged(checked) }
        filter_faction_nr.setOnCheckedChangeListener { _, checked -> presenter.onNorthernRealmsFilterChanged(checked) }
        filter_faction_sk.setOnCheckedChangeListener { _, checked -> presenter.onSkelligeFilterChanged(checked) }
        filter_faction_sc.setOnCheckedChangeListener { _, checked -> presenter.onScoiataelFilterChanged(checked) }
        filter_faction_ne.setOnCheckedChangeListener { _, checked -> presenter.onNeutralFilterChanged(checked) }
        filter_faction_sy.setOnCheckedChangeListener { _, checked -> presenter.onSyndicateFilterChanged(checked) }

        filter_color_bronze.setOnCheckedChangeListener { _, checked -> presenter.onBronzeChanged(checked) }
        filter_color_gold.setOnCheckedChangeListener { _, checked -> presenter.onGoldChanged(checked) }
        filter_color_leader.setOnCheckedChangeListener { _, checked -> presenter.onLeaderChanged(checked) }

        filter_rarity_common.setOnCheckedChangeListener { _, checked -> presenter.onCommonChanged(checked) }
        filter_rarity_rare.setOnCheckedChangeListener { _, checked -> presenter.onRareChanged(checked) }
        filter_rarity_epic.setOnCheckedChangeListener { _, checked -> presenter.onEpicChanged(checked) }
        filter_rarity_legendary.setOnCheckedChangeListener { _, checked -> presenter.onLegendaryChanged(checked) }

        filter_type_unit.setOnCheckedChangeListener { _, checked -> presenter.onTypeUnitChanged(checked) }
        filter_type_artifact.setOnCheckedChangeListener { _, checked -> presenter.onTypeArtifactChanged(checked) }
        filter_type_spell.setOnCheckedChangeListener { _, checked -> presenter.onTypeSpellChanged(checked) }
        filter_type_leader.setOnCheckedChangeListener { _, checked -> presenter.onTypeLeaderChanged(checked) }
        filter_type_strategem.setOnCheckedChangeListener { _, checked -> presenter.onTypeStrategemChanged(checked) }

        filter_expansion_base.setOnCheckedChangeListener { _, checked -> presenter.onBaseSetChanged(checked) }
        filter_expansion_unmillable.setOnCheckedChangeListener { _, checked -> presenter.onUnmillableSetChanged(checked) }
        filter_expansion_token.setOnCheckedChangeListener { _, checked -> presenter.onTokenSetChanged(checked) }
        filter_expansion_thronebreaker.setOnCheckedChangeListener { _, checked -> presenter.onThronebreakerSetChanged(checked) }
        filter_expansion_crimson_curse.setOnCheckedChangeListener { _, checked -> presenter.onCrimsonCurseSetChanged(checked) }
        filter_expansion_novigrad.setOnCheckedChangeListener { _, checked -> presenter.onNovigradSetChanged(checked) }
        filter_expansion_iron_judgement.setOnCheckedChangeListener { _, checked -> presenter.onIronJudgementSetChanged(checked) }
        filter_expansion_merchants_of_ofir.setOnCheckedChangeListener { _, checked -> presenter.onMerchantsOfOfirSetChanged(checked) }

        group_sort_by.setOnCheckedChangeListener { _, id ->
            val sortBy = when (id) {
                R.id.filter_sort_alphabetical -> SortedBy.ALPHABETICALLY_ASC
                R.id.filter_sort_alphabetical_desc -> SortedBy.ALPHABETICALLY_DESC
                R.id.filter_sort_provisions -> SortedBy.PROVISIONS_ASC
                R.id.filter_sort_provisions_desc -> SortedBy.PROVISIONS_DESC
                else -> SortedBy.ALPHABETICALLY_ASC
            }
            presenter.onSortedByChanged(sortBy)
        }

        inputMin.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(p0: Editable?) { /* Do nothing. */ }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { /* Do nothing. */ }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                s?.toString()?.toIntOrNull()?.let(presenter::onMinProvisionsChanged)
            }
        })

        inputMax.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(p0: Editable?) { /* Do nothing. */ }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { /* Do nothing. */ }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                s?.toString()?.toIntOrNull()?.let(presenter::onMaxProvisionsChanged)
            }
        })
    }

    override fun onDestroyView() {
        presenter.onDetach()
        super.onDestroyView()
    }

    override fun setNilfgaardFilter(checked: Boolean) { filter_faction_ng.isChecked = checked }

    override fun setMonsterFilter(checked: Boolean) { filter_faction_mon.isChecked = checked }

    override fun setNorthernRealmsFilter(checked: Boolean) { filter_faction_nr.isChecked = checked }

    override fun setScoiataelFilter(checked: Boolean) { filter_faction_sc.isChecked = checked }

    override fun setSkelligeFilter(checked: Boolean) { filter_faction_sk.isChecked = checked }

    override fun setNeutralFilter(checked: Boolean) { filter_faction_ne.isChecked = checked }

    override fun setSyndicateFilter(checked: Boolean) { filter_faction_sy.isChecked = checked }

    override fun setBronzeFilter(checked: Boolean) { filter_color_bronze.isChecked = checked }

    override fun setGoldFilter(checked: Boolean) { filter_color_gold.isChecked = checked }

    override fun setLeaderFilter(checked: Boolean) { filter_color_leader.isChecked = checked }

    override fun setCommonFilter(checked: Boolean) { filter_rarity_common.isChecked = checked }

    override fun setRareFilter(checked: Boolean) { filter_rarity_rare.isChecked = checked }

    override fun setEpicFilter(checked: Boolean) { filter_rarity_epic.isChecked = checked }

    override fun setLegendaryFilter(checked: Boolean) { filter_rarity_legendary.isChecked = checked }

    override fun setMinProvisions(provisions: Int) { inputMin.setText(provisions.toString()) }

    override fun setMaxProvisions(provisions: Int) { inputMax.setText(provisions.toString()) }

    override fun setArtifactFilter(checked: Boolean) { filter_type_artifact.isChecked = checked }

    override fun setSpellFilter(checked: Boolean) { filter_type_spell.isChecked = checked }

    override fun setUnitFilter(checked: Boolean) { filter_type_unit.isChecked = checked }

    override fun setStrategemFilter(checked: Boolean) { filter_type_strategem.isChecked = checked }

    override fun setTypeLeaderFilter(checked: Boolean) { filter_type_leader.isChecked = checked }

    override fun setBaseSetFilter(checked: Boolean) { filter_expansion_base.isChecked = checked }

    override fun setUnmillableSetFilter(checked: Boolean) { filter_expansion_unmillable.isChecked = checked }

    override fun setTokenSetFilter(checked: Boolean) { filter_expansion_token.isChecked = checked }

    override fun setThronebreakerSetFilter(checked: Boolean) { filter_expansion_thronebreaker.isChecked = checked }

    override fun setCrimsonCurseSetFilter(checked: Boolean) { filter_expansion_crimson_curse.isChecked = checked }

    override fun setNovigradSetFilter(checked: Boolean) { filter_expansion_novigrad.isChecked = checked }

    override fun setIronJudgementSetFilter(checked: Boolean) { filter_expansion_iron_judgement.isChecked = checked }

    override fun setMerchantsOfOfirSetFilter(checked: Boolean) { filter_expansion_merchants_of_ofir.isChecked = checked }

    override fun setSortedBy(sortedBy: SortedBy) {
        when (sortedBy) {
            SortedBy.ALPHABETICALLY_ASC -> filter_sort_alphabetical.isChecked = true
            SortedBy.ALPHABETICALLY_DESC -> filter_sort_alphabetical_desc.isChecked = true
            SortedBy.PROVISIONS_ASC -> filter_sort_provisions.isChecked = true
            SortedBy.PROVISIONS_DESC -> filter_sort_provisions_desc.isChecked = true
        }
    }

    override fun close() = dismiss()

    override fun showFiltersForDeckBuilder(faction: GwentFaction) {
        val factionFilters = mapOf(
            GwentFaction.NILFGAARD to filter_faction_ng,
            GwentFaction.MONSTER to filter_faction_mon,
            GwentFaction.NORTHERN_REALMS to filter_faction_nr,
            GwentFaction.SKELLIGE to filter_faction_sk,
            GwentFaction.SCOIATAEL to filter_faction_sc,
            GwentFaction.SYNDICATE to filter_faction_sy
        )

        factionFilters.entries.forEach {
            if (it.key == faction) {
                it.value.visibility = View.VISIBLE
            } else {
                it.value.visibility = View.GONE
            }
        }

        filter_type_leader.visibility = View.GONE
        filter_color_leader.visibility = View.GONE
        filter_type_strategem.visibility = View.GONE
    }

    override fun hideTokenFilterForDeckBuilder() {
        filter_expansion_token.visibility = View.GONE
    }

    companion object {

        private const val KEY_DECK_ID = "KEY_DECK_ID"

        fun getInstance(deckId: String): FilterBottomSheetDialogFragment {
            val dialog = FilterBottomSheetDialogFragment()
            val arguments = Bundle().apply {
                putString(KEY_DECK_ID, deckId)
            }
            dialog.arguments = arguments
            return dialog
        }
    }
}
