package com.assesment2.estuff.data

import android.app.Application


class Estuff : Application() {
    val database: BarangDb by lazy { BarangDb.getDatabase(this) }
}
