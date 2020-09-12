package com.nishiyama.lista

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.NumberPicker
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import kotlin.collections.ArrayList

class ListaAdapter(val context : Context, val ListaList: ArrayList<ListaController>) : RecyclerView.Adapter<ListaAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListaAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_row, parent, false)
        return ViewHolder(v)
    }

    @SuppressLint("DefaultLocale")
    override fun onBindViewHolder(holder: ListaAdapter.ViewHolder, position: Int) {
        holder.chkb.text = ListaList[position].name.capitalize()
        holder.chkb.isChecked = ListaList[position].chkd
        holder.quantidade.text = ListaList[position].quantidade.toString()
        holder.vtot.text = String.format("%.2f", (ListaList[position].valor * ListaList[position].quantidade))
        holder.valo.text = String.format("%.2f", ListaList[position].valor)
        holder.lItem.setOnLongClickListener {
            val builder: AlertDialog.Builder = AlertDialog.Builder(context, R.style.MyDialogTheme)
            builder.setTitle("Deseja deletar o item?")

            builder.setPositiveButton("Sim"
            ){ _, _ ->
                    if(ListaList[position].chkd) {
                        listen.value = -(ListaList[position].valor * ListaList[position].quantidade)
                    }

                    ListaList.removeAt(position)
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, ListaList.size)
                }
            builder.setNegativeButton("Não"
            ) { dialog, _ ->
                    dialog.cancel()
                }
            builder.show()

            true
        }

        holder.chkb.setOnClickListener { view ->
            if(view is CheckBox){
                if(view.isChecked){
                    val builder: AlertDialog.Builder = AlertDialog.Builder(context, R.style.MyDialogTheme)
                    builder.setTitle("Preço do(a) ${ListaList[position].name.capitalize()}")

                    val customLayout: View = LayoutInflater.from(context).inflate(R.layout.dpreco, null)
                    val npick: NumberPicker = customLayout.findViewById(R.id.numberpick1)
                    val input: EditText = customLayout.findViewById(R.id.editText)
                    input.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
                    npick.maxValue = 99
                    npick.minValue = 1
                    builder.setView(customLayout)



                    builder.setOnCancelListener {
                        view.isChecked = false
                        ListaList[position].chkd = false
                    }
                    builder.setPositiveButton("Inserir"
                    ) { dialog, _ ->
                        if(input.text.toString() != "") {
                            ListaList[position].quantidade = npick.value
                            ListaList[position].valor = input.text.toString().toDouble()
                        } else {
                            ListaList[position].valor = 0.00
                            ListaList[position].quantidade = 0
                            dialog.cancel()
                        }
                        ListaList[position].chkd = true
                        listen.value = (ListaList[position].valor * ListaList[position].quantidade)
                        notifyItemRangeChanged(0, ListaList.size)

                    }
                    builder.setNeutralButton("Usar anterior"
                    ) { _, _ ->
                        ListaList[position].chkd = true
                        listen.value = (ListaList[position].valor * ListaList[position].quantidade)
                        notifyItemRangeChanged(0, ListaList.size)

                    }
                    builder.setNegativeButton("Cancelar"
                    ) { dialog, _ ->
                        dialog.cancel()
                    }
                    builder.show()
                } else {
                    if(!view.isChecked) {
                        ListaList[position].chkd = false
                        listen.value = -(ListaList[position].valor * ListaList[position].quantidade)
                    }
                }
            }
        }
    }

    fun saveList() {
        val dataProccessor = DataProcess(context)
        dataProccessor.clean()
        for((j, _) in ListaList.withIndex()){
            dataProccessor.setStr("nome${j}", ListaList[j].name )
            dataProccessor.setStr("valor${j}", ListaList[j].valor.toString() )
            dataProccessor.setBool("ch${j}", ListaList[j].chkd)
            dataProccessor.setInt("qtd${j}", ListaList[j].quantidade)
        }
    }

    fun loadList() {
        val dataProccessor = DataProcess(context)
        for(j in 0 until dataProccessor.getSize()){
            ListaList.add(ListaController(dataProccessor.getStr("nome${j}")!!,dataProccessor.getStr("valor${j}")!!.toDouble(),dataProccessor.getBool("ch${j}"),dataProccessor.getInt("qtd${j}")))
        }
    }
    fun loadPreco() {
        for((j, _) in ListaList.withIndex())
            if (ListaList[j].chkd)
                listen.value = (ListaList[j].valor * ListaList[j].quantidade)
    }

    override fun getItemCount(): Int {
        return ListaList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val chkb: CheckBox = itemView.findViewById(R.id.checkBox)
        val valo: TextView = itemView.findViewById(R.id.textView2)
        val lItem: ConstraintLayout = itemView.findViewById(R.id.Litem)
        val quantidade: TextView = itemView.findViewById(R.id.textView8)
        val vtot: TextView = itemView.findViewById(R.id.textView4)
    }
}