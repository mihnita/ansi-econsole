package mnita.ansiconsole.participants;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.sourcelookup.ISourceLookupParticipant;
import org.eclipse.m2e.internal.launch.IMavenLaunchParticipant;

import mnita.ansiconsole.preferences.AnsiConsolePreferenceUtils;

@SuppressWarnings("restriction")
public class AnsiConsoleMavenLaunchParticipant implements IMavenLaunchParticipant {
    // Force in code what was done by hand here:
    //   https://mihai-nita.net/2020/08/23/force-maven-colors-in-eclipse-console/

    @Override
    public String getProgramArguments(
    		ILaunchConfiguration launchCfg, ILaunch launch, IProgressMonitor progMonitor) {
    	return AnsiConsolePreferenceUtils.isAnsiConsoleEnabled() ? "-Dstyle.color=always" : "";
    }

    @Override
    public List<ISourceLookupParticipant> getSourceLookupParticipants(
    		ILaunchConfiguration launchCfg, ILaunch launch, IProgressMonitor progMonitor) {
        return new ArrayList<>();
    }

    @Override
    public String getVMArguments(
    		ILaunchConfiguration launchCfg, ILaunch launch, IProgressMonitor progMonitor) {
    	return AnsiConsolePreferenceUtils.isAnsiConsoleEnabled() ? "-Djansi.passthrough=true" : "";
    }
}
