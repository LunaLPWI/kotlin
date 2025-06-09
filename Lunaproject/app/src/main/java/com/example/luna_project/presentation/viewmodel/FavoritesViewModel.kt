// FavoritesViewModel.kt
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FavoritesViewModel : ViewModel() {
    private val _favorites = MutableStateFlow<Set<String>>(emptySet())
    val favorites = _favorites.asStateFlow()

    fun toggleFavorite(id: String) {
        viewModelScope.launch {
            _favorites.value = if (_favorites.value.contains(id)) {
                _favorites.value - id
            } else {
                _favorites.value + id
            }
        }
    }
}