package com.example.randtexpress.presentation.navigation

import android.app.Activity
import android.net.Uri
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.randtexpress.presentation.ui.screens.CartScreen
import com.example.randtexpress.presentation.ui.screens.CategoryDetailScreen
import com.example.randtexpress.presentation.ui.screens.CheckoutScreen
import com.example.randtexpress.presentation.ui.screens.OrderDetailScreen
import com.example.randtexpress.presentation.ui.screens.ProductDetailScreen
import com.example.randtexpress.presentation.ui.screens.HomeScreen
import com.example.randtexpress.presentation.ui.screens.SearchScreen
import com.example.randtexpress.presentation.ui.screens.auth.LoginScreen
import com.example.randtexpress.presentation.ui.screens.auth.RegisterScreen
import com.example.randtexpress.presentation.viewmodel.SessionViewModel

private object AuthRoutes {
    const val Login = "login"
    const val Register = "register"
}

private object HomeRoutes {
    const val Home = "home"
    const val Cart = "cart"
    const val Checkout = "checkout"
    const val OrderDetail = "order/{orderId}"
    const val CategoryDetail = "category/{categoryName}"
    const val ProductDetail = "product/{productId}"
    const val Search = "search"
}

private const val SlideAnimationDurationMillis = 250

private fun slideInFromRight() = slideInHorizontally(
    initialOffsetX = { fullWidth -> fullWidth },
    animationSpec = tween(SlideAnimationDurationMillis)
)

private fun slideOutToLeft() = slideOutHorizontally(
    targetOffsetX = { fullWidth -> -fullWidth },
    animationSpec = tween(SlideAnimationDurationMillis)
)

private fun slideInFromLeft() = slideInHorizontally(
    initialOffsetX = { fullWidth -> -fullWidth },
    animationSpec = tween(SlideAnimationDurationMillis)
)

private fun slideOutToRight() = slideOutHorizontally(
    targetOffsetX = { fullWidth -> fullWidth },
    animationSpec = tween(SlideAnimationDurationMillis)
)

@Composable
fun AppNavigation(
    sessionViewModel: SessionViewModel = hiltViewModel()
) {
    val sessionState by sessionViewModel.sessionData.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val onExitApp: () -> Unit = {
        (context as? Activity)?.finish()
    }

    if (sessionState.token.isNullOrBlank()) {
        AuthNavigation(onExitApp = onExitApp)
    } else {
        HomeNavigation(
            sessionState = sessionState,
            onLogout = { sessionViewModel.logout() },
            onExitApp = onExitApp
        )
    }
}

@Composable
private fun AuthNavigation(
    onExitApp: () -> Unit,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = AuthRoutes.Login
    ) {
        composable(
            route = AuthRoutes.Login,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None }
        ) {
            LoginScreen(
                onExitApp = onExitApp,
                onRegisterClick = {
                    navController.navigate(AuthRoutes.Register)
                }
            )
        }
        composable(
            route = AuthRoutes.Register,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None }
        ) {
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
    onExitApp: () -> Unit,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = HomeRoutes.Home
    ) {
        composable(
            route = HomeRoutes.Home,
            enterTransition = { slideInFromRight() },
            exitTransition = { slideOutToLeft() },
            popEnterTransition = { slideInFromLeft() },
            popExitTransition = { slideOutToRight() }
        ) {
            HomeScreen(
                sessionState = sessionState,
                onLogout = onLogout,
                onExitApp = onExitApp,
                onProductClick = { productId ->
                    navController.navigate("product/$productId")
                },
                onCartClick = {
                    navController.navigate(HomeRoutes.Cart)
                },
                onCategoryClick = { categoryName ->
                    navController.navigate("category/${Uri.encode(categoryName)}")
                },
                onSearchClick = {
                    navController.navigate(HomeRoutes.Search)
                }
            )
        }
        composable(
            route = HomeRoutes.Cart,
            enterTransition = { slideInFromRight() },
            exitTransition = { slideOutToLeft() },
            popEnterTransition = { slideInFromLeft() },
            popExitTransition = { slideOutToRight() }
        ) {
            CartScreen(
                onBackClick = { navController.popBackStack() },
                onOrderNowClick = {
                    navController.navigate(HomeRoutes.Checkout)
                }
            )
        }
        composable(
            route = HomeRoutes.Checkout,
            enterTransition = { slideInFromRight() },
            exitTransition = { slideOutToLeft() },
            popEnterTransition = { slideInFromLeft() },
            popExitTransition = { slideOutToRight() }
        ) {
            CheckoutScreen(
                onBackClick = { navController.popBackStack() },
                onOrderCreated = { orderId ->
                    navController.navigate("order/$orderId") {
                        popUpTo(HomeRoutes.Cart) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }
        composable(
            route = HomeRoutes.OrderDetail,
            arguments = listOf(
                navArgument("orderId") { type = NavType.IntType }
            ),
            enterTransition = { slideInFromRight() },
            exitTransition = { slideOutToLeft() },
            popEnterTransition = { slideInFromLeft() },
            popExitTransition = { slideOutToRight() }
        ) { backStackEntry ->
            val orderId = backStackEntry.arguments?.getInt("orderId") ?: 0
            OrderDetailScreen(
                orderId = orderId,
                onBackClick = { navController.popBackStack() }
            )
        }
        composable(
            route = HomeRoutes.CategoryDetail,
            enterTransition = { slideInFromRight() },
            exitTransition = { slideOutToLeft() },
            popEnterTransition = { slideInFromLeft() },
            popExitTransition = { slideOutToRight() }
        ) { backStackEntry ->
            val categoryName = Uri.decode(backStackEntry.arguments?.getString("categoryName") ?: "")
            CategoryDetailScreen(
                categoryName = categoryName,
                onBackClick = { navController.popBackStack() },
                onProductClick = { productId ->
                    navController.navigate("product/$productId")
                }
            )
        }
        composable(
            route = HomeRoutes.Search,
            enterTransition = { slideInFromRight() },
            exitTransition = { slideOutToLeft() },
            popEnterTransition = { slideInFromLeft() },
            popExitTransition = { slideOutToRight() }
        ) {
            SearchScreen(
                onBackClick = { navController.popBackStack() },
                onProductClick = { productId ->
                    navController.navigate("product/$productId")
                }
            )
        }
        composable(
            route = HomeRoutes.ProductDetail,
            arguments = listOf(
                navArgument("productId") { type = NavType.IntType }
            ),
            enterTransition = { slideInFromRight() },
            exitTransition = { slideOutToLeft() },
            popEnterTransition = { slideInFromLeft() },
            popExitTransition = { slideOutToRight() }
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getInt("productId") ?: 0
            ProductDetailScreen(
                productId = productId,
                onBackClick = { navController.popBackStack() },
                onCartClick = {
                    navController.navigate(HomeRoutes.Cart)
                }
            )
        }
    }
}
