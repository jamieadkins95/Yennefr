package com.jamieadkins.gwent.data.card.mapper

import com.jamieadkins.gwent.data.Mapper
import com.jamieadkins.gwent.data.card.model.FirebaseCardResult
import com.jamieadkins.gwent.database.entity.CardEntity
import javax.inject.Inject

class ApiMapper @Inject constructor() : Mapper<FirebaseCardResult, Collection<CardEntity>>() {

    override fun map(from: FirebaseCardResult): Collection<CardEntity> {
        return from.values.mapNotNull {
            if (it.isReleased) {
                val variation = it.variations.values.firstOrNull()
                CardEntity(
                    it.ingameId,
                    it.strength ?: 0,
                    variation?.isCollectible ?: false,
                    it.rarity ?: "",
                    it.type ?: "",
                    it.faction ?: "",
                    it.provision ?: 0,
                    it.mulligans ?: 0,
                    it.name ?: mapOf(),
                    it.info ?: mapOf(),
                    it.flavor ?: mapOf(),
                    variation?.craft?.get("standard") ?: 0,
                    variation?.mill?.get("standard") ?: 0,
                    it.categories ?: listOf(),
                    it.keywords ?: listOf(),
                    it.loyalties ?: listOf(),
                    it.related ?: listOf(),
                    variation?.availability ?: ""
                )
            } else {
                null
            }
        }
    }
}