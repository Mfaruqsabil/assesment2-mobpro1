package com.assesment2.estuff

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.assesment2.estuff.data.Item
import com.assesment2.estuff.data.BarangDao
import com.assesment2.estuff.network.UpdateWorker
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit


class InventoryViewModel(private val barangDao: BarangDao) : ViewModel() {


    val allItems: LiveData<List<Item>> = barangDao.getItems().asLiveData()


    fun isStockAvailable(item: Item): Boolean {
        return (item.quantityInStock > 0)
    }

    fun scheduleUpdater(app: Application) {
        val request = OneTimeWorkRequestBuilder<UpdateWorker>()
            .setInitialDelay(1, TimeUnit.MINUTES)
            .build()

        WorkManager.getInstance(app).enqueueUniqueWork(
            MainActivity.CHANNEL_ID,
            ExistingWorkPolicy.REPLACE,
            request
        )
    }

    fun updateItem(
        itemId: Int,
        itemName: String,
        itemPrice: String,
        itemCount: String
    ) {
        val updatedItem = getUpdatedItemEntry(itemId, itemName, itemPrice, itemCount)
        updateItem(updatedItem)
    }



    private fun updateItem(item: Item) {
        viewModelScope.launch {
            barangDao.update(item)
        }
    }


    fun sellItem(item: Item) {
        if (item.quantityInStock > 0) {

            val newItem = item.copy(quantityInStock = item.quantityInStock - 1)
            updateItem(newItem)
        }
    }


    fun addNewItem(itemName: String, itemPrice: String, itemCount: String) {
        val newItem = getNewItemEntry(itemName, itemPrice, itemCount)
        insertItem(newItem)
    }


    private fun insertItem(item: Item) {
        viewModelScope.launch {
            barangDao.insert(item)
        }
    }


    fun deleteItem(item: Item) {
        viewModelScope.launch {
            barangDao.delete(item)
        }
    }


    fun retrieveItem(id: Int): LiveData<Item> {
        return barangDao.getItem(id).asLiveData()
    }


    fun isEntryValid(itemName: String, itemPrice: String, itemCount: String): Boolean {
        if (itemName.isBlank() || itemPrice.isBlank() || itemCount.isBlank()) {
            return false
        }
        return true
    }


    private fun getNewItemEntry(itemName: String, itemPrice: String, itemCount: String): Item {
        return Item(
            itemName = itemName,
            itemPrice = itemPrice.toDouble(),
            quantityInStock = itemCount.toInt()
        )
    }


    private fun getUpdatedItemEntry(
        itemId: Int,
        itemName: String,
        itemPrice: String,
        itemCount: String
    ): Item {
        return Item(
            id = itemId,
            itemName = itemName,
            itemPrice = itemPrice.toDouble(),
            quantityInStock = itemCount.toInt()
        )
    }
}


class InventoryViewModelFactory(private val barangDao: BarangDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(InventoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return InventoryViewModel(barangDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

