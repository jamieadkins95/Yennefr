package com.jamieadkins.gwent.main

import android.content.Context
import com.jamieadkins.gwent.R
import com.jamieadkins.gwent.domain.GwentFaction
import com.jamieadkins.gwent.domain.card.model.GwentCardColour
import com.jamieadkins.gwent.domain.card.model.GwentCardLoyalty
import com.jamieadkins.gwent.domain.card.model.GwentCardRarity
import com.jamieadkins.gwent.domain.card.model.GwentCardSet

object GwentStringHelper {

    fun getRarityString(context: Context, rarity: GwentCardRarity?): String? {
        return when(rarity) {
            GwentCardRarity.COMMON -> context.getString(R.string.common)
            GwentCardRarity.RARE -> context.getString(R.string.rare)
            GwentCardRarity.EPIC -> context.getString(R.string.epic)
            GwentCardRarity.LEGENDARY -> context.getString(R.string.legendary)
            else -> null
        }
    }

    fun getCardSetString(context: Context, set: GwentCardSet?): String? {
        return when(set) {
            GwentCardSet.BASE -> context.getString(R.string.base)
            GwentCardSet.THRONEBREAKER -> context.getString(R.string.thronebreaker)
            else -> null
        }
    }

    fun getColourString(context: Context, colour: GwentCardColour?): String? {
        return when(colour) {
            GwentCardColour.BRONZE -> context.getString(R.string.bronze)
            GwentCardColour.GOLD -> context.getString(R.string.gold)
            GwentCardColour.LEADER -> context.getString(R.string.leader)
            else -> null
        }
    }

    fun getFactionString(context: Context, faction: GwentFaction?): String? {
        return when(faction) {
            GwentFaction.MONSTER -> context.getString(R.string.monster)
            GwentFaction.NORTHERN_REALMS -> context.getString(R.string.northern_realms)
            GwentFaction.SCOIATAEL -> context.getString(R.string.scoiatael)
            GwentFaction.SKELLIGE -> context.getString(R.string.skellige)
            GwentFaction.NILFGAARD -> context.getString(R.string.nilfgaard)
            GwentFaction.NEUTRAL -> context.getString(R.string.neutral)
            else -> null
        }
    }
}