package com.betulnecanli.todomvvm.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Notes)

    @Delete
    suspend fun deleteNote(note: Notes)

    @Update
    suspend fun updateNote(note: Notes)

    @Query("SELECT * FROM notes_table")
    fun getAllNotes() : Flow<List<Notes>>


    fun getNotes(query: String, sortOrder: SortOrder): Flow<List<Notes>> =
        when(sortOrder){
            SortOrder.BY_DATE ->{
                getNotesSortedByCreatedDate(query)
            }
            SortOrder.BY_NAME ->{
                getNotesSortedByName(query)
            }

        }

    @Query("SELECT * FROM notes_table WHERE title LIKE '%' || :query || '%' ORDER BY title ASC")
    fun getNotesSortedByName(query: String): Flow<List<Notes>>


    @Query("SELECT * FROM notes_table WHERE title LIKE '%' || :query || '%' ORDER BY date DESC")
    fun getNotesSortedByCreatedDate(query: String): Flow<List<Notes>>


    @Query("DELETE FROM notes_table")
    suspend fun deleteAllNotes()

}