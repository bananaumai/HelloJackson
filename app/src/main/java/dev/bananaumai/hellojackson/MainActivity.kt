package dev.bananaumai.hellojackson

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

data class Person(val name: String)

data class Office(
    val name: String,
    @JsonProperty("optional_field") val optionalField: Boolean? = null,
    val members: List<Person> = emptyList()
)

val TAG = "bananaumai"

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val om = jacksonObjectMapper()

        tryDeserialize(om)
        trySerialize(om)
    }
}

fun tryDeserialize(om: ObjectMapper) {
    val personJson = """{"name": "Yuta"}"""
    val p = om.readValue<Person>(personJson)

    Log.d(TAG, "p: $p")

    val personsJson = """[{"name": "Naoki"}, {"name": "Jun"}, {"name": "Takeshi"}]"""
    val ps = om.readValue<List<Person>>(personsJson)
    Log.d(TAG, "ps: ${ps.joinToString()}")


    val officeJson1 = """{"name": "TYO", "members": [{"name": "Naoki"}, {"name": "Jun"}, {"name": "Takeshi"}, {"name": "Yuta"}]}"""
    val o1 = om.readValue<Office>(officeJson1)
    Log.d(TAG, "o1: $o1")

    val officeJson2 = """{"name": "SFO", "optional_field": true, "members": []}"""
    val o2 = om.readValue<Office>(officeJson2)
    Log.d(TAG, "o2: $o2")
}

fun trySerialize(om: ObjectMapper) {
    val person = Person("Yuta")
    Log.d(TAG, om.writeValueAsString(person))
    // {"name":"Yuta"}

    val persons = listOf(person)
    Log.d(TAG, om.writeValueAsString(persons))
    // [{"name":"Yuta"}]

    val office1 = Office(name = "TYO")
    Log.d(TAG, om.writeValueAsString(office1))
    // {"name":"TYO","optional_field":null,"members":[]}

    val office2 = Office(name = "TYO", optionalField = null)
    Log.d(TAG, om.writeValueAsString(office2))
    // {"name":"TYO","optional_field":null,"members":[]}

    val office3 = Office(name = "TYO", optionalField = true, members = persons)
    Log.d(TAG, om.writeValueAsString(office3))
    // {"name":"TYO","optional_field":true,"members":[{"name":"Yuta"}]}
}