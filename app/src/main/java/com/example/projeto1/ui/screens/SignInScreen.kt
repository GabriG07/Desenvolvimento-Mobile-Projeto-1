package com.example.projeto1.ui.screens

import android.content.SharedPreferences
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projeto1.R
import com.example.projeto1.ui.theme.ColorBackground
import com.example.projeto1.ui.theme.ColorGold
import com.example.projeto1.ui.theme.ColorSurface
import com.example.projeto1.ui.theme.ColorTextPrimary
import com.example.projeto1.ui.theme.ColorTextSecondary
import com.example.projeto1.data.UserModel
import com.example.projeto1.ui.OnBoardingEvents


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SignInScreen(
    onHome: () -> Unit,
    action: (OnBoardingEvents) -> Unit,
    sharedPreferences: SharedPreferences
) {

    val keyboard = LocalSoftwareKeyboardController.current

    var userModel by remember { mutableStateOf(UserModel()) }


    Scaffold() { padding ->
        Column(
            modifier = Modifier
                .padding(top = (32.dp))
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Spacer(Modifier.width(56.dp))
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize().fillMaxHeight()
                        .padding(top = 56.dp, bottom = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.verticalScroll(rememberScrollState())
                    ) {
                        Text(
                            modifier = Modifier.padding(bottom = (16.dp), start = (24.dp)),
                            text = stringResource(R.string.sign_in_page_title),
                            color = ColorTextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center
                        )
                        OutlinedTextField(
                            value = userModel.userName,
                            onValueChange = {
                                userModel = userModel.copy(userName = it)
                            },
                            placeholder = { Text(stringResource(R.string.sign_in_user_field)) },
                            leadingIcon = { Icon(Icons.Filled.Person, contentDescription = null) },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                            keyboardActions = KeyboardActions(onSearch = {
                                keyboard?.hide()
                            }),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = ColorSurface,
                                unfocusedContainerColor = ColorSurface,
                                focusedBorderColor = Color.Transparent,
                                unfocusedBorderColor = Color.Transparent,
                                focusedTextColor = ColorTextPrimary,
                                unfocusedTextColor = ColorTextPrimary,
                                cursorColor = ColorTextPrimary,
                                focusedPlaceholderColor = ColorTextSecondary,
                                unfocusedPlaceholderColor = ColorTextSecondary
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                        )
                        OutlinedTextField(
                            value = userModel.mobileNumber,
                            onValueChange = {
                                userModel = userModel.copy(mobileNumber = it)
                            },
                            placeholder = { Text(stringResource(R.string.sign_in_phone_field)) },
                            leadingIcon = { Icon(Icons.Filled.Phone, contentDescription = null) },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            keyboardActions = KeyboardActions(onSearch = {
                                keyboard?.hide()
                            }),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = ColorSurface,
                                unfocusedContainerColor = ColorSurface,
                                focusedBorderColor = Color.Transparent,
                                unfocusedBorderColor = Color.Transparent,
                                focusedTextColor = ColorTextPrimary,
                                unfocusedTextColor = ColorTextPrimary,
                                cursorColor = ColorTextPrimary,
                                focusedPlaceholderColor = ColorTextSecondary,
                                unfocusedPlaceholderColor = ColorTextSecondary
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                        )
                        Spacer(modifier = Modifier.height(48.dp))
                    }
                    Button(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp,),
                        onClick = {
                            action(OnBoardingEvents.LoginClick(userModel, sharedPreferences) {
                                status -> if(status) onHome()
                            }, )
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ColorGold,
                            contentColor = ColorBackground
                        )
                    ) {
                        Text(stringResource(R.string.sign_in_confirm))
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        modifier= Modifier.fillMaxWidth().clickable(onClick = {
                            action(OnBoardingEvents.SignUpClick(userModel){})
                        }),
                        text = stringResource(R.string.create_account),
                        color = ColorTextPrimary,
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier= Modifier.height(320.dp))
                }
            }
        }
    }
}