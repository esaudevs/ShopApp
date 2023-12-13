package com.esaudev.shopapp.ui.cart

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.esaudev.shopapp.R
import com.esaudev.shopapp.databinding.FragmentCartBinding
import com.esaudev.shopapp.ext.formatAsMoney
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CartFragment: Fragment() {

    private var _binding: FragmentCartBinding? = null
    private val binding: FragmentCartBinding
        get() = _binding!!

    private val viewModel: CartViewModel by viewModels()

    @Inject lateinit var cartListAdapter: CartListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)

        initComponents()
        subscribeViewModel()
        viewModel.listenCartChanges()

        return binding.root
    }

    private fun initComponents() {
        binding.rvCartItems.apply {
            adapter = cartListAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(false)
        }

        cartListAdapter.setAddClickListener {
            viewModel.addQuantity(it.id)
        }

        cartListAdapter.setRemoveClickListener {
            viewModel.removeQuantity(it.id)
        }

        cartListAdapter.setDeleteClickListener {
            viewModel.delete(it.id)
        }

        binding.bPay.setOnClickListener {
            viewModel.completeOrder()
        }
    }

    private fun subscribeViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiEvent.collect { uiEvent ->
                    when(uiEvent) {
                        is CartUiEvent.OrderCompleted -> {
                            Toast.makeText(context, getString(R.string.order_completed), Toast.LENGTH_SHORT).show()
                        }
                        is CartUiEvent.Error -> {
                            Toast.makeText(context, getString(R.string.cart_error), Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }


        viewModel.cart.observe(viewLifecycleOwner) { uiState ->
            when(uiState) {
                is CartUiState.Empty -> {
                    binding.rvCartItems.visibility = View.GONE
                    binding.bPay.isEnabled = false
                    binding.tvEmptyCart.visibility = View.VISIBLE
                    binding.tvSubTotalAmount.text = 0.0.formatAsMoney()
                }
                is CartUiState.WithItems -> {
                    binding.tvEmptyCart.visibility = View.GONE
                    binding.rvCartItems.visibility = View.VISIBLE
                    binding.bPay.isEnabled = true
                    cartListAdapter.submitList(uiState.items)
                    binding.tvSubTotalAmount.text = uiState.items.sumOf { it.quantity*it.price }.formatAsMoney()
                }
            }
        }
    }
}