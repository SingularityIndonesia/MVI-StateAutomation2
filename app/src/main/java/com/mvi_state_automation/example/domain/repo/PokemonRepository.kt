package com.mvi_state_automation.example.domain.repo

import arrow.core.Either
import com.mvi_state_automation.example.domain.model.PokemonData
import com.mvi_state_automation.example.domain.payload.GetPokemonListPayload
import com.singularity_code.codebase.util.serialization.ErrorMessage
import kotlinx.coroutines.flow.Flow

interface PokemonRepository {
    fun getPokemonList(
        payload: GetPokemonListPayload
    ): Flow<Either<ErrorMessage, List<PokemonData>>>
}