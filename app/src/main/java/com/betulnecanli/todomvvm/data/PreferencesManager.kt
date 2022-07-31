package com.betulnecanli.todomvvm.data

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton


private val Context.dataStore : DataStore<Preferences> by preferencesDataStore("user_preferences")

enum class SortOrder{
    BY_NAME,
    BY_DATE
}

data class FilterPreferences(val sortOrder: SortOrder, val hideCompleted: Boolean)
data class FilterPreferencesNotes(val sortOrder: SortOrder)
@Singleton
class PreferencesManager @Inject constructor(@ApplicationContext context : Context) {

    private val dataStore : DataStore<Preferences> = context.dataStore
    private val dataStoreNotes : DataStore<Preferences> = context.dataStore

    val preferencesFlow = dataStore.data
        .catch {
            exception ->
            if(exception is IOException){
                Log.e(TAG, "Error reading preferences", exception)
                emit(emptyPreferences())
            }
            else{
                throw exception
            }
        }


        .map {
        preferences ->
        val sortOrder = SortOrder.valueOf(preferences[PreferencesKeys.SORT_ORDER]
            ?: SortOrder.BY_DATE.name)



        val hideCompleted = preferences[PreferencesKeys.HIDE_COMPLETED] ?: false


        FilterPreferences(sortOrder, hideCompleted)




    }

    val notesPreferencesFlow = dataStoreNotes.data
        .catch {
                exception ->
            if(exception is IOException){
                Log.e(TAG, "Error reading preferences", exception)
                emit(emptyPreferences())
            }
            else{
                throw exception
            }
        }
        .map {
                preferences ->

            val sortOrderNotes = SortOrder.valueOf(preferences[PreferencesKeys.SORT_ORDER_NOTES]
                  ?: SortOrder.BY_DATE.name)


            FilterPreferencesNotes(sortOrderNotes)



        }



    suspend fun updateSortOrder(sortOrder: SortOrder){
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.SORT_ORDER] = sortOrder.name
        }
    }

    suspend fun updateSortOrderNotes(sortOrder: SortOrder){
        dataStore.edit {  preferences ->
            preferences[PreferencesKeys.SORT_ORDER_NOTES] = sortOrder.name

        }
    }



    suspend fun updateHideCompleted(hideCompleted: Boolean){
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.HIDE_COMPLETED] = hideCompleted
        }
    }


    private object PreferencesKeys{
        val SORT_ORDER = stringPreferencesKey("sort_order")
        val SORT_ORDER_NOTES = stringPreferencesKey("sort_order_notes")
        val HIDE_COMPLETED = booleanPreferencesKey("hide_completed")
    }
}