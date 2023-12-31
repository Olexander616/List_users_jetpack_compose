package com.example.composible_list_like_facebook

import android.content.Context
import android.graphics.drawable.Icon
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountBox
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.composible_list_like_facebook.ui.theme.Composible_List_Like_FacebookTheme
import com.github.javafaker.Faker
import java.util.Random

data class User(
    val id: Long,
    val photoUrl: String,
    val name: String,
    val status: String,

)

fun createUserList(): List<User>{
    val faker = Faker.instance(Random(0))
    val images = mutableListOf(
        "https://cloudinary-marketing-res.cloudinary.com/images/w_1000,c_scale/v1679921049/Image_URL_header/Image_URL_header-png?_i=AA",
        "https://d27jswm5an3efw.cloudfront.net/app/uploads/2019/07/how-to-make-a-url-for-a-picture-on-your-computer-4.jpg",
        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQIMhh2utsW2Jv8x3YBuXw-Xo2rGbUoJm-AWQ&usqp=CAU",
    )
    val list = List(100){
        index->
        val id = index + 1L
        User(
            id = id,
            photoUrl = images[index % images.size],
            name = faker.name().fullName(),
            status = faker.shakespeare().hamletQuote()
        )
    }
    return list
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
        AppScreen()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppScreen() {
    var userList by remember {
        mutableStateOf(createUserList())
    }
    val context: Context = LocalContext.current

    LazyColumn(contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp)){
        items(
            items = userList,
            key = { user -> user.id},
        ){ user ->
            UserCard(
                user = user,
                onUserClicked = {
                    Toast.makeText(context,
                        user.name.toString(),
                        Toast.LENGTH_SHORT).show()
                },
                onUserDeleted = {
                    userList-=user
                },
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
fun UserCard(
    user: User,
    onUserClicked: () ->Unit,
    onUserDeleted: () ->Unit,
    modifier: Modifier = Modifier,
) {
    Card (
        shape = RoundedCornerShape(size = 6.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        ),
        modifier = modifier.clickable (
            interactionSource = remember {
                MutableInteractionSource()
            },
            indication = rememberRipple()
        ){
            onUserClicked()
        }
    ){
        Row (modifier = Modifier.padding(all = 8.dp)){
            UserImage(
                url = user.photoUrl)
            Spacer(modifier = Modifier.width(16.dp))
            UserInfo(user = user)
            DeleteUserButton(onClick = onUserDeleted)
        }
    }
}

@Composable
private fun UserImage(url: String){
    var placeholder = rememberVectorPainter(image = Icons.Rounded.AccountCircle)
    AsyncImage(model = url, contentDescription = stringResource(R.string.user_photo),
        placeholder = placeholder,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .size(64.dp)
            .aspectRatio(1f / 1f)
            .clip(CircleShape),)

}

@Composable
private fun RowScope.UserInfo(user: User){
    Column (modifier = Modifier.weight(1f)){
        Text(text = user.name,
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontSize = 18.sp,)
        Spacer(modifier = Modifier.height(6.dp))
        Text(text = user.status,
            color = Color.Black,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            fontSize = 16.sp,)
    }
}

@Composable
private fun RowScope.DeleteUserButton(onClick: ()-> Unit){
    IconButton(onClick = onClick,
        modifier = Modifier.align(Alignment.CenterVertically)){
        Icon(
            imageVector = Icons.Rounded.Delete,
            contentDescription = stringResource(R.string.delete)
        )
    }  
}