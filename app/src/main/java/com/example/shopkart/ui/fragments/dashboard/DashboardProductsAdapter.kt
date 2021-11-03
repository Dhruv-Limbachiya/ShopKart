package com.example.shopkart.ui.fragments.dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.shopkart.data.model.Product
import com.example.shopkart.databinding.LayoutDashboardItemBinding
import com.example.shopkart.ui.fragments.product.ProductDiffCallback

/**
 * Created by Dhruv Limbachiya on 29-10-2021.
 */
class DashboardProductsAdapter : ListAdapter<Product, DashboardProductsAdapter.DashboardViewHolder>(
    ProductDiffCallback()
) {

    private var onProductClickListener : ((Product) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder {
        return DashboardViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {
        val product = getItem(position) // current product.
        holder.bind(product,onProductClickListener)
    }

    /**
     * Setter method for onProductClickListener property.
     */
    fun setOnProductItemClickListener(onProductClickListener : (Product) -> Unit) {
        this.onProductClickListener = onProductClickListener
    }

    class DashboardViewHolder(val binding : LayoutDashboardItemBinding) : RecyclerView.ViewHolder(binding.root) {

        /**
         * Bind views with product data.
         */
        fun bind(product: Product?, onProductClickListener: ((Product) -> Unit)?) {
            product?.let {
                binding.product = it // assigns product in binding product variable.

                binding.root.setOnClickListener {
                    onProductClickListener?.let {
                        it(product)
                    }
                }
            }
        }

        companion object {

            fun from(parent: ViewGroup) : DashboardViewHolder {
                return DashboardViewHolder(
                    LayoutDashboardItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
                )
            }

        }
    }

}
