package com.nishiyama.lista

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.input.*


val listen : MutableLiveData<Double> =  MutableLiveData()

class MainActivity : AppCompatActivity() {
    var lista = ArrayList<ListaController>()
    private var adapter = ListaAdapter(this, lista)
    private var total: Double = 0.00

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val rvRecyclerView = findViewById<RecyclerView>(R.id.recyclerView)

        rvRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        rvRecyclerView.adapter = adapter


        fab.setOnClickListener {
            //addLista()
            fab.visibility = View.INVISIBLE
            coordinatorLayout.visibility = View.INVISIBLE
            input.visibility = View.VISIBLE
            inputEdit.requestFocus()

            val constraintLayout: ConstraintLayout = findViewById(R.id.mainlayout)
            val constraintSet = ConstraintSet()
            constraintSet.clone(constraintLayout)
            constraintSet.connect(
                R.id.recyclerView,
                ConstraintSet.BOTTOM,
                R.id.input,
                ConstraintSet.TOP,
                0
            )
            constraintSet.applyTo(constraintLayout)

            val imm =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
        }

        btnAdd.setOnClickListener {
            addLista()
        }

        btnSair.setOnClickListener {
            fab.visibility = View.VISIBLE
            coordinatorLayout.visibility = View.VISIBLE
            input.visibility = View.INVISIBLE

            val constraintLayout: ConstraintLayout = findViewById(R.id.mainlayout)
            val constraintSet = ConstraintSet()
            constraintSet.clone(constraintLayout)
            constraintSet.connect(
                R.id.recyclerView,
                ConstraintSet.BOTTOM,
                R.id.coordinatorLayout,
                ConstraintSet.TOP,
                0
            )
            constraintSet.applyTo(constraintLayout)

            val imm = getSystemService(
                Context.INPUT_METHOD_SERVICE
            ) as InputMethodManager
            imm.hideSoftInputFromWindow(inputEdit.windowToken, 0)
        }

        inputEdit.setOnKeyListener(View.OnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                addLista()

                return@OnKeyListener true
            }
            false
        })

        listen.observe(this, Observer {
            if (lista.isNotEmpty()) {
                total += listen.value!!.toDouble()
                if (total < 0) {
                    total = 0.0
                }
                valTotal2.text = String.format("%.2f", total)
            }
        })

        bottomAppBar.setOnMenuItemClickListener { item ->
            if (item != null) when (item.itemId) {
                R.id.salvar -> {
                    val builder: AlertDialog.Builder = AlertDialog.Builder(this, R.style.MyDialogTheme)
                    builder.setTitle("Deseja salvar?")

                    builder.setPositiveButton("Sim"
                    ){ _, _ ->
                        if (lista.isNotEmpty()) {
                            adapter.saveList()
                            showToast(this, "Lista salva!")
                        }   else {
                            showToast(this, "Lista em branco! Não foi possível salvar.")
                        }
                    }
                    builder.setNegativeButton("Não"
                    ) { dialog, _ ->
                        dialog.cancel()
                    }
                    builder.show()

                }
                R.id.carregar -> {
                    val builder: AlertDialog.Builder = AlertDialog.Builder(this, R.style.MyDialogTheme)
                    builder.setTitle("Deseja carregar?")

                    builder.setPositiveButton("Sim"
                    ){ _, _ ->
                        val dataProcess = DataProcess(this)
                        if (dataProcess.getSize() != 0) {
                            lista.clear()
                            adapter.loadList()
                            total = 0.0
                            valTotal2.text = "0,00"
                            adapter.loadPreco()
                            adapter.notifyItemRangeChanged(0, lista.size)
                            textView11.visibility = View.INVISIBLE
                            imageView.visibility = View.INVISIBLE
                            showToast(this, "Lista carregada!")
                        } else {
                            showToast(this, "Não há lista salva.")
                        }
                    }
                    builder.setNegativeButton("Não"
                    ) { dialog, _ ->
                        dialog.cancel()
                    }
                    builder.show()

                }
                R.id.limpar -> {
                    val builder: AlertDialog.Builder = AlertDialog.Builder(this, R.style.MyDialogTheme)
                    builder.setTitle("Limpar lista atual?")

                    builder.setPositiveButton("Sim"
                    ){ _, _ ->
                        lista.clear()
                        total = 0.0
                        valTotal2.text = "0,00"
                        adapter.notifyDataSetChanged()
                        textView11.visibility = View.VISIBLE
                        imageView.visibility = View.VISIBLE
                    }
                    builder.setNegativeButton("Não"
                    ) { dialog, _ ->
                        dialog.cancel()
                    }
                    builder.show()


                }
                else -> super.onOptionsItemSelected(item)
            }
            true
        }
        this?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (input.visibility == View.VISIBLE){
                    fab.visibility = View.VISIBLE
                    coordinatorLayout.visibility = View.VISIBLE
                    input.visibility = View.INVISIBLE

                    val constraintLayout: ConstraintLayout = findViewById(R.id.mainlayout)
                    val constraintSet = ConstraintSet()
                    constraintSet.clone(constraintLayout)
                    constraintSet.connect(
                        R.id.recyclerView,
                        ConstraintSet.BOTTOM,
                        R.id.coordinatorLayout,
                        ConstraintSet.TOP,
                        0
                    )
                    constraintSet.applyTo(constraintLayout)
                }
            }
        })
    }

    private fun showToast(context: Context?, ToastMessage: String?) {
        val toast = Toast.makeText(context, ToastMessage, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0)
        toast.show()
    }
    private fun addLista() {
        if (inputEdit.text.isNotBlank()) {
            lista.add(ListaController(inputEdit.text.toString(), 0.00,false,0))
            adapter.notifyItemInserted(lista.size - 1)
            inputEdit.setText("")
        }
        textView11.visibility = View.INVISIBLE
        imageView.visibility = View.INVISIBLE
    }
}

