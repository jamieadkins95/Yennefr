package com.jamieadkins.decktracker.data

import com.google.gson.Gson
import com.jamieadkins.gwent.card.data.CardRepositoryImpl
import com.jamieadkins.gwent.domain.card.repository.CardRepository
import dagger.Binds
import dagger.Lazy
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@Module
abstract class CardPredictorDataModule {

    @Binds
    @DeckTrackerScope
    abstract fun cardRepository(cardRepository: CardRepositoryImpl): CardRepository

    @Module
    companion object {

        @JvmStatic
        @Provides
        @DeckTrackerScope
        fun providesCardPredictorRetrofit(okHttpClient: Lazy<OkHttpClient>, gson: Gson): Retrofit {
            return Retrofit.Builder()
                .baseUrl("https://us-central1-gwent-9e62a.cloudfunctions.net/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .callFactory { okHttpClient.get().newCall(it) }
                .build()
        }

        @JvmStatic
        @Provides
        @DeckTrackerScope
        fun providesCardPredictorService(retrofit: Retrofit): CardPredictorApi = retrofit.create(CardPredictorApi::class.java)
    }
}