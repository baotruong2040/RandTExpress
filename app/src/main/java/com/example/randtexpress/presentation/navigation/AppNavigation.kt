package com.example.randtexpress.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.randtexpress.presentation.ui.screens.CategoryDetailScreen
import com.example.randtexpress.presentation.ui.screens.HomeScreen
import com.example.randtexpress.presentation.ui.screens.auth.LoginScreen
import com.example.randtexpress.presentation.ui.screens.auth.RegisterScreen
import com.example.randtexpress.presentation.viewmodel.SessionViewModel

private object AuthRoutes {
    const val Login = "login"
    const val Register = "register"
}

private object HomeRoutes {
    const val Home = "home"
    const val CategoryDetail = "category/{categoryName}"
}

@Composable
fun AppNavigation(
    sessionViewModel: SessionViewModel = hiltViewModel()
) {
    val sessionState by sessionViewModel.sessionData.collectAsStateWithLifecycle()

    if (sessionState.token.isNullOrBlank()) {
        AuthNavigation()
    } else {
        HomeNavigation(
            sessionState = sessionState,
            onLogout = { sessionViewModel.logout() }
        )
    }
}

@Composable
private fun AuthNavigation(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = AuthRoutes.Login
    ) {
        composable(AuthRoutes.Login) {
            LoginScreen(
                onRegisterClick = {
                    navController.navigate(AuthRoutes.Register)
                }
            )
        }
        composable(AuthRoutes.Register) {
            RegisterScreen(
                onLoginClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}

@Composable
private fun HomeNavigation(
    sessionState: com.example.randtexpress.data.preferences.SessionData,
    onLogout: () -> Unit,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = HomeRoutes.Home
    ) {
        composable(HomeRoutes.Home) {
            HomeScreen(
                sessionState = sessionState,
                onLogout = onLogout,
                onCategoryClick = { categoryName ->
                    navController.navigate("category/$categoryName")
                }
            )
        }
        composable(HomeRoutes.CategoryDetail) { backStackEntry ->
            val categoryName = backStackEntry.arguments?.getString("categoryName") ?: ""
            CategoryDetailScreen(
                categoryName = categoryName,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
