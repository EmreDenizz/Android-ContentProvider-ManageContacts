// Emre Deniz (301371047)
// Assignment 2

package com.example.mapd721_a2_emredeniz

import android.Manifest.permission.*
import android.annotation.SuppressLint
import android.content.ContentProviderOperation
import android.content.ContentResolver
import android.content.ContentUris
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.pm.PackageManager
import android.net.Uri
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.mapd721_a2_emredeniz.ui.theme.MAPD721A2EmreDenizTheme

// Main activity
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
fun ContactUnit(contact: Contact) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(5.dp)
    ) {
        Text(text = contact.displayName)
        Spacer(modifier = Modifier.width(5.dp))
        Text(text = contact.phoneNumber)
    }
}

@Composable
fun MainScreen(context: ComponentActivity) {

    // Check if loaded before
    var loadedBefore by remember { mutableStateOf(true) }
    // State: List of contacts
    var contacts by remember { mutableStateOf(emptyList<Contact>()) }

    // Perform data loading
    LaunchedEffect(loadedBefore) {
        if (loadedBefore) {
            // Load contacts when it's the first time
            contacts = getContacts(context)
            loadedBefore = false
        }
    }

    // Define states for contactName and contactNumber
    var contactName by remember { mutableStateOf("") }
    var contactNumber by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize()) {

        Spacer(modifier = Modifier.height(20.dp))

        // Contact Name field
        OutlinedTextField(
            modifier = Modifier
                .padding(start = 15.dp, end = 15.dp)
                .fillMaxWidth(),
            value = contactName,
            onValueChange = { contactName = it },
            label = { Text(text = "Contact Name", color = Color.Black, fontSize = 14.sp) }
        )

        // Contact Number field
        OutlinedTextField(
            modifier = Modifier
                .padding(start = 15.dp, end = 15.dp)
                .fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Phone
            ),
            value = contactNumber,
            onValueChange = { contactNumber = it },
            label = { Text(text = "Contact No", color = Color.Black, fontSize = 14.sp) }
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Buttons
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp)
        ) {
            // Load button
            Button(
                onClick = {
                    // Get data from Contacts
                    contacts = getContacts(context)
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
                    // Save data to Contacts
                    if(contactName != "" && contactNumber != ""){
                        saveContact(context, contactName, contactNumber)
                    }
                    else{
                        Toast.makeText(context, "Fill all the fields.", Toast.LENGTH_SHORT).show()
                    }
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
            if (contacts.isEmpty()) {
                Text(text = "No contacts available.")
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
                        ContactUnit(contact)
                    }
                }
            }
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

// Save contact function
@SuppressLint("Range")
fun saveContact(context: ComponentActivity, name: String, number: String) {
    // Request the permission for WRITE_CONTACTS
    if (ContextCompat.checkSelfPermission(context, WRITE_CONTACTS) == PackageManager.PERMISSION_DENIED) {
        ActivityCompat.requestPermissions(context, arrayOf(WRITE_CONTACTS), 100)
        Log.d(TAG, "Permission requested")
    } else {
        Log.d(TAG, "Permission already granted")
    }

    // Insert a new raw contact
    val rawContactUri: Uri = context.contentResolver.insert(ContactsContract.RawContacts.CONTENT_URI, ContentValues())!!

    // Get the raw contactID
    val rContactId = ContentUris.parseId(rawContactUri)

    // Add contact name
    val contentValuesName = ContentValues()
    contentValuesName.put(ContactsContract.Data.RAW_CONTACT_ID, rContactId)
    contentValuesName.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
    contentValuesName.put(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, name)
    context.contentResolver.insert(ContactsContract.Data.CONTENT_URI, contentValuesName)

    // Add contact phone number
    val contentValuesPhone = ContentValues()
    contentValuesPhone.put(ContactsContract.Data.RAW_CONTACT_ID, rContactId)
    contentValuesPhone.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
    contentValuesPhone.put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
    contentValuesPhone.put(ContactsContract.CommonDataKinds.Phone.NUMBER, number)
    context.contentResolver.insert(ContactsContract.Data.CONTENT_URI, contentValuesPhone)

    Toast.makeText(context, "SAVED", Toast.LENGTH_SHORT).show()
}

// Contact data classes
data class Contact(val displayName: String, var phoneNumber: String)
data class ContactIdNames(val contactId: String, var displayName: String)
data class ContactIdNumbers(val contactId: String, var phoneNumber: String)

// Get Contact function
@SuppressLint("Range")
fun getContacts(context: ComponentActivity): List<Contact> {
    val contacts = mutableListOf<Contact>()
    val contactNames = mutableListOf<ContactIdNames>()
    val contactNumbers = mutableListOf<ContactIdNumbers>()

    // Get contact id and names
    context.contentResolver.query(
        ContactsContract.Contacts.CONTENT_URI,
        arrayOf(ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME_PRIMARY),
        null,
        null,
        null
    )?.use { cursor ->
        if (cursor.moveToFirst()) {
            do {
                val contactId =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                val displayName =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY))

                if(displayName != null){
                    contactNames.add(ContactIdNames(contactId, displayName))
                }
            } while (cursor.moveToNext())
        }
    }

    // Get contact id and phones
    context.contentResolver.query(
        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
        arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.CONTACT_ID),
        null,
        null,
        null
    )?.use { cursor ->
        if (cursor.moveToFirst()) {
            do {
                val contactId =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID))
               val phoneNumber =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))

                contactNumbers.add(ContactIdNumbers(contactId, phoneNumber))
            } while (cursor.moveToNext())
        }
    }

    // Match the contact names and phones
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
        MainScreen(context = ComponentActivity())
    }
}
