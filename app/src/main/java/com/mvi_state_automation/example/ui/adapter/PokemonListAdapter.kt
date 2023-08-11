package com.mvi_state_automation.example.ui.adapter

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mvi_state_automation.example.databinding.ItemPokemonBinding
import com.mvi_state_automation.example.domain.model.PokemonDisplayData
import com.mvi_state_automation.example.util.getColorAttribute

class PokemonListAdapter(
    private val onClick: (PokemonDisplayData) -> Unit
) : ListAdapter<PokemonDisplayData, PokemonListAdapter.PokemonItemHolder>(DummyDiffer) {

    class PokemonItemHolder(
        private val binding: ItemPokemonBinding,
        private val onClick: (PokemonDisplayData) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun onBind(
            data: PokemonDisplayData
        ) {
            // data
            binding.pokemonName.text = data.pokemonData.name
            binding.pokemonPower.text = data.pokemonData.power

            // appearance
            val context = binding.root.context
            binding.card.setCardBackgroundColor(
                ColorStateList.valueOf(
                    context.getColorAttribute(
                        if (data.selected)
                            com.google.android.material.R.attr.colorPrimaryContainer
                        else
                            com.google.android.material.R.attr.colorSurface
                    )
                )
            )

            // action
            binding.root.setOnClickListener {
                onClick.invoke(data)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PokemonItemHolder {
        return PokemonItemHolder(
            ItemPokemonBinding.inflate(LayoutInflater.from(parent.context)),
            onClick
        )
    }

    override fun onBindViewHolder(
        holder: PokemonItemHolder,
        position: Int
    ) {
        holder.onBind(
            currentList[position]
        )
    }
}

private object DummyDiffer : DiffUtil.ItemCallback<PokemonDisplayData>() {
    override fun areItemsTheSame(
        oldItem: PokemonDisplayData,
        newItem: PokemonDisplayData
    ): Boolean {
        return oldItem.pokemonData.id == newItem.pokemonData.id
    }

    override fun areContentsTheSame(
        oldItem: PokemonDisplayData,
        newItem: PokemonDisplayData
    ): Boolean {
        return oldItem.hashCode() == newItem.hashCode()
    }

}