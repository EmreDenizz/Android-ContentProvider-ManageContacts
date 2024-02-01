// Emre Deniz (301371047)
// Assignment 2

package com.example.mapd721_a2_emredeniz

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mapd721_a2_emredeniz.ui.theme.MAPD721A2EmreDenizTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MAPD721A2EmreDenizTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@Composable
fun MainScreen() {
    // Define context, scope and datastore
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize()) {

        Spacer(modifier = Modifier.height(20.dp))

        // Username field
        OutlinedTextField(
            modifier = Modifier.padding(start = 15.dp, end = 15.dp).fillMaxWidth(),
            value = "",
            onValueChange = { },
            label = { Text(text = "Contact Name", color = Color.Black, fontSize = 14.sp) }
        )

        // Email field
        OutlinedTextField(
            modifier = Modifier.padding(start = 15.dp, end = 15.dp).fillMaxWidth(),
            value = "",
            onValueChange = { },
            label = { Text(text = "Contact No", color = Color.Black, fontSize = 14.sp) }
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Buttons
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth().padding(15.dp)
        ) {
            // Load button
            Button(
                onClick = {
                    // Load data from Datastore if exist

                    Toast.makeText(context, "LOADED", Toast.LENGTH_SHORT).show()
                },
                colors = ButtonDefaults.buttonColors(Color(235, 186, 150))
            ) {
                Text(
                    text = "Load",
                    fontSize = 20.sp,
                    color = Color.Black
                )
            }

            // Save button
            Button(
                onClick = {
                    // Save data to Datastore
                    Toast.makeText(context, "SAVED", Toast.LENGTH_SHORT).show()
                },
                colors = ButtonDefaults.buttonColors(Color(34, 210, 9))
            ){
                Text(
                    text = "Save",
                    fontSize = 20.sp,
                    color = Color.Black
                )
            }

        }

        Spacer(modifier = Modifier.height(400.dp))

        // About section
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .border(BorderStroke(1.dp, Color.Black))
                .background(Color(230, 230, 230))
        ) {
            Text(
                text = "Emre Deniz" + "\n" + "301371047",
                color = Color.Black,
                fontSize = 20.sp,
                modifier = Modifier.padding(15.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MAPD721A2EmreDenizTheme {
        MainScreen()
    }
}
