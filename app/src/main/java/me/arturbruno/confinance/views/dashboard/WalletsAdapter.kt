package me.arturbruno.confinance.views.dashboard

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import me.arturbruno.confinance.R
import me.arturbruno.confinance.databinding.WalletCardBinding
import me.arturbruno.confinance.getCurrencySymbol
import me.arturbruno.confinance.models.AccountType
import me.arturbruno.confinance.views.BankAccountDetailsActivity
import java.text.NumberFormat

class WalletsAdapter(private val onClick: (WalletData) -> Unit) : ListAdapter<WalletData, WalletsAdapter.WalletViewHolder>(WalletItemCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WalletViewHolder =
        WalletViewHolder.from(parent, onClick)

    override fun onBindViewHolder(holder: WalletViewHolder, position: Int) {
        val currentPositionItem = getItem(position)
        val context = holder.binding.root.context
        if (currentPositionItem is WalletData.BankAccountItem) {
            holder.binding.apply {
                walletImage.setImageResource(R.drawable.ic_baseline_account_balance_wallet_24)
                walletName.text = currentPositionItem.data.name
                walletBalance.text = context.getString(R.string.amount, getCurrencySymbol(), currentPositionItem.data.balance)
                walletType.text =
                    if (currentPositionItem.data.type == AccountType.CHECKING_ACCOUNT)
                        context.getString(R.string.checking_account) else
                        context.getString(R.string.savings_account)
            }
        } else if (currentPositionItem is WalletData.CreditCardItem){
            holder.binding.apply {
                    walletImage.setImageResource(R.drawable.ic_baseline_credit_card_24)
                    walletName.text = currentPositionItem.data.name
                    walletBalance.text = context.getString(R.string.amount, getCurrencySymbol(), currentPositionItem.data.invoice)
                    walletType.text = context.getString(R.string.credit_card)
            }
        }
        holder.binding.root.setOnClickListener {
            holder.onClick(currentPositionItem)
        }
    }

    class WalletViewHolder(val binding: WalletCardBinding, val onClick: (WalletData) -> Unit) : RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup, onClick: (WalletData) -> Unit): WalletViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = WalletCardBinding.inflate(layoutInflater)
                return WalletViewHolder(binding, onClick)
            }
        }
    }
}

class WalletItemCallback : DiffUtil.ItemCallback<WalletData>() {
    override fun areItemsTheSame(oldItem: WalletData, newItem: WalletData): Boolean {
        var result = false
        if (oldItem is WalletData.BankAccountItem && newItem is WalletData.BankAccountItem) {
            result = oldItem.data.id == newItem.data.id
        } else if (oldItem is WalletData.CreditCardItem && newItem is WalletData.CreditCardItem) {
            result = oldItem.data.id == newItem.data.id
        }
        return result
    }

    override fun areContentsTheSame(oldItem: WalletData, newItem: WalletData): Boolean {
        var result = false
        if (oldItem is WalletData.BankAccountItem && newItem is WalletData.BankAccountItem) {
            result = oldItem.data == newItem.data
        } else if (oldItem is WalletData.CreditCardItem && newItem is WalletData.CreditCardItem) {
            result = oldItem.data == newItem.data
        }
        return result
    }
}