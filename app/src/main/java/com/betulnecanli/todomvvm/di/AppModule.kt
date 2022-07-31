package com.betulnecanli.todomvvm.di

import android.app.Application
import androidx.room.Room
import com.betulnecanli.todomvvm.data.NoteDatabase
import com.betulnecanli.todomvvm.data.TaskDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(
        app : Application,
        callBack : TaskDatabase.Callback
    ) = Room.databaseBuilder(app, TaskDatabase::class.java, "task_database")
        .fallbackToDestructiveMigration()
        .addCallback(callBack)
        .build()


    @Provides
    fun providesDao(db : TaskDatabase) = db.taskDao()

    @Provides
    @Singleton
    fun provideNoteDatabase(
        app: Application,
    )
    = Room.databaseBuilder(app, NoteDatabase::class.java, "note_database")
        .fallbackToDestructiveMigration()
        .build()

    @Provides
    fun providesNoteDao(db: NoteDatabase) = db.notesDao()



    @ApplicationScope
    @Provides
    @Singleton
    fun provideApplicationScope() = CoroutineScope(SupervisorJob())




}

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class ApplicationScope