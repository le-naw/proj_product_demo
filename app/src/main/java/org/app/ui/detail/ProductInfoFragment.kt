package org.app.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.app.demo.android.databinding.FragmentProductBinding

class ProductInfoFragment : Fragment() {

    private lateinit var binding: FragmentProductBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        fun newInstance(counter: Int?) = ProductInfoFragment()
    }
}