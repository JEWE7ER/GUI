import javafx.util.StringConverter
import java.util.UUID

object CConverterUUID: StringConverter<UUID>() {
    override fun toString(item: UUID?)
                        = item?.toString() ?: ""
    override fun fromString(string: String?): UUID?{
                        if (string == null) return null
                        if (string.length!=32) return null
                        return try {
                            UUID.fromString(string)
                        }
                        catch (e: Exception){
                            null
                        }
    }
}
