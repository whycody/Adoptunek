package pl.adoptunek.adoptunek.register

interface RegisterContract {

    interface RegisterView {
        fun showLoadingNextBtn(loading: Boolean)

        fun showLoadingGoogleBtn(loading: Boolean)

        fun registerOperationCompleted(successfull: Boolean, error: String = "empty")
    }

    interface RegisterPresenter {
        fun nextBtnClicked(email: String, pass: String, repPass: String)

        fun loginWithGoogleClicked()
    }
}