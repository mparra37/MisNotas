package parra.mario.misnotas

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Toast
import kotlinx.android.synthetic.main.nota_layout.view.*
import java.io.File
import java.lang.Exception

class AdaptadorNota: BaseAdapter {
    var contexto: Context? = null
    var notas = ArrayList<Nota>()

    constructor(contexto: Context, notas: ArrayList<Nota>){
        this.contexto = contexto
        this.notas = notas
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var inflador = LayoutInflater.from(contexto)
        var vista = inflador.inflate(R.layout.nota_layout,null)
        var nota = notas[position]

        vista.tv_titulo_det.text = nota.titulo
        vista.tv_contenido_det.text = nota.contenido

        vista.btn_borrar.setOnClickListener{
            eliminarExterno(nota.titulo)
            notas.remove(nota)
            this.notifyDataSetChanged()
        }

        vista.btn_editar.setOnClickListener{
            var intent = Intent(contexto, MainActivity::class.java)
            intent.putExtra("titulo", nota.titulo)
            intent.putExtra("contenido", nota.contenido)
            (contexto as Activity).startActivityForResult(intent,123)
        }

        return vista


    }

    private fun eliminarExterno(titulo: String){

        if (titulo == ""){
            Toast.makeText(contexto, "Error: título vacío", Toast.LENGTH_SHORT).show()
        }else{
            try{

                //val ofi = openFileInput(titulo + ".txt")
                val archivo = File(album(),titulo+".txt")
                archivo.delete()

                Toast.makeText(contexto, "Se eliminó el archivo", Toast.LENGTH_SHORT).show()
            }catch(e: Exception){

                Log.e("errorz", e.message)
                Toast.makeText(contexto, "Error al eliminar el archivo", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun album(): String{
        val album = File(Environment.getExternalStorageDirectory(), "notas")
        if(!album.exists()){
            album.mkdir()
        }

        return album.absolutePath
    }

    override fun getItem(position: Int): Any {
        return notas[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return notas.size
    }
}