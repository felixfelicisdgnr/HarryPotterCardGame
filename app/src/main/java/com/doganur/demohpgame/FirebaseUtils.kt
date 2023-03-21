package com.doganur.demohpgame

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import java.util.*


object FirebaseUtils {

    fun getCardsFromCollectionRandom(
        collectionName: String,
        cardCounter: Int,
        doubleIt: Boolean,
        doOnSuccess: (List<SingleCard>) -> Unit
    ) {
        val db = FirebaseFirestore.getInstance()
        db.collection(collectionName).get().addOnSuccessListener { result ->
            val list = mutableListOf<SingleCard>()
            var counter = 0
            for (document in result) {
                if (counter == cardCounter) {
                    break
                }
                list.add(document.toSingleCard())
                if(doubleIt){
                    list.add(document.toSingleCard())
                }
                counter++
            }
            doOnSuccess(list.shuffled())
        }
    }

    fun QueryDocumentSnapshot.toSingleCard(): SingleCard {
        return SingleCard(
            this.get("House") as? String?,
            ((this.get("HouseScore") as? String?) ?: "0").toInt(),
            this.get("Name") as? String?,
            ((this.get("Score") as? String?) ?: "0").toInt(),
            this.get("URL") as? String?,
        )
    }
}