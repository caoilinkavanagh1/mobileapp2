package ie.wit.donationx.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ie.wit.donationx.R
import ie.wit.donationx.databinding.CardDonationBinding
import ie.wit.donationx.models.DonationModel
import ie.wit.donationx.utils.customTransformation
import ie.wit.donationx.ui.report.ReportViewModel
import android.util.Log


interface DonationClickListener {
    fun onDonationClick(donation: DonationModel)
}

class DonationAdapter constructor(private var donations: ArrayList<DonationModel>,
                                  private val listener: DonationClickListener,
                                  private val reportViewModel: ReportViewModel,
                                  private val readOnly: Boolean)
    : RecyclerView.Adapter<DonationAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardDonationBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding,readOnly)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val donation = donations[position]

        holder.bind(donation, listener)
        holder.updateFavoriteButtonColor(checkIfFavorite(donation))

        holder.binding.favoriteButton.setOnClickListener {
            val updatedFavoriteStatus = !checkIfFavorite(donation)
            donation.isFavorite = updatedFavoriteStatus
            reportViewModel.updateFavoriteStatus(donation)
            holder.updateFavoriteButtonColor(updatedFavoriteStatus)
        }
    }

    private fun checkIfFavorite(donation: DonationModel): Boolean {
        return donation.isFavorite
    }

    fun removeAt(position: Int) {
        donations.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun getItemCount(): Int = donations.size

    inner class MainHolder(val binding: CardDonationBinding, val readOnlyRow: Boolean) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(donation: DonationModel, listener: DonationClickListener) {
            binding.root.tag = donation
            binding.donation = donation
            Picasso.get().load(donation.profilepic.toUri())
                .resize(200, 200)
                .transform(customTransformation())
                .centerCrop()
                .into(binding.imageIcon)

            // Update the favorite button color based on the isFavorite value if not readOnlyRow
            if (readOnlyRow) {
                updateFavoriteButtonColor(donation.isFavorite)
            }

            binding.root.setOnClickListener { listener.onDonationClick(donation) }
            binding.executePendingBindings()
        }

        internal fun updateFavoriteButtonColor(isFavorite: Boolean) {
            if (isFavorite) {
                binding.favoriteButton.setColorFilter(ContextCompat.getColor(binding.root.context, R.color.red))
            } else {
                binding.favoriteButton.clearColorFilter()
            }
        }
    }
}