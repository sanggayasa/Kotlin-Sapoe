package com.akarinti.sapoe.view.main.visit.answer_parent

import android.app.Activity
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.akarinti.sapoe.R
import com.akarinti.sapoe.base.BaseMvpActivity
import com.akarinti.sapoe.model.Schedule
import com.akarinti.sapoe.model.questionnaire.ParentAcOffline
import com.akarinti.sapoe.model.questionnaire.ParentCleanOffline
import com.akarinti.sapoe.model.questionnaire.ParentNbOffline
import com.akarinti.sapoe.objects.Params
import com.akarinti.sapoe.view.main.visit.answer_parent.adapter.AnswerAdapter
import com.akarinti.sapoe.view.main.visit.answer_parent.adapter.ChildAcAnswerAdapter
import com.akarinti.sapoe.view.main.visit.answer_parent.adapter.ChildCleanAnswerAdapter
import com.akarinti.sapoe.view.main.visit.answer_parent.adapter.ChildNbAnswerAdapter
import com.akarinti.sapoe.view.main.visit.answer_parent.child.AnswerChildActivity
import com.akarinti.sapoe.view.main.visit.answer_parent.question.SAnswerActivity
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_answer_parent.*
import kotlinx.android.synthetic.main.toolbar_navigation.*
import org.jetbrains.anko.intentFor
import javax.inject.Inject

class AnswerParentActivity : BaseMvpActivity<AnswerParentPresenter>(), AnswerParentContract.View {

    companion object {
        fun newInstance(activity: Activity, schedule: Schedule) {
            activity.startActivity(activity.intentFor<AnswerParentActivity>(
                    Params.BUNDLE_SCHEDULE to schedule))
        }
    }

    @Inject
    override lateinit var presenter: AnswerParentPresenter
    private var schedule: Schedule? = null
    private lateinit var adapterCl: ChildCleanAnswerAdapter
    private lateinit var adapterNb: ChildNbAnswerAdapter
    private lateinit var adapterAc: ChildAcAnswerAdapter
    private lateinit var adapterQuestion: AnswerAdapter

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

    override fun getLayout(): Int = R.layout.activity_answer_parent

    private fun initBundle() {
        intent?.extras?.let {
            schedule = it.getParcelable(Params.BUNDLE_SCHEDULE)
        }
        schedule?.let {
            showLoading()
            loadData()
        }
    }

    private fun loadData() {
        showLoading()
        schedule?.let {
            when (it.type) {
                Params.TYPE_AC -> presenter.getQuestionaireParentAC(it.id ?: "")
                Params.TYPE_NEONBOX -> presenter.getQuestionaireParentNB(it.id ?: "")
                Params.TYPE_CLEAN -> presenter.getQuestionaireParentClean(it.id ?: "")
                else -> { }
            }
        }
    }

    private fun initView() {
        ivMap.visibility = View.VISIBLE
        bottomButton.visibility = View.INVISIBLE
        tvTitle.setText(R.string.kunjungan_parent)
    }

    private fun initAction() {
        ivBack.setOnClickListener { onBackPressed() }
    }

    private fun initQuestionList() {
        rvQuestion.apply {
            layoutManager = LinearLayoutManager(this@AnswerParentActivity)
            adapter = adapterQuestion
            isNestedScrollingEnabled = false
        }
    }

    override fun onQuestionaireParentAc(data: ParentAcOffline?) {
        data?.let {
            tvPlace.text = it.location?.name?:"-"
            tvAddress.text = it.location?.address ?:"-"
            ivStatus.setImageResource(R.drawable.label_ac)
            adapterAc = ChildAcAnswerAdapter(it.itemList?: listOf(), it.location?.name?:"-")
            rvChild.apply {
                layoutManager = LinearLayoutManager(this@AnswerParentActivity)
                adapter = adapterAc
                isNestedScrollingEnabled = false
            }
            adapterAc.setOnItemClickListener { _, _, pos ->
                AnswerChildActivity.newInstance(this, it, schedule?.type, pos)
            }
            adapterQuestion = AnswerAdapter(it.formList?: listOf())
            adapterQuestion.setOnItemClickListener { _, _, position ->
                SAnswerActivity.newInstance(this, it, schedule?.type, position)
//                when(adapterQuestion.data[position]?.questionList?.get(0)?.answerType) {
//                    Params.QUESTION_PICTURE -> PictureAnswerActivity.newInstance(this, it, schedule?.type, position)
//                    Params.QUESTION_COMBOBOX -> ComboBoxAnswerActivity.newInstance(this, it, schedule?.type, position)
//                }
            }
            initQuestionList()
        }
        dismissLoading()
    }

    override fun onQuestionaireParentNb(data: ParentNbOffline?) {
        data?.let {
            tvPlace.text = it.location?.name?:"-"
            tvAddress.text = it.location?.address ?:"-"
            ivStatus.setImageResource(R.drawable.label_nb)
            adapterNb = ChildNbAnswerAdapter(it.itemList?: listOf(), it.location?.name?:"-")
            rvChild.apply {
                layoutManager = LinearLayoutManager(this@AnswerParentActivity)
                adapter = adapterNb
                isNestedScrollingEnabled = false
            }
            adapterNb.setOnItemClickListener { _, _, pos ->
                AnswerChildActivity.newInstance(this, it, schedule?.type, pos)
            }
            adapterQuestion = AnswerAdapter(it.formList?: listOf())
            adapterQuestion.setOnItemClickListener { _, _, position ->
                SAnswerActivity.newInstance(this, it, schedule?.type, position)
//                when(adapterQuestion.data[position]?.questionList?.get(0)?.answerType) {
//                    Params.QUESTION_PICTURE -> PictureAnswerActivity.newInstance(this, it, schedule?.type, position)
//                    Params.QUESTION_COMBOBOX -> ComboBoxAnswerActivity.newInstance(this, it, schedule?.type, position)
//                }
            }
            initQuestionList()
        }
        dismissLoading()
    }

    override fun onQuestionaireParentClean(data: ParentCleanOffline?) {
        data?.let {
            tvPlace.text = it.location?.name?:"-"
            tvAddress.text = it.location?.address ?:"-"
            ivStatus.setImageResource(R.drawable.label_clean)
            adapterCl = ChildCleanAnswerAdapter(it.itemList?: listOf(), it.location?.name?:"-")
            rvChild.apply {
                layoutManager = LinearLayoutManager(this@AnswerParentActivity)
                adapter = adapterCl
                isNestedScrollingEnabled = false
            }
            adapterCl.setOnItemClickListener { _, _, pos ->
                AnswerChildActivity.newInstance(this, it, schedule?.type, pos)
            }
            adapterQuestion = AnswerAdapter(it.formList?: listOf())
            adapterQuestion.setOnItemClickListener { _, _, position ->
                SAnswerActivity.newInstance(this, it, schedule?.type, position)
//                when(adapterQuestion.data[position]?.questionList?.get(0)?.answerType) {
//                    Params.QUESTION_PICTURE -> PictureAnswerActivity.newInstance(this, it, schedule?.type, position)
//                    Params.QUESTION_COMBOBOX -> ComboBoxAnswerActivity.newInstance(this, it, schedule?.type, position)
//                }
            }
            initQuestionList()
        }
        dismissLoading()
    }
}