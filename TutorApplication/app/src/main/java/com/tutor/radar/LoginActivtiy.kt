package com.tutor.radar

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.tutor.radar.dto.User
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.client.RestTemplate
import java.lang.Exception
import java.net.URI

class LoginActivtiy  : AppCompatActivity() {
    var restTemplate : RestTemplate? = null
    var httpHeaders : HttpHeaders? = null
    var usernameField : EditText? = null
    var passwordField : EditText? = null
    var textField : TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        restTemplate = RestTemplate()
        usernameField = findViewById(R.id.username)
        passwordField = findViewById(R.id.password)
        textField = findViewById(R.id.textView9)
        restTemplate?.messageConverters?.add(MappingJackson2HttpMessageConverter())
        httpHeaders?.contentType = MediaType.APPLICATION_JSON
    }

    fun userLogin(view:View){
        if(usernameField?.text==null || usernameField!!.text.isEmpty()){
            textField?.text = "Email cannot be empty"
            return
        }
        if(passwordField?.text==null || passwordField!!.text.isEmpty()){
            textField?.text = "Password cannot be empty"
            return
        }
        var user = User()
        user.email = usernameField?.text.toString()
        user.password = passwordField?.text.toString()
        var httpEntity = HttpEntity<User>(user,httpHeaders)
        Thread(Runnable {
            try {
                var result = restTemplate?.postForEntity(URI("http://192.168.15.1:8081/tutorApp/user/login"), httpEntity, User::class.java)
                runOnUiThread {
                    var code = result?.statusCode
                    var intent : Intent? = null
                    if (code == HttpStatus.OK) {
                        var resultantUser = result?.body
                        if(resultantUser?.role.equals("admin")) {
                           intent =  Intent(this, ComingSoonActivity::class.java)
                        }else if(resultantUser?.role.equals("student")){
                            intent =  Intent(this, SearchSubjectsActivity::class.java)
                        }else if(resultantUser?.role.equals("tutor")){
                            intent =  Intent(this, ComingSoonActivity::class.java)
                        }else if(resultantUser?.role.equals("institute")){
                            intent =  Intent(this, ComingSoonActivity::class.java)
                        }
                    }else{
                        intent =  Intent(this, LoginActivtiy::class.java)
                    }
                    startActivity(intent)
                }
            }catch (e:Exception){
                e.printStackTrace()
            }
        }).start()
    }
}