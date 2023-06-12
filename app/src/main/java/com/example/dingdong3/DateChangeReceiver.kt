
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.dingdong3.MainActivity

class DateChangeReceiver(private val mainActivity: MainActivity) : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_DATE_CHANGED) {
            mainActivity.initToday()
        }
    }
}
