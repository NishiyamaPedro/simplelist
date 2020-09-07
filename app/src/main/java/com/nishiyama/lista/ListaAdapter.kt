package com.nishiyama.lista

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView


class ListaAdapter(val context : Context, val ListaList: ArrayList<ListaController>) : RecyclerView.Adapter<ListaAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListaAdapter.ViewHolder {
        val v = LayoutInflater.from(parent?.context).inflate(R.layout.list_row, parent, false)
        return ViewHolder(v);
    }

    override fun onBindViewHolder(holder: ListaAdapter.ViewHolder, position: Int) {
        holder?.chkb?.text = ListaList[position].name.capitalize()
        holder?.chkb?.isChecked = ListaList[position].chkd
        holder?.valo?.text = String.format("%.2f", ListaList[position].valor)
        holder?.lItem?.setOnLongClickListener(View.OnLongClickListener { view ->
            val builder: AlertDialog.Builder = AlertDialog.Builder(context)
            builder.setTitle("Deseja deletar o item?")

            builder.setPositiveButton("Sim",
                DialogInterface.OnClickListener { dialog, which ->
                    if(ListaList[position].chkd) {
                        listen.value = -(ListaList[position].valor)
                    }

                    ListaList.removeAt(position)
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, ListaList.size)
                })
            builder.setNegativeButton("Não",
                DialogInterface.OnClickListener { dialog, which ->
                    dialog.cancel()
                })
            builder.show()

            true
        })

        holder?.chkb?.setOnClickListener(View.OnClickListener { view ->
            if(view is CheckBox){
                if(view.isChecked){
                    val builder: AlertDialog.Builder = AlertDialog.Builder(context)
                    builder.setTitle("Insira o preço")

                    val input = EditText(context)
                    input.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
                    builder.setView(input)

                    builder.setOnCancelListener(DialogInterface.OnCancelListener {
                        view.isChecked = false
                        ListaList[position].chkd = false
                    })
                    builder.setPositiveButton("Inserir",
                        DialogInterface.OnClickListener { dialog, which ->
                            if(input.text.toString() != "") {
                                ListaList[position].valor = input.text.toString().toDouble()
                            } else {
                                ListaList[position].valor = 0.00
                            }
                            ListaList[position].chkd = true
                            listen.value = ListaList[position].valor
                            notifyDataSetChanged()

                        })
                    builder.setNeutralButton("Usar anterior",
                        DialogInterface.OnClickListener { dialog, which ->
                            ListaList[position].chkd = true
                            listen.value = ListaList[position].valor
                            notifyDataSetChanged()

                        })
                    builder.setNegativeButton("Cancelar",
                        DialogInterface.OnClickListener { dialog, which ->
                            dialog.cancel()
                        })
                    builder.show()
                } else {
                    if(!view.isChecked) {
                        ListaList[position].chkd = false
                        listen.value = -(ListaList[position].valor)
                    }
                }
            }
        })
    }

    public fun saveList() {
        val dataProccessor = DataProcess(context)
        dataProccessor.clean()
        for((j, i) in ListaList.withIndex()){
            dataProccessor.setStr("nome${j}", ListaList[j].name )
            dataProccessor.setStr("valor${j}", ListaList[j].valor.toString() )
            dataProccessor.setBool("ch${j}", ListaList[j].chkd)
        }
    }

    public fun loadList() {
        val dataProccessor = DataProcess(context)
        for(j in 0 until dataProccessor.getSize()){
            ListaList.add(ListaController(dataProccessor.getStr("nome${j}")!!,dataProccessor.getStr("valor${j}")!!.toDouble(),dataProccessor.getBool("ch${j}")))
        }
    }

    override fun getItemCount(): Int {
        return ListaList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val chkb = itemView.findViewById<CheckBox>(R.id.checkBox)
        val valo = itemView.findViewById<TextView>(R.id.textView2)
        val lItem = itemView.findViewById<ConstraintLayout>(R.id.Litem)
    }
}