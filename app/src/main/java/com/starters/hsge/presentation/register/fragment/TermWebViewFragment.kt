package com.starters.hsge.presentation.register.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import android.webkit.*
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.starters.hsge.R
import com.starters.hsge.databinding.FragmentTermWebViewBinding
import com.starters.hsge.presentation.common.base.BaseFragment
import com.starters.hsge.presentation.common.util.LoadingDialog
import timber.log.Timber


class TermWebViewFragment : BaseFragment<FragmentTermWebViewBinding>(R.layout.fragment_term_web_view) {

    private val args: TermWebViewFragmentArgs by navArgs()
    private lateinit var callback: OnBackPressedCallback

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val url = args.termUrl

        Timber.d("주소 $url")

        binding.webView.apply {
            webViewClient = CustomWebViewClient()
            settings.javaScriptEnabled = true
        }
        binding.webView.loadUrl(url)
        LoadingDialog.showDogLoadingDialog(requireContext())


    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if(binding.webView.canGoBack()){
                    binding.webView.goBack()
                    LoadingDialog.showDogLoadingDialog(requireContext())
                } else{
                    findNavController().navigateUp()
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    inner class CustomWebViewClient : WebViewClient() {

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            LoadingDialog.dismissDogLoadingDialog()
        }

        override fun onLoadResource(view: WebView?, url: String?) {
            super.onLoadResource(view, url)
        }

        override fun onReceivedError(
            view: WebView?,
            request: WebResourceRequest?,
            error: WebResourceError?
        ) {
            super.onReceivedError(view, request, error)
            LoadingDialog.dismissDogLoadingDialog()
        }
    }
}