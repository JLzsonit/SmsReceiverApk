package apps.cronometerSteps

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.record.StepsRecord
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(android.R.layout.activity_list_item)

        // Request Health Connect permissions on launch
        lifecycleScope.launch {
            try {
                val healthConnectClient = HealthConnectClient.getOrCreate(applicationContext)
                val permissions = setOf(HealthPermission.getReadPermission(StepsRecord::class))
                
                val granted = healthConnectClient.permissionController.getGrantedPermissions()
                if (!granted.containsAll(permissions)) {
                    // Triggers system permission prompt
                    healthConnectClient.permissionController.createRequestPermissionResultContract()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
