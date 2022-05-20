package domain.dtos

data class UserCredentialsDTO(
    val username: String,
    val password: String,
    val dbToken: String
)

data class NewPasswordDTOWithUsername(
    val username: String,
    val oldPassword: String,
    val newPassword: String
)

data class NewPasswordDTO(
    val oldPassword: String,
    val newPassword: String
)