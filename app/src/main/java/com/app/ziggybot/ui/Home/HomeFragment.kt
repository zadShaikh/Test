package com.app.ziggybot.ui.Home

import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.aallam.openai.api.BetaOpenAI
import com.app.ziggybot.data.network.model.message
import com.app.ziggybot.data.network.model.parameterChat
import com.app.ziggybot.databinding.FragmentHomeBinding
import com.app.ziggybot.ui.MyViewModelFactory
import com.mycardsapplication.base.BaseFragment
import com.mycardsapplication.common.Constants
import com.mycardsapplication.common.Result
import com.mycardsapplication.data.network.model.chatGptResponse
import com.mycardsapplication.data.network.model.speechTotextResponse
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate),
    TextToSpeech.OnInitListener {
    private val viewModel: HomeViewModel by viewModels { MyViewModelFactory() }

    private val requiredPermissions = arrayOf(android.Manifest.permission.RECORD_AUDIO)
    var RandomAudioFileName = "ABCDEFGHIJKLMNOP"
    lateinit var random: Random
    lateinit var AudioSavePathInDevice: String
    private var recorder: MediaRecorder? = null
    private var tts: TextToSpeech? = null
    lateinit var message1: ArrayList<message>
    companion object {
        val REQUEST_RECORD_AUDIO_PERMISSION = 201
    }

    fun CreateRandomAudioFileName(string: Int): String? {
        random = Random()
        val stringBuilder = StringBuilder(string)
        var i = 0
        while (i < string) {
            stringBuilder.append(RandomAudioFileName.get(random.nextInt(RandomAudioFileName.length)))
            i++
        }
        return stringBuilder.toString()
    }

    @OptIn(BetaOpenAI::class)
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
         message1 = ArrayList()
        val msgModel = message("system", "You are a helpful assistant.")
        message1.add(msgModel)
        viewModel.SpeechToTextApiResponseData.observe(viewLifecycleOwner) {
            handleSpeechtoTextResponse(
                it
            )
        }
        viewModel.ChatGptApiResponseData.observe(viewLifecycleOwner) {
            handleChatGPTResponse(
                it
            )
        }
        tts = TextToSpeech(requireActivity(), this)
//        binding.buttonFirst.setOnClickListener {
////            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
//            startRecording()
//
//
//        }
//        binding.buttonSecond.setOnClickListener {
////            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
//            stopRecording()
//        }
        requestAudioPermission()
        binding.buttonFirst.setOnLongClickListener {
            if (tts != null) {
                tts!!.stop()
            }
            startRecording()
            return@setOnLongClickListener true
        }
        binding.buttonFirst.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_UP -> {
                        stopRecording()
                    }
//                    MotionEvent.ACTION_DOWN -> {stopRecording()}
                }

                return v?.onTouchEvent(event) ?: true
            }
        })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        val audioRecordPermissionGranted =
            requestCode == REQUEST_RECORD_AUDIO_PERMISSION &&
                    grantResults.firstOrNull() == PackageManager.PERMISSION_GRANTED

        if (!audioRecordPermissionGranted) {
//            finish()
        }
    }

    // Permission request
    private fun requestAudioPermission() {
        requestPermissions(requiredPermissions, REQUEST_RECORD_AUDIO_PERMISSION)
    }

    // start recording
    @RequiresApi(Build.VERSION_CODES.S)
    private fun startRecording() {

        val file: File
        if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
//            file = requireActivity().getExternalFilesDir(Environment.DIRECTORY_RECORDINGS)!!
            file = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        } else {
            file = requireActivity().cacheDir
        }

        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss")
            .format(Date())
        val imageFileName: String = CreateRandomAudioFileName(5) + timeStamp + "_"
        var audioFile: File? = null
        try {
//            audioFile = File.createTempFile(imageFileName, ".3gp", file)
            audioFile = File.createTempFile(imageFileName, ".mp3", file)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        AudioSavePathInDevice = audioFile?.path.toString()


        recorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile(AudioSavePathInDevice)
            prepare()
        }
        recorder?.start()
//        soundVisualizerView.startVisualizing(false)
//        recordTimeTextView.startCountUp()
//        state = State.ON_RECORDING
    }

    // stop recording
    private fun stopRecording() {
        recorder?.run {
            stop()
            release()

            viewModel.speechTotextApi(
                Constants.OPENAI_KEY,
                Constants.WHISPER,
                AudioSavePathInDevice
            )

            // null //// By doing so, you avoid taking up memory.
            recorder = null
//        soundVisualizerView.stopVisualizing()
//        recordTimeTextView.stopCountUp()
//        state = State.AFTER_RECORDING
        }

    }

    //Speech to text Api response
    private fun handleSpeechtoTextResponse(response: Result<speechTotextResponse>) {
        when (response) {
            is Result.Loading -> binding.progressBar.isVisible = true
            is Result.Failure -> {
                showToastMessage(response.message)
                binding.progressBar.isVisible = false
            }
            is Result.Success -> handleSpeechtoTextApiSuccess(response)
        }
    }

    private fun handleSpeechtoTextApiSuccess(data: Result.Success<speechTotextResponse>) {
        binding.progressBar.isVisible = false

//        if (data.value.isSuccess()) {
        binding.textviewQuestion.text = "Question : " + data.value.textt.toString()
//            showToastMessage(data.value.textt)
//        }


        val msgModel = message("user", data.value.textt.toString())
        message1.add(msgModel)
        val parChatGpt = parameterChat(Constants.CHATGPT_MODEL, message1)

        viewModel.ChatGptApi(
            Constants.OPENAI_KEY,
            parChatGpt
        )

//        viewModel.ChatGptApi(
//            Constants.OPENAI_KEY,
//            Constants.CHATGPT_MODEL,
//            "",
//            chatGptMessage,modal,jdonn
//        )

    }


    //Chat GPT Api response
    private fun handleChatGPTResponse(response: Result<chatGptResponse>) {
        when (response) {
            is Result.Loading -> binding.progressBar.isVisible = true
            is Result.Failure -> {
                showToastMessage(response.message)
                binding.progressBar.isVisible = false
            }
            is Result.Success -> handleChatGPTApiSuccess(response)
        }
    }

    private fun handleChatGPTApiSuccess(data: Result.Success<chatGptResponse>) {
        binding.progressBar.isVisible = false

        binding.textviewAns.text = "Answer : " + data.value.choices.last().message.content
//            showToastMessage(data.value.textt)
        val msgModel = message("assistant", data.value.choices.last().message.content)
        message1.add(msgModel)
        speakOut(data.value.choices.last().message.content)

    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            // set US English as language for tts
            val result = tts!!.setLanguage(Locale.US)

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS","The Language specified is not supported!")
            } else {
//                buttonSpeak!!.isEnabled = true
            }

        } else {
            Log.e("TTS", "Initilization Failed!")
        }
    }
    private fun speakOut(text : String) {
        tts!!.speak(text, TextToSpeech.QUEUE_FLUSH, null,"")
    }

    public override fun onDestroy() {
        // Shutdown TTS
        if (tts != null) {
            tts!!.stop()
            tts!!.shutdown()
        }
        super.onDestroy()
    }
}