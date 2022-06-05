package mnita.ansiconsole.commands;

import java.util.Map;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.State;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.commands.IElementUpdater;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.handlers.RegistryToggleState;
import org.eclipse.ui.menus.UIElement;

import mnita.ansiconsole.preferences.AnsiConsolePreferenceUtils;

public class EnableDisableHandler extends AbstractHandler implements IElementUpdater {
    public static final String COMMAND_ID = "AnsiConsole.command.enable_disable";

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        if (wasMyEvent(event)) {
            // The preferences are saved, just update the icon state
            boolean value = AnsiConsolePreferenceUtils.isAnsiConsoleEnabled();
            event.getCommand().getState(RegistryToggleState.STATE_ID).setValue(value);
        } else {
            // Update the icon state state.
            // toggleCommandState returns the previous value
            boolean value = ! HandlerUtil.toggleCommandState(event.getCommand());
            // Also update the preferences
            AnsiConsolePreferenceUtils.setAnsiConsoleEnabled(value);
        }
        return null;
    }

    @Override
    public void updateElement(UIElement element, @SuppressWarnings("rawtypes") Map parameters) {
        ICommandService service = PlatformUI.getWorkbench().getService(ICommandService.class);
        Command command = service.getCommand(COMMAND_ID);
        State state = command.getState(RegistryToggleState.STATE_ID);
        AnsiConsolePreferenceUtils.setAnsiConsoleEnabled((Boolean) state.getValue());
    }

    // If I executed the command "by hand" from the properties dialog
    // it has a "dummy" event that I've created.
    private static boolean wasMyEvent(ExecutionEvent executionEvent) {
        Object trigger = executionEvent.getTrigger();
        if (trigger instanceof Event) {
            Event internalEvent = (Event) trigger;
            if (internalEvent.time == 0 && internalEvent.type == 0) { // My event
                return true;
            }
        }
        return false;
    }
}
