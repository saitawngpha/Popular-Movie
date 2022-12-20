package com.saitawngpha.popularmovie.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.saitawngpha.popularmovie.R
import com.saitawngpha.popularmovie.adapter.MoviesAdapter
import com.saitawngpha.popularmovie.databinding.FragmentMoviesBinding
import com.saitawngpha.popularmovie.repository.ApiRepository
import com.saitawngpha.popularmovie.response.MoviesListResponse
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject
import javax.security.auth.callback.Callback

@AndroidEntryPoint
class MoviesFragment : Fragment() {

    private lateinit var binding: FragmentMoviesBinding

    @Inject
    lateinit var apiRepository: ApiRepository

    @Inject
    lateinit var moviesAdapter: MoviesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMoviesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            prgBarMovies.visibility = View.VISIBLE
            apiRepository.getPopularMoviesList(1).enqueue(object : retrofit2.Callback<MoviesListResponse> {
                override fun onResponse(call: Call<MoviesListResponse>, response: Response<MoviesListResponse>) {
                    prgBarMovies.visibility = View.GONE
                    when(response.code()){
                        200 -> {
                            response.body().let {
                                if(it?.results!!.isNotEmpty()){
                                    moviesAdapter.differ.submitList(it.results)
                                }
                                rlMovies.apply {
                                    layoutManager = LinearLayoutManager(requireContext())
                                    adapter = moviesAdapter
                                }
                                //set data to pass to details fragment
                                moviesAdapter.serOnItemClickListener {
                                    val directions = MoviesFragmentDirections.actionMoviesFragmentToMoviesDetailsFragment(it.id)
                                    findNavController().navigate(directions)
                                }
                            }
                        }
                        401 -> {
                            Toast.makeText(requireContext(), "Invalid API key: You must be granted a valid key.", Toast.LENGTH_SHORT).show()
                        }
                        404 -> {
                            Toast.makeText(requireContext(), "The resource you requested could not be found.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                override fun onFailure(call: Call<MoviesListResponse>, t: Throwable) {
                    prgBarMovies.visibility = View.VISIBLE
                    Toast.makeText(requireContext(), "can't connect to server!", Toast.LENGTH_SHORT).show()
                }

            })
        }

    }

}