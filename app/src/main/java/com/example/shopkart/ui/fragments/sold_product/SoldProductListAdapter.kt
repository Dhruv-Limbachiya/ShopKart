package com.example.shopkart.ui.fragments.sold_product

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.shopkart.data.model.SoldProduct
import com.example.shopkart.databinding.LayoutSoldProductItemBinding

/**
 * Created By Dhruv Limbachiya on 23-11-2021 11:52 AM.
 */
class SoldProductListAdapter :
    ListAdapter<SoldProduct, SoldProductListAdapter.SoldProductListViewHolder>(
        SoldProductItemDiffCallback()
    ) {

    private var onSoldProductClickListener: ((SoldProduct) -> Unit)? = null

    /**
     * Setter method for onSoldProductClickListener property.
     */
    fun setOnSoldProductItemClickListener(onSoldProductClickListener: (SoldProduct) -> Unit) {
        this.onSoldProductClickListener = onSoldProductClickListener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SoldProductListViewHolder {
        return SoldProductListViewHolder.from(parent)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: SoldProductListViewHolder, position: Int) {
        val product = getItem(position) // current product.
        holder.bind(product, onSoldProductClickListener)
    }

    class SoldProductListViewHolder(val binding: LayoutSoldProductItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        /**
         * Bind views with sold product data.
         */
        @RequiresApi(Build.VERSION_CODES.M)
        fun bind(order: SoldProduct?, onOrderClickListener: ((SoldProduct) -> Unit)?) {
            order?.let { item ->
                binding.product = item  // assigns product in binding product variable.
                binding.root.setOnClickListener {
                    onOrderClickListener?.let { listener ->
                        listener(item)
                    }
                }
            }
        }

        companion object {
            fun from(parent: ViewGroup): SoldProductListViewHolder {
                return SoldProductListViewHolder(
                    LayoutSoldProductItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
        }
    }
}


class SoldProductItemDiffCallback : DiffUtil.ItemCallback<SoldProduct>() {
    override fun areItemsTheSame(oldItem: SoldProduct, newItem: SoldProduct) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: SoldProduct, newItem: SoldProduct) =
        oldItem.hashCode() == newItem.hashCode()
}


