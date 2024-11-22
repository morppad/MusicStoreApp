package com.example.musicstoreapp.ui

import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicstoreapp.R
import com.example.musicstoreapp.adapter.TableAdapter
import okhttp3.*
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import kotlin.concurrent.thread

class AdminActivity : AppCompatActivity() {

    private lateinit var spinnerTables: Spinner
    private lateinit var btnLoadTable: Button
    private lateinit var recyclerView: RecyclerView

    private lateinit var inputProductName: EditText
    private lateinit var inputProductPrice: EditText
    private lateinit var inputProductCategory: EditText
    private lateinit var btnSelectImage: Button
    private lateinit var btnAddProduct: Button

    private var selectedImageUri: Uri? = null
    private var selectedImageFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        spinnerTables = findViewById(R.id.spinnerTables)
        btnLoadTable = findViewById(R.id.btnLoadTable)
        recyclerView = findViewById(R.id.recyclerView)

        inputProductName = findViewById(R.id.inputProductName)
        inputProductPrice = findViewById(R.id.inputProductPrice)
        inputProductCategory = findViewById(R.id.inputProductCategory)
        btnSelectImage = findViewById(R.id.btnSelectImage)
        btnAddProduct = findViewById(R.id.btnAddProduct)

        recyclerView.layoutManager = LinearLayoutManager(this)

        loadTableList()

        btnLoadTable.setOnClickListener {
            val selectedTable = spinnerTables.selectedItem as? String
            if (selectedTable != null) {
                loadTableData(selectedTable)
            } else {
                Toast.makeText(this, "Пожалуйста, выберите таблицу", Toast.LENGTH_SHORT).show()
            }
        }

        btnSelectImage.setOnClickListener {
            openImagePicker()
        }

        btnAddProduct.setOnClickListener {
            addProduct()
        }
    }

    private fun loadTableList() {
        thread {
            try {
                val client = OkHttpClient()
                val request = Request.Builder()
                    .url("http://213.108.23.147:8080/api/admin/tables")
                    .build()
                val response = client.newCall(request).execute()

                if (response.isSuccessful) {
                    val json = JSONArray(response.body?.string())
                    val tables = List(json.length()) { i -> json.getString(i) }

                    runOnUiThread {
                        spinnerTables.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, tables)
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this, "Не удалось загрузить список таблиц", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                runOnUiThread {
                    Toast.makeText(this, "Ошибка при загрузке списка таблиц", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun loadTableData(tableName: String) {
        thread {
            try {
                val client = OkHttpClient()
                val request = Request.Builder()
                    .url("http://213.108.23.147:8080/api/admin/view-table/$tableName")
                    .build()
                val response = client.newCall(request).execute()

                if (response.isSuccessful) {
                    val json = JSONArray(response.body?.string())
                    val rows = mutableListOf<Map<String, Any>>()

                    for (i in 0 until json.length()) {
                        val row = json.getJSONObject(i)
                        val map = row.keys().asSequence().associateWith { row[it] }
                        rows.add(map)
                    }

                    runOnUiThread {
                        recyclerView.adapter = TableAdapter(rows)
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this, "Не удалось загрузить данные таблицы", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                runOnUiThread {
                    Toast.makeText(this, "Ошибка при загрузке данных таблицы", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*"
        }
        startActivityForResult(intent, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            selectedImageUri = data?.data
            selectedImageUri?.let { uri ->
                selectedImageFile = createTempFileFromUri(uri)
                Toast.makeText(this, "Изображение выбрано", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun createTempFileFromUri(uri: Uri): File? {
        val contentResolver: ContentResolver = contentResolver
        val fileName = contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            cursor.moveToFirst()
            cursor.getString(nameIndex)
        } ?: return null

        val file = File(cacheDir, fileName)
        contentResolver.openInputStream(uri)?.use { inputStream ->
            FileOutputStream(file).use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }
        return file
    }

    private fun addProduct() {
        val name = inputProductName.text.toString()
        val price = inputProductPrice.text.toString()
        val category = inputProductCategory.text.toString()

        if (name.isEmpty() || price.isEmpty() || category.isEmpty() || selectedImageFile == null) {
            Toast.makeText(this, "Заполните все поля и выберите изображение", Toast.LENGTH_SHORT).show()
            return
        }

        thread {
            try {
                val client = OkHttpClient()
                val requestBody = MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("name", name)
                    .addFormDataPart("price", price)
                    .addFormDataPart("category", category)
                    .addFormDataPart("image", selectedImageFile!!.name, selectedImageFile!!.asRequestBody())
                    .build()

                val request = Request.Builder()
                    .url("http://213.108.23.147:8080/api/products")
                    .post(requestBody)
                    .build()

                val response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    runOnUiThread {
                        Toast.makeText(this, "Товар добавлен", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this, "Ошибка при добавлении товара", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
                runOnUiThread {
                    Toast.makeText(this, "Ошибка сети", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
