package com.akarinti.sapoe.view.main.visit.answer_parent.question

import android.app.Activity
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.akarinti.sapoe.R
import com.akarinti.sapoe.base.BaseActivity
import com.akarinti.sapoe.extension.formatDigit
import com.akarinti.sapoe.model.questionnaire.ParentAcOffline
import com.akarinti.sapoe.model.questionnaire.ParentCleanOffline
import com.akarinti.sapoe.model.questionnaire.ParentNbOffline
import com.akarinti.sapoe.model.questionnaire.QuestionDetailOffline
import com.akarinti.sapoe.objects.Params
import com.akarinti.sapoe.utils.GlideApp
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_question.*
import kotlinx.android.synthetic.main.sticky_button.*
import kotlinx.android.synthetic.main.toolbar_navigation.*
import org.jetbrains.anko.intentFor


class SAnswerActivity : BaseActivity() {

    companion object {
        fun newInstance(activity: Activity, parent: ParentAcOffline?, scheduleType: String?, selectedFormIndex: Int?, selectedChildIndex: Int? = -1) {
            activity.startActivity(activity.intentFor<SAnswerActivity>(
                Params.BUNDLE_PARENT_AC to parent,
                Params.BUNDLE_SCHEDULE_TYPE to scheduleType,
                Params.BUNDLE_SCHEDULE_FORM_IDX to selectedFormIndex,
                Params.BUNDLE_SCHEDULE_CHILD_IDX to selectedChildIndex))
        }
        fun newInstance(activity: Activity, parent: ParentNbOffline?, scheduleType: String?, selectedFormIndex: Int?, selectedChildIndex: Int? = -1) {
            activity.startActivity(activity.intentFor<SAnswerActivity>(
                Params.BUNDLE_PARENT_NB to parent,
                Params.BUNDLE_SCHEDULE_TYPE to scheduleType,
                Params.BUNDLE_SCHEDULE_FORM_IDX to selectedFormIndex,
                Params.BUNDLE_SCHEDULE_CHILD_IDX to selectedChildIndex))
        }
        fun newInstance(activity: Activity, parent: ParentCleanOffline?, scheduleType: String?, selectedFormIndex: Int?, selectedChildIndex: Int? = -1) {
            activity.startActivity(activity.intentFor<SAnswerActivity>(
                Params.BUNDLE_PARENT_CLEAN to parent,
                Params.BUNDLE_SCHEDULE_TYPE to scheduleType,
                Params.BUNDLE_SCHEDULE_FORM_IDX to selectedFormIndex,
                Params.BUNDLE_SCHEDULE_CHILD_IDX to selectedChildIndex))
        }
    }

    private var scheduleType: String? = null
    private var formIndex: Int = -1
    private var childIndex: Int = -1

    private var dataAc: ParentAcOffline? = null
    private var dataNb: ParentNbOffline? = null
    private var dataCl: ParentCleanOffline? = null

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
            dataAc = it.getParcelable(Params.BUNDLE_PARENT_AC)
            dataNb = it.getParcelable(Params.BUNDLE_PARENT_NB)
            dataCl = it.getParcelable(Params.BUNDLE_PARENT_CLEAN)
            scheduleType = it.getString(Params.BUNDLE_SCHEDULE_TYPE)
            formIndex = it.getInt(Params.BUNDLE_SCHEDULE_FORM_IDX)
            childIndex = it.getInt(Params.BUNDLE_SCHEDULE_CHILD_IDX)
        }

        if (null != scheduleType && formIndex != -1) {
            when (scheduleType) {
                Params.TYPE_AC -> {
                    dataAc?.let { parent ->
                        if (childIndex != -1) {
                            tvTitle.text = parent.itemList?.get(childIndex)?.formList?.get(formIndex)?.name
                            parent.itemList?.get(childIndex)?.formList?.get(formIndex)?.questionList?.let {
                                addQuestions(it)
                            }
                        } else {
                            tvTitle.text = parent.formList?.get(formIndex)?.name
                            parent.formList?.get(formIndex)?.questionList?.let {
                                addQuestions(it)
                            }
                        }
                        tvLatValue.text = parent.location?.lat?.formatDigit(5)
                        tvLongValue.text = parent.location?.long?.formatDigit(5)
                    }
                }
                Params.TYPE_NEONBOX -> {
                    dataNb?.let { parent ->
                        if (childIndex != -1) {
                            tvTitle.text = parent.itemList?.get(childIndex)?.formList?.get(formIndex)?.name
                            parent.itemList?.get(childIndex)?.formList?.get(formIndex)?.questionList?.let {
                                addQuestions(it)
                            }
                        } else {
                            tvTitle.text = parent.formList?.get(formIndex)?.name
                            parent.formList?.get(formIndex)?.questionList?.let {
                                addQuestions(it)
                            }
                        }
                        tvLatValue.text = parent.location?.lat?.formatDigit(5)
                        tvLongValue.text = parent.location?.long?.formatDigit(5)
                    }
                }
                Params.TYPE_CLEAN -> {
                    dataCl?.let { parent ->
                        if (childIndex != -1) {
                            tvTitle.text = parent.itemList?.get(childIndex)?.formList?.get(formIndex)?.name
                            parent.itemList?.get(childIndex)?.formList?.get(formIndex)?.questionList?.let {
                                addQuestions(it)
                            }
                        } else {
                            tvTitle.text = parent.formList?.get(formIndex)?.name
                            parent.formList?.get(formIndex)?.questionList?.let {
                                addQuestions(it)
                            }
                        }
                        tvLatValue.text = parent.location?.lat?.formatDigit(5)
                        tvLongValue.text = parent.location?.long?.formatDigit(5)
                    }
                }
            }
        }
    }

    private fun addQuestions(dataQuestion: List<QuestionDetailOffline>) {
        for (i in dataQuestion.indices) {
            when (dataQuestion[i].answerType) {
                "freetext" -> addText(dataQuestion[i].question ?: "-", dataQuestion[i].answerData?.answer)
                "combobox" -> addSpinner(dataQuestion[i].question ?: "-", dataQuestion[i].answerData?.answer)
                "numeric" -> addNumeric(dataQuestion[i].question ?: "-", dataQuestion[i].answerData?.answer)
                "picture" -> addPicture(dataQuestion[i].question ?: "-", dataQuestion[i].answerData?.answer)
                else -> {}
            }
        }
    }

    private fun initView() {
        btnAction.visibility = View.INVISIBLE
    }

    private fun initAction() {
        ivBack.setOnClickListener { onBackPressed() }
    }

    private fun addSpinner(questionTitle: String, answer: String? = null) {
        val layoutInflater: LayoutInflater = LayoutInflater.from(this)
        val view: View = layoutInflater.inflate(R.layout.spinner_default, contentQuestion, false)
        val tvQuestion = view.findViewById<TextView>(R.id.tvQuestion)
        val tvAnswer = view.findViewById<TextView>(R.id.tvAnswer)

        tvQuestion.text = questionTitle
        tvAnswer.text = answer

        contentQuestion.addView(view)
    }

    private fun addText(questionTitle: String, answer: String? = null) {
        val view: View = LayoutInflater.from(this).inflate(R.layout.textview_default, contentQuestion, false)
        val tvQuestion = view.findViewById<TextView>(R.id.tvQuestion)
        val tvAnswer = view.findViewById<TextView>(R.id.tvAnswer)

        tvQuestion.text = questionTitle
        tvAnswer.text = answer
        contentQuestion.addView(view)
    }

    private fun addNumeric(questionTitle: String, answer: String? = null) {
        val view: View = LayoutInflater.from(this).inflate(R.layout.numericview_default, contentQuestion, false)
        val tvQuestion = view.findViewById<TextView>(R.id.tvQuestion)
        val tvAnswer = view.findViewById<TextView>(R.id.tvAnswer)

        tvQuestion.text = questionTitle
        if (null != answer) {
            tvAnswer.text = answer
        }
        contentQuestion.addView(view)
    }

    private fun addPicture(questionTitle: String, answer: String? = null) {
        val view: View = LayoutInflater.from(this).inflate(R.layout.picture_default, contentQuestion, false)
        val tvFotoTitle = view.findViewById<TextView>(R.id.tvFotoTitle)
        val ivResult = view.findViewById<ImageView>(R.id.ivResult)
        view.findViewById<ImageView>(R.id.ivIcon).visibility = View.INVISIBLE
        view.findViewById<TextView>(R.id.btnAmbil).visibility = View.INVISIBLE

        tvFotoTitle.text = questionTitle
        GlideApp.with(this)
            .load(answer)
            .fitCenter()
            .listener(object : RequestListener<Drawable?> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable?>?,
                    isFirstResource: Boolean
                ): Boolean {
//                    toast("onLoadFailed : ${e?.message}")
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable?>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
//                    toast("onResourceReady : $dataSource")
                    return false
                }
            })
            .into(ivResult)
        contentQuestion.addView(view)
    }
}