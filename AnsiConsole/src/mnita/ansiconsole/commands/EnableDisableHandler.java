package mnita.ansiconsole.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.State;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.handlers.RegistryToggleState;

import mnita.ansiconsole.AnsiConsoleActivator;
import mnita.ansiconsole.preferences.AnsiConsolePreferenceConstants;
import mnita.ansiconsole.preferences.AnsiConsolePreferenceUtils;

public class EnableDisableHandler extends AbstractHandler {
	public static final String COMMAND_ID = "AnsiConsole.command.enable_disable";

	public EnableDisableHandler() {
		init();
	}

	/**
	 * Listens for changes of the
	 * {@link AnsiConsolePreferenceConstants#PREF_ANSI_CONSOLE_ENABLED}
	 * preference and sets the initial toggle state to the preference value.
	 */
	protected void init() {
		ICommandService service = PlatformUI.getWorkbench().getService(ICommandService.class);
		Command command = service.getCommand(COMMAND_ID);
		State state = command.getState(RegistryToggleState.STATE_ID);

		IPreferenceStore store = AnsiConsoleActivator.getDefault().getPreferenceStore();
		
		store.addPropertyChangeListener(event -> {
			if (event.getProperty() == AnsiConsolePreferenceConstants.PREF_ANSI_CONSOLE_ENABLED) {
				state.setValue(event.getNewValue());
			}
		});

		state.setValue(store.getBoolean(AnsiConsolePreferenceConstants.PREF_ANSI_CONSOLE_ENABLED));
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		AnsiConsolePreferenceUtils.setAnsiConsoleEnabled(!HandlerUtil.toggleCommandState(event.getCommand()));
		return null;
	}
}
