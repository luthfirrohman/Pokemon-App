package com.luthfirrohman.movieapps.favorite.favorite

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.luthfirrohman.movieapps.R
import com.luthfirrohman.movieapps.core.ui.FavoriteAdapter
import com.luthfirrohman.movieapps.detail.DetailActivity
import com.luthfirrohman.movieapps.favorite.databinding.FragmentFavoriteMovieBinding
import com.luthfirrohman.movieapps.favorite.di.favoriteModule
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules

class FavoriteMovieFragment : Fragment() {

    private var _binding: FragmentFavoriteMovieBinding? = null
    private val binding get() = _binding
    internal val favoriteViewModel: FavoriteViewModel by viewModel()
    private var adapter = FavoriteAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentFavoriteMovieBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadKoinModules(favoriteModule)

        itemTouchHelper.attachToRecyclerView(binding?.rvFavMovie)

        showProgressBarMovie(true)

        getMoviesData()

        showRecyclerViewMovies()
    }

    internal fun showRecyclerViewMovies() {
        binding?.apply {
            rvFavMovie.layoutManager = LinearLayoutManager(context)
            rvFavMovie.setHasFixedSize(true)
            rvFavMovie.isNestedScrollingEnabled = true
            rvFavMovie.adapter = adapter
        }
    }

    private fun showProgressBarMovie(status: Boolean) {
        binding?.apply {
            if (status) {
                progressbarMovie.visibility = View.VISIBLE
                rvFavMovie.visibility = View.GONE
            } else {
                progressbarMovie.visibility = View.GONE
                rvFavMovie.visibility = View.VISIBLE
            }
        }
    }

    internal fun getMoviesData() {
        adapter.onItemClick = {
            val intent = Intent(activity, DetailActivity::class.java)
            intent.apply {
                putExtra(DetailActivity.EXTRA_ID, it.id)
                putExtra(DetailActivity.EXTRA_TYPE, DetailActivity.TYPE_MOVIE)
            }
            startActivity(intent)
        }
        favoriteViewModel.getFavoriteMovies().observe(viewLifecycleOwner) { movies ->
            if (movies != null) {
                adapter.submitList(movies)
                showProgressBarMovie(false)
            }
            if (movies.toString() == "[]") {
                binding?.emptyImage?.visibility = View.VISIBLE
                binding?.emptyText?.visibility = View.VISIBLE
            }
        }
    }

    private val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.Callback() {
        override fun getMovementFlags(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder
        ): Int =
            makeMovementFlags(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT)

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean = true

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            if (view != null) {
                val swipedPosition = viewHolder.bindingAdapterPosition
                val moviesEntity = adapter.getSwipedData(swipedPosition)

                MaterialAlertDialogBuilder(requireActivity())
                    .setTitle("Confirm")
                    .setMessage(
                        resources.getString(
                            R.string.fill_remove_action,
                            moviesEntity?.title
                        )
                    )

                    .setNegativeButton(resources.getString(R.string.cancel)) { _, _ ->
                        getMoviesData()
                        showRecyclerViewMovies()
                    }
                    .setPositiveButton("YES") { _, _ ->
                        moviesEntity?.let { favoriteViewModel.setFavoriteMovie(it) }
                        val snackBar = Snackbar.make(
                            view as View,
                            getString(R.string.fill_remove_action, moviesEntity?.title),
                            Snackbar.LENGTH_LONG
                        )
                        context?.apply {
                            let { ContextCompat.getColor(it, R.color.bg_primary_dark) }.let {
                                snackBar.setBackgroundTint(
                                    it
                                )
                            }
                            let { ContextCompat.getColor(it, R.color.white) }.let {
                                snackBar.setTextColor(
                                    it
                                )
                            }
                        }
                        snackBar.show()
                    }
                    .setCancelable(false)
                    .show()
            }
        }
    })

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        binding?.rvFavMovie?.adapter = null
    }
}