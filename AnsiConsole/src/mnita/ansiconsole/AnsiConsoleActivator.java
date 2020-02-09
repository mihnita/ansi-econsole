package mnita.ansiconsole;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

//import org.eclipse.jface.resource.ImageDescriptor;
//import org.eclipse.jface.resource.ResourceLocator;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.ui.console.IConsolePageParticipant;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

public class AnsiConsoleActivator extends AbstractUIPlugin {

//    Should match Bundle-SymbolicName in MANIFEST.MF
//    public static final String PLUGIN_ID = "net.mihai-nita.ansicon.plugin"; //$NON-NLS-1$

    private static AnsiConsoleActivator plugin;

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

//    public static ImageDescriptor getImageDescriptor(String path) {
//        return ResourceLocator.imageDescriptorFromBundle(PLUGIN_ID, path).get();
//    }

    private final Map<StyledText, IConsolePageParticipant> viewers = new HashMap<>();

    public void addViewer(StyledText viewer, IConsolePageParticipant participant) {
        viewers.put(viewer, participant);
    }

    public void removeViewerWithPageParticipant(IConsolePageParticipant participant) {
        Set<StyledText> toRemove = new HashSet<>();

        for (Entry<StyledText, IConsolePageParticipant> entry : viewers.entrySet()) {
            if (entry.getValue() == participant)
                toRemove.add(entry.getKey());
        }

        for (StyledText viewer : toRemove)
            viewers.remove(viewer);
    }
}
