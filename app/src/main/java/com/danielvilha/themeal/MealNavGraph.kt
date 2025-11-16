package com.danielvilha.themeal

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.danielvilha.themeal.ui.route.CategoryListRoute
import com.danielvilha.themeal.ui.route.CategoryRoute
import com.danielvilha.themeal.ui.route.MealDetailsRoute

object MealRoutes {
    const val Categories = "categories"
    const val Meals = "meals"
    const val MealDetails = "mealDetails"
}

@Composable
fun MealNavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = MealRoutes.Categories
    ) {

        composable(MealRoutes.Categories) {
            CategoryListRoute(
                onCategoryClick = { categoryName ->
                    navController.navigate("${MealRoutes.Meals}/$categoryName")
                }
            )
        }

        composable(
            route = "${MealRoutes.Meals}/{category}",
            arguments = listOf(
                navArgument("category") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val category = backStackEntry.arguments?.getString("category") ?: return@composable

            CategoryRoute(
                category = category,
                onBackClick = { navController.popBackStack() },
                onMealClick = { mealId ->
                    navController.navigate("${MealRoutes.MealDetails}/$mealId")
                }
            )
        }

        composable(
            route = "${MealRoutes.MealDetails}/{mealId}",
            arguments = listOf(
                navArgument("mealId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val mealId = backStackEntry.arguments?.getString("mealId") ?: return@composable

            MealDetailsRoute(
                mealId = mealId,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}