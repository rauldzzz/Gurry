package com.example.gurry.ui.components

import AuthViewModel
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.gurry.R
import com.example.gurry.ui.screens.AccountScreen
import com.example.gurry.ui.screens.HomeScreen
import com.example.gurry.ui.screens.MarketScreen
import com.example.gurry.ui.screens.StableScreen
import com.example.gurry.ui.theme.Account
import com.example.gurry.ui.theme.HomePage
import com.example.gurry.ui.theme.Market
import com.example.gurry.ui.theme.Stable
import com.example.gurry.ui.viewmodels.MarketViewModel
import com.example.gurry.ui.viewmodels.StableViewModel

@Composable
fun NavBar(
    authViewModel: AuthViewModel,
    marketViewModel: MarketViewModel,
    stableViewModel: StableViewModel
) {
    val navController = rememberNavController()
    var currentRoute by remember { mutableStateOf(value = HomePage.route) }
    val horseIcon = ImageVector.vectorResource(id = R.drawable.horse)
    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.onPrimary,
                contentColor = MaterialTheme.colorScheme.onSurface,  // Color contenido por defecto
                tonalElevation = 8.dp // Opcional: Sombra/Elevación
            ){
                val itemColors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.secondary, // Icono seleccionado
                    selectedTextColor = MaterialTheme.colorScheme.secondary, // Texto seleccionado
                    indicatorColor = Color.Transparent, // La "píldora" de fondo al seleccionar
                    unselectedIconColor = MaterialTheme.colorScheme.primary, // Icono no seleccionado
                    unselectedTextColor = MaterialTheme.colorScheme.primary  // Texto no seleccionado
                )
                NavigationBarItem(
                    selected = currentRoute == HomePage.route,
                    onClick = {
                        currentRoute = HomePage.route
                        navController.navigate(HomePage.route)
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Home"
                        )
                    },
                    label = { Text("Home")},
                    colors = itemColors
                )
                NavigationBarItem(
                    selected = currentRoute == Market.route,
                    onClick = {
                        currentRoute = Market.route
                        navController.navigate(Market.route)
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = "Market"
                        )
                    },
                    label = {Text("Market")},
                    colors = itemColors
                )
                NavigationBarItem(
                    selected = currentRoute == Stable.route,
                    onClick = {
                        currentRoute = Stable.route
                        navController.navigate(Stable.route)
                    },

                    icon = { Icon(imageVector = horseIcon, contentDescription = null) },
                    label = {Text("Stable")},
                    colors = itemColors
                )
                NavigationBarItem(
                    selected = currentRoute == Account.route,
                    onClick = {
                        currentRoute = Account.route
                        navController.navigate(Account.route)
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Account"
                        )
                    },
                    label = {Text("Account")},
                    colors = itemColors
                )
            }
        },
        modifier = Modifier.fillMaxSize()
    )
    { innerPadding ->
        NavigationManager(innerPadding, navController, authViewModel, marketViewModel, stableViewModel)
    }
}

@Composable
private fun NavigationManager(
    innerPadding: PaddingValues,
    navController: NavHostController,
    authViewModel: AuthViewModel,
    marketViewModel: MarketViewModel,
    stableViewModel: StableViewModel
)
{
    NavHost(
        navController = navController,
        startDestination = HomePage.route,
        modifier = Modifier.padding(paddingValues = innerPadding)
    ){
        composable(HomePage.route){
            HomeScreen(authViewModel)
        }
        composable(Market.route){
            MarketScreen(marketViewModel, authViewModel)
        }
        composable(Stable.route){
            StableScreen(stableViewModel)
        }
        composable(Account.route){
            AccountScreen(authViewModel)
        }
    }
}

/*@Preview
@Composable
fun NavBarPreview(){
    GurryTheme {
        NavBar(authViewModel)
    }
}
*/