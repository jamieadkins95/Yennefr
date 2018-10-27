package com.jamieadkins.gwent.filter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import com.jamieadkins.commonutils.bus.RxBus

import com.jamieadkins.gwent.R
import com.jamieadkins.gwent.bus.*
import com.jamieadkins.gwent.main.GwentStringHelper
import kotterknife.bindView

class FilterRecyclerViewAdapter : RecyclerView.Adapter<FilterRecyclerViewAdapter.FilterViewHolder>() {
    var filters = listOf<FilterableItem>()
            set(value) {
                field = value
                notifyDataSetChanged()
            }

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return filters[position].hashCode().toLong()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterViewHolder {
        return FilterViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.item_checkable, parent, false))
    }

    override fun onBindViewHolder(holder: FilterViewHolder, position: Int) {
        val filter = filters[position]
        holder.checkBox.isChecked = filter.isChecked
        holder.itemView.setOnClickListener {
            val newState = !holder.checkBox.isChecked
            when (filter) {
                is FactionFilter -> RxBus.post(FilterChangeEvent(FactionFilter(filter.faction, newState)))
                is ColourFilter -> RxBus.post(FilterChangeEvent(ColourFilter(filter.colour, newState)))
                is RarityFilter -> RxBus.post(FilterChangeEvent(RarityFilter(filter.rarity, newState)))
                is CardSetFilter -> RxBus.post(FilterChangeEvent(CardSetFilter(filter.set, newState)))
            }
        }

        when (filter) {
            is FactionFilter -> holder.tvName.text = GwentStringHelper.getFactionString(holder.itemView.context, filter.faction)
            is ColourFilter -> holder.tvName.text = GwentStringHelper.getColourString(holder.itemView.context, filter.colour)
            is RarityFilter -> holder.tvName.text = GwentStringHelper.getRarityString(holder.itemView.context, filter.rarity)
            is CardSetFilter -> holder.tvName.text = GwentStringHelper.getCardSetString(holder.itemView.context, filter.set)
        }
    }

    override fun getItemCount(): Int {
        return filters.size
    }

    inner class FilterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName by bindView<TextView>(R.id.name)
        val checkBox by bindView<CheckBox>(R.id.checkbox)
    }
}
