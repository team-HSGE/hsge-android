package com.starters.hsge.presentation.main.management

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.starters.hsge.R
import com.starters.hsge.databinding.FragmentManagementBinding
import com.starters.hsge.presentation.common.base.BaseFragment
import com.starters.hsge.presentation.common.util.CustomDecoration
import com.starters.hsge.presentation.main.MainActivity

class ManagementFragment : BaseFragment<FragmentManagementBinding>(R.layout.fragment_management) {

    private lateinit var callback: OnBackPressedCallback

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val list = listOf(
            Doggi(
                R.drawable.ic_dog_profile_1,
                "몽이",
                R.drawable.ic_gender_male_black,
                "1개월",
                "포메라니안"
            ),
            Doggi(
                R.drawable.ic_dog_profile_1,
                "몽이",
                R.drawable.ic_gender_male_black,
                "1개월",
                "포메라니안"
            ),
            Doggi(
                R.drawable.ic_dog_profile_1,
                "몽이",
                R.drawable.ic_gender_male_black,
                "1개월",
                "포메라니안"
            )
        )


        val customDecoration = CustomDecoration(1f, 0f, Color.parseColor("#EFEFEF"))
        binding.rvDogList.addItemDecoration(customDecoration)


        val adapter = DogListAdapter(list)
        binding.rvDogList.layoutManager = LinearLayoutManager(context)
        binding.rvDogList.adapter = adapter

        setNavigation()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigateUp()
                visibleBtmNav()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }

    private fun setNavigation() {
        binding.toolBar.setNavigationOnClickListener {
            findNavController().navigateUp()
            visibleBtmNav()
        }
    }

    private fun visibleBtmNav(){
        (activity as MainActivity).binding.navigationMain.visibility = View.VISIBLE
    }
}


data class Doggi(
    val dogPhoto: Int,
    val dogName: String,
    val dogSex: Int,
    val dogAge: String,
    val dogBreed: String,
)