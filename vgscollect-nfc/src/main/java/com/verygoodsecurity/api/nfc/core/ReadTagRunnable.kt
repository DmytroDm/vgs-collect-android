package com.verygoodsecurity.api.nfc.core

import android.nfc.Tag
import com.verygoodsecurity.api.nfc.core.content.*
import com.verygoodsecurity.api.nfc.core.model.ApplicationFileLocator
import com.verygoodsecurity.api.nfc.core.model.Card
import com.verygoodsecurity.api.nfc.core.content.CommandAPDU
import com.verygoodsecurity.api.nfc.core.utils.*
import java.io.ByteArrayOutputStream

internal class ReadTagRunnable(
    @Suppress("unused") private val tag: Tag,
    private val listener: ResultListener,
) : Runnable {

    private val provider = IsoDepProvider(tag)

    override fun run() {
        provider.connect()

        provider.transceive(
            CommandAPDU(CommandEnum.SELECT, PPSE).toBytes()
        )?.takeIf { it.isSuccessful() }
            ?.run { parseFCITemplate(this) }
            ?.takeIf { it.isSuccessful() }
            ?.run { extractCardData(this) }
            ?.also { listener.onSuccess(it) } ?: listener.onFailure("error")
    }

    private fun parseFCITemplate(source: ByteArray): ByteArray {
        return source.getTLVValue(EMV.SFI)
            ?.takeIf {
                val fci = it.byteArrayToInt()
                val fciCommand = CommandAPDU(
                    CommandEnum.READ_RECORD, fci, fci shl 3 or 4, 0
                ).toBytes()
                val data = provider.transceive(fciCommand)

                data != null && it.compareADPU(TrailerADPU.SW_6C)
            }
            ?.run {
                val fci = byteArrayToInt()
                val leCommand = CommandAPDU(
                    CommandEnum.READ_RECORD, fci, fci shl 3 or 4, this[size - 1].toInt()
                ).toBytes()

                provider.transceive(leCommand)!!
            } ?: source
    }

    private fun extractCardData(data: ByteArray): Card? {
        var card: Card? = null
        val aids: List<ByteArray> = data.getAids()
        for (aid in aids) {
            val label: String = extractApplicationLabel(data)
            card = extractPublicCardData(aid, label)
            if (card != null) break
        }
        return card
    }

    private fun extractApplicationLabel(pData: ByteArray?): String {
        var label = ""
        val labelByte = pData?.getTLVValue(EMV.APPLICATION_LABEL)
        if (labelByte != null) {
            label = String(labelByte)
        }
        return label
    }

    private fun extractPublicCardData(aid: ByteArray, label: String): Card? {
        val data: ByteArray? = provider.transceive(
            CommandAPDU(CommandEnum.SELECT, aid, 0).toBytes()
        )

        if (data.isSuccessful()) {
            data?.getTLVValue(EMV.LOG_ENTRY, EMV.VISA_LOG_ENTRY)

            val pdol = data?.getTLVValue(EMV.PDOL)

            var gpo: ByteArray? = getGetProcessingOptions(pdol)

            if (gpo.isNotSuccessful()) {
                gpo = getGetProcessingOptions(null)
            }

            var data2 = gpo?.getTLVValue(EMV.RESPONSE_MESSAGE_TEMPLATE_1)
            if (data2 == null) {
                data2 = gpo?.getTLVValue(EMV.APPLICATION_FILE_LOCATOR)
            } else {
                data2.copyOfRange(2, data2.size)
            }

            if (data2 != null) {
                val listAfl: MutableList<ApplicationFileLocator> =
                    data2.extractApplicationFileLocator()

                for (afl in listAfl) {
                    for (index in afl.firstRecord..afl.lastRecord) {
                        val c = CommandAPDU(
                            CommandEnum.READ_RECORD,
                            index,
                            afl.sfi shl 3 or 4,
                            0
                        ).toBytes()
                        var info: ByteArray = provider.transceive(c)!!

                        if (info.compareADPU(TrailerADPU.SW_6C)) {
                            val s = CommandAPDU(
                                CommandEnum.READ_RECORD,
                                index,
                                afl.sfi shl 3 or 4,
                                info[info.size - 1].toInt()
                            ).toBytes()

                            info = provider.transceive(s)!!
                        }

                        if (info.isSuccessful()) {
                            val cardData = parseTrack2(info, label)
                            if (cardData != null) {
                                return cardData
                            }
                        }
                    }
                }

            }
        }
        return null
    }

    private fun getGetProcessingOptions(data: ByteArray?): ByteArray? {
        return ByteArrayOutputStream().use {
            val command = EMV.COMMAND_TEMPLATE.id.toHexByteArray()
            it.write(command)
            it.write(
                data?.parseTotalTagsLength() ?: 0
            )
            provider.transceive(CommandAPDU(CommandEnum.GPO, it.toByteArray(), 0).toBytes())
        }
    }

    private fun parseTrack2(data: ByteArray, label: String): Card? {
        return data.getTLVValue(EMV.TRACK_2_EQV_DATA, EMV.TRACK2_DATA)
            ?.bytesToStringNoSpace()
            ?.run {
                with(PaymentCardParser()) {
                    if (parse(this@run)) {
                        Card(label, number, expirationDate)
                    } else {
                        null
                    }
                }
            }
    }

    companion object {

        /**
         * PPSE directory "2PAY.SYS.DDF01"
         */
        private val PPSE = "2PAY.SYS.DDF01".toByteArray()
    }

    interface ResultListener {

        fun onSuccess(card: Card)

        fun onFailure(error: String)
    }
}