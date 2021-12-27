package rachman.forniandi.workmanagerapp.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.work.*
import rachman.forniandi.workmanagerapp.R
import rachman.forniandi.workmanagerapp.data.work.EmployeeCoroutineWork
import rachman.forniandi.workmanagerapp.data.work.RandomNumberPeriodicWork
import rachman.forniandi.workmanagerapp.data.work.UserRxJavaWorker
import rachman.forniandi.workmanagerapp.databinding.FragmentNotificationsBinding
import java.util.concurrent.TimeUnit

class NotificationsFragment : Fragment() {

  private lateinit var notificationsViewModel: NotificationsViewModel
  private var _binding: FragmentNotificationsBinding? = null
  private lateinit var workManager: WorkManager
  // This property is only valid between onCreateView and
  // onDestroyView.
  private val binding get() = _binding!!

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    notificationsViewModel =
            ViewModelProvider(this).get(NotificationsViewModel::class.java)

    _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
    val root: View = binding.root

    val textView: TextView = binding.textNotifications
    notificationsViewModel.text.observe(viewLifecycleOwner, Observer {
      textView.text = it
    })
    return root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    workManager = WorkManager.getInstance(requireContext())

    //setup a periodic work request to insert data to local db
    periodicWorkRequest()

    periodicWorkRequestRxJava()
  }

  private fun periodicWorkRequest(){
    //constraints
    val constraintsPeriodic = Constraints.Builder()
      .setRequiredNetworkType(NetworkType.CONNECTED)
      .build()

    /*periodic work request*/
    val randomNumberWorkRequest = PeriodicWorkRequest.Builder(
      EmployeeCoroutineWork::class.java,15, TimeUnit.MINUTES)
      .setConstraints(constraintsPeriodic)
      .build()

    workManager.enqueue(randomNumberWorkRequest)
  }

  private fun periodicWorkRequestRxJava(){
    //constraints
    val constraintsPeriodic = Constraints.Builder()
      .setRequiredNetworkType(NetworkType.CONNECTED)
      .build()

    /*periodic work request*/
    val workRequestRxJava = PeriodicWorkRequest.Builder(
      UserRxJavaWorker::class.java,15, TimeUnit.MINUTES)
      .setConstraints(constraintsPeriodic)
      .build()

    workManager.enqueue(workRequestRxJava)
  }


override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}