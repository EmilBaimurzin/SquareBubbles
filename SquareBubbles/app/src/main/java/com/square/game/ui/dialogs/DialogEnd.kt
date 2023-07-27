package com.square.game.ui.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.square.game.R
import com.square.game.core.library.ViewBindingDialog
import com.square.game.databinding.DialogEndBinding
import com.square.game.domain.other.AppSharedPrefs
import com.square.game.domain.other.Game

class DialogEnd: ViewBindingDialog<DialogEndBinding>(DialogEndBinding::inflate) {
    private val args: DialogEndArgs by navArgs()
    private val sharedPrefs by lazy {
        AppSharedPrefs(requireContext())
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return Dialog(requireContext(), R.style.Dialog_No_Border)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog!!.setCancelable(false)
        dialog!!.setOnKeyListener { _, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                findNavController().popBackStack(R.id.fragmentMain, false, false)
                true
            } else {
                false
            }
        }

        binding.buttonClose.setOnClickListener {
            findNavController().popBackStack(R.id.fragmentMain, false, false)
        }

        binding.buttonRestart.setOnClickListener {
            findNavController().popBackStack(R.id.fragmentMain, false, false)
            when (args.game) {
                Game.TIMED -> findNavController().navigate(R.id.action_fragmentMain_to_fragmentTimed)
                Game.MOVES -> findNavController().navigate(R.id.action_fragmentMain_to_fragmentMoves)
                Game.ENDLESS -> findNavController().navigate(R.id.action_fragmentMain_to_fragmentEndless)
            }
        }

        if (sharedPrefs.getBestScores(args.game) < args.scores) {
            sharedPrefs.setBestScores(args.scores, args.game)
        }

        binding.scoresTextView.text = args.scores.toString()
        binding.bestScoresTextView.text = sharedPrefs.getBestScores(args.game).toString()
    }
}