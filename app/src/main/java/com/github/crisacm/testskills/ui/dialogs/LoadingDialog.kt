package com.github.crisacm.testskills.ui.dialogs

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.github.crisacm.testskills.databinding.DialogLoadingBinding

class LoadingDialog constructor(
    private var cancellable: Boolean = false,
    private var msg: String = DEFAULT_MSG
) : DialogFragment() {
    constructor() : this(false, DEFAULT_MSG)

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
    }

    companion object {
        private const val DEFAULT_MSG = "Cargando información, espere un momento por favor..."

        @JvmStatic
        fun show(activity: Activity, tag: String) =
            LoadingDialog().also {
                activity as AppCompatActivity
                it.show(activity.supportFragmentManager, tag)
            }

        @JvmStatic
        fun show(activity: Activity, _msg: String, tag: String) =
            LoadingDialog(msg = _msg).also {
                activity as AppCompatActivity
                it.show(activity.supportFragmentManager, tag)
            }

        @JvmStatic
        fun show(activity: Activity, cancellable: Boolean, _msg: String, tag: String) =
            LoadingDialog(cancellable, _msg).also {
                activity as AppCompatActivity
                it.show(activity.supportFragmentManager, tag)
            }

        @JvmStatic
        fun show(activity: Activity, cancellable: Boolean, tag: String) =
            LoadingDialog(cancellable).also {
                activity as AppCompatActivity
                it.show(activity.supportFragmentManager, tag)
            }

        @JvmStatic
        fun newInstance(cancellable: Boolean = false, msg: String = DEFAULT_MSG) = LoadingDialog(cancellable, msg)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = DialogLoadingBinding.inflate(inflater, container, false)
        dialog?.setCancelable(cancellable)
        binding.textView.text = msg
        return binding.root
    }
}