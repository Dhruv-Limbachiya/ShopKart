package com.example.shopkart.ui.fragments.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.shopkart.data.model.CartItem
import com.example.shopkart.data.model.Product
import com.example.shopkart.databinding.LayoutCartItemBinding

/**
 * Created By Dhruv Limbachiya on 03-11-2021 19:34.
 */
class CartListAdapter :
    ListAdapter<CartItem, CartListAdapter.CartListViewHolder>(CartItemDiffCallback()) {

    private var deleteProductListener: ((CartItem) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartListViewHolder {
        return CartListViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: CartListViewHolder, position: Int) {
        val cartItem = getItem(position) // current product.
        holder.bind(cartItem, deleteProductListener)
    }

    /**
     * Setter method to delete product listener
     */
    fun setDeleteProductListener(onDeleteProductListener: (CartItem) -> Unit) {
        deleteProductListener = onDeleteProductListener
    }

    class CartListViewHolder(val binding: LayoutCartItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        /**
         * Bind views with cart data.
         */
        fun bind(
            cartItem: CartItem?,
            deleteProductListener: ((CartItem) -> Unit)?,
        ) {
            cartItem?.let {
                binding.cart = it // assigns product in binding product variable.
                binding.ivDeleteProduct.setOnClickListener {
                    deleteProductListener?.let {
                        it(cartItem)
                    }
                }
            }
        }

        companion object {
            fun from(parent: ViewGroup): CartListViewHolder {
                return CartListViewHolder(
                    LayoutCartItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
        }
    }

}


class CartItemDiffCallback : DiffUtil.ItemCallback<CartItem>() {
    override fun areItemsTheSame(oldItem: CartItem, newItem: CartItem) = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: CartItem, newItem: CartItem) = oldItem.id == newItem.id
}
