package com.example.clientmanager.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.clientmanager.ui.screens.*

@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.ClientList.route) {

        composable(Screen.ClientList.route) {
            ClientListScreen(
                onAddClient = { navController.navigate(Screen.AddClient.route) },
                onOpenClient = { id -> navController.navigate(Screen.ClientDetail.createRoute(id)) },
                onOpenDashboard = { navController.navigate(Screen.Dashboard.route) }
            )
        }

        composable(Screen.AddClient.route) {
            AddClientScreen(
                onBack = { navController.popBackStack() },
                onSaved = { id ->
                    navController.popBackStack()
                    navController.navigate(Screen.ClientDetail.createRoute(id))
                }
            )
        }

        composable(
            route = Screen.ClientDetail.route,
            arguments = listOf(navArgument("clientId") { type = NavType.LongType })
        ) { backStackEntry ->
            val clientId = backStackEntry.arguments?.getLong("clientId") ?: return@composable
            ClientDetailScreen(
                clientId = clientId,
                onBack = { navController.popBackStack() },
                onAddVisit = { navController.navigate(Screen.AddVisit.createRoute(clientId)) },
                onExportPdf = { navController.navigate("pdf_export/$clientId") }
            )
        }

        composable(
            route = Screen.AddVisit.route,
            arguments = listOf(navArgument("clientId") { type = NavType.LongType })
        ) { backStackEntry ->
            val clientId = backStackEntry.arguments?.getLong("clientId") ?: return@composable
            AddVisitScreen(
                clientId = clientId,
                onBack = { navController.popBackStack() },
                onSaved = { navController.popBackStack() }
            )
        }

        composable(
            route = "pdf_export/{clientId}",
            arguments = listOf(navArgument("clientId") { type = NavType.LongType })
        ) { backStackEntry ->
            val clientId = backStackEntry.arguments?.getLong("clientId") ?: return@composable
            PdfExportScreen(clientId = clientId, onBack = { navController.popBackStack() })
        }

        composable(Screen.Dashboard.route) {
            DashboardScreen(onBack = { navController.popBackStack() })
        }
    }
}
