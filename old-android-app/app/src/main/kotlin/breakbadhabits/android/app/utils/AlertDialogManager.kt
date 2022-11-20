package breakbadhabits.android.app.utils

import android.content.Context
import androidx.appcompat.app.AlertDialog

class AlertDialogManager {

    fun showAlert(
        context: Context,
        title: String?,
        message: String,
        positiveButtonTitle: String? = null,
        negativeButtonTitle: String? = null,
        onPositive: () -> Unit = {},
        onNegative: () -> Unit = {},
        onDismiss: () -> Unit = {}
    ) = AlertDialog.Builder(context).apply {
        setTitle(title)
        setMessage(message)

        if (positiveButtonTitle != null) {
            setPositiveButton(positiveButtonTitle) { dialog, _ ->
                onPositive()
                dialog.dismiss()
            }
        }

        if (negativeButtonTitle != null) {
            setNegativeButton(negativeButtonTitle) { dialog, _ ->
                onNegative()
                dialog.dismiss()
            }
        }

        setOnDismissListener {
            onDismiss()
        }
    }.create().show()
}