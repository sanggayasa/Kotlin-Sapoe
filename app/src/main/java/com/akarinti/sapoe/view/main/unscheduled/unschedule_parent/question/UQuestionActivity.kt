package com.akarinti.sapoe.view.main.unscheduled.unschedule_parent.question

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
import com.akarinti.sapoe.model.questionnaire.*
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


class UQuestionActivity : BaseMvpActivity<UQuestionPresenter>(),
    UQuestionContract.View {

    companion object {
        fun newInstance(activity: Activity, unscheduleID: String?, unscheduleType: String?, selectedFormIndex: Int?, selectedChildIndex: Int? = -1) {
            activity.startActivityForResult(activity.intentFor<UQuestionActivity>(
                    Params.BUNDLE_UNSCHEDULE_ID to unscheduleID,
                    Params.BUNDLE_UNSCHEDULE_TYPE to unscheduleType,
                    Params.BUNDLE_UNSCHEDULE_FORM_IDX to selectedFormIndex,
                    Params.BUNDLE_UNSCHEDULE_CHILD_IDX to selectedChildIndex
                ), CodeIntent.OPEN_QUESTION
            )
        }
    }

    private var tempAnswer: ArrayList<String> = ArrayList()

    private var unscheduleID: String? = null
    private var unscheduleType: String? = null
    private var formIndex: Int = -1
    private var childIndex: Int = -1

    private lateinit var dataAc: ParentAcOffline
    private lateinit var dataNb: ParentNbOffline
    private lateinit var dataCl: ParentCleanOffline
    private lateinit var dataMcds: ParentOtherOffline
    private lateinit var dataQc: ParentOtherOffline

    //picture
    private var currentIdx: Int = -1
    private var currentQuestionTitle: String? = null

    @Inject
    override lateinit var presenter: UQuestionPresenter

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
            unscheduleID = it.getString(Params.BUNDLE_UNSCHEDULE_ID)
            unscheduleType = it.getString(Params.BUNDLE_UNSCHEDULE_TYPE)
            formIndex = it.getInt(Params.BUNDLE_UNSCHEDULE_FORM_IDX)
            childIndex = it.getInt(Params.BUNDLE_UNSCHEDULE_CHILD_IDX)
        }

        if (null != unscheduleID && null != unscheduleType && formIndex != -1) {
            when (unscheduleType) {
                Params.TYPE_AC -> {
                    dataAc = presenter.inprogressRepository.unscheduledAc?.find { it.id == unscheduleID }!!
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
                    dataNb = presenter.inprogressRepository.unscheduledNb?.find { it.id == unscheduleID }!!
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
                    dataCl = presenter.inprogressRepository.unscheduledCl?.find { it.id == unscheduleID }!!
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
                Params.TYPE_MCDS -> {
                    dataMcds = presenter.inprogressRepository.unscheduledMcds?.find { it.id == unscheduleID }!!
                    if (null != dataMcds) {
                        if (childIndex != -1) {
                            tvTitle.text = dataMcds.itemList?.get(childIndex)?.formList?.get(formIndex)?.name
                            dataMcds.itemList?.get(childIndex)?.formList?.get(formIndex)?.questionList?.let {
                                addQuestions(it)
                            }
                        } else {
                            tvTitle.text = dataMcds.formList?.get(formIndex)?.name
                            dataMcds.formList?.get(formIndex)?.questionList?.let {
                                addQuestions(it)
                            }
                        }
                    }
                }
                Params.TYPE_QC -> {
                    dataQc = presenter.inprogressRepository.unscheduledQc?.find { it.id == unscheduleID }!!
                    if (null != dataQc) {
                        if (childIndex != -1) {
                            tvTitle.text = dataQc.itemList?.get(childIndex)?.formList?.get(formIndex)?.name
                            dataQc.itemList?.get(childIndex)?.formList?.get(formIndex)?.questionList?.let {
                                addQuestions(it)
                            }
                        } else {
                            tvTitle.text = dataQc.formList?.get(formIndex)?.name
                            dataQc.formList?.get(formIndex)?.questionList?.let {
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
            if (null != unscheduleID && null != unscheduleType && formIndex != -1) {
                when (unscheduleType) {
                    Params.TYPE_AC -> {
                        val orignData: MutableList<ParentAcOffline> = (presenter.inprogressRepository.unscheduledAc ?: ArrayList()).toMutableList()
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
                        presenter.inprogressRepository.unscheduledAc = orignData
                    }
                    Params.TYPE_NEONBOX -> {
                        val orignData: MutableList<ParentNbOffline> = (presenter.inprogressRepository.unscheduledNb ?: ArrayList()).toMutableList()
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
                        presenter.inprogressRepository.unscheduledNb = orignData
                    }
                    Params.TYPE_CLEAN -> {
                        val orignData: MutableList<ParentCleanOffline> = (presenter.inprogressRepository.unscheduledCl ?: ArrayList()).toMutableList()
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
                        presenter.inprogressRepository.unscheduledCl = orignData
                    }
                    Params.TYPE_MCDS -> {
                        val orignData: MutableList<ParentOtherOffline> = (presenter.inprogressRepository.unscheduledMcds ?: ArrayList()).toMutableList()
                        orignData.remove(dataMcds)
                        if (childIndex != -1) {
                            dataMcds.itemList?.get(childIndex)?.formList?.get(formIndex)?.questionList?.let {
                                for (i in it.indices) {
                                    dataMcds.itemList!![childIndex].formList!![formIndex].questionList!![i].answerLocal = tempAnswer[i]
                                }
                                dataMcds.itemList!![childIndex].formList!![formIndex].isDone = true
                            }
                        } else {
                            dataMcds.formList?.get(formIndex)?.questionList?.let {
                                for (i in it.indices) {
                                    dataMcds.formList!![formIndex].questionList!![i].answerLocal = tempAnswer[i]
                                }
                                dataMcds.formList!![formIndex].isDone = true
                            }
                        }

                        orignData.add(dataMcds)
                        presenter.inprogressRepository.unscheduledMcds = orignData
                    }
                    Params.TYPE_QC -> {
                        val orignData: MutableList<ParentOtherOffline> = (presenter.inprogressRepository.unscheduledQc ?: ArrayList()).toMutableList()
                        orignData.remove(dataQc)
                        if (childIndex != -1) {
                            dataQc.itemList?.get(childIndex)?.formList?.get(formIndex)?.questionList?.let {
                                for (i in it.indices) {
                                    dataQc.itemList!![childIndex].formList!![formIndex].questionList!![i].answerLocal = tempAnswer[i]
                                }
                                dataQc.itemList!![childIndex].formList!![formIndex].isDone = true
                            }
                        } else {
                            dataQc.formList?.get(formIndex)?.questionList?.let {
                                for (i in it.indices) {
                                    dataQc.formList!![formIndex].questionList!![i].answerLocal = tempAnswer[i]
                                }
                                dataQc.formList!![formIndex].isDone = true
                            }
                        }

                        orignData.add(dataQc)
                        presenter.inprogressRepository.unscheduledQc = orignData
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
    }

    // Picture - start
    private fun addPicture(idx: Int, questionTitle: String, answer: String? = null) {
        val view: View = LayoutInflater.from(this).inflate(R.layout.picture_default, contentQuestion, false)
        val tvFotoTitle = view.findViewById<TextView>(R.id.tvFotoTitle)
        val btnAmbil = view.findViewById<TextView>(R.id.btnAmbil)
        val btnFotoUlang = view.findViewById<TextView>(R.id.btnFotoUlang)
        val ivResult = view.findViewById<ImageView>(R.id.ivResult)

        tvFotoTitle.text = questionTitle

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
        if (null != unscheduleType && formIndex != -1) {
            when (unscheduleType) {
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
                Params.TYPE_MCDS -> {
                    if (childIndex != -1)
                        isDone = checkQuestion(dataMcds.itemList?.get(childIndex)?.formList?.get(formIndex)?.questionList)
                    else
                        isDone = checkQuestion(dataMcds.formList?.get(formIndex)?.questionList)
                }
                Params.TYPE_QC -> {
                    if (childIndex != -1)
                        isDone = checkQuestion(dataQc.itemList?.get(childIndex)?.formList?.get(formIndex)?.questionList)
                    else
                        isDone = checkQuestion(dataQc.formList?.get(formIndex)?.questionList)
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