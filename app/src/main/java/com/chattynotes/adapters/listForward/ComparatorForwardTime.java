package com.chattynotes.adapters.listForward;

import com.chattynotes.adapters.listChat.model.InterfaceChat;
import com.chattynotes.adapters.listForward.model.InterfaceForward;

import java.util.Comparator;

public class ComparatorForwardTime implements Comparator<InterfaceForward> {

	@Override
	public int compare(InterfaceForward lhs, InterfaceForward rhs) {
		return Long.valueOf(lhs.getLastNoteTimestamp()).compareTo(rhs.getLastNoteTimestamp());
		//Compares its two arguments for order. Returns a negative integer, zero, or a positive integer
		//as the first argument is less than, equal to, or greater than the second.
	}

}
