package com.starters.hsge.presentation.register.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.starters.hsge.R
import com.starters.hsge.databinding.FragmentUserImageBinding
import com.starters.hsge.presentation.common.base.BaseFragment
import com.starters.hsge.presentation.register.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UserImageFragment : BaseFragment<FragmentUserImageBinding>(R.layout.fragment_user_image) {

    var resId : Int = 0
    private val registerViewModel: RegisterViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            resId = registerViewModel.fetchUserIcon().first()
            Log.d("이미지ID3", "${resId}")
        }

        if (resId != 0){
            binding.btnNext.isEnabled = true
            binding.ivUserImage.setImageResource(resId)

            binding.ivUserImage.setOnClickListener {
                Navigation.findNavController(binding.root)
                    .navigate(R.id.action_userImageFragment_to_userProfileIconFragment)
            }

        } else {
            binding.ivUserImage.setOnClickListener {
                Navigation.findNavController(binding.root)
                    .navigate(R.id.action_userImageFragment_to_userProfileIconFragment)
            }
        }

        binding.btnNext.setOnClickListener {
            if(resId != 0) {
                Navigation.findNavController(binding.root)
                    .navigate(R.id.action_userImageFragment_to_dogPhotoFragment)

                lifecycleScope.launch{
                    registerViewModel.deleteAllInfo()
                }

            } else {
                return@setOnClickListener
            }
        }




        //initListener()
        setNavigation()
    }

//    private fun initListener() {
//        binding.btnNext.setOnClickListener {
//            if(resId != 0) {
//                Navigation.findNavController(binding.root)
//                    .navigate(R.id.action_userImageFragment_to_dogPhotoFragment)
//
//                lifecycleScope.launch{
//                    registerViewModel.deleteAllInfo()
//                }
//
//            } else {
//                return@setOnClickListener
//            }
//        }
//
//        binding.ivUserImage.setOnClickListener {
//            Navigation.findNavController(binding.root)
//                .navigate(R.id.action_userImageFragment_to_userProfileIconFragment)
//        }
//    }

    private fun getSharedPreferences() {
//        resId = prefs.getInt("resId", 0)
//        Log.d("sp", "${resId}")

    }

    private fun setNavigation() {
        binding.toolBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

}