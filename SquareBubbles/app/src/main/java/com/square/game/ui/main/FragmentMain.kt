package com.square.game.ui.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.square.game.R
import com.square.game.databinding.FragmentMainBinding
import com.square.game.domain.other.Game
import com.square.game.ui.game.CallbackViewModel
import com.square.game.ui.other.ViewBindingFragment

class FragmentMain : ViewBindingFragment<FragmentMainBinding>(FragmentMainBinding::inflate) {
    private val viewModel: CallbackViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            buttonTimed.setOnClickListener {
                findNavController().navigate(R.id.action_fragmentMain_to_fragmentTimed)
            }
            buttonMoves.setOnClickListener {
                findNavController().navigate(R.id.action_fragmentMain_to_fragmentMoves)
            }
            buttonEndless.setOnClickListener {
                findNavController().navigate(R.id.action_fragmentMain_to_fragmentEndless)
            }
        }

        binding.buttonSettings.setOnClickListener {
            viewModel.callback = null
            findNavController().navigate(
                FragmentMainDirections.actionFragmentMainToDialogOptions(
                    Game.ENDLESS,
                    false
                )
            )
        }

        binding.privacyText.setOnClickListener {
            requireActivity().startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.google.com")
                )
            )
        }
    }
}