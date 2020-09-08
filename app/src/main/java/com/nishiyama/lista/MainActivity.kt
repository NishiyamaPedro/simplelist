package com.nishiyama.lista

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomappbar.BottomAppBar
import kotlinx.android.synthetic.main.activity_main.*


val listen : MutableLiveData<Double> =  MutableLiveData<Double>()

class MainActivity : AppCompatActivity() {
    var recyclerView: RecyclerView? = null
    var lista = ArrayList<ListaController>()
    var adapter = ListaAdapter(this, lista)
    var total: Double = 0.00


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.salvar -> {
                adapter.saveList()
                ShowTost(this,"Lista salva!")
                true
            }
            R.id.carregar -> {
                lista.clear()
                adapter.loadList()
                total = 0.0
                adapter.loadPreco()
                adapter.notifyItemRangeChanged(0, lista.size);
                ShowTost(this,"Lista carregada!")
                true
            }
            R.id.limpar -> {
                lista.clear()
                valTotal.text = "0,00"
                adapter.notifyDataSetChanged()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val rvRecyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        rvRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        rvRecyclerView.adapter = adapter



        button6.setOnClickListener {
            addLista()
        }
        edit1.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                addLista()
                return@OnKeyListener true
            }
            false
        })

        listen.observe(this, Observer {
            total += listen.value!!.toDouble()
            if (total < 0) {
                total = 0.0
            }
            valTotal.text = String.format("%.2f", total)


        })

    }
    fun ShowTost(context: Context?, ToastMessage: String?) {
        val toast = Toast.makeText(context, ToastMessage, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0)
        toast.show()
    }
    fun addLista() {
        if (edit1.text.isNotBlank()) {
            lista.add(ListaController(edit1.text.toString(), 0.00,false,0))
            adapter.notifyItemInserted(lista.size - 1)
            edit1.setText("")
        }
    }
}

