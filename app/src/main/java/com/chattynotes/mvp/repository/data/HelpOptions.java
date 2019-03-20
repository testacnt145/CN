package com.chattynotes.mvp.repository.data;

import com.chattynotes.mvp.repository.JavaStringRepository;

public class HelpOptions implements JavaStringRepository {

    //java (hardcoded)
    //database
    //file
    //network
    //preference

    @Override
    public String[] getOptions() {
        return new String[] {
                "How to use",
                "Privacy policy",
        };
    }
}
