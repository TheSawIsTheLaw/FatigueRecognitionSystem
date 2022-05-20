package domain.response

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

object ResponseCreator
{
    private fun prepareMessage(
        code: Int,
        message: String,
        description: String
    ): ResponseMessage
    {
        return ResponseMessage(code, message, description)
    }

    fun internalServerErrorResponse(
        message: String,
        description: String
    ): ResponseEntity<ResponseMessage>
    {
        return ResponseEntity(
            prepareMessage(500, message, description),
            HttpStatus.INTERNAL_SERVER_ERROR
        )
    }

    fun okResponse(
        message: String,
        description: String
    ): ResponseEntity<ResponseMessage>
    {
        return ResponseEntity(
            prepareMessage(200, message, description),
            HttpStatus.OK
        )
    }

    fun userNotFoundResponse(
        message: String,
        description: String
    ): ResponseEntity<ResponseMessage>
    {
        return ResponseEntity(
            prepareMessage(404, message, description),
            HttpStatus.NOT_FOUND
        )
    }

}