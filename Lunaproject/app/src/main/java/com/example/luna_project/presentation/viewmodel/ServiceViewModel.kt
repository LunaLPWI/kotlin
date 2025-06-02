import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.luna_project.data.models.Barber
import java.time.LocalDate

class ServiceScreenViewModel : ViewModel() {

    // Detalhes da reserva
    var selectedDate = mutableStateOf<LocalDate?>(null)
    var selectedTime = mutableStateOf<String?>(null)

    // Lista de barbearias e serviços selecionados
    var selectedBarber = mutableStateOf<Barber?>(null)  // Adicionado para o selectedBarber
    var selectedServices = mutableStateListOf<Triple<Long, String, Double>>()

    // Preço total
    var totalPrice = mutableStateOf(0.0)

    // Função para atualizar os dados da reserva
    fun updateReservationDetails(date: LocalDate?, time: String?, services: List<Triple<Long ,String, Double>>, barber: Barber?) {
        selectedDate.value = date
        selectedTime.value = time
        selectedServices.clear()
        selectedServices.addAll(services)
        selectedBarber.value = barber  // Atualizando o selectedBarber
        totalPrice.value = services.sumOf { it.third }
    }
}