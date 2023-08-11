package com.mvi_state_automation.example.domain.payload

import com.singularity_code.codebase.pattern.Payload

data class GetPokemonListPayload(
    val nothing: Unit = Unit
) : Payload