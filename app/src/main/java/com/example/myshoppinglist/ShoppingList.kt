package com.example.myshoppinglist

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment
import java.nio.file.WatchEvent


@Composable
fun ShoppingListFun(){

    var sItem by remember { mutableStateOf(listOf<ShoppingItem>()) }
    var alertDisplay by remember { mutableStateOf(false) }
    var itemName by remember { mutableStateOf("")  }
    var itemQty by remember { mutableStateOf("")  }

    Column(
        modifier = Modifier.fillMaxSize()
    ){
        Button(
            onClick = {
                alertDisplay = true
                      },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ){
            Text(text = "Add a Item")
        }
        LazyColumn(
            //It Will Occupy the Total screen Width and
            // Hence the Button moves to the Top of the Screen
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ){
            items(sItem){
                item ->
                if(item.isEditing){
                    ShoppingItemEditor(
                        item = item,
                        onEditComplete = {
                            editedName , editedQty ->
                            sItem = sItem.map { it.copy(isEditing = false) }
                            val editedItem = sItem.find { it.id == item.id }
                            editedItem?.let {
                                it.name = editedName
                                it.qty = editedQty
                            }
                        }
                    )
                }
                else{
                    ShoppingItemList(
                        item = item,
                        onEditClick = {
                            sItem = sItem.map { it.copy(isEditing = item.id == it.id) }
                        },
                        onDeleteClick = {
                            sItem = sItem - item
                        }
                    )
                }
            }
        }
    }
    if(alertDisplay){
        AlertDialog(
            onDismissRequest = { alertDisplay=false },
            title = { Text(text = "Add Shopping Items")},
            text = {
                Column {
                    OutlinedTextField(
                        value = itemName,
                        onValueChange = {itemName = it},
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                        )
                    OutlinedTextField(
                        value = itemQty,
                        onValueChange = {itemQty = it},
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )
                }
            },
            confirmButton = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ){
                    Button(
                        onClick = {
                            if(itemName.isNotBlank()){
                                val newItem = ShoppingItem(
                                    id = sItem.size + 1,
                                    name = itemName,
                                    qty = itemQty.toInt()
                                )
                                sItem = sItem + newItem;
                                alertDisplay = false
                                itemName = ""
                                itemQty = ""
                            }
                        }
                    ) {
                        Text(text = "Add")
                    }
                    Button(
                        onClick = { alertDisplay = false }) {
                        Text(text = "Cancel")
                    }
                }
            }
        )
    }
}

@Composable
fun ShoppingItemEditor(
    item:ShoppingItem,
    onEditComplete : (String,Int) -> Unit
){
    var editedName by remember { mutableStateOf(item.name) }
    var editedQty by remember { mutableStateOf(item.qty.toString()) }
    var isEditing by remember { mutableStateOf(item.isEditing) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ){
        Column (
            verticalArrangement = Arrangement.Center
        ){
            BasicTextField(
                value = editedName,
                onValueChange = {editedName = it},
                singleLine = true,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(8.dp)
            )
            BasicTextField(
                value = editedQty,
                onValueChange = {editedQty = it},
                singleLine = true,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(8.dp)
            )
        }
        Button(onClick = {
            isEditing = false
            onEditComplete(editedName,editedQty.toInt() ?: 1)
        }) {
            Text(text = "Save")
        }
    }


}

data class ShoppingItem(
    var id:Int,
    var name:String,
    var qty:Int,
    var isEditing:Boolean = false
)

@Composable
fun ShoppingItemList(
    item: ShoppingItem,
    onEditClick : () -> Unit,
    onDeleteClick : () -> Unit
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(
                border = BorderStroke(2.dp, Color.Green),
                shape = RoundedCornerShape(20)
            ),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(text = item.name, modifier = Modifier.padding(8.dp))
        Text(text = "Qty : ${item.qty}", modifier = Modifier.padding(8.dp))
        Row (
            modifier= Modifier.padding(8.dp)
        ){
            IconButton(onClick = onEditClick){
                Icon(imageVector = Icons.Default.Edit, contentDescription = null)
            }
            IconButton(onClick = onDeleteClick) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = null)
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun ShoppingListFunPreview(){
    ShoppingListFun();
}