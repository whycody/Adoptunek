package pl.adoptunek.adoptunek.network

import android.content.Context
import android.net.ConnectivityManager
import android.os.CountDownTimer

class NetworkConnectionImpl(val context: Context,
                            val interractor: NetworkContract.NetworkInterractor? = null)
    : NetworkContract.NetworkConnection {

    private var seconds = 4

    override fun isConnectionAvailable(): Boolean {
        return try {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = cm.activeNetworkInfo
            networkInfo!=null&&networkInfo.isConnected
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    override fun checkConnectionWithTimer() {
        try {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            var networkInfo = cm.activeNetworkInfo
            var returned = false
            object: CountDownTimer((seconds*1000).toLong(),1000){
                override fun onTick(p0: Long) {
                    networkInfo = cm.activeNetworkInfo
                    if(networkInfo!=null&&networkInfo.isConnected&&!returned) {
                        interractor?.connectionIsAvailable(true)
                        returned = true
                        seconds = 4
                    }
                }

                override fun onFinish() {
                    if(!returned){
                        interractor?.connectionIsAvailable(networkInfo!=null&&networkInfo.isConnected)
                        seconds = 2
                    }
                }
            }.start()
        } catch (e: Exception) {
            e.printStackTrace()
           interractor?.connectionIsAvailable(false)
        }
    }
}