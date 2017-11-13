package rraya.nearsoft.com.pokedex

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import rraya.nearsoft.com.pokedex.services.PokeAPI

class PokeDexMain : AppCompatActivity() {


    private val retrofit by lazy {
        Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://pokeapi.co/api/v2/")
                .build()
    }

    private val pokemonRV by lazy { findViewById<RecyclerView>(R.id.recyclerView) }
    private var pokemonListAdapter = PokemonAdapter()

    private var offset: Int = 0
    private var loading = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_poke_dex_main)
        pokemonRV.adapter = pokemonListAdapter
        val layoutManager = GridLayoutManager(this, 3)
        pokemonRV.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            private var firstVisibleItem = 0
            private var visibleItemCount = 0
            private var totalItemCount = 0
            private var pastVisibleItems = 0

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (dy > 0) {
                    visibleItemCount = recyclerView.childCount
                    totalItemCount = layoutManager.itemCount
                    firstVisibleItem = layoutManager.findFirstVisibleItemPosition()
                    pastVisibleItems = layoutManager.findFirstVisibleItemPosition()

                    if (loading) {
                        if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                            loading = false
                            offset += 20
                            obtainPokemon(offset)
                        }
                    }
                }
            }
        })

        pokemonRV.layoutManager = layoutManager

        loading = true
        obtainPokemon(offset)
    }

    private fun obtainPokemon(offset: Int) {
        val service = retrofit.create(PokeAPI::class.java)

        service.getPokemnonList(20, offset)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            loading = true
                            val pokemonResponse = result.results
                            pokemonListAdapter.addPokemons(pokemonResponse)
                        },
                        { error -> loading = true }
                )
    }
}
