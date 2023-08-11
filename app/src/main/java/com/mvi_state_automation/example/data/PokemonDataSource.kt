package com.mvi_state_automation.example.data

import arrow.core.Either
import arrow.core.Either.Right
import com.mvi_state_automation.example.domain.model.PokemonData
import com.mvi_state_automation.example.domain.payload.GetPokemonListPayload
import com.mvi_state_automation.example.domain.repo.PokemonRepository
import com.singularity_code.codebase.util.serialization.ErrorMessage
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.koin.dsl.binds
import org.koin.dsl.module

class PokemonDataSource : PokemonRepository {

    companion object {
        val MOD = module {
            /** declare modules here **/
            factory {
                PokemonDataSource()
            }.binds(
                arrayOf(
                    PokemonRepository::class
                )
            )
        }
    }

    override fun getPokemonList(
        payload: GetPokemonListPayload
    ): Flow<Either<ErrorMessage, List<PokemonData>>> = flow {

        // emulate loading
        delay(3000)

        // emit fake data
        val power = listOf("Cuteness", "Fire", "Water")
        val pokemonList = (1..20).map {
            PokemonData(
                id = it.toString(),
                name = "Pokemon $it",
                power = power.random()
            )
        }.let {
            Right(it)
        }

        emit(pokemonList)
    }
}
