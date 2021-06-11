package com.luthfirrohman.movieapps.movie

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.luthfirrohman.movieapps.core.ui.MovieAdapter
import com.luthfirrohman.movieapps.databinding.FragmentMovieBinding
import com.luthfirrohman.movieapps.core.data.Status
import com.luthfirrohman.movieapps.detail.DetailActivity
import org.koin.android.viewmodel.ext.android.viewModel


class MovieFragment : Fragment() {

    private var _binding: FragmentMovieBinding? = null
    private val binding get() = _binding as FragmentMovieBinding
    private val movieViewModel: MovieViewModel by viewModel()
    private var adapter = MovieAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMovieBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showProgressBarMovie(true)

        getMoviesData()

        showRecyclerViewMovies()
    }

    private fun showRecyclerViewMovies() {
        binding.apply {
            rvMovie.layoutManager = LinearLayoutManager(context)
            rvMovie.setHasFixedSize(true)
            rvMovie.isNestedScrollingEnabled = true
            rvMovie.adapter = adapter
        }
    }

    private fun showProgressBarMovie(status: Boolean) {
        binding.apply {
            if (status) {
                progressbarMovie.visibility = View.VISIBLE
                rvMovie.visibility = View.GONE
            } else {
                progressbarMovie.visibility = View.GONE
                rvMovie.visibility = View.VISIBLE
            }
        }
    }

    private fun getMoviesData() {
        adapter.onItemClick = {
            val intent = Intent(activity, DetailActivity::class.java)
            intent.apply {
                putExtra(DetailActivity.EXTRA_ID, it.id)
                putExtra(DetailActivity.EXTRA_TYPE, DetailActivity.TYPE_MOVIE)
            }
            startActivity(intent)
        }
        movieViewModel.getMovies().observe(viewLifecycleOwner, {
            if (it != null) {
                when (it.status) {
                    Status.LOADING -> showProgressBarMovie(true)
                    Status.SUCCESS -> {
                        adapter.submitList(it.data)
                        showProgressBarMovie(false)
                    }
                    Status.ERROR -> {
                        showProgressBarMovie(false)
                        Toast.makeText(requireActivity(), "Error", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }
}