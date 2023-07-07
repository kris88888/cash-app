package com.example.cashappstocks.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.cashappstocks.R

@Composable
fun ErrorScreen(modifier: Modifier = Modifier,
                errorMessage: String,
                snackbarHostState: SnackbarHostState,
                onRetryClick: () -> Unit) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = errorMessage,
            style = MaterialTheme.typography.bodyMedium
        )
        Button(onClick = onRetryClick) {
            Text(
                text = stringResource(id = R.string.retry),
                style = MaterialTheme.typography.headlineSmall
            )
        }
    }
    val message = stringResource(id = R.string.error_message)
    LaunchedEffect(Unit) {
        snackbarHostState.showSnackbar(
            message,
            duration = SnackbarDuration.Long
        )
    }
}