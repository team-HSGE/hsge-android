package com.starters.hsge.presentation.register.fragment

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.starters.hsge.R
import com.starters.hsge.databinding.FragmentDogPhotoBinding
import com.starters.hsge.domain.UriUtil
import com.starters.hsge.presentation.common.base.BaseFragment
import com.starters.hsge.presentation.register.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

@AndroidEntryPoint
class DogPhotoFragment : BaseFragment<FragmentDogPhotoBinding>(R.layout.fragment_dog_photo) {

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private val OPEN_GALLERY = 1
    private val registerViewModel: RegisterViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //https://stackoverflow.com/questions/41928803/how-to-parse-json-in-kotlin
        val imageResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    //이미지 받으면 ImageView에 적용
                    val imageUri = result.data?.data
                    registerViewModel.dogPhoto = imageUri.toString()
                    context?.let {
                        if (imageUri != null) {

                            //==== 개별로 보내기 ====
//                            val textHashMap = hashMapOf<String, RequestBody>()
//                            val nickName = "이태만"
//                            val latitude = "123"
//                            val longitude = "456"
//                            val breed = "Poodle"
//                            val petName = "태미니"
//                            val description = "설명설명설명"
//                            val gender = "female"
//                            val neutralization = false
//                            val nickNameRequestBody: RequestBody = nickName.toPlainRequestBody()
//                            val latitudeRequestBody: RequestBody = latitude.toPlainRequestBody()
//                            val breedRequestBody: RequestBody = breed.toPlainRequestBody()
//
//                            textHashMap["nickname"] = nickNameRequestBody
//                            textHashMap["latitude"] = latitudeRequestBody
//                            textHashMap["breed"] = breedRequestBody
                            //=================================================

                            // val jsonBody = RequestBody.create(parse("application/json"),jsonObject)
                            val name = "이태만"
                            val neuter = false
                            val textHashMap = hashMapOf<String, RequestBody>()
                            val jsonObject = JSONObject("{\"nickname\":\"${name}}\",\"latitude\":\"77\",\"longitude\":\"33\",\"breed\":\"Poodle\",\"neutralization\":${neuter}}").toString()
                            val jsonBody = RequestBody.create("application/json".toMediaTypeOrNull(),jsonObject)
                            textHashMap["signUpDto"] = jsonBody
                            val imageToFile = UriUtil.toFile(it, imageUri)
                            lifecycleScope.launch {
                                registerViewModel.loadImage(imageToFile, textHashMap)
                            }
                            setButtonEnable()


                        }
                    }
                    imageUri?.let {
                        //서버 업로드를 위해 파일 형태로 변환
                        //imageFile = File(getRealPathFromURI(it))

                        //이미지 불러오기
                        Glide.with(this)
                            .load(imageUri)
                            .fitCenter()
                            .circleCrop()
                            .into(binding.ivDogPhoto)
                    }


                }
            }

        // 권한 요청 -> launch로 권한 대화상자 열기
        requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
                if (isGranted) {
                    // Permission is granted
                    // 갤러리 열기
                    val intent = Intent(Intent.ACTION_GET_CONTENT)
                    intent.setType("image/*")
                    startActivityForResult(intent, OPEN_GALLERY)

                    // 이미지 등록
                    imageResult.launch(intent)

                } else {
                    // Explain to the user
                }

            }

        initListener()
        setNavigation()

    }

    private fun initListener() {

        binding.ivDogPhoto.setOnClickListener {
            // 시스템 권한 대화상자 요청
            requestPermissionLauncher.launch(READ_EXTERNAL_STORAGE)
        }

        binding.btnNext.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_dogPhotoFragment_to_dogNameFragment)

        }
    }

    private fun setButtonEnable() {
        binding.btnNext.isEnabled = !registerViewModel.dogPhoto.isNullOrEmpty()
    }

    private fun setNavigation() {
        binding.toolBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun String?.toPlainRequestBody() = requireNotNull(this).toRequestBody("text/plain".toMediaTypeOrNull())


    // Api 호출 시 파라미터로 이미지 포함 다만, 이때에는 이미지의 경로를 찾아 File 형태로 추가
//    private fun getRealPathFromUri(uri: Uri): String {
//
//        val buildName = Build.MANUFACTURER
//        if (buildName.equals("sample")) {
//                return uri.path!!
//            }
//        var columnIndex = 0
//        val proj = arrayOf(MediaStore.Images.Media.DATA)
//        val cursor = context?.contentResolver?.query(uri, proj, null, null, null)
//        if (cursor!!.moveToFirst()) {
//            columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
//        }
//        val result = cursor.getString(columnIndex)
//        cursor.close()
//        return result
//    }
}