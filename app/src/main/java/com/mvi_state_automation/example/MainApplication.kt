package com.mvi_state_automation.example

import android.app.Application
import com.mvi_state_automation.example.data.PokemonDataSource
import com.mvi_state_automation.example.ui.screen.dashboard.DashboardViewModel
import org.koin.core.context.GlobalContext.startKoin

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(
                PokemonDataSource.MOD,
                DashboardViewModel.MOD
            )
        }
    }
}