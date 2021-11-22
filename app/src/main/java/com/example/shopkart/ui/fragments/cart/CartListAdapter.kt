package com.example.shopkart.ui.fragments.cart

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.shopkart.R
import com.example.shopkart.data.model.CartItem
import com.example.shopkart.databinding.LayoutCartItemBinding

/**
 * Created By Dhruv Limbachiya on 03-11-2021 19:34.
 */
class CartListAdapter :
    ListAdapter<CartItem, CartListAdapter.CartListViewHolder>(CartItemDiffCallback()) {

    /**
     * variable tracks whether the adapter is used for displaying editable cart items in CartListFragment or uneditable cart items in CheckoutFragment.
     * true means - Editable (CartListFragment). false means - Non-Editable (CheckoutFragment)
     */
    private var isEditable = true

    /**
     * Setter for isEditable property.
     */
    fun setIsEditable(editable: Boolean) {
        isEditable = editable
    }

    /**
     *  Cart Item delete listener of type function which takes two input.
     *  1. CartItem : cartItem to delete.
     *  2. Boolean : To check whether to display warning before deleting or delete directly without warning.
     */
    private var deleteProductListener: ((CartItem, Boolean) -> Unit)? = null

    /**  CartQuantity listener of type function which takes two input
     *  1. String : cart item id.
     *  2. String : stock quantity.
     *  2. String : message to display using SnackBar.
     */
    private var cartQuantityListener: ((String?, String?, String?) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartListViewHolder {
        return CartListViewHolder.from(parent)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: CartListViewHolder, position: Int) {
        val cartItem = getItem(position) // current product.
        holder.bind(cartItem, deleteProductListener, cartQuantityListener, isEditable)
    }

    /**
     * Setter method to delete product listener
     */
    fun setDeleteProductListener(onDeleteProductListener: (CartItem, Boolean) -> Unit) {
        deleteProductListener = onDeleteProductListener
    }

    /**
     * Setter method to stock quantity listener
     */
    fun setCartQuantityListener(stockQuantityListener: (String?, String?, String?) -> Unit) {
        this.cartQuantityListener = stockQuantityListener
    }

    class CartListViewHolder(val binding: LayoutCartItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        var mStockQuantity = 0
        var mCartQuantity = 0

        /**
         * Bind views with cart data.
         */
        @RequiresApi(Build.VERSION_CODES.M)
        fun bind(
            cartItem: CartItem?,
            deleteProductListener: ((CartItem, Boolean) -> Unit)?,
            cartQuantityListener: ((String?, String?, String?) -> Unit)?,
            isEditable: Boolean,
        ) {
            cartItem?.let { item ->
                binding.cart = item // assigns product in binding product variable.
                mStockQuantity = item.stock_quantity.toInt()
                mCartQuantity = item.cart_quantity.toInt()

                if (isEditable) {

                    if(mStockQuantity == 0) {
                        // Hide "+" and "-" views.
                        binding.ivAdd.isVisible = false
                        binding.ivRemove.isVisible = false

                        binding.tvCartItemQuantity.text = binding.root.context.resources.getString(R.string.text_out_stock)
                        binding.tvCartItemQuantity.setTextColor(binding.root.context.resources.getColor(
                            R.color.primary))

                    } else {
                        // Show "+" and "-" views.
                        binding.ivAdd.isVisible = true
                        binding.ivRemove.isVisible = true
                        binding.tvCartItemQuantity.text = item.cart_quantity
                        binding.tvCartItemQuantity.setTextColor(binding.root.context.resources.getColor(
                            R.color.gray))
                    }

                    binding.ivDeleteProduct.setOnClickListener {
                        deleteProductListener?.let {
                            it(cartItem, true)
                        }
                    }

                    binding.ivAdd.setOnClickListener {
                        if (mCartQuantity < mStockQuantity) {
                            val incrementedValue = (++mCartQuantity).toString()
                            cartQuantityListener?.let { listener ->
                                listener(item.id, incrementedValue, null)
                            }
                        } else {
                            cartQuantityListener?.let { listener ->
                                listener(
                                    null,
                                    null,
                                    "Available stock is ${item.cart_quantity}!. You can not add more than stock quantity."
                                )
                            }
                        }
                    }

                    binding.ivRemove.setOnClickListener {
                        // Remove the cart item from the list when cart quantity reached at value 0.
                        if (mCartQuantity == 1) {
                            deleteProductListener?.let { it(cartItem, false) }
                            return@setOnClickListener
                        }

                        val decrementedValue = (--mCartQuantity).toString()

                        cartQuantityListener?.let { listener ->
                            listener(item.id, decrementedValue, null)
                        }
                    }
                } else {
                    // Adapter used to display cart item in CheckoutFragment.
                    binding.ivRemove.isVisible = false
                    binding.ivAdd.isVisible = false
                    binding.ivDeleteProduct.isVisible = false
                    binding.tvCartItemQuantity.text = item.cart_quantity
                    binding.tvCartItemQuantity.setTextColor(binding.root.context.resources.getColor(
                        R.color.primary))
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
    override fun areContentsTheSame(oldItem: CartItem, newItem: CartItem) =
        oldItem.hashCode() == newItem.hashCode()
}
