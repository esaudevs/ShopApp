package com.esaudev.shopapp.ui.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.esaudev.shopapp.databinding.ItemCartBinding
import com.esaudev.shopapp.domain.model.CartItem
import com.esaudev.shopapp.ext.formatAsMoney
import com.esaudev.shopapp.ext.load
import com.esaudev.shopapp.ui.common.BaseListViewHolder
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject

@FragmentScoped
class CartListAdapter @Inject constructor()
    : ListAdapter<CartItem, BaseListViewHolder<*>>(DiffUtilCallback) {

    private object DiffUtilCallback : DiffUtil.ItemCallback<CartItem>() {
        override fun areItemsTheSame(oldItem: CartItem, newItem: CartItem): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: CartItem, newItem: CartItem): Boolean =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseListViewHolder<*> {
        val itemBinding =
            ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BindViewHolderList(itemBinding)
    }

    override fun onBindViewHolder(holder: BaseListViewHolder<*>, position: Int) {
        when (holder) {
            is BindViewHolderList -> holder.bind(getItem(position), position)
        }
    }

    protected var onDeleteClick : ((CartItem) -> Unit)? = null
    protected var onAddClick: ((CartItem) -> Unit)? = null
    protected var onRemoveClick: ((CartItem) -> Unit)? = null

    fun setDeleteClickListener(listener: (CartItem) -> Unit){
        onDeleteClick = listener
    }

    fun setAddClickListener(listener: (CartItem) -> Unit){
        onAddClick = listener
    }

    fun setRemoveClickListener(listener: (CartItem) -> Unit){
        onRemoveClick = listener
    }

    inner class BindViewHolderList(
        private val binding: ItemCartBinding,
    ) : BaseListViewHolder<CartItem>(binding.root) {

        override fun bind(item: CartItem, position: Int) = with(binding) {

            bAdd.setOnClickListener {
                onAddClick?.let { click ->
                    click(item)
                }
            }

            bRemove.setOnClickListener {
                onRemoveClick?.let { click ->
                    click(item)
                }
            }

            bDelete.setOnClickListener {
                onDeleteClick?.let { click ->
                    click(item)
                }
            }

            tvTitle.text = item.title
            tvPrice.text = item.price.formatAsMoney()
            tvQuantity.text = item.quantity.toString()

            bRemove.isEnabled = item.quantity > 1
            bAdd.isEnabled = item.quantity < 99

            ivImage.load(url = item.imageUrl)
        }
    }
}