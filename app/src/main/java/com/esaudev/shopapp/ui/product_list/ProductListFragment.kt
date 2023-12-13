package com.esaudev.shopapp.ui.product_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.esaudev.shopapp.databinding.FragmentProductListBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProductListFragment: Fragment() {

    private var _binding: FragmentProductListBinding? = null
    private val binding: FragmentProductListBinding
        get() = _binding!!

    private val viewModel: ProductListViewModel by viewModels()

    @Inject
    lateinit var productListAdapter: ProductListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductListBinding.inflate(inflater, container, false)

        initComponents()
        subscribeViewModel()

        viewModel.getProducts()

        return binding.root
    }

    private fun initComponents() {
        binding.rvProducts.apply {
            adapter = productListAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(false)
        }

        productListAdapter.setProductClickListener {
            findNavController().navigate(
                ProductListFragmentDirections.actionProductListFragmentToProductDetailFragment(
                    productId = it.id
                )
            )
        }
    }

    private fun subscribeViewModel() {
        viewModel.products.observe(viewLifecycleOwner) { products ->
            productListAdapter.submitList(products)
        }
    }
}