package com.example.partsasign1.data.remote.api

import com.example.partsasign1.data.remote.model.RecepcionDto
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RecepcionApiTest {

    private val server = MockWebServer()
    private lateinit var api: RecepcionApi

    @Before
    fun setUp() {
        server.start()
        api = Retrofit.Builder()
            .baseUrl(server.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RecepcionApi::class.java)
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun crear_enviaElBodyCorrecto_yParseaLaRespuesta() = runBlocking {
        val requestBody = RecepcionDto(
            repuestoId = "R-123",
            cantidadRecibida = 10,
            recibidoPor = "ana",
            fechaRecepcion = "2024-10-01",
            observacion = "ok"
        )

        val responseJson = """
            {
                "id": 5,
                "repuestoId": "R-123",
                "cantidadRecibida": 10,
                "recibidoPor": "ana",
                "fechaRecepcion": "2024-10-01",
                "observacion": "ok"
            }
        """.trimIndent()

        server.enqueue(
            MockResponse()
                .setResponseCode(201)
                .addHeader("Content-Type", "application/json")
                .setBody(responseJson)
        )

        val response = api.crear(requestBody)

        val recorded = server.takeRequest()
        assertEquals("/api/recepciones", recorded.path)
        assertEquals("POST", recorded.method)
        assertEquals(
            """{"repuestoId":"R-123","cantidadRecibida":10,"recibidoPor":"ana","fechaRecepcion":"2024-10-01","observacion":"ok"}""",
            recorded.body.readUtf8()
        )

        assertEquals(5, response.id)
        assertEquals(requestBody.repuestoId, response.repuestoId)
        assertEquals(requestBody.cantidadRecibida, response.cantidadRecibida)
        assertEquals(requestBody.recibidoPor, response.recibidoPor)
        assertEquals(requestBody.fechaRecepcion, response.fechaRecepcion)
        assertEquals(requestBody.observacion, response.observacion)
    }
}
