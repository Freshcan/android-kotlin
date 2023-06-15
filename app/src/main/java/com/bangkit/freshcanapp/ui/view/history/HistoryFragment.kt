package com.bangkit.freshcanapp.ui.view.history

import android.animation.ValueAnimator
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.animation.DecelerateInterpolator
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bangkit.freshcanapp.R
import com.bangkit.freshcanapp.data.remote.response.HistoryItemResponse
import com.bangkit.freshcanapp.data.remote.response.PayloadItem
import com.bangkit.freshcanapp.databinding.FragmentHistoryBinding
import com.bangkit.freshcanapp.ui.adapter.HistoryAdapter
import com.bangkit.freshcanapp.ui.view.login.LoginViewModel
import com.bangkit.freshcanapp.utils.Injection
import com.bangkit.freshcanapp.utils.ViewModelFactory
import com.google.android.material.tabs.TabLayout


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HistoryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class HistoryFragment : Fragment() {
    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var historyViewModel: HistoryViewModel
    private lateinit var tabs: TabLayout
    private lateinit var viewPager: ViewPager2
    private var tabsInitialY: Float? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = GridLayoutManager(requireActivity(), 2)
        binding.rvHistory.layoutManager = layoutManager
        tabs = requireActivity().findViewById(R.id.tabs)
        viewPager = requireActivity().findViewById(R.id.view_pager)

        // wait for the layout measured and call method
        tabs.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                tabs.viewTreeObserver.removeOnGlobalLayoutListener(this)
                setUpAnimation()
            }
        })

        setupViewModel()
    }

    override fun onStart() {
        super.onStart()
        historyViewModel.getAllDummyHistory()

    }

    private fun setupViewModel() {
        historyViewModel = ViewModelProvider(
            this,
            ViewModelFactory(Injection.provideRepository(requireActivity(), requireActivity().dataStore))
        )[HistoryViewModel::class.java]

        historyViewModel.getAllDummyHistory()

        historyViewModel.listHistoryResponse.observe(viewLifecycleOwner, {
            setUpRecycler(it)
        })


    }

    private fun setUpAnimation() {
        tabsInitialY = tabs.y
        Log.e("HistoryFragment", "Outside Scroll: ${tabsInitialY}")
        binding.rvHistory.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) {
                    // Scrolling down, hide the TabLayout
//                    tabs.visibility = View.GONE
                    val newPosY = tabs.y + dy
                    val screenHeight = resources.displayMetrics.heightPixels
                    Log.e("HistoryFragment", "Scrolled Down dy: ${dy}")

                    if (newPosY < screenHeight) {
                        tabs.y = newPosY
                    } else {
                        tabs.y = screenHeight.toFloat()
                    }
                } else if (dy < 0) {
                    Log.e("HistoryFragment", "Scrolled Up dy: ${dy}")
                    // Scrolling up, show the TabLayout
//                    tabs.visibility = View.VISIBLE
                    val newPosY = tabs.y + dy
                    if (newPosY > tabsInitialY!!) {
                        tabs.y = newPosY
                    } else {
                        tabs.y = tabsInitialY!!
                    }
                }
            }
        })
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            var previousPage = 1
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

                if (previousPage != position) {
                    previousPage = position
                    val animator = ValueAnimator.ofFloat(tabs.y, tabsInitialY!!)
                    animator.addUpdateListener { valueAnimator ->
                        val animatedValue = valueAnimator.animatedValue as Float
                        tabs.y = animatedValue
                    }
                    animator.duration = 300 // Set the duration of the animation in milliseconds
                    animator.interpolator = DecelerateInterpolator() // Optional: Set the desired interpolator for the animation
                    animator.start()
                }
            }
        })
    }

    private fun setUpRecycler(listHistory: List<PayloadItem>) {
        val listHistoryPlus = ArrayList<PayloadItem>()
        for(item in listHistory){
            listHistoryPlus.add(item)
        }
        val adapter = HistoryAdapter(listHistoryPlus)

        binding.rvHistory.adapter = adapter
//        binding.edReview.setText("")
    }



    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HistoryFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HistoryFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}