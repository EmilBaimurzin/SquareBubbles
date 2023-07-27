package com.square.game.domain.game.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.square.game.R
import com.square.game.databinding.ItemGameBinding

class GameAdapter : RecyclerView.Adapter<GameViewHolder>() {
    var items = mutableListOf<GameItem>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        return GameViewHolder(
            ItemGameBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ), parent.context
        )
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        holder.bind(items[position])
    }
}

class GameViewHolder(private val binding: ItemGameBinding, private val context: Context) : RecyclerView.ViewHolder(binding.root) {
    @SuppressLint("ClickableViewAccessibility")
    fun bind(item: GameItem) {

        if (item.isSelected) {
            binding.root.foreground = ResourcesCompat.getDrawable(context.resources, R.drawable.bg_selected, null)
        } else {
            binding.root.foreground = null
        }

        when (item.value) {
            1 -> binding.imgSymbol.setImageResource(R.drawable.symbol01)
            2 -> binding.imgSymbol.setImageResource(R.drawable.symbol02)
            3 -> binding.imgSymbol.setImageResource(R.drawable.symbol03)
            4 -> binding.imgSymbol.setImageResource(R.drawable.symbol04)
            5 -> binding.imgSymbol.setImageResource(R.drawable.symbol05)
            else -> binding.imgSymbol.setImageResource(R.drawable.symbol06)
        }
    }
}
