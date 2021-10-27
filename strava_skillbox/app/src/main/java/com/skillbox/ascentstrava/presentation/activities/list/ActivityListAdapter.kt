package com.skillbox.ascentstrava.presentation.activities.list

import android.annotation.SuppressLint
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
import com.skillbox.ascentstrava.presentation.profile.Athlete
import java.text.SimpleDateFormat
import java.util.*

class ActivityListAdapter(
    viewModel: ActivityListViewModel,
    private val onShareClicked: (activityUrl: String) -> Unit
) :
    ListAdapter<ActivityItem, ActivityListAdapter.ActivitiesListViewHolder>(
        ActivityDiffUtilCallback()
    ) {

    val athlete = viewModel.getAthlete()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ActivitiesListViewHolder {
        val binding =
            ItemActivityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ActivitiesListViewHolder(binding, onShareClicked)
    }

    override fun onBindViewHolder(holder: ActivitiesListViewHolder, position: Int) {
        holder.bind(getItem(position), athlete)
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

        @SuppressLint("SetTextI18n", "ResourceAsColor")
        fun bind(activityItem: ActivityItem, athlete: Athlete?) {

            if (athlete != null) {
                binding.athleteNameTextView.text = "${athlete.firstName} ${athlete.lastName}"
            }
            binding.activityNameTextView.text = activityItem.name
            binding.distanceCountTextView.text =
                "${activityItem.distance?.toInt()?.div(1000)} $KM"
            binding.timeCountTextView.text = "${activityItem.elapsedTime?.div(60)}$MIN"
            binding.typeValueTextView.text = activityItem.type

            when (binding.typeValueTextView.text) {
                ActivityType.Run.toString() -> binding.typeImageView.setImageResource(R.drawable.run)
                ActivityType.Ride.toString() -> binding.typeImageView.setImageResource(R.drawable.ride)
                ActivityType.Walk.toString() -> binding.typeImageView.setImageResource(R.drawable.walk)
                ActivityType.Hike.toString() -> binding.typeImageView.setImageResource(R.drawable.hike)
            }

            if (activityItem.isPending == true) {
                binding.pendingTextView.visibility = View.VISIBLE
            }

            if (athlete != null) {
                Glide.with(itemView)
                    .load(athlete.photoUrl)
                    .placeholder(R.drawable.ic_placeholder)
                    .fallback(R.drawable.ic_placeholder)
                    .error(R.drawable.ic_error_placeholder)
                    .into(binding.athleteImageView)
            }

            val date = activityItem.startedAt
            if (date != null) {
                val currentFormat = SimpleDateFormat(
                    "yyyy-MM-dd'T'HH:mm:ss",
                    Locale.ROOT
                )
                val targetFormat = SimpleDateFormat("MMM dd,yyyy hh:mm a", Locale.ROOT)
                val calendar = Calendar.getInstance()
                calendar.time = currentFormat.parse(date)
                binding.dateTimeTextView.text = targetFormat.format(calendar.time)
            }
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

    companion object {
        private const val KM = "km"
        private const val MIN = "m"
    }
}