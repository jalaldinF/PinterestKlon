package uz.example.apppinterest.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.navigation.NavController
import androidx.navigation.Navigation
import uz.example.apppinterest.R

class SearchFragment : Fragment(R.layout.fragment_search), View.OnClickListener {

    private lateinit var edtSearch: EditText
    private lateinit var navController: NavController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews(view)
    }

    private fun initViews(view: View) {
        edtSearch = view.findViewById(R.id.edtSearch)
        navController = Navigation.findNavController(view)

        edtSearch.setOnClickListener(this)
    }


    override fun onClick(v: View?) {
        if (v!!.id == R.id.edtSearch) {
            openSearchResultFragment()
        }
    }

    private fun openSearchResultFragment() {
        navController.navigate(R.id.action_searchFragment_to_searchResultFragment)
    }


}