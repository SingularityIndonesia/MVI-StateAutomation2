package com.mvi_state_automation.example.ui.screen.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.fragment.app.Fragment
import com.mvi_state_automation.example.databinding.ScreenDashboardBinding
import com.mvi_state_automation.example.domain.model.PokemonDisplayData
import com.mvi_state_automation.example.ui.adapter.PokemonListAdapter
import com.mvi_state_automation.example.util.ButtonState
import com.singularity_code.codebase.util.flow.collect
import org.koin.androidx.viewmodel.ext.android.viewModel

class DashboardScreen : Fragment() {
    private lateinit var binding: ScreenDashboardBinding
    private val viewModel: DashboardViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ScreenDashboardBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        setupPokemonList()
        setupLoadingScreen()
        setupSearch()
        setupButton()
    }

    private fun setupPokemonList() {
        val adapter = setupAdapter()
        setupListData(adapter)
    }

    private fun setupAdapter(): PokemonListAdapter {
        val adapter = PokemonListAdapter {
            viewModel.inputIntent.set(
                Intent.SelectPokemon(
                    pokemonData = it.pokemonData
                )
            )
        }

        val list = mutableListOf<PokemonDisplayData>()
        adapter.submitList(list)

        binding.rv.adapter = adapter

        return adapter
    }

    private fun setupListData(
        adapter: PokemonListAdapter
    ) {
        collect(
            viewModel.pokemonListDisplay,
            adapter::submitList
        )
    }

    private fun setupLoadingScreen() {
        // visibility
        collect(
            viewModel.loadingScreenVisibility,
            binding.loadingScreen::setVisibility
        )
    }

    private fun setupSearch() {
        binding.search.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.inputIntent.set(
                    Intent.FilterList(query ?: "")
                )

                return true
            }

            override fun onQueryTextChange(
                newText: String?
            ): Boolean {
                return false
            }
        })
    }

    private fun setupButton() {
        // action
        binding.loadBtn.setOnClickListener {
            viewModel.inputIntent.set(
                Intent.LoadData()
            )
        }

        // state
        collect(viewModel.buttonState) {
            when (it) {
                ButtonState.Loading -> setButtonAppearanceLoading()
                ButtonState.Disabled -> setButtonAppearanceDisabled()
                ButtonState.Default -> setButtonAppearanceDefault()
            }
        }
    }

    private fun setButtonAppearanceLoading() {
        binding.loadBtn.text = "Loading.."
        binding.loadBtn.isClickable = false
    }

    private fun setButtonAppearanceDisabled() {
        binding.loadBtn.text = "Load Data"
        binding.loadBtn.isEnabled = false
    }

    private fun setButtonAppearanceDefault() {
        binding.loadBtn.text = "Load Data"
        binding.loadBtn.isEnabled = true
        binding.loadBtn.isClickable = true
    }

}