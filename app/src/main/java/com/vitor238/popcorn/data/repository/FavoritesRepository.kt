package com.vitor238.popcorn.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.ListenerRegistration
import com.vitor238.popcorn.data.model.Favorite
import com.vitor238.popcorn.utils.ApiStatus
import com.vitor238.popcorn.utils.FirestoreReferences


class FavoritesRepository(currentUserId: String) {

    private val favoritesRef = FirestoreReferences.getFavoritesRef(currentUserId)
    private val _favorite = MutableLiveData<Favorite?>()
    val favorite: LiveData<Favorite?>
        get() = _favorite
    private var _status = MutableLiveData<ApiStatus>()

    val status: LiveData<ApiStatus>
        get() = _status

    private var favoriteSavedListener: ListenerRegistration? = null
    private var favoritesListListener: ListenerRegistration? = null

    private val _favoritesList = MutableLiveData<List<Favorite>>()
    val favoritesList: LiveData<List<Favorite>>
        get() = _favoritesList

    fun saveFavorite(favorite: Favorite) {
        favoritesRef.document(favorite.id!!).set(favorite).addOnSuccessListener {
            Log.i(TAG, "Favorite saved")
        }.addOnFailureListener {
            Log.w(TAG, "Error saving document", it)
        }
    }

    fun checkSaved(favorite: Favorite) {
        val query = favoritesRef.whereEqualTo("mediaId", favorite.mediaId)
            .whereEqualTo("mediaType", favorite.mediaType)

        favoriteSavedListener = query.addSnapshotListener { value, e ->
            if (e != null) {
                Log.w(TAG, "Listen failed.", e)
                return@addSnapshotListener
            }

            val favorites = mutableListOf<Favorite>()
            for (doc in value!!) {
                favorites.add(doc.toObject(Favorite::class.java))
            }
            if (favorites.isNotEmpty()) {
                _favorite.value = favorites[0]
            } else {
                _favorite.value = null
            }
        }
    }

    fun removeFavorite(favorite: Favorite) {
        favoritesRef.document(favorite.id!!)
            .delete()
            .addOnSuccessListener {
                Log.d(TAG, "Favorite successfully deleted!")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error deleting document", e)
            }
    }

    fun getAllFavorites() {
        _status.value = ApiStatus.LOADING

        favoritesListListener = favoritesRef.addSnapshotListener { value, e ->
            if (e != null) {
                _status.value = ApiStatus.ERROR
                return@addSnapshotListener
            }

            val list = mutableListOf<Favorite>()
            for (doc in value!!) {
                list.add(doc.toObject(Favorite::class.java))
            }
            _favoritesList.value = list
            Log.i(TAG, "getAllFavorites: $list")
            _status.value = ApiStatus.DONE

        }
    }

    fun detachFavoriteSavedListener() {
        favoriteSavedListener?.remove()
    }

    fun detachFavoritesListListener() {
        favoritesListListener?.remove()
    }

    companion object {
        val TAG = FavoritesRepository::class.simpleName
    }
}