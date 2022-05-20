package domain.logicentities

data class USUserCredentials(
    val username: String,
    val password: String,
    val dbToken: String
)

data class TokenInformation(
    val token: String,
    val tokenID: String
)

data class USCredentialsChangeInfo(
    val oldUsername: String,
    val newUsername: String,
    val oldPassword: String,
    val newPassword: String
)