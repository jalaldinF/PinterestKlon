package uz.example.apppinterest.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.imageview.ShapeableImageView
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uz.example.apppinterest.R
import uz.example.apppinterest.adapter.SavedPhotoAdapter
import uz.example.apppinterest.database.SavedDatabase
import uz.example.apppinterest.helper.SpaceItemDecoration
import uz.example.apppinterest.model.userprofile.User
import uz.example.apppinterest.networking.ApiClient
import uz.example.apppinterest.networking.services.ApiService

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private lateinit var apiService: ApiService
    private lateinit var ivProfile: ShapeableImageView
    private lateinit var tvFullName: TextView
    private lateinit var tvUsername: TextView
    private lateinit var tvFollow: TextView

    private lateinit var rvSaved: RecyclerView
    private lateinit var staggeredGridLayoutManager: StaggeredGridLayoutManager
    private lateinit var savedPhotoAdapter: SavedPhotoAdapter

    private lateinit var savedDatabase: SavedDatabase

    private lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        apiService = ApiClient(requireContext()).createServiceWithAuth(ApiService::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews(view)
    }

    private fun initViews(view: View) {
        ivProfile = view.findViewById(R.id.ivProfile)
        getUser()

        tvFullName = view.findViewById(R.id.tvFullName)
        tvUsername = view.findViewById(R.id.tvUsername)
        tvFollow = view.findViewById(R.id.tvFollow)

        rvSaved = view.findViewById(R.id.rvSaved)
        staggeredGridLayoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        rvSaved.layoutManager = staggeredGridLayoutManager
        rvSaved.addItemDecoration(SpaceItemDecoration(20))
        savedPhotoAdapter = SavedPhotoAdapter()

        navController = Navigation.findNavController(view)

        savedDatabase = SavedDatabase.getInstance(requireContext())

        //savedDatabase.savedDao().clearSaved()

        getSaved()

        controlClick()
    }

    private fun controlClick() {
        savedPhotoAdapter.photoClick = {
            navController.navigate(
                R.id.action_profileFragment_to_photoDetailFragment,
                bundleOf(
                    "photoID" to it.savedID,
                    "photoUrl" to it.url,
                    "description" to it.description
                )
            )
        }
    }

    private fun getSaved() {
        savedPhotoAdapter.submitData(savedDatabase.savedDao().getSaved())
        rvSaved.adapter = savedPhotoAdapter
    }

    private fun getUser() {
        apiService.getUser("diyor0123").enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {

                val user = response.body()!!

                if (response.body()!!.profile_image != null) {
                    Picasso.get()
                        .load(user.profile_image.large)
                        .into(ivProfile)

                    tvFullName.text = user.name
                    tvUsername.text = "@${user.username}"
                    tvFollow.text =
                        "${user.followers_count} followers · ${user.following_count} following"
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {

            }

        })
    }
}