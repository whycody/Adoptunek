package pl.adoptunek.adoptunek.login

interface LoginContract {

    interface LoginView{
        fun showLoadingNextBtn(loading: Boolean)

        fun showLoadingGoogleBtn(loading: Boolean)

        fun loginOperationCompleted(successfull: Boolean, error: String = "empty")
    }

    interface LoginPresenter{
        fun nextBtnClicked(email: String, pass: String)

        fun loginWithGoogleBtnClicked()
    }
}