package com.verygoodsecurity.demoapp.compose

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.verygoodsecurity.demoapp.compose.theme.Theme
import com.verygoodsecurity.vgscollect.compose.VGSTextField
import com.verygoodsecurity.vgscollect.core.VGSCollect
import com.verygoodsecurity.vgscollect.core.model.network.VGSRequest
import kotlinx.coroutines.launch

class ComposeActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Theme {
                Content(VGSCollect(this, "tntt1rsray8"))
            }
        }
    }
}

@Composable
fun Content(collect: VGSCollect? = null) {
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
    ) {
        VGSTextField(
            collect = collect,
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                scope.launch {
                    val response = collect?.submitAsync(
                        VGSRequest.VGSRequestBuilder()
                            .setPath("/post")
                            .build()
                    )
                    Log.d("Compose", "Response = " + response.toString())
                }
            }) {
            Text(text = "Submit")
        }
    }
}

@Preview
@Composable
fun ContentPreview() {
    Content()
}