package ie.wit.donationx.ui.report

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import ie.wit.donationx.firebase.FirebaseDBManager
import ie.wit.donationx.firebase.FirebaseDBManager.database
import ie.wit.donationx.models.DonationModel
import timber.log.Timber
import java.lang.Exception

class ReportViewModel : ViewModel() {

    private val donationsList =
        MutableLiveData<List<DonationModel>>()
    var readOnly = MutableLiveData(false)

    val observableDonationsList: LiveData<List<DonationModel>>
        get() = donationsList

    var liveFirebaseUser = MutableLiveData<FirebaseUser>()

    init { load() }

    fun load() {
        try {
            //DonationManager.findAll(liveFirebaseUser.value?.email!!, donationsList)
            readOnly.value = false
            FirebaseDBManager.findAll(liveFirebaseUser.value?.uid!!,donationsList)
            Timber.i("Report Load Success : ${donationsList.value.toString()}")
        }
        catch (e: Exception) {
            Timber.i("Report Load Error : $e.message")
        }
    }

    fun loadAll() {
        try {
            readOnly.value = true
            FirebaseDBManager.findAll(donationsList)
            Timber.i("Report LoadAll Success : ${donationsList.value.toString()}")
        }
        catch (e: Exception) {
            Timber.i("Report LoadAll Error : $e.message")
        }
    }

    fun delete(userid: String, id: String) {
        try {
            //DonationManager.delete(userid,id)
            FirebaseDBManager.delete(userid,id)
            Timber.i("Report Delete Success")
        }
        catch (e: Exception) {
            Timber.i("Report Delete Error : $e.message")
        }
    }

/*    fun updateFavoriteStatus(donation: DonationModel) {
        val key = donation.uid
        key?.let {
            database.child("donations").child(key).child("isFavorite").setValue(donation.isFavorite)
        }
    }*/

    fun updateFavoriteStatus(donation: DonationModel) {
        val updates = hashMapOf<String, Any>("isFavorite" to donation.isFavorite)
        database.child("donations").child(donation.uid!!).updateChildren(updates).addOnSuccessListener {
            val updatedDonationsList = donationsList.value?.map {
                if (it.uid == donation.uid) {
                    it.copy(isFavorite = donation.isFavorite)
                } else {
                    it
                }
            } ?: emptyList()
            donationsList.value = updatedDonationsList
        }
    }

    fun reloadData() {
        if (readOnly.value == true) {
            loadAll()
        } else {
            load()
        }
    }

    fun updateFavoriteStatus(position: Int, isFavorite: Boolean) {
        val donation = donationsList.value?.get(position) ?: return
        val updates = hashMapOf<String, Any>("isFavorite" to isFavorite)
        database.child("donations").child(donation.uid!!).updateChildren(updates)
            .addOnSuccessListener {
                val updatedDonationsList = donationsList.value?.map {
                    if (it.uid == donation.uid) {
                        it.copy(isFavorite = isFavorite)
                    } else {
                        it
                    }
                } ?: emptyList()
                donationsList.value = updatedDonationsList
            }
    }








    /* fun updateFavoriteStatus(donation: DonationModel) {
         val updates = hashMapOf<String, Any>("isFavorite" to donation.isFavorite)
         database.child("donations").child(donation.uid!!).updateChildren(updates)
     }*/
}

