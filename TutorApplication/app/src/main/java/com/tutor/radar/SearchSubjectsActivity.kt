package com.tutor.radar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.tutor.radar.dto.User
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.client.RestTemplate
import java.io.Serializable
import java.net.URI
import kotlin.reflect.typeOf

class SearchSubjectsActivity : AppCompatActivity() {
    var spinner : Spinner? = null
    var arrayList = ArrayList<String>()
    var subject: String? = null
    var httpHeaders : HttpHeaders? = null
    var restTemplate : RestTemplate? = null
    var listView : ListView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_subjects)
        arrayList.add("java");
        arrayList.add("android");
        arrayList.add("C Language");
        arrayList.add("CPP Language");
        arrayList.add("Go Language");
        arrayList.add("Php");
        spinner = findViewById(R.id.spinner2)
        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, arrayList)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner?.adapter = arrayAdapter
        spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                subject = parent.getItemAtPosition(position).toString()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        restTemplate = RestTemplate()
        restTemplate?.messageConverters?.add(MappingJackson2HttpMessageConverter())
        httpHeaders?.contentType = MediaType.APPLICATION_JSON

    }
    var selectedUser : User? = null
    fun getTutorsForSubject(view:View){
        Thread(Runnable {
            try {
                var result = restTemplate?.getForEntity(URI("http://192.168.15.1:8081/tutorApp/user/getTutors/$subject"), Array<User>::class.java)
                var lst : Array<User> = result?.body as Array<User>
                runOnUiThread {
                    var spinnerView : Spinner = findViewById(R.id.spinner3)
                    var arrayListnew : ArrayList<String> = ArrayList<String>()
                    for(ls in 0 until lst.size){
                        arrayListnew.add(lst[ls].name!!)
                    }
                    val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, arrayListnew)
                    spinnerView?.adapter = arrayAdapter
                    spinnerView?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                            selectedUser = lst.get(position)
                        }
                        override fun onNothingSelected(parent: AdapterView<*>?) {}
                    }
                }
            }catch(e:Exception){
                e.printStackTrace()
            }
        }).start()
    }

    fun redirectToNextViewWithDetails(view:View){
        val intent = Intent(this,TutorProfileActivity::class.java)
        intent.putExtra("name",selectedUser?.name )
        intent.putExtra("address",selectedUser?.address )
        intent.putExtra("city",selectedUser?.city )
        intent.putExtra("country",selectedUser?.country )
        intent.putExtra("email",selectedUser?.email )
        intent.putExtra("experienceYear",selectedUser?.experienceYear )
        intent.putExtra("institute",selectedUser?.institute )
        intent.putExtra("province",selectedUser?.province )
        intent.putExtra("subject",selectedUser?.subject )
        startActivity(intent)
    }

}