package ui;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import service.Command;
import service.CreditService;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.mockito.Mockito.*;

class MenuTest {

    @Mock
    private CreditService service;

    @Mock
    private Command mockCommand;

    private final InputStream originalSystemIn = System.in;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() {
        // повертаю контроль клавіатурі після тесту
        System.setIn(originalSystemIn);
    }

    // імітація введення користувачем
    private void provideInput(String data) {
        ByteArrayInputStream testIn = new ByteArrayInputStream(data.getBytes());
        System.setIn(testIn);
    }

    // перевірка чи працює команда "exit"
    @Test
    void testMenuExitCommand() {
        provideInput("exit\n");

        Menu menu = new Menu(service);
        menu.run();
    }

    // перевірка чи працює команда help
    @Test
    void testMenuHelpCommand() {
        // exit для виходу з циклу
        provideInput("help\nexit\n");

        Menu menu = new Menu(service);

        // реєстрація фейкової команди
        when(mockCommand.getDesc()).thenReturn("Mock Description");
        menu.registerCommand("test", mockCommand);

        menu.run();

        // перевірка чи метод getDesc() викликався хоча б один раз
        verify(mockCommand, atLeastOnce()).getDesc();
    }

    @Test
    void testExecuteValidCommand() {
        provideInput("test param1\nexit\n");

        Menu menu = new Menu(service);
        menu.registerCommand("test", mockCommand);

        menu.run();

        // перевірка чи команда виконалася з параметром "param1"
        verify(mockCommand).execute("param1");
    }

    @Test
    void testExecuteValidCommandWithoutParams() {
        provideInput("test\nexit\n");

        Menu menu = new Menu(service);
        menu.registerCommand("test", mockCommand);

        menu.run();

        // якщо немає параметрів, то параметр має бути пустим рядком
        // перевірка
        verify(mockCommand).execute("");
    }

    @Test
    void testUnknownCommand() {
        // перевірка на неіснуючу команду
        provideInput("abracadabra\nexit\n");

        Menu menu = new Menu(service);
        menu.run();
    }
}