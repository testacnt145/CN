package com.chattynotes.mvp.repository.data;

import com.chattynotes.mvp.repository.JavaStringRepository;

public class BackupOptions implements JavaStringRepository {

    //java (hardcoded)
    //database
    //file
    //network
    //preference

    @Override
    public String[] getOptions() {
        return new String[] {
                "Local storage backup",
        };
    }
}
