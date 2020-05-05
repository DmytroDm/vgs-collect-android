package com.verygoodsecurity.vgscollect

import com.verygoodsecurity.vgscollect.core.model.state.*
import com.verygoodsecurity.vgscollect.view.card.FieldType
import org.junit.Assert.assertTrue
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.*

class VGSFieldStateTest {

    @Test
    fun map_VGSFieldStateTo_CardNumberState() {
        val ce = VGSFieldState(type = FieldType.CARD_NUMBER)

        val ce1 = ce.mapToFieldState()
        assertTrue(ce1 is FieldState.CardNumberState)
    }

    @Test
    fun map_VGSFieldStateTo_CardExpirationDate() {
        val ce = VGSFieldState(type = FieldType.CARD_EXPIRATION_DATE)

        val ce1 = ce.mapToFieldState()
        assertTrue(ce1 is FieldState.CardExpirationDateState)
    }

    @Test
    fun map_VGSFieldStateTo_CardName() {
        val ce = VGSFieldState(type = FieldType.CARD_HOLDER_NAME)

        val ce1 = ce.mapToFieldState()
        assertTrue(ce1 is FieldState.CardHolderNameState)
    }

    @Test
    fun map_VGSFieldStateTo_CVCState() {
        val ce = VGSFieldState(type = FieldType.CVC)

        val ce1 = ce.mapToFieldState()
        assertTrue(ce1 is FieldState.CVCState)
    }

    @Test
    fun map_VGSFieldStateTo_Info() {
        val ce = VGSFieldState(type = FieldType.INFO)

        val ce1 = ce.mapToFieldState()
        assertTrue(ce1 is FieldState.InfoState)
    }

    @Test
    fun mapToFieldStateCardNumber() {
        val content = FieldContent.CardNumberContent()
        content.data = "5555 5555 1234 5678"
        val oldState = VGSFieldState(isFocusable = true,
            isRequired = true,
            isValid = true,
            type = FieldType.INFO,
            content = content,
            fieldName = "fn")

        val newState = oldState.mapToFieldState()

        assertTrue(newState.hasFocus == oldState.isFocusable)
        assertTrue(newState.isRequired == oldState.isRequired)
        assertTrue(newState.isEmpty == oldState.content?.data.isNullOrEmpty())
        assertTrue(newState.isValid == oldState.isValid)
        assertTrue(newState.fieldName == oldState.fieldName)
    }

    @Test
    fun mapToFieldStateCardNumberInfo() {
        val content = FieldContent.CardNumberContent()
        content.data = "5555 5555 1234 5678"
        val oldState = VGSFieldState(isFocusable = true,
            isRequired = true,
            isValid = true,
            type = FieldType.CARD_NUMBER,
            content = content,
            fieldName = "fn")

        val newState = oldState.mapToFieldState()

        assertTrue(newState is FieldState.CardNumberState)

        val c = (newState as FieldState.CardNumberState)

        assertTrue(c.number == "5555 55## #### 5678")
    }

    @Test
    fun mapBin() {
        val content = FieldContent.CardNumberContent()
        content.data = "5512 3455 1234 5"
        val oldState = VGSFieldState(isFocusable = true,
            isRequired = true,
            isValid = true,
            type = FieldType.CARD_NUMBER,
            content = content,
            fieldName = "fn")

        val newState = oldState.mapToFieldState()

        assertTrue(newState is FieldState.CardNumberState)

        val c = (newState as FieldState.CardNumberState)

        assertTrue(c.bin == "551234")
    }

    @Test
    fun mapLast4() {
        val content = FieldContent.CardNumberContent()
        content.data = "5555 5555 1234 5"
        val oldState = VGSFieldState(isFocusable = true,
            isRequired = true,
            isValid = true,
            type = FieldType.CARD_NUMBER,
            content = content,
            fieldName = "fn")

        val newState = oldState.mapToFieldState()

        assertTrue(newState is FieldState.CardNumberState)

        val c = (newState as FieldState.CardNumberState)

        assertTrue(c.last == "5")
    }

    @Test
    fun test_map_date_mm_yy() {
        val date = "12/24"
        val fieldDateFormat: SimpleDateFormat = SimpleDateFormat("MM/yy", Locale.getDefault())
        val fieldDateOutPutFormat: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        val content = FieldContent.CreditCardExpDateContent()
        content.data = date


        val c = Calendar.getInstance()
        c.time = fieldDateFormat.parse(date)
        content.handleOutputFormat(c, fieldDateFormat, fieldDateOutPutFormat)

        assertTrue(content.rawData == "2024-12-01")
    }

    @Test
    fun test_map_date_dd_mm_yyyy() {
        val date = "31-12-24"
        val fieldDateFormat = SimpleDateFormat("dd-MM-yy", Locale.getDefault())
        val fieldDateOutPutFormat = SimpleDateFormat("MM/yyyy", Locale.getDefault())

        val content = FieldContent.CreditCardExpDateContent()
        content.data = date


        val c = Calendar.getInstance()
        c.time = fieldDateFormat.parse(date)
        content.handleOutputFormat(c, fieldDateFormat, fieldDateOutPutFormat)

        assertTrue(content.rawData == "12/2024")
    }
}