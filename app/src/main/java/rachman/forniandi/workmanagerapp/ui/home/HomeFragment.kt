package rachman.forniandi.workmanagerapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import rachman.forniandi.workmanagerapp.R
import rachman.forniandi.workmanagerapp.data.work.RandomNumberWork
import rachman.forniandi.workmanagerapp.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

  private lateinit var homeViewModel: HomeViewModel
private var _binding: FragmentHomeBinding? = null
  // This property is only valid between onCreateView and
  // onDestroyView.
  private val binding get() = _binding!!

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

    _binding = FragmentHomeBinding.inflate(inflater, container, false)
    val root: View = binding.root

    val textView: TextView = binding.textHome
    homeViewModel.text.observe(viewLifecycleOwner, Observer {
      textView.text = it
    })
    return root
  }

  private fun oneTimeRequest(){

    //workManager
    val workManager = WorkManager.getInstance(requireContext())

    //constraints for workmanager
    val constraints = Constraints.Builder()
      .setRequiresCharging(true)
      .setRequiredNetworkType(NetworkType.CONNECTED)
      .build()

    //work request class  allows us to define how and when we want our work to get executed
    val randomNumberWorkRequest = OneTimeWorkRequest.Builder(RandomNumberWork::class.java)
      .setConstraints(constraints)
      .build()

    workManager.enqueue(randomNumberWorkRequest)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    buttonClickListener()


  }

  private fun buttonClickListener(){
    binding.btnStart.setOnClickListener {
      oneTimeRequest()
    }

    binding.btnCancel.setOnClickListener {

    }
  }

override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}