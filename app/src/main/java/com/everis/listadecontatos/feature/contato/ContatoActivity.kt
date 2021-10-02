package com.everis.listadecontatos.feature.contato

import android.app.DatePickerDialog
import android.os.Bundle
import android.service.autofill.OnClickAction
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.DatePicker
import android.widget.TextView
import androidx.appcompat.app.AlertDialog

import com.everis.listadecontatos.R
import com.everis.listadecontatos.application.ContatoApplication
import com.everis.listadecontatos.bases.BaseActivity
import com.everis.listadecontatos.feature.listacontatos.model.ContatosVO
import kotlinx.android.synthetic.main.activity_contato.*
import kotlinx.android.synthetic.main.activity_contato.progress
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*


class ContatoActivity : BaseActivity(), DatePickerDialog.OnDateSetListener {


    private var idContato: Int = -1


















    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contato)

        setupContato()
        btnSalvarConato.setOnClickListener { onClickSalvarContato() }
    }


    private fun setupContato() {
        idContato = intent.getIntExtra("index", -1)
        if (idContato == -1) {


            btnExcluirContato.visibility = View.GONE

            return

        }
        progress.visibility = View.VISIBLE


        Thread(Runnable {
            Thread.sleep(1500)
            var lista = ContatoApplication.instance.helperDB?.buscarContatos("$idContato", true)
                ?: return@Runnable
            var contato = lista.getOrNull(0) ?: return@Runnable
            runOnUiThread {
                etNome.setText(contato.nome)
                etData.setText(contato.data)
                progress.visibility = View.GONE
            }
        }).start()
    }


    private fun onClickSalvarContato() {
        val builder = AlertDialog.Builder(this)
        if (etNome.text.isEmpty()) {
            builder.setTitle("ATENÇÃO")
            builder.setMessage("Voçê tem que preencher os campos")
            builder.show()
        }

        if (etData.text.isEmpty()) {
            builder.setTitle("ATENÇÃO")
            builder.setMessage("Voçê tem que preencher os campos")
            builder.show()
        } else {
            val nome = etNome.text.toString()



            val hora = etHora.text.toString()
            var data = etData.text.toString()
            val contato = ContatosVO(
                idContato,
                nome,
                hora,
                data

            )
            progress.visibility = View.VISIBLE
            Thread(Runnable {
                Thread.sleep(1500)
                if (idContato == -1) {

                    ContatoApplication.instance.helperDB?.salvarContato(contato)
                } else {
                    ContatoApplication.instance.helperDB?.updateContato(contato)
                }
                runOnUiThread {
                    progress.visibility = View.GONE
                    finish()
                }
            }).start()
        }
    }

    fun onClickExcluirContato(view: View) {
        if (idContato > -1) {

            progress.visibility = View.VISIBLE
            Thread(Runnable {
                Thread.sleep(1500)
                ContatoApplication.instance.helperDB?.deletarCoontato(idContato)
                runOnUiThread {
                    progress.visibility = View.GONE
                    finish()
                }
            }).start()
        }
    }



    var mSimpleDate = SimpleDateFormat("dd/MM/yyyy")
    fun cptData(view: android.view.View) {
        DatePickerDialog(this, this, 2021, 1, 1).show()
    }


    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {

        var dat = Calendar.getInstance()
        dat.set(year, month, dayOfMonth)
        etData?.setText(
            mSimpleDate.format(dat.time)
        )

    }



}





