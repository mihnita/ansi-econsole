package mnita.ansiconsole.participants;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.sourcelookup.ISourceLookupParticipant;
import org.eclipse.m2e.internal.launch.IMavenLaunchParticipant;

@SuppressWarnings("restriction")
public class AnsiConsoleMavenLaunchParticipant implements IMavenLaunchParticipant {
    // Force in code what was done by hand here:
    //   https://mihai-nita.net/2020/08/23/force-maven-colors-in-eclipse-console/

    @Override
    public String getProgramArguments(ILaunchConfiguration arg0, ILaunch arg1, IProgressMonitor arg2) {
        return "-Dstyle.color=always";
    }

    @Override
    public List<ISourceLookupParticipant> getSourceLookupParticipants(ILaunchConfiguration arg0, ILaunch arg1, IProgressMonitor arg2) {
        return new ArrayList<>();
    }

    @Override
    public String getVMArguments(ILaunchConfiguration arg0, ILaunch arg1, IProgressMonitor arg2) {
        return "-Djansi.passthrough=true";
    }
}
