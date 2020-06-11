package com.verygoodsecurity.vgscollect.card.formatter

import com.verygoodsecurity.vgscollect.core.model.state.FieldState
import com.verygoodsecurity.vgscollect.view.card.CardType
import com.verygoodsecurity.vgscollect.view.card.formatter.CardMaskAdapter
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class CardMaskAdapterTest {

    companion object {
        private const val BIN = "37123"
        private const val MASK = "## ## # ##### ### ##"
    }

    private lateinit var adapter: TestAdapter

    @Before
    fun setupAdapter() {
        adapter = TestAdapter()
    }

    @Test
    fun test_get_item() {
        val type = CardType.AMERICAN_EXPRESS
        val stete = createAmexState1()
        val mask = adapter.getItem(type, stete, type.mask)


        Assert.assertEquals(mask, type.mask)
        Assert.assertEquals(type.name, stete.cardBrand)
    }

    @Test
    fun test_get_item_override() {
        val type = CardType.AMERICAN_EXPRESS
        val stete = createAmexState2()
        val mask = adapter.getItem(type, stete, type.mask)


        Assert.assertEquals(mask, MASK)
        Assert.assertEquals(type.name, stete.cardBrand)
    }

    private fun createAmexState1(): FieldState.CardNumberState {
        val state = FieldState.CardNumberState()
        state.bin = "370000"
        state.last = "123"
        state.cardBrand = CardType.AMERICAN_EXPRESS.name
        return state
    }
    private fun createAmexState2(): FieldState.CardNumberState {
        val state = FieldState.CardNumberState()
        state.bin = BIN
        state.last = "123"
        state.cardBrand = CardType.AMERICAN_EXPRESS.name
        return state
    }

    class TestAdapter : CardMaskAdapter() {

        private var currentMask:String? = null

        override fun getMask(
            cardType: CardType,
            state: FieldState.CardNumberState?,
            mask: String
        ): String {
            return when(cardType) {
                CardType.AMERICAN_EXPRESS -> {
                    currentMask = if (state?.bin != null && state.bin!!.contains(BIN)) {
                       MASK
                    } else {
                        mask
                    }
                    currentMask?:mask
                }
                else -> {
                    currentMask = super.getMask(cardType, state, mask)
                    mask
                }
            }
        }
    }
}