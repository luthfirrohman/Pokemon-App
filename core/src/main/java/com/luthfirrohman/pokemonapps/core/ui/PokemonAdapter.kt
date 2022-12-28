package com.luthfirrohman.pokemonapps.core.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.luthfirrohman.pokemonapps.core.data.source.remote.response.ResultsItem
import com.luthfirrohman.pokemonapps.core.databinding.ItemRowMovieBinding
import com.luthfirrohman.pokemonapps.core.domain.model.Pokemon

class MovieAdapter :
    PagedListAdapter<Pokemon, MovieAdapter.ListViewHolder>(DIFF_CALLBACK) {
    var onItemClick: ((Pokemon) -> Unit)? = null


    inner class ListViewHolder(itemView: ItemRowMovieBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        private val binding = ItemRowMovieBinding.bind(itemView.root)
        private val imgPhoto = itemView.imgPoster
        private val tvTitle = itemView.tvItemTitle
        fun bind(pokemon: Pokemon) {
            tvTitle.text = pokemon.title


            Glide.with(itemView.context)
                .load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/back/${pokemon.tagline!!.substring(pokemon.tagline!!.length - 3, pokemon.tagline!!.length - 1)}.png")
                .apply(RequestOptions().transform(RoundedCorners(10)).override(90, 120))
                .into(imgPhoto)
        }

        init {
            binding.root.setOnClickListener {
                getItem(bindingAdapterPosition)?.let { it1 -> onItemClick?.invoke(it1) }
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ListViewHolder {
        val view =
            ItemRowMovieBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Pokemon>() {
            override fun areItemsTheSame(
                oldItem: Pokemon,
                newItem: Pokemon
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: Pokemon,
                newItem: Pokemon
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

}

