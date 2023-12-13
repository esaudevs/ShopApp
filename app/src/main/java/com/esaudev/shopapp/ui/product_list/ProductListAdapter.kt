package com.esaudev.shopapp.ui.product_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.esaudev.shopapp.databinding.ItemProductBinding
import com.esaudev.shopapp.domain.model.Product
import com.esaudev.shopapp.ext.capitalizeFirstLetter
import com.esaudev.shopapp.ext.formatAsMoney
import com.esaudev.shopapp.ext.load
import com.esaudev.shopapp.ui.common.BaseListViewHolder
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject

@FragmentScoped
class ProductListAdapter @Inject constructor()
    : ListAdapter<Product, BaseListViewHolder<*>>(DiffUtilCallback) {

    private object DiffUtilCallback : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseListViewHolder<*> {
        val itemBinding =
            ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BindViewHolderList(itemBinding)
    }

    override fun onBindViewHolder(holder: BaseListViewHolder<*>, position: Int) {
        when (holder) {
            is BindViewHolderList -> holder.bind(getItem(position), position)
        }
    }

    protected var onProductClickListener : ((Product) -> Unit)? = null
    fun setProductClickListener(listener: (Product) -> Unit){
        onProductClickListener = listener
    }

    inner class BindViewHolderList(
        private val binding: ItemProductBinding
    ) : BaseListViewHolder<Product>(binding.root) {

        override fun bind(item: Product, position: Int) = with(binding) {

            clProductParent.setOnClickListener {
                onProductClickListener?.let { click ->
                    click(item)
                }
            }

            tvTitle.text = item.title
            tvDescription.text = item.description.capitalizeFirstLetter()
            tvPrice.text = item.price.formatAsMoney()

            ivImage.load(url = item.imageUrl)
        }
    }
}