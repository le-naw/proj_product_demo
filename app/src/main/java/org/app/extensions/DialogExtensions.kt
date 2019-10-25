package org.app.extensions

import android.content.Context
import android.view.View
import androidx.fragment.app.FragmentManager
import org.app.demo.android.R
import org.app.common.DemoDialog

fun FragmentManager.showDialogError(msg: String) {
    DemoDialog()
        .setMessageText(msg)
        .setRightButton(R.string.act_ok, null, R.color.red)
        .show(this)
}

fun View.showDialogError(msg: String) {
    context.getFragmentManager()?.showDialogError(msg)
}

fun Context.showDialogError(msg: String) {
    getFragmentManager()?.showDialogError(msg)
}

fun FragmentManager.showDialogError(res: Int, msg: String) {
    DemoDialog()
        .setTitle(res)
        .setMessageText(msg)
        .setRightButton(R.string.act_ok, null, R.color.red)
        .show(this)
}

fun View.showDialogError(res: Int, msg: String) {
    context.getFragmentManager()?.showDialogError(res, msg)
}

fun Context.showDialogError(res: Int, msg: String) {
    getFragmentManager()?.showDialogError(res, msg)
}

fun FragmentManager.showDialogNetworkError() {
    DemoDialog()
        .setTitle(R.string.tit_network_error)
        .setMessage(R.string.msg_network_error)
        .setRightButton(R.string.act_ok, null, R.color.red)
        .show(this)
}

fun View.showDialogNetworkError() {
    context.getFragmentManager()?.showDialogNetworkError()
}

fun Context.showDialogNetworkError() {
    getFragmentManager()?.showDialogNetworkError()
}
