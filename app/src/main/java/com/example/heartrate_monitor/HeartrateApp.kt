package com.example.heartrate_monitor

import android.app.Application
import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.heartrate_monitor.db.ExerciseDao
import com.example.heartrate_monitor.db.HeartrateDatabase
import com.example.heartrate_monitor.db.HeartrateRepository
import com.facebook.flipper.android.AndroidFlipperClient
import com.facebook.flipper.android.utils.FlipperUtils
import com.facebook.flipper.plugins.databases.DatabasesFlipperPlugin
import com.facebook.flipper.plugins.inspector.DescriptorMapping
import com.facebook.flipper.plugins.inspector.InspectorFlipperPlugin
import com.facebook.soloader.SoLoader
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Provider
import javax.inject.Singleton

@HiltAndroidApp
class HeartrateApp : Application() {

    override fun onCreate() {
        super.onCreate()

        SoLoader.init(this, false)
        if (BuildConfig.DEBUG && FlipperUtils.shouldEnableFlipper(this)) {
            val client = AndroidFlipperClient.getInstance(this)
            client.addPlugin(InspectorFlipperPlugin(this, DescriptorMapping.withDefaults()))
            client.addPlugin(DatabasesFlipperPlugin(this));
            client.start()
        }
    }
}

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
        provider: Provider<ExerciseDao>
    ): HeartrateDatabase =
        Room.databaseBuilder(context, HeartrateDatabase::class.java, "heartrate_database")
            .addCallback(ExerciseCallback(provider)).build()

    @Provides
    fun provideExerciseDao(database: HeartrateDatabase): ExerciseDao = database.getExerciseDao()


    @Provides
    @Singleton
    fun provideHeartrateRepository(
        exerciseDao: ExerciseDao
    ): HeartrateRepository = HeartrateRepository(
        exerciseDao
    )
}

class ExerciseCallback(
    private val provider: Provider<ExerciseDao>
) : RoomDatabase.Callback() {

    private val applicationScope = CoroutineScope(SupervisorJob())

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        applicationScope.launch(Dispatchers.IO) {
            populateDatabase()
        }
    }

    private suspend fun populateDatabase() {
    }
}