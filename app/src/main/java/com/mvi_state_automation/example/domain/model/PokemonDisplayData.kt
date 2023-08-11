package com.mvi_state_automation.example.domain.model

data class PokemonDisplayData(
    val pokemonData: PokemonData,
    val selected: Boolean = false
)