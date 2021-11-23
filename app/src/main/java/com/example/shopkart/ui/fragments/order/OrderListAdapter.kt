package com.example.shopkart.ui.fragments.order

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.shopkart.data.model.Order
import com.example.shopkart.databinding.LayoutOrderItemBinding

/**
 * Created By Dhruv Limbachiya on 23-11-2021 11:52 AM.
 */
class OrderListAdapter :
    ListAdapter<Order, OrderListAdapter.OrderListViewHolder>(OrderItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderListViewHolder {
        return OrderListViewHolder.from(parent)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: OrderListViewHolder, position: Int) {
        val order = getItem(position) // current product.
        holder.bind(order)
    }

    class OrderListViewHolder(val binding: LayoutOrderItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        /**
         * Bind views with cart data.
         */
        @RequiresApi(Build.VERSION_CODES.M)
        fun bind(order: Order?) {
            order?.let { item ->
                binding.order = item // assigns product in binding product variable.
            }
        }

        companion object {
            fun from(parent: ViewGroup): OrderListViewHolder {
                return OrderListViewHolder(
                    LayoutOrderItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
        }
    }
}


class OrderItemDiffCallback : DiffUtil.ItemCallback<Order>() {
    override fun areItemsTheSame(oldItem: Order, newItem: Order) = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Order, newItem: Order) =
        oldItem.hashCode() == newItem.hashCode()
}


