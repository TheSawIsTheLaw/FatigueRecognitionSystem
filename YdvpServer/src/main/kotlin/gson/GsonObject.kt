package gson

import com.google.gson.Gson

object GsonObject {
    val gson: Gson by lazy { Gson() }
}