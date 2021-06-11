package com.luthfirrohman.movieapps.tvshow

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.luthfirrohman.movieapps.core.ui.TvShowAdapter
import com.luthfirrohman.movieapps.databinding.FragmentTvShowBinding
import com.luthfirrohman.movieapps.core.data.Status
import com.luthfirrohman.movieapps.detail.DetailActivity
import org.koin.android.viewmodel.ext.android.viewModel

class TvShowFragment : Fragment() {

    private var _binding: FragmentTvShowBinding? = null
    private val binding get() = _binding as FragmentTvShowBinding
    private val tvShowViewModel: TvShowViewModel by viewModel()
    private var adapter = TvShowAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentTvShowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showProgressBarTvShow(true)

        getTvShowData()

        showRecyclerViewMovies()
    }

    private fun showRecyclerViewMovies() {
        binding.apply {
            rvTvShow.layoutManager = LinearLayoutManager(context)
            rvTvShow.setHasFixedSize(true)
            rvTvShow.isNestedScrollingEnabled = true
            rvTvShow.adapter = adapter
        }
    }

    private fun showProgressBarTvShow(status: Boolean) {
        binding.apply {
            if (status) {
                progressbarTvshow.visibility = View.VISIBLE
                rvTvShow.visibility = View.GONE
            } else {
                progressbarTvshow.visibility = View.GONE
                rvTvShow.visibility = View.VISIBLE
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
        tvShowViewModel.getTvShows().observe(viewLifecycleOwner, {
            if (it != null) {
                when (it.status) {
                    Status.LOADING -> showProgressBarTvShow(true)
                    Status.SUCCESS -> {
                        it.data.let { it1 -> adapter.submitList(it1) }
                        showProgressBarTvShow(false)
                    }
                    Status.ERROR -> {
                        showProgressBarTvShow(false)
                        Toast.makeText(requireActivity(), "Error", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }
}