package com.starters.hsge.presentation.register.fragment

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.drawable.Drawable
import android.media.Image
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.datastore.dataStore
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.starters.hsge.R
import com.starters.hsge.databinding.FragmentUserProfileIconBinding
import com.starters.hsge.presentation.common.base.BaseFragment
import com.starters.hsge.presentation.login.LoginActivity
import com.starters.hsge.presentation.register.adapter.UserProfileIconAdapter
import com.starters.hsge.presentation.register.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UserProfileIconFragment : BaseFragment<FragmentUserProfileIconBinding>(R.layout.fragment_user_profile_icon) {

    private lateinit var adapter: UserProfileIconAdapter
    private val registerViewModel: RegisterViewModel by viewModels()

    var userIconList = listOf<Int>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userIconList = listOf<Int>(
            R.drawable.ic_dog_profile_1,
            R.drawable.ic_dog_profile_6,
            R.drawable.ic_dog_profile_11,
            R.drawable.ic_dog_profile_2,
            R.drawable.ic_dog_profile_7,
            R.drawable.ic_dog_profile_12,
            R.drawable.ic_dog_profile_3,
            R.drawable.ic_dog_profile_8,
            R.drawable.ic_dog_profile_13,
            R.drawable.ic_dog_profile_4,
            R.drawable.ic_dog_profile_9,
            R.drawable.ic_dog_profile_14,
            R.drawable.ic_dog_profile_5,
            R.drawable.ic_dog_profile_10,
            R.drawable.ic_dog_profile_15
        )

        setNavigation()
        initRecyclerView(userIconList)
    }

    private fun initRecyclerView(list: List<Int>) {
        adapter = UserProfileIconAdapter(list)
        binding.rvProfileIcon.adapter = adapter

        lifecycleScope.launch {
            registerViewModel.saveUserIcon(0)
        }

        adapter.setItemClickListener(object: UserProfileIconAdapter.OnItemClickListener{
            override fun onClick(v: View, resId: Int) {
                // 클릭 시 이벤트 작성
                //prefs.edit().putInt("resId", resId).apply()

                lifecycleScope.launch {
                    registerViewModel.saveUserIcon(resId)
                    Log.d("이미지ID2", "${resId}")
                }

                Navigation.findNavController(binding.root)
                    .navigate(R.id.action_userProfileIconFragment_to_userImageFragment)
            }

        })
    }

    private fun setNavigation() {
        binding.toolBar.setNavigationOnClickListener {
            findNavController().navigateUp()

        }
    }
}