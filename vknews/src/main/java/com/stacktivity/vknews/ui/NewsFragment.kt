package com.stacktivity.vknews.ui

import android.animation.AnimatorInflater
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.Interpolator
import android.viewbinding.library.fragment.viewBinding
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import com.stacktivity.core.utils.FragmentManagers.replaceFragment
import com.stacktivity.vknews.R
import com.stacktivity.vknews.adapter.CardStackAdapter
import com.stacktivity.vknews.databinding.NewsScreenBinding
import com.stacktivity.vknews.utils.launchWhenStarted
import com.vk.api.sdk.VK
import com.yuyakaido.android.cardstackview.*
import kotlinx.coroutines.flow.onEach

class NewsFragment : Fragment(R.layout.news_screen), CardStackListener {

    private val mTag: String = NewsFragment::class.java.simpleName

    private val manager by lazy { CardStackLayoutManager(requireContext(), this) }
    private val adapter by lazy { CardStackAdapter() }

    private val binding: NewsScreenBinding by viewBinding()

    private val viewModel: NewsViewModel by lazy {
        ViewModelProvider(this, NewsViewModelFactory(testMode)).get(NewsViewModel::class.java)
    }

    private var testMode = false

    private val swipeAnimationDuration = Duration.Normal.duration
    private val likeAnimationSetting = SwipeAnimationSetting.Builder()
        .setDirection(Direction.Right)
        .setDuration(swipeAnimationDuration)
        .setInterpolator(AccelerateInterpolator())
        .build()
    private val dislikeAnimationSetting = SwipeAnimationSetting.Builder()
        .setDirection(Direction.Left)
        .setDuration(swipeAnimationDuration)
        .setInterpolator(AccelerateInterpolator())
        .build()

    companion object {
        const val KEY_TEST_MODE = "keyTest"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        testMode = arguments?.getBoolean(KEY_TEST_MODE, false) ?: false

        if (VK.isLoggedIn().not() && testMode.not()) {
            showLoginScreen()
        }

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()
        initUI()
        viewModel.fetchItems()
    }

    private fun setupObservers() {
        setupButtonsListeners()

        viewModel.newsFlow.onEach { news ->
            Log.d(mTag, "get ${news.size} news, adapter: ${adapter.currentList.size}")
            adapter.submitList(news)
            Log.d(mTag, "get ${news.size} news, result: ${adapter.currentList.size}")
        }.launchWhenStarted(lifecycleScope)
    }

    private fun initUI() {
        initNewsView()
        setupButtonsAnimations()
    }

    private fun showLoginScreen() {
        requireActivity().supportFragmentManager.popBackStack()
        replaceFragment(requireActivity().supportFragmentManager, LoginFragment(), R.id.container)
    }

    private fun initNewsView() {
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

    private fun setupButtonsAnimations() {
        binding.buttonLike.stateListAnimator = AnimatorInflater.loadStateListAnimator(
            context,
            R.animator.action_button_focus_animator
        )

        binding.buttonDislike.stateListAnimator = AnimatorInflater.loadStateListAnimator(
            context,
            R.animator.action_button_focus_animator
        )
    }

    private fun setupButtonsListeners() {

        binding.buttonDislike.setOnClickListener {
            enableButtons(false)
            manager.setSwipeAnimationSetting(dislikeAnimationSetting)
            binding.cardStackView.swipe()
            it.postDelayed({
                enableButtons(true)
            }, swipeAnimationDuration.toLong())
        }

        binding.buttonLike.setOnClickListener {
            enableButtons(false)
            manager.setSwipeAnimationSetting(likeAnimationSetting)
            binding.cardStackView.swipe()
            it.postDelayed({
                enableButtons(true)
            }, swipeAnimationDuration.toLong())
        }

        binding.btnBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun enableButtons(enable: Boolean) {
        binding.buttonDislike.isEnabled = enable
        binding.buttonLike.isEnabled = enable
    }

    private fun onNewsLiked() {
        val swipedId = manager.topPosition - 1
        val item = adapter.currentList[swipedId]
        viewModel.onNewsLiked(item)
    }

    private fun onNewsDisliked() {
        val swipedId = manager.topPosition - 1
        val item = adapter.currentList[swipedId]
        viewModel.onNewsDisliked(item)
    }


    // CardStackListener

    override fun onCardDragging(direction: Direction, ratio: Float) {}

    override fun onCardSwiped(direction: Direction) {
        if (direction == Direction.Left) {
            onNewsDisliked()
        } else {
            onNewsLiked()
        }

        if ( adapter.itemCount - manager.topPosition - 20 < 0) {
            viewModel.fetchItems()
        }
    }

    override fun onCardRewound() {}

    override fun onCardCanceled() {}

    override fun onCardAppeared(view: View, position: Int) {}

    override fun onCardDisappeared(view: View, position: Int) {}
}