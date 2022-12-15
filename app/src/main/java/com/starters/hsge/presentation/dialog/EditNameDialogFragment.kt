package com.starters.hsge.presentation.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.starters.hsge.R
import com.starters.hsge.databinding.FragmentEditNameDialogBinding

class EditNameDialogFragment : DialogFragment() {

    private lateinit var binding: FragmentEditNameDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_edit_name_dialog,
            container,
            false
        )

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        initListener()

    }

    private fun initListener() {
        binding.tvDialogOkBtn.setOnClickListener {
            dismiss()
        }

        binding.tvDialogCancelBtn.setOnClickListener {
            dismiss()
        }
    }

    override fun onResume() {
        super.onResume()

        fun dialogFragmentResize(context: Context, dialogFragment: DialogFragment, width: Float, height: Float) {

            val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

            if (Build.VERSION.SDK_INT < 30) {

                val display = windowManager.defaultDisplay
                val size = Point()

                display.getSize(size)

                val window = dialogFragment.dialog?.window

                val x = (size.x * width).toInt()
                val y = (size.y * height).toInt()
                window?.setLayout(x, y)

            } else {

                val rect = windowManager.currentWindowMetrics.bounds

                val window = dialogFragment.dialog?.window

                val x = (rect.width() * width).toInt()
                val y = (rect.height() * height).toInt()

                window?.setLayout(x, y)
            }
        }

        dialogFragmentResize(requireContext(), this@EditNameDialogFragment, 0.9f, 0.30f)
    }

    companion object {
        const val TAG = "EditNameDialog"
    }
}
