package com.starters.hsge.presentation.common.base

import android.app.Application
import android.content.SharedPreferences
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.starters.hsge.presentation.common.util.LoadingDialog

abstract class BaseFragment<T: ViewDataBinding>(
    @LayoutRes val layoutResId: Int
) : Fragment(){
    private var _binding: T? = null
    protected val binding get() = _binding!!
    lateinit var mLoadingDialog : LoadingDialog


    companion object {
        lateinit var prefs: SharedPreferences
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, layoutResId, container, false)
        prefs = requireContext().getSharedPreferences("HSGE", Application.MODE_PRIVATE)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun showLoadingDialog(context: Context){
        mLoadingDialog = LoadingDialog(context)
        mLoadingDialog.show()
    }

    // 띄워 놓은 로딩 다이얼로그 없앰
    fun dismissLoadingDialog() {
        if(mLoadingDialog.isShowing){
            mLoadingDialog.dismiss()
        }
    }
}