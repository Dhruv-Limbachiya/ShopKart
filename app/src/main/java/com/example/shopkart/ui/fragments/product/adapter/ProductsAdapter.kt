package com.example.shopkart.ui.fragments.product.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.shopkart.data.model.Product
import com.example.shopkart.databinding.LayoutProductItemBinding

/**
 * Created by Dhruv Limbachiya on 29-10-2021.
 */
class ProductsAdapter : ListAdapter<Product,ProductsAdapter.ProductViewHolder>(ProductDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
       return ProductViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = getItem(position) // current product.
        holder.bind(product)
    }


    class ProductViewHolder(val binding : LayoutProductItemBinding) : RecyclerView.ViewHolder(binding.root) {

        /**
         * Bind views with product data.
         */
        fun bind(product: Product?) {
            product?.let {
                binding.product = it // assigns product in binding product variable.
            }
        }

        companion object {

            fun from(parent: ViewGroup) : ProductViewHolder {
                return ProductViewHolder(
                    LayoutProductItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
                )
            }

        }
    }

}

class ProductDiffCallback : DiffUtil.ItemCallback<Product>() {
    override fun areItemsTheSame(oldItem: Product, newItem: Product) = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Product, newItem: Product)  = oldItem.id == newItem.id
}