package com.coderzf1.folderinformation

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment.getExternalStorageDirectory
import android.provider.DocumentsContract
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.coderzf1.folderinformation.ui.theme.FolderInformationTheme


class MainActivity : ComponentActivity() {

    private lateinit var launcher:ActivityResultLauncher<Uri?>

    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel:MainActivityViewModel by viewModels()

        launcher = registerForActivityResult(ActivityResultContracts.OpenDocumentTree()
        ) { uri ->
            if (uri == null) return@registerForActivityResult
            viewModel.setUri(uri)
            val contentResolver = applicationContext.contentResolver
            val takeFlags:Int = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
            contentResolver.takePersistableUriPermission(uri,takeFlags)


        }


        setContent {
            FolderInformationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        val currentUri = viewModel.chosenUriState.collectAsState().value.currentUri
                        if(currentUri != null) {
                            val documentId = DocumentsContract.getTreeDocumentId(currentUri)
                            val split = documentId.split(":")

                            if (split.size > 1 && "com.android.externalstorage.documents" == currentUri.authority) {
                                val path = split[1]
                                val fullPath = "${getExternalStorageDirectory()}/$path"
                                Text(
                                    fullPath
                                )
                            }
                        }
                        Button(
                                onClick = {
                                    launcher.launch(null)
                                }) {
                            Text("Browse Folder")
                        }
                    }

                    }
                }
            }
        }
    }


