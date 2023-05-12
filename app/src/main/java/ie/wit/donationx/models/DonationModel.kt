package ie.wit.donationx.models

import android.os.Parcelable
import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@IgnoreExtraProperties
@Parcelize
data class DonationModel(
      var uid: String? = "",
      var isFavorite: Boolean = false,
      var paymentmethod: String = "N/A",
      var subject: String? = "",
      var description: String? = "",
      var amount: Int = 0,
      var message: String = "See Task Information!",
      var upvotes: Int = 0,
      var profilepic: String = "",
      var latitude: Double = 0.0,
      var longitude: Double = 0.0,
      var email: String? = "joe@bloggs.com")
      : Parcelable
{
      @Exclude
      fun toMap(): Map<String, Any?> {
            return mapOf(
                  "uid" to uid,
                  "isFavorite" to isFavorite,
                  "paymentmethod" to paymentmethod,
                  "subject" to subject,
                  "description" to description,
                  "amount" to amount,
                  "message" to message,
                  "upvotes" to upvotes,
                  "profilepic" to profilepic,
                  "latitude" to latitude,
                  "longitude" to longitude,
                  "email" to email
            )
      }
}


