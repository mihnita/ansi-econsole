package mnita.ansiconsole;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.ui.console.IConsolePageParticipant;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

public class AnsiConsoleActivator extends AbstractUIPlugin {

    public static final String PLUGIN_ID = "net.mihai-nita.AnsiConsole"; //$NON-NLS-1$

    private static AnsiConsoleActivator plugin;

    public AnsiConsoleActivator() {
    }

    @Override
    public void start(BundleContext context) throws Exception {
        super.start(context);
        plugin = this;
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        plugin = null;
        super.stop(context);
    }

    public static AnsiConsoleActivator getDefault() {
        return plugin;
    }

    public static ImageDescriptor getImageDescriptor(String path) {
        return imageDescriptorFromPlugin(PLUGIN_ID, path);
    }

    private Map<StyledText, IConsolePageParticipant> viewers = new HashMap<StyledText, IConsolePageParticipant>();

    public void addViewer(StyledText viewer, IConsolePageParticipant participant) {
        viewers.put(viewer, participant);
    }

    public void removeViewerWithPageParticipant(IConsolePageParticipant participant) {
        Set<StyledText> toRemove = new HashSet<StyledText>();

        for (StyledText viewer : viewers.keySet()) {
            if (viewers.get(viewer) == participant)
                toRemove.add(viewer);
        }

        for (StyledText viewer : toRemove)
            viewers.remove(viewer);
    }
}
