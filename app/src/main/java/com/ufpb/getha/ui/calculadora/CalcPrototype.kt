package com.ufpb.getha.ui.calculadora

sealed class Variavel {
    data class Definida(val v: Double): Variavel() {
        fun getVal(): Double {
            return this.v
        }
    }
    data object Indefinida: Variavel()
}

data class Conta (
    val c1: Variavel,
    val v1: Variavel,
    val c2: Variavel,
    val v2: Variavel,
)

fun conta(conta: Conta): Double? {
    if (conta.c1 is Variavel.Indefinida) {
        val v1 = (conta.v1 as Variavel.Definida).getVal()
        val c2 = (conta.c2 as Variavel.Definida).getVal()
        val v2 = (conta.v2 as Variavel.Definida).getVal()

        return (c2 * v2) / v1
    } else if (conta.v1 is Variavel.Indefinida) {
        val c1 = (conta.c1 as Variavel.Definida).getVal()
        val c2 = (conta.c2 as Variavel.Definida).getVal()
        val v2 = (conta.v2 as Variavel.Definida).getVal()

        return (c2 * v2) / c1
    } else if (conta.c2 is Variavel.Indefinida) {
        val c1 = (conta.c1 as Variavel.Definida).getVal()
        val v1 = (conta.v1 as Variavel.Definida).getVal()
        val v2 = (conta.v2 as Variavel.Definida).getVal()

        return (c1 * v1) / v2
    } else if (conta.v2 is Variavel.Indefinida) {
        val c1 = (conta.c1 as Variavel.Definida).getVal()
        val v1 = (conta.v1 as Variavel.Definida).getVal()
        val c2 = (conta.c2 as Variavel.Definida).getVal()

        return (c1 * v1) / c2
    } else {
        return null
    }
}