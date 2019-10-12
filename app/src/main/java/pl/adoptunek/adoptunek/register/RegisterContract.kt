package pl.adoptunek.adoptunek.register

interface RegisterContract {

    interface RegisterView {
        fun showLoadingNextBtn(loading: Boolean)

        fun registerOperationResult(successfull: Boolean, error: String = "empty")
    }

    interface RegisterPresenter {
        fun nextBtnClicked(email: String, pass: String, repPass: String)
    }
}