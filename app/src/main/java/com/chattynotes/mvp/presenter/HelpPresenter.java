package com.chattynotes.mvp.presenter;

import com.chattynotes.mvp.repository.JavaStringRepository;
import com.chattynotes.mvp.view.HelpView;

public class HelpPresenter {

    private HelpView view;
    private JavaStringRepository repository;

    public HelpPresenter(HelpView view, JavaStringRepository repository) {
        this.view = view;
        this.repository = repository;
    }

    public void loadHelpOption() {
        String[] helpOptions = repository.getOptions();
        view.displayHelpOptions(helpOptions);
    }

}
