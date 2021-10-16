package com.skillbox.ascentstrava.presentation.share

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.skillbox.ascentstrava.R
import com.skillbox.ascentstrava.databinding.ItemContactBinding
import com.skillbox.ascentstrava.presentation.share.data.Contact


class ContactListAdapter(
    private val onItemClicked: (contact: Contact) -> Unit
) :
    ListAdapter<Contact, ContactListAdapter.ContactViewHolder>(ContactDiffUtilCallback()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ContactViewHolder {
        val binding = ItemContactBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ContactViewHolder(binding, onItemClicked)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ContactViewHolder(
        private val binding: ItemContactBinding,
        private val onItemClicked: (contact: Contact) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                val item = (bindingAdapter as? ContactListAdapter)?.getItem(bindingAdapterPosition)
                    ?: return@setOnClickListener
                onItemClicked.invoke(item)
            }
        }

        fun bind(contact: Contact) {
            binding.nameTextView.text = contact.name
            binding.phoneNumberTextView.text = contact.phone

            Glide.with(itemView)
                .load(contact.imageUri)
                .placeholder(R.drawable.ic_placeholder)
                .fallback(R.drawable.ic_placeholder)
                .error(R.drawable.ic_error_placeholder)
                .into(binding.athleteImageView)
        }
    }

    class ContactDiffUtilCallback : DiffUtil.ItemCallback<Contact>() {
        override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Contact, newItem: Contact): Boolean {
            return oldItem == newItem
        }
    }

}