package com.mvi_state_automation.example.ui.screen.dashboard

import android.view.View
import androidx.lifecycle.ViewModel
import com.mvi_state_automation.example.domain.model.PokemonData
import com.mvi_state_automation.example.domain.model.PokemonDisplayData
import com.mvi_state_automation.example.domain.payload.GetPokemonListPayload
import com.mvi_state_automation.example.domain.repo.PokemonRepository
import com.mvi_state_automation.example.util.ButtonState
import com.mvi_state_automation.example.util.auto
import com.mvi_state_automation.example.util.updateOn
import com.singularity_code.codebase.pattern.VMData
import com.singularity_code.codebase.util.flow.provider
import com.singularity_code.codebase.util.flow.register
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

sealed class Intent {
    class LoadData : Intent()

    class FilterList(
        val filterByName: String
    ) : Intent()

    class SelectPokemon(
        var pokemonData: PokemonData
    ) : Intent()
}

class DashboardViewModel(
    private val pokemonRepository: PokemonRepository
) : ViewModel() {

    companion object {
        val MOD = module {
            /** declare modules here **/
            viewModel {
                DashboardViewModel(
                    get()
                )
            }
        }
    }

    val inputIntent by register<Intent?>(null)

    private val filterByName by auto {
        /** internal state **/
        val register by register("")

        /** updater **/
        updateOn(
            inputIntent.data to false
        ) {
            /** data **/
            val intent = inputIntent.data.first() ?: return@updateOn

            /** logic here **/
            if (intent !is Intent.FilterList) return@updateOn

            /** update filter **/
            val filter = intent.filterByName

            /** trigger state update **/
            register.set(filter)
        }

        /** return internal state **/
        register
    }

    private val selectedPokemon by auto {
        /** internal state **/
        val register by register<PokemonData?>(null)

        /** updater **/
        updateOn(
            inputIntent.data to false
        ) {
            /** data **/
            val intent = inputIntent.data.first() ?: return@updateOn

            /** logic here **/
            if (intent !is Intent.SelectPokemon) return@updateOn

            /** update selected pokemon **/
            val selectedPokemon = intent.pokemonData

            /** trigger state update **/
            register.set(selectedPokemon)
        }

        /** return internal state **/
        register
    }

    private val pokemonListProvider by auto {
        /** internal entity **/
        val provider by provider(
            pokemonRepository::getPokemonList
        )

        /** updater **/
        updateOn(
            inputIntent.data to false
        ) {
            /** data **/
            val intent = inputIntent.data.first() ?: return@updateOn

            /** logic here **/
            /** handling intent **/
            if (intent !is Intent.LoadData) return@updateOn

            val payload = GetPokemonListPayload()

            /** trigger provider update **/
            provider.update(payload)
        }

        /** return internal entity **/
        provider
    }

    val pokemonListDisplay by auto {
        /** internal state **/
        val state = MutableStateFlow(listOf<PokemonDisplayData>())

        /** updater **/
        updateOn(
            pokemonListProvider.success to false,
            selectedPokemon.data to false,
            filterByName.data to false
        ) {
            /** data **/
            val pokemonListData = pokemonListProvider.success.first().second ?: listOf()
            val selectedPokemon = selectedPokemon.data.first()
            val fiterByName = filterByName.data.first()

            /** logic here **/
            val pokemonListDisplay = pokemonListData
                .filter {
                    it.name?.contains(fiterByName) ?: false
                }
                .map {
                    PokemonDisplayData(
                        pokemonData = it,
                        selected = selectedPokemon?.id == it.id,
                    )
                }

            /** trigger state update **/
            state.emit(pokemonListDisplay)
        }

        /** return internal flow **/
        state.asStateFlow()
    }

    val loadingScreenVisibility by auto {
        pokemonListProvider.loading.map {
            if(it) View.VISIBLE else View.GONE
        }
    }

    val buttonState by auto {
        /** internal state **/
        val state = MutableStateFlow(ButtonState.Default)

        /** updater **/
        updateOn(
            pokemonListProvider.state to false
        ) {
            /** data **/
            val providerState = pokemonListProvider.state.first() ?: return@updateOn

            /** logic here **/
            val buttonState = when (providerState) {
                is VMData.Loading -> ButtonState.Loading
                is VMData.Success -> ButtonState.Disabled
                else -> ButtonState.Default
            }

            /** trigger state update **/
            state.emit(buttonState)
        }

        /** return internal flow **/
        state.asStateFlow()
    }
}