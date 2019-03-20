package com.chattynotes.adapters.listChat;

import com.chattynotes.adapters.listChat.model.InterfaceChat;

import java.util.Comparator;

public class ComparatorChatTime implements Comparator<InterfaceChat> {

	@Override
	public int compare(InterfaceChat lhs, InterfaceChat rhs) {
		return Long.valueOf(lhs.getLastNoteTimestamp()).compareTo(rhs.getLastNoteTimestamp());
		//Compares its two arguments for order. Returns a negative integer, zero, or a positive integer
		//as the first argument is less than, equal to, or greater than the second.
	}

}
