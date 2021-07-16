package com.stacktivity.vknews.ui

import android.animation.AnimatorInflater
import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.Interpolator
import android.viewbinding.library.fragment.viewBinding
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DiffUtil
import com.stacktivity.vknews.R
import com.stacktivity.vknews.databinding.NewsScreenBinding
import com.yuyakaido.android.cardstackview.*
import java.util.ArrayList

class NewsFragment : Fragment(R.layout.news_screen), CardStackListener {

    private val mTag: String = NewsFragment::class.java.simpleName
    private val manager by lazy { CardStackLayoutManager(requireContext(), this) }
    private val adapter by lazy { CardStackAdapter(createSpots()) }

    private val binding: NewsScreenBinding by viewBinding()

    private val viewModel: NewsViewModel by lazy {
        ViewModelProvider(this).get(NewsViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
        initialize()
        setupButton()
    }

    private fun setupToolbar() {
        requireActivity().setActionBar(binding.toolbar)
        requireActivity().actionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_toolbar_back)
        }
    }

    private fun initialize() {
        val overlayInterpolator = Interpolator { if (it < 0.5) it * 2 else 1f }

        manager.apply {
            setStackFrom(StackFrom.Bottom)
            setVisibleCount(2)
            setTranslationInterval(20.0f)
            setScaleInterval(0.85f)
            setSwipeThreshold(0.2f)
            setMaxDegree(120.0f)
            setDirections(Direction.HORIZONTAL)
            setCanScrollHorizontal(true)
            setCanScrollVertical(false)
            setSwipeableMethod(SwipeableMethod.AutomaticAndManual)
            setOverlayInterpolator(overlayInterpolator)
        }

        binding.cardStackView.apply {
            layoutManager = manager
            adapter = this@NewsFragment.adapter
            itemAnimator.apply {
                if (this is DefaultItemAnimator) {
                    supportsChangeAnimations = false
                }
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupButton() {

        binding.buttonLike.stateListAnimator = AnimatorInflater.loadStateListAnimator(context,
            R.animator.action_button_focus_animator)

        binding.buttonDislike.stateListAnimator = AnimatorInflater.loadStateListAnimator(context,
            R.animator.action_button_focus_animator)

        binding.buttonDislike.setOnClickListener {
            val setting = SwipeAnimationSetting.Builder()
                .setDirection(Direction.Left)
                .setDuration(Duration.Normal.duration)
                .setInterpolator(AccelerateInterpolator())
                .build()
            manager.setSwipeAnimationSetting(setting)
            binding.cardStackView.swipe()
        }

        binding.buttonLike.setOnClickListener {
            val setting = SwipeAnimationSetting.Builder()
                .setDirection(Direction.Right)
                .setDuration(Duration.Normal.duration)
                .setInterpolator(AccelerateInterpolator())
                .build()
            manager.setSwipeAnimationSetting(setting)
            binding.cardStackView.swipe()
        }
    }

    private fun paginate() {
        val old = adapter.getSpots()
        val new = old.plus(createSpots())
        val callback = NewsItemDiffCallback(old, new)
        val result = DiffUtil.calculateDiff(callback)
        adapter.setSpots(new)
        result.dispatchUpdatesTo(adapter)
    }

    private fun createSpots(): List<NewsItem> {
        val spots = ArrayList<NewsItem>()
        spots.add(
            NewsItem(
                name = "Kyoto: Yasaka Shrine",
                timePassed = "час назад",
                imageUrl = "https://source.unsplash.com/Xq1ntWruZQI/600x800"
            )
        )
        spots.add(
            NewsItem(
                name = "Kyoto: Fushimi Inari Shrine",
                timePassed = "три часа назад",
                imageUrl = "https://source.unsplash.com/NYyCqdBOKwc/600x800"
            )
        )
        spots.add(
            NewsItem(
                name = "Kyoto: Bamboo Forest",
                timePassed = "сегодня в 8:30",
                imageUrl = "https://source.unsplash.com/buF62ewDLcQ/600x800"
            )
        )
        spots.add(
            NewsItem(
                name = "New York: Brooklyn Bridge",
                timePassed = "три часа назад",
                imageUrl = "https://source.unsplash.com/THozNzxEP3g/600x800"
            )
        )
        spots.add(
            NewsItem(
                name = "Empire State Building",
                timePassed = "год назад",
                imageUrl = "https://source.unsplash.com/USrZRcRS2Lw/600x800"
            )
        )
        spots.add(
            NewsItem(
                name = "The statue of Liberty",
                timePassed = "месяц назад",
                imageUrl = "https://source.unsplash.com/PeFk7fzxTdk/600x800"
            )
        )
        spots.add(
            NewsItem(
                name = "Louvre Museum",
                timePassed = "вчера в 18:00",
                imageUrl = "https://source.unsplash.com/LrMWHKqilUw/600x800"
            )
        )
        spots.add(
            NewsItem(
                name = "Paris: Eiffel Tower",
                timePassed = "вчера в 18:00",
                imageUrl = "https://source.unsplash.com/HN-5Z6AmxrM/600x800"
            )
        )
        spots.add(
            NewsItem(
                name = "London: Big Ben",
                timePassed = "вчера в 18:00",
                imageUrl = "https://source.unsplash.com/CdVAUADdqEc/600x800"
            )
        )
        spots.add(
            NewsItem(
                name = "China: Great Wall of China",
                timePassed = "вчера в 18:00",
                imageUrl = "https://source.unsplash.com/AWh9C-QjhE4/600x800"
            )
        )
        return spots
    }


    override fun onCardDragging(direction: Direction, ratio: Float) {
        if (direction == Direction.Left) {
            binding.buttonDislike.customSize
        }
        Log.d(mTag, "onCardDragging: d = ${direction.name}, r = $ratio")
    }

    override fun onCardSwiped(direction: Direction) {
        Log.d(mTag, "onCardSwiped: p = ${manager.topPosition}, d = $direction")
        if (manager.topPosition == adapter.itemCount - 5) {
            paginate()
        }
    }

    override fun onCardRewound() {
        Log.d(mTag, "onCardRewound: ${manager.topPosition}")
    }

    override fun onCardCanceled() {
        Log.d(mTag, "onCardCanceled: ${manager.topPosition}")
    }

    override fun onCardAppeared(view: View, position: Int) {
        Log.d(mTag, "onCardAppeared: ($position)")
    }

    override fun onCardDisappeared(view: View, position: Int) {
        Log.d(mTag, "onCardDisappeared: ($position)")
    }
}