package com.esaudev.shopapp.ui.product_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.esaudev.shopapp.databinding.FragmentProductListBinding

class ProductListFragment: Fragment() {

    private var _binding: FragmentProductListBinding? = null
    private val binding: FragmentProductListBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductListBinding.inflate(inflater, container, false)
        return binding.root
    }

}