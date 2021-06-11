package com.luthfirrohman.movieapps.favorite.favorite

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class SectionsPagerAdapter(fragment: FragmentActivity) : FragmentStateAdapter(fragment) {

    private val fragment = arrayOf(
        FavoriteMovieFragment(),
        FavoriteTvShowFragment()
    )

    override fun getItemCount(): Int = fragment.size

    override fun createFragment(position: Int): Fragment = fragment[position]
}