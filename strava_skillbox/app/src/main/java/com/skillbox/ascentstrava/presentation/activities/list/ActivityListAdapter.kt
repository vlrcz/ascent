package com.skillbox.ascentstrava.presentation.activities.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.skillbox.ascentstrava.R
import com.skillbox.ascentstrava.data.AuthConfig
import com.skillbox.ascentstrava.databinding.ItemActivityBinding
import com.skillbox.ascentstrava.presentation.activities.data.ActivityItem
import com.skillbox.ascentstrava.presentation.activities.data.ActivityType
import java.util.*

class ActivityListAdapter(
    private val onShareClicked: (activityUrl: String) -> Unit
) :
    ListAdapter<ActivityItem, ActivityListAdapter.ActivitiesListViewHolder>(
        ActivityDiffUtilCallback()
    ) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ActivitiesListViewHolder {
        val binding =
            ItemActivityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ActivitiesListViewHolder(binding, onShareClicked)
    }

    override fun onBindViewHolder(holder: ActivitiesListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ActivitiesListViewHolder(
        private val binding: ItemActivityBinding,
        private val onShareClicked: (activityUrl: String) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.shareActivityBtn.setOnClickListener {
                val item =
                    (bindingAdapter as? ActivityListAdapter)?.getItem(bindingAdapterPosition)
                        ?: return@setOnClickListener
                val url = AuthConfig.ACTIVITY_URL + item.stravaId
                onShareClicked.invoke(url)
            }
        }

        fun bind(item: ActivityItem) {
            binding.athleteNameTextView.text = item.athleteName
            binding.activityNameTextView.text = item.name
            binding.distanceCountTextView.text = item.distance
            binding.timeCountTextView.text = item.elapsedTime
            binding.typeValueTextView.text = item.type
            binding.dateTimeTextView.text = item.startedAt

            when (binding.typeValueTextView.text) {
                ActivityType.Run.toString() -> binding.typeImageView.setImageResource(R.drawable.run)
                ActivityType.Ride.toString() -> binding.typeImageView.setImageResource(R.drawable.ride)
                ActivityType.Walk.toString() -> binding.typeImageView.setImageResource(R.drawable.walk)
                ActivityType.Hike.toString() -> binding.typeImageView.setImageResource(R.drawable.hike)
            }

            if (item.isPending) {
                binding.pendingTextView.visibility = View.VISIBLE
            } else {
                binding.pendingTextView.visibility = View.GONE
            }

            Glide.with(itemView)
                .load(item.athleteImage)
                .placeholder(R.drawable.ic_placeholder)
                .fallback(R.drawable.ic_placeholder)
                .error(R.drawable.ic_error_placeholder)
                .into(binding.athleteImageView)
        }
    }

    class ActivityDiffUtilCallback : DiffUtil.ItemCallback<ActivityItem>() {
        override fun areItemsTheSame(oldItem: ActivityItem, newItem: ActivityItem): Boolean {
            return oldItem.uniqueId == newItem.uniqueId || oldItem.stravaId == newItem.stravaId
        }

        override fun areContentsTheSame(oldItem: ActivityItem, newItem: ActivityItem): Boolean {
            return oldItem == newItem
        }
    }
}