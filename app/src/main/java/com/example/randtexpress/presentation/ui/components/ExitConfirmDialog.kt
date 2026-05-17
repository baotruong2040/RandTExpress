package com.example.randtexpress.presentation.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.randtexpress.R

@Composable
fun ExitConfirmDialog(
    onConfirmExit: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = stringResource(R.string.exit_app_title)) },
        text = { Text(text = stringResource(R.string.exit_app_message)) },
        confirmButton = {
            TextButton(onClick = onConfirmExit) {
                Text(text = stringResource(R.string.exit_app_confirm))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(R.string.cancel))
            }
        }
    )
}
