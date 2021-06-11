package com.luthfirrohman.movieapps.core.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.luthfirrohman.movieapps.core.databinding.ItemRowMovieBinding
import com.luthfirrohman.movieapps.core.domain.model.Movie

class TvShowAdapter :
    PagedListAdapter<Movie, TvShowAdapter.ListViewHolder>(DIFF_CALLBACK) {

    var onItemClick: ((Movie) -> Unit)? = null

    inner class ListViewHolder(itemView: ItemRowMovieBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        private val binding = ItemRowMovieBinding.bind(itemView.root)
        private val imgPhoto = itemView.imgPoster
        private val tvTitle = itemView.tvItemTitle
        private val tvDesc = itemView.tvItemDesc
        private val tvRating = itemView.tvRating
        fun bind(movie: Movie) {
            tvTitle.text = movie.title
            tvDesc.text = movie.overview
            tvRating.text = movie.voteAverage.toString()

            Glide.with(itemView.context)
                .load("https://image.tmdb.org/t/p/w500/${movie.posterPath}")
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
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Movie>() {
            override fun areItemsTheSame(
                oldItem: Movie,
                newItem: Movie
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: Movie,
                newItem: Movie
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

}