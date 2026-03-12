import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.credentials.GetCredentialRequest
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gurry.database.UserEntity
import com.example.gurry.database.UserEntityDao
import com.google.android.gms.common.api.ApiException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val user: com.google.firebase.auth.FirebaseUser?) : AuthState()
    data class Error(val message: String) : AuthState()
}

data class RegistrationData(
    val username: String = "",
    val profilePicUrl: String = "img_1" // Imagen por defecto
)

sealed interface RegisterAction {
    data class NameChanged(val name: String) : RegisterAction
    data class PictureSelected(val url: String) : RegisterAction
    object SubmitRegistration : RegisterAction
}

class AuthViewModel(private val userDao: UserEntityDao) : ViewModel() {
    // Usamos FirebaseAuth.getInstance() para mayor compatibilidad con el BoM

    private val WEB_CLIENT_ID = "648612551761-t25nddnt424ool2cj3bkbalmo4p6atfl.apps.googleusercontent.com"
    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val db = FirebaseFirestore.getInstance()
    private val _authState = mutableStateOf<AuthState>(AuthState.Idle)
    val authState: State<AuthState> = _authState
    private val _registrationState = MutableStateFlow(RegistrationData())
    val registrationState = _registrationState.asStateFlow()
    private val _isRegitered = mutableStateOf<Boolean?>(null)
    val isRegistered: State<Boolean?> = _isRegitered
    private val _currentUserData = MutableStateFlow<UserEntity?>(null)

    val currentUserData = _currentUserData.asStateFlow()
    private var userJob: Job? = null
    init {
        checkCurrentUser()
    }

    fun prepareGetCredentialRequest(): GetCredentialRequest {
        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(true)
            .setServerClientId(WEB_CLIENT_ID)
            .setAutoSelectEnabled(true) // Mejora la UX para usuarios que ya entraron
            .build()

        return GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()
    }

    fun signInWithGoogleId(idToken: String) {
        _authState.value = AuthState.Loading
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnSuccessListener { authResult ->
                checkUserRegistration(authResult.user?.uid ?: "")
            }
            .addOnFailureListener { e ->
                _authState.value = AuthState.Error(e.localizedMessage ?: "Error Firebase")
            }
    }

    // El ViewModel procesa el Token (Aquí ya no hay UI involucrada)
    fun onTokenReceived(idToken: String) {
        _authState.value = AuthState.Loading
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnSuccessListener { authResult ->
                val uid = authResult.user?.uid ?: ""
                listenToUser(uid)
                checkUserRegistration(authResult.user?.uid ?: "")
            }
            .addOnFailureListener { e ->
                _authState.value = AuthState.Error(e.localizedMessage ?: "Error Firebase")
            }
    }
    private fun listenToUser(uid: String) {
        userJob?.cancel()
        userJob = viewModelScope.launch {
            userDao.getUserFlow(uid).collect { userEntity ->
                _currentUserData.value = userEntity
            }
        }
    }
    private fun checkCurrentUser() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            // 1. Ponemos estado de carga para que no se vea el botón de Google
            _authState.value = AuthState.Loading
            listenToUser(currentUser.uid)
            // 2. IMPORTANTE: Lanzamos la verificación de base de datos inmediatamente
            checkUserRegistration(currentUser.uid)
        }
    }

    private fun signInWithFirebase(idToken: String) {
        _authState.value = AuthState.Loading
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                checkUserRegistration(auth.currentUser?.uid ?: "")
            }else {
                    _authState.value = AuthState.Error(task.exception?.message ?: "Error Firebase")
                }
            }
    }

    private fun checkUserRegistration(uid: String) {
        Log.d("GurryDebug", "Intentando leer Firestore para UID: $uid") // LOG NUEVO

        db.collection("users").document(uid).get()
            .addOnSuccessListener { document ->
                Log.d("GurryDebug", "Lectura exitosa. Existe: ${document.exists()}") // LOG NUEVO
                if (document.exists()) {
                    val user = document.toObject(UserEntity::class.java)
                    viewModelScope.launch {
                        user?.let {
                            userDao.insertOrUpdateUser(it)
                            _isRegitered.value = true
                        }
                    }
                } else {
                    Log.d("GurryDebug", "Usuario no existe en DB, enviando a registro") // LOG NUEVO
                    _isRegitered.value = false
                }
                // Importante: Quitamos el loading pase lo que pase
                _authState.value = AuthState.Success(auth.currentUser)
            }
            .addOnFailureListener { e ->
                Log.e("GurryDebug", "ERROR CRÍTICO FIRESTORE: ${e.message}", e) // LOG NUEVO
                _authState.value = AuthState.Error("Error DB: ${e.message}")
            }
    }

    fun completeRegistration() {
        val currentUser = auth.currentUser ?: return
        val registrationData = registrationState.value

        _authState.value = AuthState.Loading

        // 1. Creamos el objeto UserEntity con los datos del formulario
        val newUser = UserEntity(
            uid = currentUser.uid,
            username = registrationData.username,
            email = currentUser.email ?: "",
            balance = 100f, // Regalo de bienvenida
            profilePic = registrationData.profilePicUrl,
            isRegistered = true
        )

        // 2. Guardamos en Firebase Firestore primero
        db.collection("users").document(currentUser.uid).set(newUser)
            .addOnSuccessListener {
                // 3. Si Firebase tiene éxito, guardamos en la caché SQLite
                viewModelScope.launch {
                    userDao.insertOrUpdateUser(newUser) // Usamos la instancia inyectada
                    listenToUser(newUser.uid)
                    _isRegitered.value = true // Cambiamos el estado para navegar
                    _authState.value = AuthState.Success(currentUser)
                }
            }
            .addOnFailureListener { e ->
                _authState.value = AuthState.Error("Fallo al registrar: ${e.message}")
            }
    }
    sealed interface RegisterAction {
        data class NameChanged(val name: String) : RegisterAction
        data class PictureSelected(val url: String) : RegisterAction
        object SubmitRegistration : RegisterAction
    }

    fun onRegisterAction(action: RegisterAction) {
        when (action) {
            is RegisterAction.NameChanged ->
                _registrationState.update { it.copy(username = action.name) }
            is RegisterAction.PictureSelected ->
                _registrationState.update { it.copy(profilePicUrl = action.url) }
            RegisterAction.SubmitRegistration ->
                completeRegistration()
        }
    }

    fun onCredentialResult(idToken: String) {
        signInWithGoogleId(idToken)
    }

    fun onCredentialError(error: String) {
        Log.e("GurryAuth", error)
        _authState.value = AuthState.Error(error)
    }

    fun signOut() {
        auth.signOut()
        userJob?.cancel()
        _currentUserData.value = null
        _authState.value = AuthState.Idle
    }


}
