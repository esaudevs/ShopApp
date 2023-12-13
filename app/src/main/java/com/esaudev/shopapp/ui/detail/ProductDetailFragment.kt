package com.esaudev.shopapp.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.esaudev.shopapp.R
import com.esaudev.shopapp.databinding.FragmentProductDetailBinding
import com.esaudev.shopapp.domain.model.Product
import com.esaudev.shopapp.ext.formatAsMoney
import com.esaudev.shopapp.ext.load
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductDetailFragment: Fragment() {

    private var _binding: FragmentProductDetailBinding? = null
    private val binding: FragmentProductDetailBinding
        get() = _binding!!

    private val viewModel: ProductDetailViewModel by viewModels()
    private val args: ProductDetailFragmentArgs by navArgs()

    lateinit var product: Product

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductDetailBinding.inflate(inflater, container, false)

        initListeners()
        subscribeViewModel()
        viewModel.getProductDetail(args.productId)
        return binding.root
    }

    private fun initListeners() {
        binding.bBack.setOnClickListener {
            activity?.onBackPressedDispatcher?.onBackPressed()
        }

        binding.bAddToCart.setOnClickListener {
            viewModel.addToCart(product)
        }
    }

    private fun subscribeViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiEvent.collect { uiEvent ->
                    when(uiEvent) {
                        is ProductDetailUiEvent.AddedToCart -> {
                            Toast.makeText(context, getString(R.string.added_to_cart), Toast.LENGTH_SHORT).show()
                        }
                        is ProductDetailUiEvent.Error -> {
                            Toast.makeText(context, getString(R.string.cart_error), Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        viewModel.product.observe(viewLifecycleOwner) { uiState ->
            when(uiState) {
                is ProductDetailUiState.Loading -> {
                    binding.cDetails.visibility = View.GONE
                    binding.ivImage.visibility = View.GONE
                    binding.pbDetail.visibility = View.VISIBLE
                }
                is ProductDetailUiState.Idle -> {
                    product = uiState.product
                    binding.pbDetail.visibility = View.GONE
                    binding.tvPrice.text = uiState.product.price.formatAsMoney()
                    binding.tvTitle.text = uiState.product.title
                    binding.tvDescription.text = uiState.product.description
                    binding.ivImage.load(url = uiState.product.imageUrl)
                    binding.cDetails.visibility = View.VISIBLE
                    binding.ivImage.visibility = View.VISIBLE
                    binding.bAddToCart.text = getString(R.string.add_to_cart)
                    binding.bAddToCart.isEnabled = true
                    binding.pbAddToCart.visibility = View.GONE
                }
                is ProductDetailUiState.AddingToCart -> {
                    binding.bAddToCart.text = ""
                    binding.bAddToCart.isEnabled = false
                    binding.pbAddToCart.visibility = View.VISIBLE
                }
            }
        }
    }
}