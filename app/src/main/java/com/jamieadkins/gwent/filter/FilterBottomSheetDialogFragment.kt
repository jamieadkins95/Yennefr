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
        presenter.onAttach()
        btnApply.setOnClickListener { presenter.applyFilters() }
        btnReset.setOnClickListener { presenter.resetFilters() }

        filter_faction_ng.setOnCheckedChangeListener { _, checked -> presenter.onNilfgaardFilterChanged(checked) }
        filter_faction_mon.setOnCheckedChangeListener { _, checked -> presenter.onMonsterFilterChanged(checked) }
        filter_faction_nr.setOnCheckedChangeListener { _, checked -> presenter.onNorthernRealmsFilterChanged(checked) }
        filter_faction_sk.setOnCheckedChangeListener { _, checked -> presenter.onSkelligeFilterChanged(checked) }
        filter_faction_sc.setOnCheckedChangeListener { _, checked -> presenter.onScoiataelFilterChanged(checked) }
        filter_faction_ne.setOnCheckedChangeListener { _, checked -> presenter.onNeutralFilterChanged(checked) }

        filter_color_bronze.setOnCheckedChangeListener { _, checked -> presenter.onBronzeChanged(checked) }
        filter_color_gold.setOnCheckedChangeListener { _, checked -> presenter.onGoldChanged(checked) }
        filter_color_leader.setOnCheckedChangeListener { _, checked -> presenter.onLeaderChanged(checked) }

        filter_rarity_common.setOnCheckedChangeListener { _, checked -> presenter.onCommonChanged(checked) }
        filter_rarity_rare.setOnCheckedChangeListener { _, checked -> presenter.onRareChanged(checked) }
        filter_rarity_epic.setOnCheckedChangeListener { _, checked -> presenter.onEpicChanged(checked) }
        filter_rarity_legendary.setOnCheckedChangeListener { _, checked -> presenter.onLegendaryChanged(checked) }

        filter_set_base.setOnCheckedChangeListener { _, checked -> presenter.onBaseSetChanged(checked) }
        filter_set_thronebreaker.setOnCheckedChangeListener { _, checked -> presenter.onThronebreakerChanged(checked) }

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

    override fun setBronzeFilter(checked: Boolean) { filter_color_bronze.isChecked = checked }

    override fun setGoldFilter(checked: Boolean) { filter_color_gold.isChecked = checked }

    override fun setLeaderFilter(checked: Boolean) { filter_color_leader.isChecked = checked }

    override fun setCommonFilter(checked: Boolean) { filter_rarity_common.isChecked = checked }

    override fun setRareFilter(checked: Boolean) { filter_rarity_rare.isChecked = checked }

    override fun setEpicFilter(checked: Boolean) { filter_rarity_epic.isChecked = checked }

    override fun setLegendaryFilter(checked: Boolean) { filter_rarity_legendary.isChecked = checked }

    override fun setBaseSetFilter(checked: Boolean) { filter_set_base.isChecked = checked }

    override fun setThronebreakerFilter(checked: Boolean) { filter_set_thronebreaker.isChecked = checked }

    override fun setMinProvisions(provisions: Int) { inputMin.setText(provisions.toString()) }

    override fun setMaxProvisions(provisions: Int) { inputMax.setText(provisions.toString()) }

    override fun close() = dismiss()
}
