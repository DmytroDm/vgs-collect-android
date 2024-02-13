
[![UT](https://img.shields.io/badge/Unit_Test-pass-green)]()
[![license](https://img.shields.io/badge/License-MIT-green.svg)](https://github.com/verygoodsecurity/vgs-collect-android/blob/master/LICENSE)
<img src="./ZeroDataLogo.png" width="55" hspace="8">

# VGS Plaid SDK

**VGS Plaid SDK** - is a product suite that allows to integrate Paid SDK with VGS Vault.

Table of contents
=================

<!--ts-->
* [Integration](#integration)
* [License](#license)
<!--te-->

## Integration

### Dependency

**VGS Plaid SDK** is currently under development, so in order to test it, manual dependency generation and setup required:
 - Clone the repository.
 - Build the AAR file in **Android Studio**(select the library module in the **Project** window and click **Build > Build APK**).
 - [Add AAR file as a dependency](https://developer.android.com/studio/projects/android-library#psd-add-aar-jar-dependency).

### How to use

Create result launcher in your activity:
```kotlin
private val vgsPlaidActivityResultLauncher = registerForActivityResult(
    ActivityResultContracts.StartActivityForResult(),
    ::handlePlaidResult
)
```

Launch VGSPlaidActivity with link token as intent extra:
```kotlin
val intent = Intent(this, VGSPlaidActivity::class.java)
intent.putExtra(KEY_EXTRA_LINK_TOKEN, token)
vgsPlaidActivityResultLauncher.launch(intent)
```

Hande result:
```kotlin
private fun handlePlaidResult(result: ActivityResult) {
    Log.d(TAG, result.toString())
    if (result.resultCode == Activity.RESULT_OK) {
        val publicToken = result.data?.getStringExtra(BUNDLE_KEY_PUBLIC_TOKEN)
    } else {
        Log.d(TAG, "Error/Canceled") 
    }
}
```

## License
VGSCollect Android SDK is released under the MIT license. [See LICENSE](https://github.com/verygoodsecurity/vgs-collect-android/blob/master/LICENSE) for details.