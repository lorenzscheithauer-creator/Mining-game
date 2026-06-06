package de.pdfwerkzeugkasten.data.history

import androidx.room.*
import de.pdfwerkzeugkasten.domain.model.HistoryItem
import de.pdfwerkzeugkasten.domain.model.PdfToolType
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "history")
data class HistoryEntity(@PrimaryKey(autoGenerate = true) val id: Long = 0, val toolType: String, val displayName: String, val createdAt: Long, val outputSizeBytes: Long, val inputSizeBytes: Long, val outputUriString: String?)
fun HistoryEntity.toDomain() = HistoryItem(id, PdfToolType.valueOf(toolType), displayName, createdAt, outputSizeBytes, inputSizeBytes, outputUriString)
fun HistoryItem.toEntity() = HistoryEntity(id, toolType.name, displayName, createdAt, outputSizeBytes, inputSizeBytes, outputUriString)
@Dao interface HistoryDao { @Query("SELECT * FROM history ORDER BY createdAt DESC LIMIT 100") fun observe(): Flow<List<HistoryEntity>>; @Insert suspend fun insert(item: HistoryEntity); @Query("DELETE FROM history") suspend fun clear() }
@Database(entities = [HistoryEntity::class], version = 1) abstract class HistoryDatabase : RoomDatabase() { abstract fun dao(): HistoryDao }
class HistoryRepository(private val dao: HistoryDao) { fun observe(): Flow<List<HistoryItem>> = kotlinx.coroutines.flow.map(dao.observe()) { it.map(HistoryEntity::toDomain) }; suspend fun add(item: HistoryItem) = dao.insert(item.toEntity()); suspend fun clear() = dao.clear() }
