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
import com.luthfirrohman.movieapps.favorite.databinding.FragmentFavoriteTvShowBinding
import com.luthfirrohman.movieapps.favorite.di.favoriteModule
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules

class FavoriteTvShowFragment : Fragment() {

    private var _binding: FragmentFavoriteTvShowBinding? = null
    private val binding get() = _binding
    private val favoriteViewModel: FavoriteViewModel by viewModel()
    private var adapter = FavoriteAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentFavoriteTvShowBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadKoinModules(favoriteModule)

        itemTouchHelper.attachToRecyclerView(binding?.rvFavTvShow)

        showProgressBarTvShow(true)

        getTvShowData()

        showRecyclerViewMovies()
    }

    private fun showRecyclerViewMovies() {
        binding?.apply {
            rvFavTvShow.layoutManager = LinearLayoutManager(context)
            rvFavTvShow.setHasFixedSize(true)
            rvFavTvShow.isNestedScrollingEnabled = true
            rvFavTvShow.adapter = adapter
        }
    }

    private fun showProgressBarTvShow(status: Boolean) {
        binding?.apply {
            if (status) {
                progressbarTvshow.visibility = View.VISIBLE
                rvFavTvShow.visibility = View.GONE
            } else {
                progressbarTvshow.visibility = View.GONE
                rvFavTvShow.visibility = View.VISIBLE
            }
        }
    }

    private fun getTvShowData() {
        adapter.onItemClick = {
            val intent = Intent(activity, DetailActivity::class.java)
            intent.apply {
                putExtra(DetailActivity.EXTRA_ID, it.id)
                putExtra(DetailActivity.EXTRA_TYPE, DetailActivity.TYPE_TV)
            }
            startActivity(intent)
        }
        favoriteViewModel.getFavoriteTvShows().observe(viewLifecycleOwner, { tv ->
            if (tv != null) {
                adapter.submitList(tv)
                showProgressBarTvShow(false)
            }
            if (tv.toString() == "[]") {
                binding?.emptyImage?.visibility = View.VISIBLE
                binding?.emptyText?.visibility = View.VISIBLE
            }
        })
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
                val tvShowsEntity = adapter.getSwipedData(swipedPosition)

                MaterialAlertDialogBuilder(requireActivity())
                    .setTitle("Confirm")
                    .setMessage(
                        resources.getString(
                            R.string.fill_remove_action,
                            tvShowsEntity?.title
                        )
                    )

                    .setNegativeButton(resources.getString(R.string.cancel)) { _, _ ->
                        getTvShowData()
                        showRecyclerViewMovies()
                    }
                    .setPositiveButton("YES") { _, _ ->
                        tvShowsEntity?.let { favoriteViewModel.setFavoriteTvShow(it) }
                        val snackBar = Snackbar.make(
                            view as View,
                            getString(R.string.fill_remove_action, tvShowsEntity?.title),
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
        binding?.rvFavTvShow?.adapter = null
    }
}