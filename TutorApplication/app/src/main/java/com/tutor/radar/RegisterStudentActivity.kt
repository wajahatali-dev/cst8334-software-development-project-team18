package com.tutor.radar

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.tutor.radar.dto.User
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.client.RestTemplate
import org.w3c.dom.Text
import java.net.URI

class RegisterStudentActivity : AppCompatActivity() {
    var restTemplate : RestTemplate? = null
    var httpHeaders : HttpHeaders? = null
    var textView : TextView? = null
    var editTextName : TextView? = null
    var editTextPostalAddress : TextView? = null
    var editCity : TextView? = null
    var editCountry : TextView? = null
    var editInstitute : TextView? = null
    var editProvince : TextView? = null
    var editTextTextEmailAddress : TextView? = null
    var editTextTextPassword : TextView? = null
    var editTextTextPassword2 : TextView? = null
    var rg : RadioGroup? = null
    var gender : String? = null
    var radioButton : RadioButton? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_student)
        restTemplate = RestTemplate()
        restTemplate?.messageConverters?.add(MappingJackson2HttpMessageConverter())
        httpHeaders?.contentType = MediaType.APPLICATION_JSON
        textView = findViewById(R.id.textView)
        editTextName  = findViewById(R.id.editTextName)
        editTextPostalAddress = findViewById(R.id.editTextPostalAddress)
        editCity = findViewById(R.id.editCity)
        editCountry = findViewById(R.id.editCountry)
        editInstitute = findViewById(R.id.editInstitute)
        editProvince = findViewById(R.id.editProvince)
        editTextTextEmailAddress = findViewById(R.id.editTextTextEmailAddress)
        editTextTextPassword = findViewById(R.id.editTextTextPassword)
        editTextTextPassword2 = findViewById(R.id.editTextTextPassword2)
        rg = findViewById(R.id.genderRadio);
        radioButton = findViewById(rg?.checkedRadioButtonId!!)
        gender = radioButton?.text.toString()
    }

    fun sendRegisterRequest(view:View){
        if(editTextTextEmailAddress?.text!!.isEmpty()){
            textView?.text = "Email Address Required !!"
            return
        }
        if(editTextTextPassword?.text!!.isEmpty()){
            textView?.text = "Password Required !!"
            return
        }
        if(!editTextTextPassword2?.text.toString().equals(editTextTextPassword?.text.toString())){
            textView?.text = "Confirm password and password do not match !!"
            return
        }
        var user = User()
        user.name = editTextName?.text.toString()
        user.address = editTextPostalAddress?.text.toString()
        user.city = editCity?.text.toString()
        user.country = editCountry?.text.toString()
        user.institute = editInstitute?.text.toString()
        user.province = editProvince?.text.toString()
        user.email = editTextTextEmailAddress?.text.toString()
        user.password = editTextTextPassword?.text.toString()
        user.gender = gender
        user.role ="student"
        var httpEntity = HttpEntity<User>(user,httpHeaders)
        Thread(Runnable {
            var result = restTemplate?.postForEntity(URI("http://192.168.15.1:8081/tutorApp/user/register"),httpEntity,User::class.java)
            runOnUiThread {
                var code = result?.statusCode
                var intent : Intent? = null
                if (code == HttpStatus.OK) {
                    intent =  Intent(this, LoginActivtiy::class.java)
                }else{
                    intent =  Intent(this, RegisterStudentActivity::class.java)
                }
                startActivity(intent)
            }
        }).start()
    }
}