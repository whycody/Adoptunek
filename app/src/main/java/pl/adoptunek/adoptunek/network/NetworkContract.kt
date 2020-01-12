package pl.adoptunek.adoptunek.network

interface NetworkContract {

    interface NetworkConnection {

        fun isConnectionAvailable(): Boolean

        fun checkConnectionWithTimer()
    }

    interface NetworkInterractor {

        fun connectionIsAvailable(available: Boolean)
    }
}