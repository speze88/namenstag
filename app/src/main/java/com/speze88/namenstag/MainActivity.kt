package com.speze88.namenstag

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.speze88.namenstag.ui.navigation.NamenstagNavGraph
import com.speze88.namenstag.ui.theme.NamenstagTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NamenstagTheme {
                NamenstagNavGraph()
            }
        }
    }
}
