package rraya.nearsoft.com.pokedex.services

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query
import rraya.nearsoft.com.pokedex.models.PokemonResponse

interface PokeAPI {

    @GET("poke")
    fun getPokemnonList(@Query("limit") limit: Int, @Query("offset") offset: Int ): Observable<PokemonResponse>

}