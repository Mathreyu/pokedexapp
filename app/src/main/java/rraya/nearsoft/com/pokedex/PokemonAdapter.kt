package rraya.nearsoft.com.pokedex

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import rraya.nearsoft.com.pokedex.models.Pokemon


class PokemonAdapter : RecyclerView.Adapter<PokemonAdapter.PokemonHolder>() {

    var pokemons: MutableList<Pokemon> = mutableListOf()

    override fun onBindViewHolder(holder: PokemonHolder, position: Int) {
        val pokemon = pokemons.get(position)
        holder.pokemonTextView.text = pokemon.name

        val pokemonNumber = extractNumber(pokemon.url)

       Glide.with(holder.itemView.context)
               .asBitmap()
               .load("http://pokeapi.co/media/sprites/pokemon/$pokemonNumber.png")
               .into(holder.pokemonImageView)

    }

    private fun extractNumber(url: String): Int {
        val splittedUrl : List<String>  = url.split("/")
        return splittedUrl.get(splittedUrl.size - 2).toInt()
    }

    override fun getItemCount(): Int {
        return pokemons.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): PokemonHolder {
        val inflater = LayoutInflater.from(parent?.context)
        val view = inflater.inflate(R.layout.pokemon_item, parent, false)
        return PokemonHolder(view)
    }

    inner class PokemonHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val pokemonImageView: ImageView by lazy { itemView.findViewById<ImageView>(R.id.fotoImageView) }
        val pokemonTextView: TextView by lazy { itemView.findViewById<TextView>(R.id.nombreTextView) }

    }

    fun addPokemons(pokemonResponse: ArrayList<Pokemon>) {
        pokemons.addAll(pokemonResponse)
        notifyDataSetChanged()
    }
}