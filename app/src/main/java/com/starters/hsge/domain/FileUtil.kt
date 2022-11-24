package com.starters.hsge.domain

import android.content.Context
import android.net.Uri
import android.os.Environment
import java.io.File
import java.io.FileOutputStream

/**
 * Android 10 이상에서는 저장소 접근 정책 변경
 * Uri 파일의 절대 경로 얻어내는 것이 불가
 * 임시 파일로 복사본 생성 -> 복사본을 서버에 업로드
 * https://yjyoon-dev.github.io/android/2022/04/09/android-05/
 */
object FileUtil {
    //임시 파일 생성
    fun createTempFile(context: Context, fileName: String): File {
        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File(storageDir, fileName)
    }

    // 파일 내용 스트림 복사
    fun copyToFile(context: Context, uri: Uri, file: File) {
        val inputStream = context.contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)

        val buffer = ByteArray(4 * 1024)
        while (true) {
            val byteCount = inputStream!!.read(buffer)
            if (byteCount < 0) break
            outputStream.write(buffer, 0, byteCount)
        }

        outputStream.flush()
    }
}