package cat.copernic.esther.morales.doglist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager

import android.widget.Toast

import androidx.appcompat.widget.SearchView


import androidx.recyclerview.widget.LinearLayoutManager
import cat.copernic.esther.morales.doglist.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {

    private  lateinit var binding: ActivityMainBinding
    private  lateinit var adapter: DogAdapter
    private val dogImages = mutableListOf<String>()//listado de imágenes


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.svDogs.setOnQueryTextListener(this)//le implementamos el método setOnQueryTextListener
        initRecyclerView()
    }

  private fun initRecyclerView(){
        adapter= DogAdapter(dogImages)
        binding.rvDogs.layoutManager=LinearLayoutManager(this)
        binding.rvDogs.adapter=adapter
  }

    //método para retrofit. Instancia de retrofit, tendrá la url original y el conversor de JSON
    private fun getRetrofit():Retrofit{

        return Retrofit.Builder()
            .baseUrl("https://dog.ceo/api/breed/")
            .addConverterFactory(GsonConverterFactory.create())//librería de GSON que importamos al principio
            .build()
    }

    private fun searchByName(query:String){
                        //hacer corrutina para que cuando el usuario introduzca nombre vaya a buscar a internet
        CoroutineScope(Dispatchers.IO).launch {
            //se va a hacer en un hilo secundario
            val call = getRetrofit().create(APIService::class.java).getDogsByBreeds("$query/images")
            val puppies = call.body()
            runOnUiThread() { //para volver al hilo principal, ya que la parte visual se hace en el hilo principal
            if (call.isSuccessful) {
                //si funciona muestra recyclerview
                val images=puppies?.images?: emptyList() //si es null tenemos una lista vacía
                dogImages.clear()//borra todo lo que tengas
                dogImages.addAll(images)
                adapter.notifyDataSetChanged()
            } else {
                //muestra error
                showError()
            }
            hideKeyBoard()
        }
        }
    }

    private fun hideKeyBoard() {//esconder el teclado
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.viewRoot.windowToken, 0)
    }

    private fun showError() {
        Toast.makeText(this, "Ha ocurrido un error", Toast.LENGTH_LONG).show()
    }

    override fun onQueryTextSubmit(query: String?): Boolean {//cuando le damos a buscar
        if(!query.isNullOrEmpty()){//si el texto introducido por el usuario no es null ni vacío
            searchByName(query.toLowerCase())
        }
        return true

    }

    override fun onQueryTextChange(newText: String?): Boolean {//este método no es necesario pero nos obliga a ponerlo al implementar
                                                                //SearchView.OnQueryTextListener
        return true
    }
}




