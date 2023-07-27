package com.square.game.ui.moves

import android.annotation.SuppressLint
import android.content.Context.VIBRATOR_SERVICE
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.MotionEvent
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.square.game.databinding.FragmentMovesBinding
import com.square.game.databinding.FragmentTimedBinding
import com.square.game.domain.game.adapter.GameAdapter
import com.square.game.domain.other.AppSharedPrefs
import com.square.game.domain.other.Game
import com.square.game.ui.endless.FragmentEndlessDirections
import com.square.game.ui.game.CallbackViewModel
import com.square.game.ui.other.ViewBindingFragment
import kotlinx.coroutines.launch


class FragmentMoves : ViewBindingFragment<FragmentMovesBinding>(FragmentMovesBinding::inflate) {
    private val callbackViewModel: CallbackViewModel by activityViewModels()
    private val viewModel: MovesViewModel by viewModels()
    private val sp by lazy {
        AppSharedPrefs(requireContext())
    }
    private lateinit var gameAdapter: GameAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        viewModel.vibroCallback = {
            if (sp.getVibro()) {
                if (Build.VERSION.SDK_INT >= 26) {
                    (requireActivity().getSystemService(VIBRATOR_SERVICE) as Vibrator).vibrate(
                        VibrationEffect.createOneShot(150, VibrationEffect.DEFAULT_AMPLITUDE)
                    )
                } else {
                    (requireActivity().getSystemService(VIBRATOR_SERVICE) as Vibrator).vibrate(150)
                }
            }
        }

        binding.buttonClose.setOnClickListener {
            findNavController().popBackStack()

        }

        callbackViewModel.callback = {
            viewModel.pauseValue = false
        }

        binding.buttonSettings.setOnClickListener {
            viewModel.pauseValue = true
            findNavController().navigate(FragmentMovesDirections.actionFragmentMovesToDialogOptions(Game.MOVES, true))
        }

        viewModel.list.observe(viewLifecycleOwner) {
            gameAdapter.items = it.toMutableList()
            gameAdapter.notifyDataSetChanged()
        }

        viewModel.moves.observe(viewLifecycleOwner) { moves ->
            binding.movesTextView.text = moves.toString()

            if (viewModel.gameValue && !viewModel.pauseValue && moves == 0) {
                end()
            }
        }

        viewModel.scores.observe(viewLifecycleOwner) {
            binding.scoresTextView.text = it.toString()
        }
    }

    private fun end() {
        viewModel.gameValue = false
        viewModel.stopTimer()
        findNavController().navigate(
            FragmentMovesDirections.actionFragmentMovesToDialogEnd(
                viewModel.scores.value!!,
                Game.MOVES
            )
        )
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initAdapter() {
        gameAdapter = GameAdapter()
        with(binding.gameRv) {
            adapter = gameAdapter
            layoutManager = GridLayoutManager(requireContext(), 6).apply {
                orientation = GridLayoutManager.VERTICAL
            }
            setHasFixedSize(true)
            itemAnimator = null
            overScrollMode = RecyclerView.OVER_SCROLL_NEVER
            addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
                override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                    val childView = rv.findChildViewUnder(e.x, e.y)
                    if (childView != null) {
                        val position = rv.getChildAdapterPosition(childView)
                        lifecycleScope.launch {
                            viewModel.selectItem(position)
                        }
                        if (e.action == MotionEvent.ACTION_UP || e.action == MotionEvent.ACTION_CANCEL) {
                            viewModel.checkSelectedItems()
                        }
                    }
                    return false
                }

                override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}

                override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
            })
        }
    }
}