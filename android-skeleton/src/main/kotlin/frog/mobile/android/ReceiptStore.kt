package frog.mobile.android

import frog.core.ActionReceipt

/** Replace with Room persistence after the first device proof works. */
class ReceiptStore {
    private val receipts = mutableListOf<ActionReceipt>()

    @Synchronized
    fun append(receipt: ActionReceipt) {
        receipts += receipt
    }

    @Synchronized
    fun latest(): ActionReceipt? = receipts.lastOrNull()

    @Synchronized
    fun all(): List<ActionReceipt> = receipts.toList()
}
