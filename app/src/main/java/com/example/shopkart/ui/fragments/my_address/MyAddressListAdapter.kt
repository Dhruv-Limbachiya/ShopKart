package com.example.shopkart.ui.fragments.my_address

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.shopkart.data.model.Address
import com.example.shopkart.databinding.LayoutAddressItemBinding

/**
 * Created By Dhruv Limbachiya on 10-11-2021 09:46 AM.
 */
class AddressListAdapter :
    ListAdapter<Address, AddressListAdapter.AddressViewHolder>(AddressDiffCallback()) {

    private var onAddressSelectedListener: ((Address) -> Unit)? = null

    /**
     * Setter method for onAddressSelectedListener property.
     */
    fun setOnAddressSelectedListener(listener: (Address) -> Unit) {
        onAddressSelectedListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        return AddressViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        holder.bind(getItem(position), onAddressSelectedListener)
    }

    class AddressViewHolder(val binding: LayoutAddressItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        /**
         * Binds address data to views
         */
        fun bind(address: Address, onAddressSelectedListener: ((Address) -> Unit)?) {
            binding.address = address
            binding.root.setOnClickListener {
                onAddressSelectedListener?.let {
                    it(address)
                }
            }
        }


        companion object {
            fun from(parent: ViewGroup): AddressViewHolder {
                return AddressViewHolder(
                    LayoutAddressItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
        }
    }
}

class AddressDiffCallback : DiffUtil.ItemCallback<Address>() {
    override fun areItemsTheSame(oldItem: Address, newItem: Address) = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Address, newItem: Address) =
        oldItem.hashCode() == newItem.hashCode()
}