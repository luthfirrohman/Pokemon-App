package com.luthfirrohman.movieapps.favorite.favorite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import com.google.android.material.tabs.TabLayoutMediator
import com.luthfirrohman.movieapps.R
import com.luthfirrohman.movieapps.favorite.databinding.FragmentFavoriteBinding
import com.luthfirrohman.movieapps.favorite.di.favoriteModule
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadKoinModules(favoriteModule)

        val sectionsPagerAdapter = activity?.let { SectionsPagerAdapter(it) }
        binding?.apply {
            viewPager.adapter = sectionsPagerAdapter
            TabLayoutMediator(
                layoutTabLayout,
                viewPager
            ) { tab, position ->
                tab.text = resources.getString(TAB_TITLES[position])
            }.attach()
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onDestroy() {
        super.onDestroy()

        unloadKoinModules(favoriteModule)
        binding?.viewPager?.adapter = null
        _binding = null
    }


    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.movie,
            R.string.tv_show
        )
    }

}