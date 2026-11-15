package apps.cronometerSteps

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.record.StepsRecord
import java.time.Instant
import java.time.temporal.ChronoUnit
// import okhttp3.* // For your Cronometer HTTP POST calls

class StepSyncWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            val healthConnectClient = HealthConnectClient.getOrCreate(applicationContext)
            
            // 1. Read today's step count from Health Connect
            val endTime = Instant.now()
            val startTime = endTime.truncatedTo(ChronoUnit.DAYS)
            
            val response = healthConnectClient.readRecords(
                ReadRecordsRequest(
                    recordType = StepsRecord::class,
                    timeRangeFilter = HealthConnectClient.TimeRangeFilter.between(startTime, endTime)
                )
            )
            val totalSteps = response.records.sumOf { it.count }

            // 2. TODO: Fire HTTP POST to Cronometer API using your stored session cookies
            // sendToCronometer(totalSteps)

            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.retry()
        }
    }
}
