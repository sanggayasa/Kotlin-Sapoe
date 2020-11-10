package com.akarinti.sapoe.view.main.visit.visit_parent.question

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.text.InputFilter
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.akarinti.sapoe.R
import com.akarinti.sapoe.base.BaseMvpActivity
import com.akarinti.sapoe.extension.checkPermissions
import com.akarinti.sapoe.extension.formatDigit
import com.akarinti.sapoe.model.questionnaire.ParentAcOffline
import com.akarinti.sapoe.model.questionnaire.ParentCleanOffline
import com.akarinti.sapoe.model.questionnaire.ParentNbOffline
import com.akarinti.sapoe.model.questionnaire.QuestionDetailOffline
import com.akarinti.sapoe.objects.CodeIntent
import com.akarinti.sapoe.objects.ImagePicker
import com.akarinti.sapoe.objects.ImageTools
import com.akarinti.sapoe.objects.Params
import com.akarinti.sapoe.utils.GlideApp
import com.akarinti.sapoe.utils.LocationUtils
import com.akarinti.sapoe.view.component.CustomSpinner
import com.jakewharton.rxbinding3.widget.textChanges
import com.thanosfisherman.mayi.MayI
import com.thanosfisherman.mayi.PermissionBean
import com.thanosfisherman.mayi.PermissionToken
import dagger.android.AndroidInjection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.vrinda.kotlinpermissions.DeviceInfo
import kotlinx.android.synthetic.main.activity_question.*
import kotlinx.android.synthetic.main.sticky_button.*
import kotlinx.android.synthetic.main.toolbar_navigation.*
import org.jetbrains.anko.intentFor
import javax.inject.Inject


class SQuestionActivity : BaseMvpActivity<SQuestionPresenter>(),
    SQuestionContract.View {

    companion object {
        fun newInstance(activity: Activity, unscheduleID: String?, unscheduleType: String?, selectedFormIndex: Int?, selectedChildIndex: Int? = -1) {
            activity.startActivityForResult(activity.intentFor<SQuestionActivity>(
                    Params.BUNDLE_UNSCHEDULE_ID to unscheduleID,
                    Params.BUNDLE_UNSCHEDULE_TYPE to unscheduleType,
                    Params.BUNDLE_UNSCHEDULE_FORM_IDX to selectedFormIndex,
                    Params.BUNDLE_UNSCHEDULE_CHILD_IDX to selectedChildIndex
                ), CodeIntent.OPEN_QUESTION
            )
        }
    }

    private var tempAnswer: ArrayList<String> = ArrayList()

    private var scheduleID: String? = null
    private var scheduleType: String? = null
    private var formIndex: Int = -1
    private var childIndex: Int = -1

    private lateinit var dataAc: ParentAcOffline
    private lateinit var dataNb: ParentNbOffline
    private lateinit var dataCl: ParentCleanOffline

    //picture
    private var currentIdx: Int = -1
    private var currentQuestionTitle: String? = null

    @Inject
    override lateinit var presenter: SQuestionPresenter

    override fun initPresenterView() {
        presenter.view = this
    }

    override fun injectView() {
        AndroidInjection.inject(this)
    }

    override fun setup() {
        initBundle()
        initView()
        initAction()
    }

    override fun getLayout(): Int = R.layout.activity_question

    private fun initBundle() {
        intent?.extras?.let {
            scheduleID = it.getString(Params.BUNDLE_UNSCHEDULE_ID)
            scheduleType = it.getString(Params.BUNDLE_UNSCHEDULE_TYPE)
            formIndex = it.getInt(Params.BUNDLE_UNSCHEDULE_FORM_IDX)
            childIndex = it.getInt(Params.BUNDLE_UNSCHEDULE_CHILD_IDX)
        }

        if (null != scheduleID && null != scheduleType && formIndex != -1) {
            when (scheduleType) {
                Params.TYPE_AC -> {
                    dataAc = presenter.inprogressRepository.scheduledAc?.find { it.id == scheduleID }!!
                    if (null != dataAc) {
                        if (childIndex != -1) {
                            tvTitle.text = dataAc.itemList?.get(childIndex)?.formList?.get(formIndex)?.name
                            dataAc.itemList?.get(childIndex)?.formList?.get(formIndex)?.questionList?.let {
                                addQuestions(it)
                            }
                        } else {
                            tvTitle.text = dataAc.formList?.get(formIndex)?.name
                            dataAc.formList?.get(formIndex)?.questionList?.let {
                                addQuestions(it)
                            }
                        }
                    }
                }
                Params.TYPE_NEONBOX -> {
                    dataNb = presenter.inprogressRepository.scheduledNb?.find { it.id == scheduleID }!!
                    if (null != dataNb) {
                        if (childIndex != -1) {
                            tvTitle.text = dataNb.itemList?.get(childIndex)?.formList?.get(formIndex)?.name
                            dataNb.itemList?.get(childIndex)?.formList?.get(formIndex)?.questionList?.let {
                                addQuestions(it)
                            }
                        } else {
                            tvTitle.text = dataNb.formList?.get(formIndex)?.name
                            dataNb.formList?.get(formIndex)?.questionList?.let {
                                addQuestions(it)
                            }
                        }
                    }
                }
                Params.TYPE_CLEAN -> {
                    dataCl = presenter.inprogressRepository.scheduledCl?.find { it.id == scheduleID }!!
                    if (null != dataCl) {
                        if (childIndex != -1) {
                            tvTitle.text = dataCl.itemList?.get(childIndex)?.formList?.get(formIndex)?.name
                            dataCl.itemList?.get(childIndex)?.formList?.get(formIndex)?.questionList?.let {
                                addQuestions(it)
                            }
                        } else {
                            tvTitle.text = dataCl.formList?.get(formIndex)?.name
                            dataCl.formList?.get(formIndex)?.questionList?.let {
                                addQuestions(it)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun addQuestions(dataQuestion: List<QuestionDetailOffline>) {
        for (i in dataQuestion.indices) {
            when (dataQuestion[i].answerType) {
                "freetext" -> addText(i, dataQuestion[i].question ?: "-", dataQuestion[i].answerLocal, dataQuestion[i].answerLength ?: 100)
                "combobox" -> addSpinner(i, dataQuestion[i].question ?: "-", dataQuestion[i].answerLocal, dataQuestion[i].answerList ?: listOf())
                "numeric" -> addNumeric(i, dataQuestion[i].question ?: "-", dataQuestion[i].answerLocal, dataQuestion[i].answerLength ?: 100)
                "picture" -> addPicture(i, dataQuestion[i].question ?: "-", dataQuestion[i].answerLocal)
                else -> {}
            }
        }
    }

    private fun initView() {
        btnAction.text = getString(R.string.simpan)
        btnAction.isEnabled = false
        tvLatValue.text = DeviceInfo.getLatLong(this)[LocationUtils.LATITUDE_INDEX].formatDigit(5)
        tvLongValue.text = DeviceInfo.getLatLong(this)[LocationUtils.LONGTITUDE_INDEX].formatDigit(5)
    }

    private fun initAction() {
        btnAction.setOnClickListener {
            if (null != scheduleID && null != scheduleType && formIndex != -1) {
                when (scheduleType) {
                    Params.TYPE_AC -> {
                        val orignData: MutableList<ParentAcOffline> = (presenter.inprogressRepository.scheduledAc ?: ArrayList()).toMutableList()
                        orignData.remove(dataAc)
                        if (childIndex != -1) {
                            dataAc.itemList?.get(childIndex)?.formList?.get(formIndex)?.questionList?.let {
                                for (i in it.indices) {
                                    dataAc.itemList!![childIndex].formList!![formIndex].questionList!![i].answerLocal = tempAnswer[i]
                                }
                                dataAc.itemList!![childIndex].formList!![formIndex].isDone = true
                            }
                        } else {
                            dataAc.formList?.get(formIndex)?.questionList?.let {
                                for (i in it.indices) {
                                    dataAc.formList!![formIndex].questionList!![i].answerLocal = tempAnswer[i]
                                }
                                dataAc.formList!![formIndex].isDone = true
                            }
                        }
                        orignData.add(dataAc)
                        presenter.inprogressRepository.scheduledAc = orignData
                    }
                    Params.TYPE_NEONBOX -> {
                        val orignData: MutableList<ParentNbOffline> = (presenter.inprogressRepository.scheduledNb ?: ArrayList()).toMutableList()
                        orignData.remove(dataNb)
                        if (childIndex != -1) {
                            dataNb.itemList?.get(childIndex)?.formList?.get(formIndex)?.questionList?.let {
                                for (i in it.indices) {
                                    dataNb.itemList!![childIndex].formList!![formIndex].questionList!![i].answerLocal = tempAnswer[i]
                                }
                                dataNb.itemList!![childIndex].formList!![formIndex].isDone = true
                            }
                        } else {
                            dataNb.formList?.get(formIndex)?.questionList?.let {
                                for (i in it.indices) {
                                    dataNb.formList!![formIndex].questionList!![i].answerLocal = tempAnswer[i]
                                }
                                dataNb.formList!![formIndex].isDone = true
                            }
                        }
                        orignData.add(dataNb)
                        presenter.inprogressRepository.scheduledNb = orignData
                    }
                    Params.TYPE_CLEAN -> {
                        val orignData: MutableList<ParentCleanOffline> = (presenter.inprogressRepository.scheduledCl ?: ArrayList()).toMutableList()
                        orignData.remove(dataCl)
                        if (childIndex != -1) {
                            dataCl.itemList?.get(childIndex)?.formList?.get(formIndex)?.questionList?.let {
                                for (i in it.indices) {
                                    dataCl.itemList!![childIndex].formList!![formIndex].questionList!![i].answerLocal = tempAnswer[i]
                                }
                                dataCl.itemList!![childIndex].formList!![formIndex].isDone = true
                            }
                        } else {
                            dataCl.formList?.get(formIndex)?.questionList?.let {
                                for (i in it.indices) {
                                    dataCl.formList!![formIndex].questionList!![i].answerLocal = tempAnswer[i]
                                }
                                dataCl.formList!![formIndex].isDone = true
                            }
                        }
                        orignData.add(dataCl)
                        presenter.inprogressRepository.scheduledCl = orignData
                    }
                }
            }
            setResult(Activity.RESULT_OK)
            finish()
        }
        ivBack.setOnClickListener { onBackPressed() }
    }

    private fun addSpinner(idx: Int, questionTitle: String, answer: String? = null, listAnswer: List<String>) {
        val layoutInflater: LayoutInflater = LayoutInflater.from(this)
        val view: View = layoutInflater.inflate(R.layout.spinner_default, contentQuestion, false)
        val tvQuestion = view.findViewById<TextView>(R.id.tvQuestion)
        val tvAnswer = view.findViewById<TextView>(R.id.tvAnswer)
        val csAnswer = view.findViewById<CustomSpinner>(R.id.csAnswer)
        val answerAdapter = ArrayAdapter(this, R.layout.item_spinner_divider_small, R.id.tvSpinnerItem, listAnswer)
        tvQuestion.text = questionTitle
        csAnswer.apply {
            adapter = answerAdapter
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    tvAnswer.text = answerAdapter.getItem(position)
                    tempAnswer.set(idx, tvAnswer.text.toString())
                    checkButton()
                }
            }
        }
        if (null != answer) {
            tvAnswer.text = answer
        }
        tempAnswer.add(idx, answer?:"")

        tvAnswer.setOnClickListener { csAnswer.performClick() }
        contentQuestion.addView(view)
        //csAnswer.setSelection(0)
        checkButton()
    }

    private fun addText(idx: Int, questionTitle: String, answer: String? = null, maxLength: Int) {
        val view: View = LayoutInflater.from(this).inflate(R.layout.textview_default, contentQuestion, false)
        val tvQuestion = view.findViewById<TextView>(R.id.tvQuestion)
        val tvAnswer = view.findViewById<TextView>(R.id.tvAnswer)

        tvQuestion.text = questionTitle
        tvAnswer.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(maxLength))
        if (null != answer) {
            tvAnswer.text = answer
        }
        tempAnswer.add(idx, answer?:"")
        addUiSubscription(tvAnswer.textChanges().observeOn(AndroidSchedulers.mainThread()).subscribe {
            tempAnswer[idx] = it.toString()
            checkButton()
        })
        contentQuestion.addView(view)
        //tvAnswer.setText("ans")
    }

    private fun addNumeric(idx: Int, questionTitle: String, answer: String? = null, maxLength: Int) {
        val view: View = LayoutInflater.from(this).inflate(R.layout.numericview_default, contentQuestion, false)
        val tvQuestion = view.findViewById<TextView>(R.id.tvQuestion)
        val tvAnswer = view.findViewById<TextView>(R.id.tvAnswer)

        tvQuestion.text = questionTitle
        tvAnswer.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(maxLength))
        if (null != answer) {
            tvAnswer.text = answer
        }
        tempAnswer.add(idx, answer?:"")
        addUiSubscription(tvAnswer.textChanges().observeOn(AndroidSchedulers.mainThread()).subscribe {
            tempAnswer[idx] = it.toString()
            checkButton()
        })
        contentQuestion.addView(view)
        //tvAnswer.text = "1"
    }

    // Picture - start
    private fun addPicture(idx: Int, questionTitle: String, answer: String? = null) {
        val view: View = LayoutInflater.from(this).inflate(R.layout.picture_default, contentQuestion, false)
        val tvFotoTitle = view.findViewById<TextView>(R.id.tvFotoTitle)
        val btnAmbil = view.findViewById<TextView>(R.id.btnAmbil)
        val btnFotoUlang = view.findViewById<TextView>(R.id.btnFotoUlang)
        val ivResult = view.findViewById<ImageView>(R.id.ivResult)

        tvFotoTitle.text = questionTitle

        //var answer = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAOEAAADhCAMAAAAJbSJIAAAAh1BMVEUxMjH8/v8tLixQUVEYGhjU0tT29/ghHx/P0dIhIiEnKScVFhXm6OqCf4CnqKnW1dUbHBvb3N/JyMlYVVUAAAANDwyGh4lEPz8kJSO5u734+vuwsLGdnJ3v8PFBQUEqKSlJSklfX1+enp+Uk5Vzc3N5d3cBBgC9v8CDhIRpaGloZGVFRkY6OjlfBncRAAADPklEQVR4nO3Z6XKiQBSGYWQIkUUFAw4ad03U0fu/vlGkEfXQahFTUvU+vxJz6PQnbS9oGAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACeyBQ9UCrX5tckjmVZQaKrKmn3duN3cf6IpJbNqVyraT3x/iz6zWazO5x5TmmV3GxqVjmgN2+IYqHW6si1H2VvtOsMR3nVaB64YpX5V2421U2qBlyUtCwmbD6W0FvaZ3X2LHg0YVfqxyOsXlnTP5Awbl9V9qSImoRv3tMC/kDCYCiUboSBWp4wFO/5A4J1acDqCc0PqXTwSELfqziTJq3ygNUTemEh1+nHz+sZtSyhH1QMaG47b0rWBT9/QRwfKuGpLNWZCj0xt6qjq5mbuMs8r3Vduwvztvxjkb3/sTupvBaagZWJsoa/vtUrQj9OCdenspTUk0RN0pv48Gc36me/L4VxmrcUZWvXW7TfJFTNV+RlCVvygpV3JEvY05elEtXX7+yFKFsZh7oFzsmm3474DlfwhISqryMvK3YXq/7eaqG7uE4J1T1s+MvguJdxg5T22ldJ2IoKH8KSQVdYLOz+l2c5d00br5LQDwvmJRFjv1EQtj/iO0K+SsIz/ZIjgzm7KBy1x+KsW1SrhIZzvWNaGTdOCvVKaAQb+6p6qV/lapZwfzy8Pl18ae9i3RLue5z0Lk/NY91n8VUSht0C7Qp+eIxhtPrFaXWlG6evknAdBydll8RH5mGwepNefvsHE81/eJWE9+xp4sbgoDE7DkrX26rThXQSUWqVMNtof6q+ukaWUDrmK3VKGKhhuVXFUbZ2/KvBPWxF8Rmpy656BGSPj0d1a6Mi1yChPTozEJ9iTPLZsz1NLMdQJ2Jbu7q8SMJLYkKn+KjNP+1u9OtnnRIaVigW7+qw4t+X0Nxdb0v3s5TuFtYsoeGO/avSof4hds0SGqb3eV442tzo+dMSWuHA3ruVMOimZZfKv3sygvHn6cunsGfdWkiTduPQ4qD5o48SD5z31K0j+ORdpLnMdOLpejhvz4ebnXfHRiH7D1W/UvtdppskTpLcEQ8AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAADAL/oP5BU3wQ/BxT8AAAAASUVORK5CYII="
        if (null != answer) {
            if (tempAnswer.size <= idx)
                tempAnswer.add(idx, answer)
            else
                tempAnswer[idx] = answer

            GlideApp.with(this).load(answer).into(ivResult)
            btnFotoUlang.visibility = View.VISIBLE
        } else {
            tempAnswer.add(idx, answer?:"")
        }
        btnAmbil.setOnClickListener {
            showPicker()
            currentIdx = idx
            currentQuestionTitle = questionTitle
        }
        btnFotoUlang.setOnClickListener {
            showPicker()
            currentIdx = idx
            currentQuestionTitle = questionTitle
        }
        contentQuestion.addView(view, idx)
    }

    private fun showPicker() {
        if (checkPermissions(Manifest.permission.CAMERA)) {
            startActivityForResult(
                ImagePicker.getPickImageIntent(this, false, true),
                CodeIntent.UPLOAD_IMAGE
            )
        } else {
            requestCameraPermission()
        }
    }

    private fun requestCameraPermission() {
        MayI.withActivity(this)
            .withPermission(Manifest.permission.CAMERA)
            .onResult(this::permissionResultSingle)
            .onRationale(this::permissionRationaleSingle)
            .check()
    }

    private fun permissionResultSingle(permission: PermissionBean) {
        if (permission.isPermanentlyDenied) {
            AlertDialog.Builder(this).setMessage("This feature need camera permission to run. Do you want to allow it now?"+
                    ImagePicker.getImagePath(this, ImagePicker.TEMP_IMAGE_NAME))
                .setCancelable(false)
                .setPositiveButton(
                    "OK"
                ) { dialog, _ ->
                    dialog.dismiss()
                }
                .setNegativeButton(
                    "Cancel"
                ) { dialog, _ ->
                    dialog.dismiss()
                    showPicker()
                }
                .create().show()
        } else if (permission.isGranted) {
            showPicker()
        }
    }

    private fun permissionRationaleSingle(bean: PermissionBean, token: PermissionToken) {
        token.continuePermissionRequest()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            CodeIntent.UPLOAD_IMAGE -> {
                val success = ImagePicker.getImageFromResult(
                    this,
                    resultCode, data, ImagePicker.TEMP_IMAGE_NAME
                )
                if (success) {
                    showLoading()
                    val imageFile = BitmapFactory.decodeFile(
                        ImagePicker.getImagePath(
                            this,
                            ImagePicker.TEMP_IMAGE_NAME
                        )
                    )
                    if (imageFile != null) {
                        contentQuestion.removeViewAt(currentIdx)
                        addPicture(currentIdx, currentQuestionTitle?:"-", "data:image/png;base64,"+ImageTools.bitmapToBase64(imageFile))
                    }
                    dismissLoading()
                }
                checkButton()
            }
            else -> { }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
    // Picture - end

    private fun checkButton() {
        var isDone = false
        if (null != scheduleType && formIndex != -1) {
            when (scheduleType) {
                Params.TYPE_AC -> {
                    if (childIndex != -1)
                        isDone = checkQuestion(dataAc.itemList?.get(childIndex)?.formList?.get(formIndex)?.questionList)
                    else
                        isDone = checkQuestion(dataAc.formList?.get(formIndex)?.questionList)
                }
                Params.TYPE_NEONBOX -> {
                    if (childIndex != -1)
                        isDone = checkQuestion(dataNb.itemList?.get(childIndex)?.formList?.get(formIndex)?.questionList)
                    else
                        isDone = checkQuestion(dataNb.formList?.get(formIndex)?.questionList)
                }
                Params.TYPE_CLEAN -> {
                    if (childIndex != -1)
                        isDone = checkQuestion(dataCl.itemList?.get(childIndex)?.formList?.get(formIndex)?.questionList)
                    else
                        isDone = checkQuestion(dataCl.formList?.get(formIndex)?.questionList)
                }
            }
        }
        btnAction.isEnabled = isDone
    }

    private fun checkQuestion(data: List<QuestionDetailOffline>?): Boolean {
        if (null != data) {
            for (i in data.indices) {
                if (tempAnswer.size > i) {
                    if (data[i].isRequired == true && tempAnswer[i] == "") {
                        return false
                    }
                } else return false
            }
            return true
        } else return false
    }
}