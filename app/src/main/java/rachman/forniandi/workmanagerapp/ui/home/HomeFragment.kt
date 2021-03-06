package rachman.forniandi.workmanagerapp.ui.home

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
import rachman.forniandi.workmanagerapp.data.work.CollectLogWork
import rachman.forniandi.workmanagerapp.data.work.DataCleanUpWork
import rachman.forniandi.workmanagerapp.data.work.RandomNumberWork
import rachman.forniandi.workmanagerapp.data.work.UploadLogWork
import rachman.forniandi.workmanagerapp.databinding.FragmentHomeBinding
import java.util.*
import java.util.concurrent.TimeUnit

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
    //workManager is a service that is responsible for scheduling all the work that we request
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
      .addTag(WORK_TAG)
      .setConstraints(constraints)
      .setInputData(inputData)
      .build()

    val dataCleanUpWorkRequest = OneTimeWorkRequestBuilder<DataCleanUpWork>()
      .addTag(WORK_TAG)
      .setInitialDelay(10,TimeUnit.SECONDS)
      .setConstraints(constraints)
      .build()

    /*
    * parallel work
    * */
    val collectLogWorkRequest = OneTimeWorkRequestBuilder<CollectLogWork>()
      .addTag(WORK_TAG)
      .build()

    val uploadLogWorkRequest = OneTimeWorkRequestBuilder<UploadLogWork>()
      .addTag(WORK_TAG)
      .build()

    val parallelWorkRequest = mutableListOf<OneTimeWorkRequest>().apply {
      this.add(collectLogWorkRequest)
      this.add(uploadLogWorkRequest)
    }

    randomNumberWorkRequestUUID= randomNumberWorkRequest.id
    //workManager.enqueue(randomNumberWorkRequest)
    workManager.beginWith(randomNumberWorkRequest)
      .then(dataCleanUpWorkRequest)
      .then(parallelWorkRequest)
      .enqueue()

    workManager.getWorkInfoByIdLiveData(randomNumberWorkRequest.id).observe(viewLifecycleOwner,{
      when{

        it.state == WorkInfo.State.RUNNING->{
          homeViewModel.msg.value = "RandomNumberWork: RUNNING"
        }

        it.state == WorkInfo.State.CANCELLED->{
          homeViewModel.msg.value = "RandomNumberWork: CANCELLED"
        }
        it.state == WorkInfo.State.FAILED->{
          homeViewModel.msg.value = "RandomNumberWork: FAILED :Error on the for loop"
          Toast.makeText(requireActivity(),"Failed",Toast.LENGTH_LONG).show()
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
  companion object{
    const val WORK_TAG="rachman.forniandi.workmanagerapp"
  }
}