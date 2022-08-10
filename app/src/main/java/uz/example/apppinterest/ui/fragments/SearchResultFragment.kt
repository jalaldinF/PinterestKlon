package uz.example.apppinterest.ui.fragments

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uz.example.apppinterest.R
import uz.example.apppinterest.adapter.SearchPhotoAdapter
import uz.example.apppinterest.helper.EndlessRecyclerViewScrollListener
import uz.example.apppinterest.helper.SpaceItemDecoration
import uz.example.apppinterest.model.search.ResponseSearch
import uz.example.apppinterest.model.search.Result
import uz.example.apppinterest.networking.ApiClient
import uz.example.apppinterest.networking.services.ApiService


class SearchResultFragment : Fragment(R.layout.fragment_search_result) {

    private lateinit var edtSearch: EditText
    private lateinit var tvCancel: TextView
    private lateinit var navController: NavController
    lateinit var list: ArrayList<Result>
    lateinit var rvSearchPhotos: RecyclerView
    var searchPhotoAdapter = SearchPhotoAdapter()

    lateinit var staggeredGridLayoutManager: StaggeredGridLayoutManager

    lateinit var query: String

    private var PAGE = 1
    private var PER_PAGE = 20

    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        apiService = ApiClient(requireContext()).createServiceWithAuth(ApiService::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews(view)
    }

    private fun initViews(view: View) {

        edtSearch = view.findViewById(R.id.edtSearch)

        tvCancel = view.findViewById(R.id.tvCancel)
        navController = Navigation.findNavController(view)
        rvSearchPhotos = view.findViewById(R.id.rvSearchPhotos)


        staggeredGridLayoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        rvSearchPhotos.layoutManager = staggeredGridLayoutManager

        list = ArrayList()
        rvSearchPhotos.addItemDecoration(SpaceItemDecoration(10))

        edtSearch.setOnEditorActionListener { _, actionId, keyEvent ->
            if ((keyEvent != null && (keyEvent.keyCode == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_SEARCH)) {
                searchPhotoAdapter.clearList()

                hideKeyboard(requireActivity(), edtSearch)

                if (edtSearch.text.isNotBlank()) {
                    query = edtSearch.text.toString()
                    getSearchResults()
                }
            }
            false
        }
        rvSearchPhotos.adapter = searchPhotoAdapter

        addLoadingMore()

        controlClick()

        doCancelAction()
    }

    private fun controlClick() {
        searchPhotoAdapter.photoClick = {
            navController.navigate(
                R.id.action_searchResultFragment_to_photoDetailFragment,
                bundleOf(
                    "photoID" to it.id,
                    "photoUrl" to it.urls.regular,
                    "description" to it.description
                )
            )
        }
    }

    override fun onResume() {
        super.onResume()
        edtSearch.post {
            edtSearch.requestFocus()
            val imm =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(edtSearch, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    private fun addLoadingMore() {
        val scrollListener = object : EndlessRecyclerViewScrollListener(
            staggeredGridLayoutManager
        ) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                getSearchResults()
            }
        }
        rvSearchPhotos.addOnScrollListener(scrollListener)
    }

    private fun hideKeyboard(activity: Activity, viewToHide: View) {
        val imm = activity
            .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(viewToHide.windowToken, 0)
    }

    private fun doCancelAction() {
        tvCancel.setOnClickListener {
            navController.navigate(R.id.action_searchResultFragment_to_searchFragment)
        }
    }

    private fun getSearchResults() {
        apiService.searchPhotos(query, PAGE++, PER_PAGE)
            .enqueue(object : Callback<ResponseSearch> {
                override fun onResponse(
                    call: Call<ResponseSearch>,
                    response: Response<ResponseSearch>
                ) {
                    searchPhotoAdapter.submitData(response.body()!!.results)
                }

                override fun onFailure(call: Call<ResponseSearch>, t: Throwable) {
                }
            })
    }
}