package com.luis.soundify.presentation.logIn

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luis.soundify.R
import com.luis.soundify.presentation.theme.LocalAppColors

@Composable
fun LogInScreen(
    viewModel: LogInViewModel,
    onGetStartedClick: () -> Unit,
    onNavigateToHome: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    LogInScreenContainer(
        state = state,
        onGetStartedClick = onGetStartedClick,
        onNavigateToHome = onNavigateToHome
    )
}

@Composable
fun LogInScreenContainer(
    state: LogInState,
    onGetStartedClick: () -> Unit,
    onNavigateToHome: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LocalAppColors.current.blueSky)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.7f)
                .background(LocalAppColors.current.blueSky)
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_girl),
                contentDescription = stringResource(R.string.login_img_girl_desc),
                contentScale = ContentScale.FillWidth,
                modifier = Modifier.fillMaxSize()
            )

            Image(
                painter = painterResource(id = R.drawable.img_ellipse),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .offset(x = (-20).dp, y = (-20).dp)
                    .size(80.dp)
            )

            Image(
                painter = painterResource(id = R.drawable.img_ellipse),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = 30.dp, y = (-10).dp)
                    .size(100.dp)
            )

            Image(
                painter = painterResource(id = R.drawable.img_ellipse),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .offset(x = 50.dp, y = (-80).dp)
                    .size(150.dp)
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
                .background(Color.Black)
                .padding(top = 40.dp, bottom = 80.dp, start = 32.dp, end = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = buildAnnotatedString {
                    append(stringResource(R.string.login_intro_part1) + " ")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(stringResource(R.string.login_intro_highlight1) + " ")
                    }
                    append(stringResource(R.string.login_intro_part2))
                    withStyle(
                        style = SpanStyle(
                            color = LocalAppColors.current.blueSky,
                            fontWeight = FontWeight.Bold
                        )
                    ) {
                        append(stringResource(R.string.login_intro_highlight2) + " ")
                    }
                    append(stringResource(R.string.login_intro_part3))
                    append(stringResource(R.string.login_intro_part4))
                    withStyle(
                        style = SpanStyle(
                            color = LocalAppColors.current.blueSky,
                            fontWeight = FontWeight.Bold
                        )
                    ) {
                        append(" " + stringResource(R.string.app_name_lowercase) + " ")
                    }
                    append(stringResource(R.string.login_intro_part5))
                },
                fontSize = 24.sp,
                color = Color.White,
                textAlign = TextAlign.Center,
                lineHeight = 36.sp,
                modifier = Modifier.padding(bottom = 40.dp)
            )

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(bottom = 60.dp)
            ) {
                Spacer(
                    modifier = Modifier
                        .width(30.dp)
                        .height(5.dp)
                        .clip(RoundedCornerShape(50))
                        .background(LocalAppColors.current.blueSky)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Spacer(
                    modifier = Modifier
                        .width(30.dp)
                        .height(5.dp)
                        .clip(RoundedCornerShape(50))
                        .background(Color.White.copy(alpha = 0.5f))
                )
            }

            Button(
                onClick = onGetStartedClick,
                colors = ButtonDefaults.buttonColors(containerColor = LocalAppColors.current.blueSky),
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
            ) {
                Text(
                    text = stringResource(R.string.login_button_get_started),
                    fontSize = 18.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        when (state) {
            LogInState.START -> {}
            LogInState.Loading -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.5f)),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(color = LocalAppColors.current.blueSky)
                }
            }

            is LogInState.Success -> {
                val data = state.tokenModel
                onNavigateToHome()
            }

            is LogInState.Error -> {
                println("Error")
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLogInScreen() {
    MaterialTheme {
        LogInScreenContainer(
            state = LogInState.START,
            onGetStartedClick = {},
            onNavigateToHome = {}
        )
    }
}