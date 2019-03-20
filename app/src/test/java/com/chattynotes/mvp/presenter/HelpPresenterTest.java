package com.chattynotes.mvp.presenter;

import com.chattynotes.mvp.repository.JavaStringRepository;
import com.chattynotes.mvp.view.HelpView;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class HelpPresenterTest {
    //[testing_: very first Mockito usage]

    private static String[] HELP_OPTIONS = new String[] {"How to use", "Privacy policy",};

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    JavaStringRepository javaRepository;

    @Mock
    HelpView view;

    private HelpPresenter presenter;
    @Before
    //calls method before each of the test
    public void setUp() throws Exception {
        presenter = new HelpPresenter(view, javaRepository);
    }

    @Test
    public void shouldHandleOptionsFound() {
        when(javaRepository.getOptions()).thenReturn(HELP_OPTIONS);
        presenter.loadHelpOption();
        verify(view).displayHelpOptions(HELP_OPTIONS);
    }

}