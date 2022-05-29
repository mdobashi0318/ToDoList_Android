package com.example.todolist.other

import io.realm.DynamicRealm
import io.realm.FieldAttribute
import io.realm.RealmMigration

class Migration : RealmMigration {

    override  fun migrate(realm: DynamicRealm, oldVersion: Long, newVersion: Long) {
        val realmSchema = realm.schema
        var oldVersion = oldVersion

        if (oldVersion == 0L) {
            val userSchema = realmSchema.get("ToDoModel")
            userSchema!!.addField("completionFlag", String::class.java, FieldAttribute.REQUIRED)
            oldVersion++
        }
    }


    override fun equals(other: Any?): Boolean {
        return other != null && other is  RealmMigration
    }

    override fun hashCode(): Int {
        return Migration().hashCode()
    }

}
