package cz.juzna.intellij.neon.ide;

import com.intellij.ide.plugins.*;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationAction;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.extensions.PluginId;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManagerListener;
import com.intellij.openapi.updateSettings.impl.pluginsAdvertisement.PluginsAdvertiser;
import cz.juzna.intellij.neon.NeonBundle;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;


public class NeonPluginListener implements ProjectManagerListener {

	private static final @NotNull Logger LOG = Logger.getInstance(CommandProcessor.class);
	private static final @NotNull String NOTIFICATION_GROUP = "NEON Pro Group";
	private static final @NotNull String PLUGIN_PAGE_URL = "https://plugins.jetbrains.com/plugin/18387-neon-nette-support";

	@Override
	public void projectOpened(@NotNull Project project) {
		neonProSuggestion(NeonBundle.message("notification.neonProSuggestion.message"));
	}

	public static void neonProSuggestion(final @NotNull String message) {
		try {
			final Notification notification = new Notification(NOTIFICATION_GROUP, message, NotificationType.IDE_UPDATE);
			notification.addAction(new NotificationAction(NeonBundle.message("notification.neonProSuggestion.button.install")) {
				@Override
				public void actionPerformed(@NotNull AnActionEvent e, @NotNull Notification notification) {
					PluginId pluginId = PluginId.getId("com.mesour.intellij.neon");
					PluginManagerCore.getPlugin(pluginId);
					PluginsAdvertiser.installAndEnable(
							new HashSet<PluginId>() {{ add(pluginId); }},
							() -> PluginManager.disablePlugin("cz.juzna.intellij.neon")
					);
				}
			});
			notification.addAction(new NotificationAction(NeonBundle.message("notification.neonProSuggestion.button.seeMore")) {
				@Override
				public void actionPerformed(@NotNull AnActionEvent e, @NotNull Notification notification) {
					openUrl();
				}
			});
			final Application app = ApplicationManager.getApplication();
			if (!app.isDisposed()) {
				app.getMessageBus().syncPublisher(Notifications.TOPIC).notify(notification);
			}

		} catch (IllegalMonitorStateException e) {
			LOG.error(e);
		}
	}

	private static void openUrl() {
		if (Desktop.isDesktopSupported()) {
			Desktop desktop = Desktop.getDesktop();

			if (desktop.isSupported(Desktop.Action.BROWSE)) {
				try {
					URI uri = new URI(PLUGIN_PAGE_URL);
					desktop.browse(uri);
				} catch (URISyntaxException | IOException ignored) {
				}
			}
		}
	}

}
