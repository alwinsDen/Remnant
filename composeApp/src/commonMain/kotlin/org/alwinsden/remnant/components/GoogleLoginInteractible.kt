package org.alwinsden.remnant.components

import androidx.compose.runtime.Composable

@Composable
expect fun GoogleLoginInteractible(updateToken: (String) -> Unit): Unit
