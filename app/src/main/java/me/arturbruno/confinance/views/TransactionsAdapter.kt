package me.arturbruno.confinance.views

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import me.arturbruno.confinance.R
import me.arturbruno.confinance.databinding.TransactionItemBinding
import me.arturbruno.confinance.getCurrencySymbol
import kotlin.math.absoluteValue

class TransactionsAdapter :
    ListAdapter<Transaction, TransactionsAdapter.TransactionViewHolder>(TransactionItemCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder =
        TransactionViewHolder.from(parent)

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val currentPositionItem = getItem(position)
        val date = Instant.fromEpochMilliseconds(currentPositionItem.date).toLocalDateTime(TimeZone.currentSystemDefault())
        val context = holder.binding.root.context
        when (currentPositionItem) {
            is Transaction.BankTransactionItem -> {
                holder.binding.apply {
                    if (currentPositionItem.data.value > 0) {
                        transactionIcon.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24)
                        transactionIcon.backgroundTintList = context.getColorStateList(R.color.green)
                    } else {
                        transactionIcon.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
                        transactionIcon.backgroundTintList = context.getColorStateList(R.color.red)
                    }
                    transactionName.text = currentPositionItem.data.name
                    transactionValue.text = context.getString(
                        R.string.amount,
                        getCurrencySymbol(),
                        currentPositionItem.data.value.absoluteValue
                    )
                    transactionDate.text = context.getString(
                        R.string.date_time,
                        date.dayOfMonth,
                        date.monthNumber,
                        date.year,
                        date.hour,
                        date.minute
                    )
                }
            }
            is Transaction.CardPurchaseItem -> {
                holder.binding.apply {
                    transactionIcon.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
                    transactionIcon.backgroundTintList = context.getColorStateList(R.color.red)
                    transactionName.text = currentPositionItem.data.name
                    transactionValue.text = context.getString(
                        R.string.amount,
                        getCurrencySymbol(),
                        currentPositionItem.data.value
                    )
                    transactionDate.text = context.getString(
                        R.string.date_time,
                        date.dayOfMonth,
                        date.monthNumber,
                        date.year,
                        date.hour,
                        date.minute
                    )
                }
            }
            is Transaction.InvoicePaymentItem -> {
                holder.binding.apply {
                    transactionIcon.setImageResource(R.drawable.ic_baseline_remove_24)
                    transactionIcon.backgroundTintList = context.getColorStateList(R.color.yellow)
                    transactionName.text = context.getString(R.string.invoice_payment)
                    transactionValue.text = context.getString(
                        R.string.amount,
                        getCurrencySymbol(),
                        currentPositionItem.data.value
                    )
                    transactionDate.text = context.getString(
                        R.string.date_time,
                        date.dayOfMonth,
                        date.monthNumber,
                        date.year,
                        date.hour,
                        date.minute
                    )
                }
            }
        }
    }

    class TransactionViewHolder(
        val binding: TransactionItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup): TransactionViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = TransactionItemBinding.inflate(layoutInflater, parent, false)
                return TransactionViewHolder(binding)
            }
        }
    }
}

class TransactionItemCallback : DiffUtil.ItemCallback<Transaction>() {
    override fun areItemsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
        var result = false
        if (oldItem is Transaction.CardPurchaseItem && newItem is Transaction.CardPurchaseItem) {
            result = oldItem.data.id == newItem.data.id
        } else if (oldItem is Transaction.BankTransactionItem && newItem is Transaction.BankTransactionItem) {
            result = oldItem.data.id == newItem.data.id
        } else if (oldItem is Transaction.InvoicePaymentItem && newItem is Transaction.InvoicePaymentItem) {
            result = oldItem.data.id == newItem.data.id
        }
        return result
    }

    override fun areContentsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
        var result = false
        if (oldItem is Transaction.CardPurchaseItem && newItem is Transaction.CardPurchaseItem) {
            result = oldItem.data == newItem.data
        } else if (oldItem is Transaction.BankTransactionItem && newItem is Transaction.BankTransactionItem) {
            result = oldItem.data == newItem.data
        } else if (oldItem is Transaction.InvoicePaymentItem && newItem is Transaction.InvoicePaymentItem) {
            result = oldItem.data == newItem.data
        }
        return result
    }
}