package com.mycardsapplication.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding


typealias Inflate<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

abstract class BaseFragment<VB : ViewBinding>(
    private val inflate: Inflate<VB>
) : Fragment() {

    private var _binding: VB? = null
    val binding get() = _binding!!
    val TAG = "BaseFragment"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflate.invoke(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun showToastMessage(message: String?, length: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(requireContext(), message.orEmpty(), length).show()
    }

    fun showErrorDialog(message: String?) {
        AlertDialog.Builder(requireContext())
            .setMessage(message)
            .create()
            .show()
    }

    fun getDeviceName(): String {
        lateinit var strDeviceName: String
        try {
            strDeviceName = android.os.Build.MODEL

        } catch (e: Exception) {
        }

        return strDeviceName
    }


}