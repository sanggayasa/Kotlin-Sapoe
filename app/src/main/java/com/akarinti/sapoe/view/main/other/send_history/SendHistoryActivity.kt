package com.akarinti.sapoe.view.main.other.send_history

import com.akarinti.sapoe.R
import com.akarinti.sapoe.base.BaseMvpActivity
import com.akarinti.sapoe.extension.format
import com.akarinti.sapoe.view.component.dialog.ConfirmDialogFragment
import com.akarinti.sapoe.view.component.dialog.FinisDialogFragment
import com.tsongkha.spinnerdatepicker.DatePickerDialog
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_send_history.*
import kotlinx.android.synthetic.main.sticky_button.*
import kotlinx.android.synthetic.main.toolbar_navigation.*
import kotlinx.android.synthetic.main.toolbar_navigation.tvTitle
import java.util.*
import javax.inject.Inject

class SendHistoryActivity : BaseMvpActivity<SendHistoryPresenter>(), ConfirmDialogFragment.Listener,
    FinisDialogFragment.Listener, SendHistoryContract.View {

    private var datePickerDialog: DatePickerDialog? = null
    var source: Int = 0
    private var startDate: Long = 0L
    private var endDate: Long = 0L

    @Inject
    override lateinit var presenter: SendHistoryPresenter

    override fun initPresenterView() {
        presenter.view = this
    }

    override fun injectView() {
        AndroidInjection.inject(this)
    }

    override fun setup() {
        initView()
        initCalendar()
        initAction()
    }

    override fun getLayout(): Int = R.layout.activity_send_history

    private fun initView() {
        tvTitle.text = getString(R.string.kirim_history)
        btnAction.text = getString(R.string.kirim)
        checkBtn()
    }

    private fun initAction() {
        ivBack.setOnClickListener {
            onBackPressed()
        }

        etDari.setOnClickListener {
            datePickerDialog?.show()
            source = 1
        }
        etSampai.setOnClickListener {
            datePickerDialog?.show()
            source = 2
        }
        btnAction.setOnClickListener {
            ConfirmDialogFragment.newInstance("", getString(R.string.kirim_history_confirm)
                , getString(R.string.kirim), getString(R.string.batal))
                .show(supportFragmentManager, ConfirmDialogFragment::class.java.canonicalName)
        }
    }

    private fun initCalendar() {
        val calendar = Calendar.getInstance()
        datePickerDialog = SpinnerDatePickerDialogBuilder()
            .context(this).callback { _, y, m, d ->
                calendar.set(Calendar.YEAR, y)
                calendar.set(Calendar.MONTH, m)
                calendar.set(Calendar.DAY_OF_MONTH, d)

                when (source) {
                    1 -> {
                        etDari.text = calendar.time.format("EEE, dd/MM/yyyy")
                        calendar.set(Calendar.HOUR, 0)
                        startDate = calendar.timeInMillis / 1000L
                        checkBtn()
                    }
                    2 -> {
                        etSampai.text = calendar.time.format("EEE, dd/MM/yyyy")
                        calendar.set(Calendar.HOUR, 23)
                        endDate = calendar.timeInMillis / 1000L
                        checkBtn()
                    }
                    else -> {
                    }
                }
                checkBtn()
            }
            .spinnerTheme(R.style.CalendarPicker)
            .showTitle(true)
            .showDaySpinner(true)
            .defaultDate(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DATE)
            )
            .maxDate(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DATE)
            )
            .minDate(1950, 0, 1)
            .build()
    }

    private fun checkBtn() {
        btnAction.isEnabled = etDari.text.toString() != "-" && etSampai.text.toString() != "-" && endDate > startDate
    }

    override fun onCancelDialogBtnPressed(tag: String?) {
    }

    override fun onConfirmDialogBtnPressed(tag: String?) {
        showLoading()
        presenter.sendHistory(startDate, endDate)
    }

    override fun onConfirmDialogDismissed() {
    }

    override fun onFinishDialogBtnPressed() {
        onBackPressed()
    }

    override fun onSendHistory() {
        dismissLoading()
        FinisDialogFragment.newInstance(getString(R.string.kirim_history_finish),
            getString(R.string.tutup),R.drawable.popup_success)
            .show(supportFragmentManager, FinisDialogFragment::class.java.canonicalName)
    }
}