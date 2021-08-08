package rachman.forniandi.workmanagerapp.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.work.*
import rachman.forniandi.workmanagerapp.R
import rachman.forniandi.workmanagerapp.data.work.CollectLogWork
import rachman.forniandi.workmanagerapp.data.work.DataCleanUpWork
import rachman.forniandi.workmanagerapp.data.work.RandomNumberWork
import rachman.forniandi.workmanagerapp.data.work.UploadLogWork
import rachman.forniandi.workmanagerapp.databinding.FragmentDashboardBinding
import rachman.forniandi.workmanagerapp.ui.home.HomeFragment
import java.util.concurrent.TimeUnit

class DashboardFragment : Fragment() {

  private lateinit var dashboardViewModel: DashboardViewModel
  private lateinit var workManager: WorkManager
private var _binding: FragmentDashboardBinding? = null
  // This property is only valid between onCreateView and
  // onDestroyView.
  private val binding get() = _binding!!

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

    _binding = FragmentDashboardBinding.inflate(inflater, container, false)
    val root: View = binding.root

    val textView: TextView = binding.textDashboard
    dashboardViewModel.text.observe(viewLifecycleOwner, Observer {
      textView.text = it
    })
    return root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    //workManager is a service that is responsible for scheduling all the work that we request
    workManager = WorkManager.getInstance(requireContext())

    binding.btnStart.setOnClickListener {
      oneTimeRequest()
    }
  }

  private fun oneTimeRequest(){



    //constraints for workmanager
    val constraints = Constraints.Builder()
      .setRequiresCharging(true)
      .setRequiredNetworkType(NetworkType.CONNECTED)
      .build()

    //input data
    /*
    the maximum number of bytes for data when it serialized is 10 * 1024 = 10Kb
    */
    val inputData = Data.Builder()
      .putInt("KEY_START",0)
      .putInt("KEY_COUNT",10)
      .build()

    //work request class  allows us to define how and when we want our work to get executed
    val randomNumberWorkRequest = OneTimeWorkRequest.Builder(RandomNumberWork::class.java)
      .addTag(HomeFragment.WORK_TAG)
      .setConstraints(constraints)
      .setInputData(inputData)
      .build()

    val dataCleanUpWorkRequest = OneTimeWorkRequestBuilder<DataCleanUpWork>()
      .addTag(HomeFragment.WORK_TAG)
      .setInitialDelay(10, TimeUnit.SECONDS)
      .setConstraints(constraints)
      .build()


    //workManager.enqueue(randomNumberWorkRequest)
    workManager.beginUniqueWork(WORK_NAME,ExistingWorkPolicy.REPLACE,randomNumberWorkRequest)
      .then(dataCleanUpWorkRequest)
      .enqueue()


  }

override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

  companion object{
    const val WORK_NAME ="100288787"
  }
}