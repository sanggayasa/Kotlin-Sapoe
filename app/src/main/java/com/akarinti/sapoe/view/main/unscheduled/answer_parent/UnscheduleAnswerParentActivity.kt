package com.akarinti.sapoe.view.main.unscheduled.answer_parent

import android.app.Activity
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.akarinti.sapoe.R
import com.akarinti.sapoe.base.BaseMvpActivity
import com.akarinti.sapoe.model.Unschedule
import com.akarinti.sapoe.model.questionnaire.ParentAcOffline
import com.akarinti.sapoe.model.questionnaire.ParentCleanOffline
import com.akarinti.sapoe.model.questionnaire.ParentNbOffline
import com.akarinti.sapoe.model.questionnaire.ParentOtherOffline
import com.akarinti.sapoe.objects.Params
import com.akarinti.sapoe.view.main.unscheduled.answer_parent.child.UnscheduleAnswerChildActivity
import com.akarinti.sapoe.view.main.unscheduled.answer_parent.question.UAnswerActivity
import com.akarinti.sapoe.view.main.visit.answer_parent.adapter.AnswerAdapter
import com.akarinti.sapoe.view.main.visit.answer_parent.adapter.ChildAcAnswerAdapter
import com.akarinti.sapoe.view.main.visit.answer_parent.adapter.ChildCleanAnswerAdapter
import com.akarinti.sapoe.view.main.visit.answer_parent.adapter.ChildNbAnswerAdapter
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_answer_parent.*
import kotlinx.android.synthetic.main.toolbar_navigation.*
import org.jetbrains.anko.intentFor
import javax.inject.Inject

class UnscheduleAnswerParentActivity : BaseMvpActivity<UnscheduleAnswerParentPresenter>(), UnscheduleAnswerParentContract.View {

    companion object {
        fun newInstance(activity: Activity, unschedule: Unschedule) {
            activity.startActivity(activity.intentFor<UnscheduleAnswerParentActivity>(
                    Params.BUNDLE_UNSCHEDULE to unschedule))
        }
    }

    @Inject
    override lateinit var presenter: UnscheduleAnswerParentPresenter
    private var unschedule: Unschedule? = null
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
            unschedule = it.getParcelable(Params.BUNDLE_UNSCHEDULE)
        }
        unschedule?.let {
            showLoading()
            loadData()
        }
    }

    private fun loadData() {
        showLoading()
        unschedule?.let {
            when (it.type) {
                Params.TYPE_AC -> presenter.getQuestionaireParentAC(it.id ?: "")
                Params.TYPE_NEONBOX -> presenter.getQuestionaireParentNB(it.id ?: "")
                Params.TYPE_CLEAN -> presenter.getQuestionaireParentClean(it.id ?: "")
                Params.TYPE_MCDS -> presenter.getQuestionaireParentMcds(it.id?: "")
                Params.TYPE_QC -> presenter.getQuestionaireParentQc(it.id?: "")
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
            layoutManager = LinearLayoutManager(this@UnscheduleAnswerParentActivity)
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
                layoutManager = LinearLayoutManager(this@UnscheduleAnswerParentActivity)
                adapter = adapterAc
                isNestedScrollingEnabled = false
            }
            adapterAc.setOnItemClickListener { _, _, pos ->
                UnscheduleAnswerChildActivity.newInstance(this, it, unschedule?.type, pos)
            }
            adapterQuestion = AnswerAdapter(it.formList?: listOf())
            adapterQuestion.setOnItemClickListener { _, _, position ->
                UAnswerActivity.newInstance(this, it, unschedule?.type, position)
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
                layoutManager = LinearLayoutManager(this@UnscheduleAnswerParentActivity)
                adapter = adapterNb
                isNestedScrollingEnabled = false
            }
            adapterNb.setOnItemClickListener { _, _, pos ->
                UnscheduleAnswerChildActivity.newInstance(this, it, unschedule?.type, pos)
            }
            adapterQuestion = AnswerAdapter(it.formList?: listOf())
            adapterQuestion.setOnItemClickListener { _, _, position ->
                UAnswerActivity.newInstance(this, it, unschedule?.type, position)
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
                layoutManager = LinearLayoutManager(this@UnscheduleAnswerParentActivity)
                adapter = adapterCl
                isNestedScrollingEnabled = false
            }
            adapterCl.setOnItemClickListener { _, _, pos ->
                UnscheduleAnswerChildActivity.newInstance(this, it, unschedule?.type, pos)
            }
            adapterQuestion = AnswerAdapter(it.formList?: listOf())
            adapterQuestion.setOnItemClickListener { _, _, position ->
                UAnswerActivity.newInstance(this, it, unschedule?.type, position)
            }
            initQuestionList()
        }
        dismissLoading()
    }

    override fun onQuestionaireParentMcds(data: ParentOtherOffline?) {
        data?.let {
            tvPlace.text = it.location?.name?:"-"
            tvAddress.text = it.location?.address ?:"-"
            ivStatus.setImageResource(R.drawable.label_mcds)

            adapterCl = ChildCleanAnswerAdapter(it.itemList?: listOf(), it.location?.name?:"-")
            rvChild.apply {
                layoutManager = LinearLayoutManager(this@UnscheduleAnswerParentActivity)
                adapter = adapterCl
                isNestedScrollingEnabled = false
            }
            adapterCl.setOnItemClickListener { _, _, pos ->
                UnscheduleAnswerChildActivity.newInstance(this, it, unschedule?.type, pos)
            }

            adapterQuestion = AnswerAdapter(it.formList?: listOf())
            adapterQuestion.setOnItemClickListener { _, _, position ->
                UAnswerActivity.newInstance(this, it, unschedule?.type, position)
            }
            initQuestionList()
        }
        dismissLoading()
    }

    override fun onQuestionaireParentQc(data: ParentOtherOffline?) {
        data?.let {
            tvPlace.text = it.location?.name?:"-"
            tvAddress.text = it.location?.address ?:"-"
            ivStatus.setImageResource(R.drawable.label_qc)

            adapterCl = ChildCleanAnswerAdapter(it.itemList?: listOf(), it.location?.name?:"-")
            rvChild.apply {
                layoutManager = LinearLayoutManager(this@UnscheduleAnswerParentActivity)
                adapter = adapterCl
                isNestedScrollingEnabled = false
            }
            adapterCl.setOnItemClickListener { _, _, pos ->
                UnscheduleAnswerChildActivity.newInstance(this, it, unschedule?.type, pos)
            }

            adapterQuestion = AnswerAdapter(it.formList?: listOf())
            adapterQuestion.setOnItemClickListener { _, _, position ->
                UAnswerActivity.newInstance(this, it, unschedule?.type, position)
            }
            initQuestionList()
        }
        dismissLoading()
    }
}