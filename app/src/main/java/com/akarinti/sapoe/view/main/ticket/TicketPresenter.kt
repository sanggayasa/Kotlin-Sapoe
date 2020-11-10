package com.akarinti.sapoe.view.main.ticket

import com.akarinti.sapoe.base.BasePresenter
import com.akarinti.sapoe.view.main.ticket.TicketContract
import javax.inject.Inject

class TicketPresenter @Inject constructor() : BasePresenter<TicketContract.View>(), TicketContract.Presenter