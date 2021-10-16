package com.skillbox.ascentstrava.presentation.activities.list

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.skillbox.ascentstrava.R
import com.skillbox.ascentstrava.data.AuthConfig
import com.skillbox.ascentstrava.databinding.ItemActivityBinding
import com.skillbox.ascentstrava.presentation.activities.data.ActivityModel
import com.skillbox.ascentstrava.presentation.activities.data.ActivityType
import com.skillbox.ascentstrava.presentation.profile.Athlete

class ActivitiesListAdapter(
    viewModel: ActivitiesViewModel,
    private val onShareClicked: (activityUrl: String) -> Unit
) :
    ListAdapter<ActivityModel, ActivitiesListAdapter.ActivitiesListViewHolder>(
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
        if (athlete != null)
            holder.bind(getItem(position), athlete)
    }

    class ActivitiesListViewHolder(
        private val binding: ItemActivityBinding,
        private val onShareClicked: (activityUrl: String) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(activityModel: ActivityModel, athlete: Athlete) {

            binding.athleteNameTextView.text = athlete.firstName + athlete.lastName
            binding.dateTimeTextView.text = activityModel.startedAt
            binding.activityNameTextView.text = activityModel.activityName
            binding.distanceCountTextView.text =
                "${activityModel.distance?.toInt()?.div(1000)} $KM"
            binding.timeCountTextView.text = "${activityModel.elapsedTime?.div(60)}$MIN"
            binding.typeValueTextView.text = activityModel.activityType

            when (binding.typeValueTextView.text) {
                ActivityType.Run.toString() -> binding.typeImageView.setImageResource(R.drawable.run)
                ActivityType.Ride.toString() -> binding.typeImageView.setImageResource(R.drawable.ride)
                ActivityType.Walk.toString() -> binding.typeImageView.setImageResource(R.drawable.walk)
                ActivityType.Hike.toString() -> binding.typeImageView.setImageResource(R.drawable.hike)
            }

            binding.shareActivityBtn.setOnClickListener {
                val url = AuthConfig.ACTIVITY_URL + activityModel.id
                onShareClicked.invoke(url)
            }

            Glide.with(itemView)
                .load(athlete.photoUrl)
                .placeholder(R.drawable.ic_placeholder)
                .fallback(R.drawable.ic_placeholder)
                .error(R.drawable.ic_error_placeholder)
                .into(binding.athleteImageView)
        }
    }

    class ActivityDiffUtilCallback : DiffUtil.ItemCallback<ActivityModel>() {
        override fun areItemsTheSame(oldItem: ActivityModel, newItem: ActivityModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ActivityModel, newItem: ActivityModel): Boolean {
            return oldItem == newItem
        }
    }

    companion object {
        private const val KM = "km"
        private const val MIN = "m"
    }

}