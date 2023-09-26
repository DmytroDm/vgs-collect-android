package com.verygoodsecurity.vgscollect.compose

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.autoSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.verygoodsecurity.vgscollect.core.OnVgsViewStateChangeListener
import com.verygoodsecurity.vgscollect.core.VGSCollect
import com.verygoodsecurity.vgscollect.core.VGSView
import com.verygoodsecurity.vgscollect.core.api.analityc.AnalyticTracker
import com.verygoodsecurity.vgscollect.core.model.state.Dependency
import com.verygoodsecurity.vgscollect.core.model.state.FieldContent
import com.verygoodsecurity.vgscollect.core.model.state.FieldState
import com.verygoodsecurity.vgscollect.core.model.state.VGSFieldState
import com.verygoodsecurity.vgscollect.core.storage.DependencyListener
import com.verygoodsecurity.vgscollect.core.storage.DependencyType
import com.verygoodsecurity.vgscollect.core.storage.OnFieldStateChangeListener
import com.verygoodsecurity.vgscollect.view.AccessibilityStatePreparer
import com.verygoodsecurity.vgscollect.view.card.FieldType

private const val INITIAL_VALUE = "INITIAL_VALUE"
private const val FIELD_NAME = "field_name"

@Composable
fun VGSTextField(
    collect: VGSCollect? = null,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    prefix: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape = TextFieldDefaults.shape,
    colors: TextFieldColors = TextFieldDefaults.colors()
) {
    var state by remember {
        mutableStateOf(
            VGSFieldState(
                content = FieldContent.InfoContent(INITIAL_VALUE),
                fieldName = FIELD_NAME
            )
        )
    }

    val view = remember {

        object : VGSView {

            var listener: OnVgsViewStateChangeListener? = null
                set(value) {
                    field = value
                    field?.emit(statePreparer.getId(), state)
                }

            override val statePreparer = object : AccessibilityStatePreparer {

                override fun getId(): Int = 1 // TODO: Implement unique id

                override fun getView(): View {
                    TODO("Refactor(required by old view, not needed here)")
                }

                override fun unsubscribe() {
                    listener = null
                }

                override fun getDependencyListener() =  object : DependencyListener {

                    override fun dispatchDependencySetting(dependency: Dependency) {
                        // TODO: Disaptch scan result
                    }
                }

                override fun setAnalyticTracker(tr: AnalyticTracker) {
                    // TODO: Implement analytics
                }
            }

            override fun getFieldName(): String? = state.fieldName

            override fun getFieldType(): FieldType = state.type

            override fun addStateListener(listener: OnVgsViewStateChangeListener) {
                this.listener = listener
            }
        }
    }

    view.listener?.emit(view.statePreparer.getId(), state)

    LaunchedEffect(Unit) {
        Log.d("Compose", "VGSTextField:bind, collector = $view")
        collect?.bindView(view)
    }

    DisposableEffect(Unit) {
        onDispose {
            Log.d("Compose", "VGSTextField:unbind, collector = $view")
            collect?.unbindView(view)
        }
    }

    TextField(
        value = state.content?.data ?: "",
        onValueChange = { state = state.copy(content = FieldContent.InfoContent(it))},
        modifier = modifier,
        enabled = enabled,
        readOnly = readOnly,
        textStyle = textStyle,
        label = label,
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        prefix = prefix,
        suffix = suffix,
        supportingText = supportingText,
        isError = isError,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = singleLine,
        maxLines = maxLines,
        minLines = minLines,
        interactionSource = interactionSource,
        shape = shape,
        colors = colors
    )
}

@Composable
@Preview
fun VGSTextFieldPreview() {
    VGSTextField()
}