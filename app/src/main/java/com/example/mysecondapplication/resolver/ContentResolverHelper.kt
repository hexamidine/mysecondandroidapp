package com.example.mysecondapplication.resolver

import android.content.ContentResolver
import android.content.Context
import android.net.Uri

private const val TABLE_NAME = "githubrepositoryentity"
private const val PROVIDER_NAME = "com.example.myfirstapplication.provider.FavoriteRepositoryProvider"
private const val URL = "content://$PROVIDER_NAME/$TABLE_NAME"
private const val ID_COLUMN_NAME = "node_id"
private val CONTENT_URI: Uri = Uri.parse(URL)

class ContentResolverHelper(context: Context) {

    private var contentResolver: ContentResolver = context.contentResolver

    val allFavoriteRepositories: ArrayList<String>
        get() {
            val repoIds: ArrayList<String> = ArrayList()
            //val projection = arrayOf<String>(KEY_ID, KEY_NAME, KEY_EMAIL)
            val cursor = contentResolver.query(CONTENT_URI, null, null, null, null)
            if (cursor != null && cursor.count > 0) {
                val nodeIdColumnIndex = cursor.getColumnIndex(ID_COLUMN_NAME)
                while (cursor.moveToNext()) {
//                    val repository = Repository(
//                        cursor.getLong(0)
//                    )
//                    companyTMRecord.id = cursor.getLong(0)
//                    companyTMRecord.name = cursor.getString(1)
//                    companyTMRecord.email = cursor.getString(2)
//
//                    companyTMList.add(repository)

                    repoIds.add(cursor.getString(nodeIdColumnIndex))
                }

                cursor.close()
            }

            return repoIds
        }

    fun removeFromFavorites(id: Long) {
        val selection = "id = ?"
        val selectionArgs = arrayOf(id.toString())
        contentResolver.delete(CONTENT_URI, selection, selectionArgs)
    }
}