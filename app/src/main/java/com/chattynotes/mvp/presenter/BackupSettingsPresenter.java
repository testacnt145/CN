package com.chattynotes.mvp.presenter;

import com.chattynotes.mvp.repository.JavaStringRepository;
import com.chattynotes.mvp.view.BackupSettingsView;

public class BackupSettingsPresenter {

    private BackupSettingsView view;
    private JavaStringRepository repository;

    public BackupSettingsPresenter(BackupSettingsView view, JavaStringRepository repository) {
        this.view = view;
        this.repository = repository;
    }

    public void loadBackupOption() {
        String[] backupOptions = repository.getOptions();
        view.displayBackupOptions(backupOptions);

    }

}
