package com.luthfirrohman.movieapps.detail

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.luthfirrohman.movieapps.R
import com.luthfirrohman.movieapps.core.data.Resource
import com.luthfirrohman.movieapps.core.domain.model.Movie
import com.luthfirrohman.movieapps.databinding.ActivityDetailBinding
import com.luthfirrohman.movieapps.core.data.Status
import org.koin.android.viewmodel.ext.android.viewModel

class DetailActivity : AppCompatActivity() {

    private lateinit var activityDetailBinding: ActivityDetailBinding
    private val detailViewModel: DetailViewModel by viewModel()
    private var menu: Menu? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityDetailBinding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(activityDetailBinding.root)
        supportActionBar?.apply {
            setBackgroundDrawable(ResourcesCompat.getDrawable(resources, R.color.bg_primary, null))
            setDisplayHomeAsUpEnabled(true)
            title = "Detail"
        }
        val id = intent.getIntExtra(EXTRA_ID, 0)
        val type = intent.getStringExtra(EXTRA_TYPE)
        showProgressBarDetails(true)
        if (id != 0 && type != null) {
            getDetails(id, type)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun showProgressBarDetails(status: Boolean) {
        activityDetailBinding.apply {
            if (status) {
                progressbarDetail.visibility = View.VISIBLE
                container.visibility = View.GONE
            } else {
                progressbarDetail.visibility = View.GONE
                container.visibility = View.VISIBLE
            }
        }
    }

    private fun getDetails(id: Int, type: String) {
        if (type.equals(TYPE_MOVIE, ignoreCase = true)) {
            detailViewModel.setSelectedMovie(id)
            detailViewModel.detailMovie.observe(this, {
                if (it != null) {
                    setContent(it)
                }
            })
        } else if (type.equals(TYPE_TV, ignoreCase = true)) {
            detailViewModel.setSelectedTvShow(id)
            detailViewModel.detailTvShow.observe(this, {
                if (it != null) {
                    setContent(it)
                }
            })
        }
    }

    private fun populateContent(content: Movie) {
        Glide.with(this)
            .load("https://image.tmdb.org/t/p/w500/${content.posterPath}")
            .apply(RequestOptions().transform(RoundedCorners(10)).override(90, 120))
            .into(activityDetailBinding.imgDetailPoster)

        Glide.with(this)
            .load("https://image.tmdb.org/t/p/w500/${content.backdropPath}")
            .apply(RequestOptions().centerCrop())
            .into(activityDetailBinding.imgDetailBackdrop)

        activityDetailBinding.apply {
            tvTitle.text = content.title
            tvTagline.text = getString(R.string.fill_tagline, content.tagline)
            tvDuration.text = getString(R.string.fill_duration, content.runtime.toString())
            tvDetailRating.text = content.voteAverage.toString()
            tvRelease.text = content.releaseDate
            tvDesc.text = content.overview

            supportActionBar?.title = content.title

            btnShare.setOnClickListener {
                val data =
                    "Watch -${content.title}- with ${content.voteAverage} Ratings only at https://www.themoviedb.org/"
                val shareData = Intent()
                shareData.apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, data)
                    type = "text/plain"
                }
                startActivity(Intent.createChooser(shareData, "Share to :"))
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_detail, menu)
        this.menu = menu
        val type = intent.getStringExtra(EXTRA_TYPE)
        if (type.equals(TYPE_MOVIE, ignoreCase = true)) {
            detailViewModel.detailMovie.observe(this, {
                if (it != null) {
                    setState(it)
                }
            })
        } else if (type.equals(TYPE_TV, ignoreCase = true)) {
            detailViewModel.detailTvShow.observe(this, {
                if (it != null) {
                    setState(it)
                }
            })
        }
        return true
    }

    private fun setState(data: Resource<Movie>) {
        return when (data.status) {
            Status.LOADING -> showProgressBarDetails(true)
            Status.SUCCESS -> if (data.data != null) {
                val state = data.data?.isFavorite
                state?.let { setFavorite(it) }
                showProgressBarDetails(false)
            } else {
                showProgressBarDetails(true)
            }
            Status.ERROR -> {
                showProgressBarDetails(false)
            }
        }
    }

    private fun setContent(data: Resource<Movie>) {
        return when (data.status) {
            Status.LOADING -> showProgressBarDetails(true)
            Status.SUCCESS -> {
                populateContent(data.data as Movie)
                showProgressBarDetails(false)
            }
            Status.ERROR -> {
                showProgressBarDetails(false)
                Toast.makeText(this, getString(R.string.error), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_favorite) {
            val type = intent.getStringExtra(EXTRA_TYPE)
            if (type.equals(TYPE_MOVIE, ignoreCase = true)) {
                detailViewModel.setFavoriteMovie()
            } else if (type.equals(TYPE_TV, ignoreCase = true)) {
                detailViewModel.setFavoriteTvShow()
            }
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setFavorite(state: Boolean) {
        if (menu == null) return
        val menuItem = menu?.findItem(R.id.action_favorite)
        if (state) {
            menuItem?.icon = ContextCompat.getDrawable(this, R.drawable.ic_favorite_on)
        } else {
            menuItem?.icon = ContextCompat.getDrawable(this, R.drawable.ic_favorite_off)
        }
    }

    companion object {
        const val EXTRA_ID = "extra_id"
        const val EXTRA_TYPE = "extra_type"
        const val TYPE_MOVIE = "MOVIE"
        const val TYPE_TV = "TV"
    }
}