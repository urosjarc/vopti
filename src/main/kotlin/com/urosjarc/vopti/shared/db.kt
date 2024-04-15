package com.urosjarc.vopti.shared

import com.urosjarc.dbmessiah.domain.C
import com.urosjarc.dbmessiah.domain.Table
import com.urosjarc.dbmessiah.impl.sqlite.SqliteSerializer
import com.urosjarc.dbmessiah.impl.sqlite.SqliteService
import com.urosjarc.dbmessiah.serializers.BasicTS
import com.urosjarc.dbmessiah.serializers.IdTS
import com.urosjarc.dbmessiah.serializers.JavaTimeTS
import java.util.*
import kotlin.reflect.KProperty1

//private fun <T : Any> createTable(primaryKey: KProperty1<T, *>): Table<T> {
    //Gather all foreign keys
//    val foreignKeys = Table.getInlineTypedForeignKeys(primaryKey = primaryKey)
//    return Table(
//        primaryKey = primaryKey,
//        foreignKeys = foreignKeys,
//        constraints = foreignKeys.map { it.first to listOf(C.CASCADE_DELETE) })
//}
//
//private val sqlite_serializer = SqliteSerializer(
//    tables = listOf(
////        createTable(Map::)
//    ),
//    globalSerializers = BasicTS.basic + JavaTimeTS.sqlite + listOf(IdTS.int({ Id<Any>(it) }, { it.value }))
//)
//
//val sqlite = SqliteService(
//    config = Properties().apply {
//        this["jdbcUrl"] = "jdbc:sqlite:./build/chinook.sqlite"
//    },
//    ser = sqlite_serializer
//)
