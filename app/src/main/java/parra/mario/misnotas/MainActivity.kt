package parra.mario.misnotas

import android.Manifest
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.support.v4.content.ContextCompat
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.FileOutputStream

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var extras = intent.extras

        if (extras != null){
            var titulo = extras.getString("titulo")
            var contenido = extras.getString("contenido")

            et_titutlo.setText(titulo)
            et_contenido.setText(contenido)
        }


        btn_guardar.setOnClickListener{
            guardarExterno()
        }
    }

    private fun guardarExterno(){
        var titulo = et_titutlo.text.toString()
        var cuerpo = et_contenido.text.toString()
        if (titulo == "" || cuerpo ==  ""){
            Toast.makeText(this, "Error: campos vacíos", Toast.LENGTH_SHORT).show()
        }else{
            if (isExternalStorageWritable() && verificarPermiso()){
                //val archivo = File(Environment.getExternalStorageDirectory(),titulo+".txt")
                val archivo = File(ubicacion(),titulo+".txt")
                val fos = FileOutputStream(archivo)
                fos.write(cuerpo.toByteArray())
                fos.close()
                Toast.makeText(this,"se guardó el archivo en la carpeta pública", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this, "Error: no se guardó el archivo", Toast.LENGTH_SHORT).show()
            }
        }
        finish()
    }

    fun isExternalStorageWritable(): Boolean{
        val state = Environment.getExternalStorageState()
        if (Environment.MEDIA_MOUNTED.equals(state)){
            return true
        }else{
            return false
        }
    }
    private fun verificarPermiso(): Boolean{
        var permiso = Manifest.permission.WRITE_EXTERNAL_STORAGE
        var verificacion = ContextCompat.checkSelfPermission(this, permiso)
        return (verificacion == PackageManager.PERMISSION_GRANTED)
    }

    private fun ubicacion(): String{
        val carpeta = File(Environment.getExternalStorageDirectory(), "notas")
        if(!carpeta.exists()){
            carpeta.mkdir()
        }

        return carpeta.absolutePath
    }
}
