package com.square.game.ui.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.square.game.R
import com.square.game.core.library.ViewBindingDialog
import com.square.game.databinding.DialogOptionsBinding
import com.square.game.domain.other.AppSharedPrefs
import com.square.game.domain.other.Game
import com.square.game.ui.game.CallbackViewModel
import com.square.game.ui.other.MainActivity

class DialogOptions : ViewBindingDialog<DialogOptionsBinding>(DialogOptionsBinding::inflate) {
    private val args: DialogOptionsArgs by navArgs()
    private val callbackViewModel: CallbackViewModel by activityViewModels()
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
                callbackViewModel.callback?.invoke()
                findNavController().popBackStack()
                true
            } else {
                false
            }
        }
        setButtons()

        binding.buttonClose.setOnClickListener {
            callbackViewModel.callback?.invoke()
            findNavController().popBackStack()
        }

        binding.buttonRestart.isVisible = args.isGame

        binding.buttonRestart.setOnClickListener {
            findNavController().popBackStack(R.id.fragmentMain, false, false)
            when (args.game) {
                Game.TIMED -> findNavController().navigate(R.id.action_fragmentMain_to_fragmentTimed)
                Game.MOVES -> findNavController().navigate(R.id.action_fragmentMain_to_fragmentMoves)
                Game.ENDLESS -> findNavController().navigate(R.id.action_fragmentMain_to_fragmentEndless)
            }
        }

        binding.vibroButton.setOnClickListener {
            sharedPrefs.setVibro(!sharedPrefs.getVibro())
            setButtons()
        }

        binding.soundButton.setOnClickListener {
            sharedPrefs.setVolume()
            setButtons()

            if (sharedPrefs.getVolumeState()) {
                (requireActivity() as MainActivity).startMusic()
            } else {
                (requireActivity() as MainActivity).pauseMusic()
            }
        }

    }

    private fun setButtons() {
        binding.soundButton.setImageResource(if (sharedPrefs.getVolumeState()) R.drawable.button_on else R.drawable.button_off)
        binding.vibroButton.setImageResource(if (sharedPrefs.getVibro()) R.drawable.button_on else R.drawable.button_off)
    }
}