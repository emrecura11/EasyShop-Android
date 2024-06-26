package com.emrecura.easyshop.screens.compose

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.emrecura.easyshop.R
import com.emrecura.easyshop.ui.theme.Orange40
import com.emrecura.easyshop.ui.theme.Orange80

@Preview(showBackground = true)
@Composable
fun LoginPagePreview() {

}

@Composable
fun LoginPage(
    onUsernameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit
) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(
                    color = Color.Transparent,
                )
        ) {
            Box(
                modifier = Modifier.align(Alignment.Center),
            ) {
                Image(
                    painter = painterResource(id = R.drawable.easy_shop_logo),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .height(180.dp)
                        .fillMaxWidth(),
                )
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(50.dp))

                    Text(
                        text = "Easy Shop",
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(top = 130.dp)
                            .fillMaxWidth(),
                        style = MaterialTheme.typography.headlineMedium,
                        color = Orange40,
                        fontWeight = FontWeight.Bold,
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    SimpleOutlinedTextFieldSample(onUsernameChange)
                    Spacer(modifier = Modifier.padding(3.dp))
                    SimpleOutlinedPasswordTextField(onPasswordChange)

                    val gradientColor = listOf(Orange40, Orange80)
                    val cornerRadius = 16.dp

                    Spacer(modifier = Modifier.padding(10.dp))

                    GradientButton(
                        gradientColors = gradientColor,
                        cornerRadius = cornerRadius,
                        nameButton = "Login",
                        roundedCornerShape = RoundedCornerShape(topStart = 30.dp, bottomEnd = 30.dp),
                        onClick = onLoginClick
                    )

                    Spacer(modifier = Modifier.padding(10.dp))
                    TextButton(onClick = {
                        // Navigate to register screen
                    }) {
                        Text(
                            text = "Create An Account",
                            letterSpacing = 1.sp,
                            style = MaterialTheme.typography.labelLarge,
                            color = Color.Black
                        )
                    }

                    Spacer(modifier = Modifier.padding(5.dp))
                    TextButton(onClick = {
                        // Navigate to reset password screen
                    }) {
                        Text(
                            text = "Reset Password",
                            letterSpacing = 1.sp,
                            style = MaterialTheme.typography.labelLarge,
                            color = Color.Black
                        )
                    }
                    Spacer(modifier = Modifier.padding(20.dp))
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun SimpleOutlinedTextFieldSample(onTextChange: (String) -> Unit) {
        val keyboardController = LocalSoftwareKeyboardController.current
        var text by rememberSaveable { mutableStateOf("") }

        OutlinedTextField(
            value = text,
            onValueChange = {
                text = it
                onTextChange(it)
            },
            shape = RoundedCornerShape(12.dp),
            label = { Text("Email", color = Color.Black, style = MaterialTheme.typography.labelMedium) },
            placeholder = { Text("Name or Email Address") },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Email
            ),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Orange40,
                unfocusedBorderColor = Orange40
            ),
            singleLine = true,
            modifier = Modifier.fillMaxWidth(0.8f),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                }
            )
        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun SimpleOutlinedPasswordTextField(onTextChange: (String) -> Unit) {
        val keyboardController = LocalSoftwareKeyboardController.current
        var password by rememberSaveable { mutableStateOf("") }

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                onTextChange(it)
            },
            shape = RoundedCornerShape(12.dp),
            label = { Text("Enter Password", color = Color.Black, style = MaterialTheme.typography.labelMedium) },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Password
            ),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Orange40,
                unfocusedBorderColor = Orange40
            ),
            singleLine = true,
            modifier = Modifier.fillMaxWidth(0.8f),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                }
            )
        )
    }

    @Composable
    private fun GradientButton(
        gradientColors: List<Color>,
        cornerRadius: Dp,
        nameButton: String,
        roundedCornerShape: RoundedCornerShape,
        onClick: () -> Unit
    ) {
        androidx.compose.material3.Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 32.dp, end = 32.dp),
            onClick = onClick,
            contentPadding = PaddingValues(),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            shape = RoundedCornerShape(cornerRadius)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.horizontalGradient(colors = gradientColors),
                        shape = roundedCornerShape
                    )
                    .clip(roundedCornerShape)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = nameButton,
                    fontSize = 20.sp,
                    color = Color.White
                )
            }
        }
    }
