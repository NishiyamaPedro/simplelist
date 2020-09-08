package com.nishiyama.lista

import android.icu.math.BigDecimal
import android.icu.util.Currency
import java.util.*

class ListaController {
    var name: String = ""
    var valor: Double = 0.00
    var chkd: Boolean = false
    var quantidade: Int = 0
    
    constructor(name: String, valor: Double, chkd: Boolean, quantidade: Int) {
        this.name = name
        this.valor = valor
        this.chkd = chkd
        this.quantidade = quantidade
    }
}