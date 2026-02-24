package com.speze88.namenstag.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Contacts
import androidx.compose.material.icons.filled.Today
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.speze88.namenstag.ui.screen.calendar.CalendarScreen
import com.speze88.namenstag.ui.screen.contacts.ContactsScreen
import com.speze88.namenstag.ui.screen.detail.SaintDetailScreen
import com.speze88.namenstag.ui.screen.search.SearchScreen
import com.speze88.namenstag.ui.screen.today.TodayScreen

data class BottomNavItem<T : Any>(
    val label: String,
    val icon: ImageVector,
    val route: T,
)

@Composable
fun NamenstagNavGraph() {
    val navController = rememberNavController()

    val bottomNavItems = listOf(
        BottomNavItem("Heute", Icons.Default.Today, TodayRoute),
        BottomNavItem("Kalender", Icons.Default.CalendarMonth, CalendarRoute),
        BottomNavItem("Kontakte", Icons.Default.Contacts, ContactsRoute),
    )

    Scaffold(
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination

            // Hide bottom bar on detail screen
            val showBottomBar = currentDestination?.hierarchy?.any { dest ->
                bottomNavItems.any { item -> dest.hasRoute(item.route::class) }
            } == true

            if (showBottomBar) {
                NavigationBar {
                    bottomNavItems.forEach { item ->
                        NavigationBarItem(
                            icon = { Icon(item.icon, contentDescription = item.label) },
                            label = { Text(item.label) },
                            selected = currentDestination?.hierarchy?.any {
                                it.hasRoute(item.route::class)
                            } == true,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                        )
                    }
                }
            }
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = TodayRoute,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable<TodayRoute> {
                TodayScreen(
                    onSaintClick = { saintId ->
                        navController.navigate(SaintDetailRoute(saintId))
                    },
                    onSearchClick = {
                        navController.navigate(SearchRoute)
                    },
                )
            }
            composable<CalendarRoute> {
                CalendarScreen(
                    onSaintClick = { saintId ->
                        navController.navigate(SaintDetailRoute(saintId))
                    },
                )
            }
            composable<ContactsRoute> {
                ContactsScreen(
                    onSaintClick = { saintId ->
                        navController.navigate(SaintDetailRoute(saintId))
                    },
                )
            }
            composable<SearchRoute> {
                SearchScreen(
                    onSaintClick = { saintId ->
                        navController.navigate(SaintDetailRoute(saintId))
                    },
                    onBack = { navController.popBackStack() },
                )
            }
            composable<SaintDetailRoute> { backStackEntry ->
                val route = backStackEntry.toRoute<SaintDetailRoute>()
                SaintDetailScreen(
                    saintId = route.saintId,
                    onBack = { navController.popBackStack() },
                )
            }
        }
    }
}
