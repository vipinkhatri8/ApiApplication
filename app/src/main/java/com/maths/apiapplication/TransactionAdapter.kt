package com.maths.apiapplication

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class TransactionAdapter(context: Context, var transactonList: ArrayList<TransactionData>) :
    RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {

    lateinit var date: String
    lateinit var time: String

    class ViewHolder(var binding: AdapterTransactionsBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: AdapterTransactionsBinding =
            AdapterTransactionsBinding.inflate(inflater, parent, false);
        return ViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = transactonList[position]
        var text = currentItem.technician.name
       var tv1 = text[0].toUpperCase()


        holder.binding.tvName.setText(tv1.toString())
        holder.binding.tvUserName.setText(currentItem.technician.name)

        var isoDateTime = currentItem.createdAt
        formatDateAndTime(isoDateTime)

        holder.binding.textTimeAndDate.setText("${time} | ${date}")
        holder.binding.textsubcategory.setText(currentItem.subcategory.subCategoryName)


        // Formatting the transaction amount
        holder.binding.texttransactionAmount.text = formatTransactionAmount(currentItem.transactionAmount)




    }




    override fun getItemCount(): Int {
        return transactonList.size;
    }
    fun formatTransactionAmount(amount: String): String {
        // Parse the string to a float
        val amountFloat = amount.toFloatOrNull() ?: return "error" // Return error if parsing fails

        // Check if the amount is an integer value when casted to int
        return if (amountFloat % 1 == 0f) {
            // If true, format without decimal places
            "$${amountFloat.toInt()}"
        } else {
            // Otherwise, keep two decimal places
            "$%.2f".format(amountFloat)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun formatDateAndTime(isoDateTime: String): Pair<String, String> {
        // Parse the ISO 8601 date-time string
        val parsedDateTime = ZonedDateTime.parse(isoDateTime)

        // Formatter for the date
        val dateFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy")
        // Formatter for the time
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm a")

        // Format date and time separately
        date = parsedDateTime.format(dateFormatter)
        time = parsedDateTime.format(timeFormatter)

        return Pair(date, time)
    }

    fun updateData(newTransactions: ArrayList<TransactionData>) {
        transactonList.clear()
        transactonList.addAll(newTransactions)
        notifyDataSetChanged()
    }

}
