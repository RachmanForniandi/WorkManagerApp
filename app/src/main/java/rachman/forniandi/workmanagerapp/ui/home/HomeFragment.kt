package rachman.forniandi.workmanagerapp.ui.home

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
import rachman.forniandi.workmanagerapp.data.work.DataCleanUp
import rachman.forniandi.workmanagerapp.data.work.RandomNumberWork
import rachman.forniandi.workmanagerapp.databinding.FragmentHomeBinding
import java.util.*

class HomeFragment : Fragment() {


  private var _binding: FragmentHomeBinding? = null
  private lateinit var homeViewModel: HomeViewModel
  private lateinit var workManager:WorkManager
  private lateinit var randomNumberWorkRequestUUID: UUID
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

    homeViewModel.resultData.observe(viewLifecycleOwner,{
      textView.text = it.toString()
    })

    homeViewModel.msg.observe(viewLifecycleOwner,{
      textView.text = it.toString()
    })
    return root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    //workManager
    workManager = WorkManager.getInstance(requireContext())


    buttonClickListener()


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
      .setConstraints(constraints)
      .setInputData(inputData)
      .build()

    val dataCleanUpWorkRequest = OneTimeWorkRequestBuilder<DataCleanUp>()
      .setConstraints(constraints)
      .build()

    randomNumberWorkRequestUUID= randomNumberWorkRequest.id
    //workManager.enqueue(randomNumberWorkRequest)
    workManager.beginWith(randomNumberWorkRequest)
      .then(dataCleanUpWorkRequest)
      .enqueue()


    workManager.getWorkInfoByIdLiveData(randomNumberWorkRequest.id).observe(viewLifecycleOwner,{
      when{

        it.state == WorkInfo.State.RUNNING->{
          homeViewModel.msg.value = "RandomNumberWork: RUNNING"
        }

        it.state == WorkInfo.State.CANCELLED->{
          homeViewModel.msg.value = "RandomNumberWork: CANCELLED"
        }
        it.state.isFinished->{
          val resultOut =it.outputData.getInt("KEY_RESULT",0)
          resultOut.let {
            homeViewModel.resultData.value = it
          }
        }
      }
    })
  }



  private fun buttonClickListener(){
    binding.btnStart.setOnClickListener {
      oneTimeRequest()
    }

    binding.btnCancel.setOnClickListener {
      workManager.cancelWorkById(randomNumberWorkRequestUUID)
    }
  }

override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}