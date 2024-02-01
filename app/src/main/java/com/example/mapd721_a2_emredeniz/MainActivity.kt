// Emre Deniz (301371047)
// Assignment 2

package com.example.mapd721_a2_emredeniz

import android.annotation.SuppressLint
import android.nfc.Tag
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.KeyboardType.Companion.Uri
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
                    MainScreen(context = this)
                }
            }
        }
    }
}

@Composable
fun ContactsList(context: ComponentActivity) {
    // State to track if it's the first time loading
    var firstTimeLoaded by remember { mutableStateOf(true) }
    // State to hold the list of contacts
    var contacts by remember { mutableStateOf(emptyList<Contact>()) }

    // LaunchedEffect to perform data loading
    LaunchedEffect(firstTimeLoaded) {
        if (firstTimeLoaded) {
            // Load contacts when it's the first time
            contacts = loadContacts(context)
            firstTimeLoaded = false
        }
    }

    // Display the list of contacts or a message if the list is empty
    if (contacts.isEmpty()) {
        Text(text = "No contacts available")
    }
    else {
        Column(
            modifier = Modifier
                .background(Color(230, 230, 230))
                .size(350.dp)
                .fillMaxWidth()
                .padding(10.dp)
                .verticalScroll(rememberScrollState())
        ) {
            for (contact in contacts) {
                ContactItem(contact)
                ContactItem(contact)
                ContactItem(contact)
                ContactItem(contact)
                ContactItem(contact)
                ContactItem(contact)
                ContactItem(contact)
            }
        }
    }
}

@Composable
fun ContactItem(contact: Contact) {
    // Display each contact in a row with an icon and text
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = contact.displayName)
        Spacer(modifier = Modifier.width(5.dp))
        Text(text = contact.phoneNumber)
    }
}

@Composable
fun MainScreen(context: ComponentActivity) {

    Column(modifier = Modifier.fillMaxSize()) {

        Spacer(modifier = Modifier.height(20.dp))

        // Contact Name field
        OutlinedTextField(
            modifier = Modifier.padding(start = 15.dp, end = 15.dp).fillMaxWidth(),
            value = "",
            onValueChange = { },
            label = { Text(text = "Contact Name", color = Color.Black, fontSize = 14.sp) }
        )

        // Contact Number field
        OutlinedTextField(
            modifier = Modifier.padding(start = 15.dp, end = 15.dp).fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Phone
            ),
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

//                    Toast.makeText(context, "LOADED", Toast.LENGTH_SHORT).show()
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
//                    Toast.makeText(context, "SAVED", Toast.LENGTH_SHORT).show()
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

        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = "CONTACTS",
            color = Color.Black,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 15.dp)
        )

        // Contacts list
        Box(
            modifier = Modifier
                .padding(17.dp)
                .border(BorderStroke(1.dp, Color.Black))
                .background(Color(230, 230, 230))
        ) {
            ContactsList(context)
        }

        Spacer(modifier = Modifier.height(50.dp))

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

// Data class to represent a contact
data class Contact(val displayName: String, var phoneNumber: String)
data class ContactIdNames(val contactId: String, var displayName: String)
data class ContactIdNumbers(val contactId: String, var phoneNumber: String)

@SuppressLint("Range")
fun loadContacts(context: ComponentActivity): List<Contact> {
    val contacts = mutableListOf<Contact>()
    val contactNames = mutableListOf<ContactIdNames>()
    val contactNumbers = mutableListOf<ContactIdNumbers>()

    // Use the content resolver to query contacts
    context.contentResolver.query(
        ContactsContract.Contacts.CONTENT_URI,
        arrayOf(ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME_PRIMARY),
        null,
        null,
        null
    )?.use { cursor ->
        // Check if the cursor has data
        if (cursor.moveToFirst()) {
            do {
                // Retrieve display name from the cursor and add to the list
                val contactId =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                val displayName =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY))

                contactNames.add(ContactIdNames(contactId, displayName))
            } while (cursor.moveToNext())
        }
    }

    context.contentResolver.query(
        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
        arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.CONTACT_ID),
        null,
        null,
        null
    )?.use { cursor ->
        // Check if the cursor has data
        if (cursor.moveToFirst()) {
            do {
                // Retrieve display name from the cursor and add to the list
                val contactId =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID))
               val phoneNumber =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))

                contactNumbers.add(ContactIdNumbers(contactId, phoneNumber))
            } while (cursor.moveToNext())
        }
    }

    for (i in 0..contactNames.size-1) {
        var id = contactNames[i].contactId
        for (j in 0..contactNumbers.size-1) {
            if(id == contactNumbers[j].contactId){
                contacts.add(Contact(contactNames[i].displayName, contactNumbers[j].phoneNumber))
            }
        }
    }

    return contacts
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MAPD721A2EmreDenizTheme {
        MainScreen(ComponentActivity())
    }
}
