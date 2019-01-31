package br.com.thingsproject.things.domain

import br.com.thingsproject.things.R

enum class TempoCusto(val string: String) {
    twelve("12 Horas | R\$ 15"),
    twenty("20 Horas | R\$ 17"),
    forty("40 Horas | R\$ 20")
}

enum class Dispo(val string: String) {
    dis("Disponivel"),
    indis("Indisponivel")
}

enum class Abas(val string: Int) {
    Meus(R.string.proprios),
    Alugados(R.string.emprestados)
}