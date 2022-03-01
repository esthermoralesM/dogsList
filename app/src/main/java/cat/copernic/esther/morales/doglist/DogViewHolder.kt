package cat.copernic.esther.morales.doglist

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import cat.copernic.esther.morales.doglist.databinding.ItemDogBinding
import com.squareup.picasso.Picasso

class DogViewHolder(view: View):RecyclerView.ViewHolder(view) {

    private val binding=ItemDogBinding.bind(view)

    fun bind(image:String){
      Picasso.get().load(image).into(binding.ivDog)  //convertimos url en imagen con la librería Picasso, y la cargamos en ivDog


    }
}