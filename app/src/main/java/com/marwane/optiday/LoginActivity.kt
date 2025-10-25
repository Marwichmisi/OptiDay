package com.marwane.optiday

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

// APPRENTISSAGE : On hérite maintenant de ComponentActivity, la base pour Compose.
class LoginActivity : ComponentActivity() {

    private lateinit var auth: FirebaseAuth

    private val googleSignInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Toast.makeText(this, "Échec de la connexion Google.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()

        // APPRENTISSAGE : setContent est le point d'entrée de Compose.
        // Tout ce qui est à l'intérieur de ce bloc sera dessiné par Compose.
        setContent {
            // On appelle notre fonction Composable qui dessine l'écran.
            LoginScreen(
                onRegisterClick = { email, password ->
                    registerUser(email, password)
                },
                onGoogleClick = {
                    signInWithGoogle()
                }
            )
        }
    }

    // ... (fonctions de logique Firebase et de navigation) ...
}


// APPRENTISSAGE : @Composable est un mot-clé magique. Il transforme une fonction Kotlin
// normale en un "composant visuel" réutilisable que Compose peut dessiner.
@Composable
fun LoginScreen(
    onRegisterClick: (String, String) -> Unit,
    onGoogleClick: () -> Unit
) {
    // APPRENTISSAGE : remember et mutableStateOf sont le cœur de l'état dans Compose.
    // `remember` dit à Compose de garder en mémoire la valeur, même si l'écran est redessiné.
    // `mutableStateOf` crée une variable dont le changement va automatiquement
    // déclencher la mise à jour de l'interface.
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // APPRENTISSAGE : Column est un Composable qui empile ses enfants verticalement.
    Column(
        modifier = Modifier
            .fillMaxSize() // Prend toute la taille de l'écran
            .padding(24.dp),
        verticalArrangement = Arrangement.Center, // Centre les éléments verticalement
        horizontalAlignment = Alignment.CenterHorizontally // Centre les éléments horizontalement
    ) {
        // APPRENTISSAGE : TextField est l'équivalent de EditText.
        // `value` est ce qui est affiché, `onValueChange` est ce qui se passe quand l'utilisateur tape.
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Adresse e-mail") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp)) // Ajoute un espace vertical

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Mot de passe") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        // APPRENTISSAGE : Button est un composant de bouton.
        // Le code dans le `onClick` est exécuté quand on clique.
        Button(
            onClick = { /* Logique de connexion à ajouter */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Se Connecter")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { onRegisterClick(email, password) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Créer un Compte")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { onGoogleClick() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Se connecter avec Google")
        }
    }
}