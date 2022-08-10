package uz.example.apppinterest.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import uz.example.apppinterest.R
import uz.example.apppinterest.adapter.ChatVPAdapter

class ChatFragment : Fragment(R.layout.fragment_chat) {

    private lateinit var tabLayout: TabLayout
    private lateinit var vpChat: ViewPager2
    private lateinit var chatVPAdapter: ChatVPAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews(view)
    }

    private fun initViews(view: View) {
        tabLayout = view.findViewById(R.id.tabLayout)
        vpChat = view.findViewById(R.id.vpChat)
        chatVPAdapter = ChatVPAdapter(requireActivity())

        addFragments()

        vpChat.adapter = chatVPAdapter
        tabLayout.setupWithViewPager(vpChat, arrayListOf("Updates", "Messages"))

    }

    private fun addFragments() {
        chatVPAdapter.addFragment(UpdatesFragment())
        chatVPAdapter.addFragment(MessagesFragment())
    }

    private fun TabLayout.setupWithViewPager(viewPager: ViewPager2, labels: List<String>) {
        if (labels.size != viewPager.adapter?.itemCount)
            throw Exception("Item count is not equal labels size")

        TabLayoutMediator(this, viewPager) { tab, position ->
            tab.text = labels[position]
        }.attach()
    }
}