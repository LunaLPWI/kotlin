import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.luna_project.data.network.RetrofitClient
import com.example.luna_project.data.models.Barbershop
import com.example.luna_project.data.models.User
import com.example.luna_project.data.session.UserSession
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BarbershopViewModel : ViewModel() {

    private val _barbershops = MutableStateFlow<List<Barbershop>>(emptyList())
    val barbershops: StateFlow<List<Barbershop>> = _barbershops
    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user


    // Função para buscar as barbearias
    fun fetchBarbershops(lat: Double, logn: Double) {
        Log.d("BarbershopFetch", "Iniciando a busca das barbearias com lat: $lat, lng: $logn")
        viewModelScope.launch {
            try {
                val userToken = UserSession.token
                val response = RetrofitClient.apiService.getBarbershops(userToken, lat, logn)
                Log.d("BarbershopFetch", userToken)
                if (response.isSuccessful) {
                    Log.d("BarbershopFetch", "Resposta da API: ${response.body()}")
                    response.body()?.let { establishments ->
                        _barbershops.value = establishments.map { establishment ->
                            Barbershop(
                                id = establishment.id,
                                name = establishment.name,
                                addressDTO = establishment.addressDTO,
                                planDTO = establishment.planDTO,
                                cnpj = establishment.cnpj,
                                openHour = establishment.openHour,
                                closeHour = establishment.closeHour,
                                lat = establishment.lat,
                                logn = establishment.lng,
                                avarageRating = establishment.avarageRating
                            )
                        }
                    }
                } else {
                    Log.e("BarbershopFetch", "Erro na resposta: ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("BarbershopFetch", "Erro: ${e.localizedMessage}")
                e.printStackTrace()
            }
        }
    }

    private val _barbershopsSearch = MutableStateFlow<List<Barbershop>>(emptyList())
    val barbershopsSearch: StateFlow<List<Barbershop>> = _barbershopsSearch

    fun fetchSearchBaberShops(query: String) {
        viewModelScope.launch {
            try {
                val userToken = UserSession.token
                val response = RetrofitClient.apiService.searchEstablishments(userToken, query)

                if (response.isSuccessful) {
                    response.body()?.let { establishments ->
                        _barbershopsSearch.value = establishments.map { establishment ->
                            Barbershop(
                                id = establishment.id,
                                name = establishment.name,
                                addressDTO = establishment.addressDTO,
                                planDTO = establishment.planDTO,
                                cnpj = establishment.cnpj,
                                openHour = establishment.openHour,
                                closeHour = establishment.closeHour,
                                lat = establishment.lat,
                                logn = establishment.lng,
                                avarageRating = establishment.avarageRating
                            )
                        }
                    }
                } else {
                    Log.e("BarbershopFetch", "Erro na resposta: ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("BarbershopFetch", "Erro: ${e.localizedMessage}")
            }
        }
    }


}
