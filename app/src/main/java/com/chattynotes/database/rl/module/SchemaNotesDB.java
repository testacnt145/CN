package com.chattynotes.database.rl.module;

import com.chattynotes.database.rl.model.Chats;
import com.chattynotes.database.rl.model.Notes;

//https://stackoverflow.com/q/41810131
@io.realm.annotations.RealmModule(library = true, classes = { Chats.class, Notes.class})
public class SchemaNotesDB {
}